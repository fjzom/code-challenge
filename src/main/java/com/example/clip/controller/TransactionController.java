package com.example.clip.controller;


import javax.persistence.PersistenceException;

import com.example.clip.model.Payment;
import com.example.clip.model.Report;
import com.example.clip.request.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.clip.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/api/clip")
public class TransactionController {

    @Autowired
    PaymentRepository paymentRepository;

    @RequestMapping(value = "/createPayload", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody PaymentRequest paymentRequest) {

        Payment payment = new Payment();
        payment.setAmount(paymentRequest.getAmount());
        payment.setUserId(paymentRequest.getUserId());

        try {
            paymentRepository.save(payment);
            log.info("Payload Created Successfully");
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (PersistenceException ex) {
            return ResponseEntity.status(HttpStatus.OK).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/usersPaymentList", method = RequestMethod.GET)
    public List<Payment> usersPaymentList() {
            log.info("Get  payment users list successfully");
            return  paymentRepository.findAll();
    }

    @RequestMapping(value = "/disbursement", method = RequestMethod.GET) 
    public Set<String>  disbursement() {
        List<Payment> response = paymentRepository.findAllByStatus("NEW");

        response.forEach(r->r.setDisbursement(calculateDisbursement(r)));
        response.forEach(r->r.setStatus("PROCESSED"));
        response.forEach(r-> paymentRepository.save(r));

        Set<String> userDisbursementLst = getUserDisbursementLst(response);

        log.info("Get disbursement list successfully");
        return userDisbursementLst;
    }
    
    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public List<Report>  report() {

        List<Payment> response = paymentRepository.findAll();
        List<Report> userReportLst = generateReportLst(response);

        log.info("Get report successfully");
        return userReportLst;
    }

    private List<Report> generateReportLst(List<Payment> response) {
        List<Report> reportLst  = new ArrayList<>();
        Set<String> userIdLst =  response.stream().map(Payment::getUserId).collect(Collectors.toSet());
        Report obj;

        for(String userId : userIdLst){
            obj = new Report();
            obj.setUser_name(obtainUserNameByUserId(response, userId));
            obj.setPayments_sum(obtainPaymentsSumListByUserId(response, userId));
            obj.setNew_payments(obtainNewPaymentsSumByUserId(response, userId));
            obj.setNew_payments_amount(obtainNewPaymentsAmountSumListByUserId(response, userId));

            reportLst.add(obj);
        }

       return reportLst;

    }

    private static BigDecimal obtainNewPaymentsAmountSumListByUserId(List<Payment> response, String userId) {
        List<BigDecimal> newPaymentsAmountSumListByUserId = response.stream()
                .filter(r -> r.getUserId().equalsIgnoreCase(userId) && r.getStatus().equalsIgnoreCase("NEW"))
                .map(Payment::getAmount).collect(Collectors.toList());
        return newPaymentsAmountSumListByUserId.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private static long obtainNewPaymentsSumByUserId(List<Payment> response, String userId) {
        return response.stream()
                .filter(r -> r.getUserId().equalsIgnoreCase(userId) && r.getStatus().equalsIgnoreCase("NEW"))
                .count();
    }

    private static  BigDecimal obtainPaymentsSumListByUserId(List<Payment> response, String userId) {
        List<BigDecimal> paymentsSumListByUserId = response.stream()
                .filter(r-> r.getUserId().equalsIgnoreCase(userId))
                .map(Payment::getAmount).collect(Collectors.toList());
        return paymentsSumListByUserId.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static String obtainUserNameByUserId(List<Payment> response, String userId) {
        return response.stream()
                .filter(r -> r.getUserId().equalsIgnoreCase(userId))
                .map(n -> n.getUser().getName())
                .findFirst().orElseThrow();
    }


    private static Set<String> getUserDisbursementLst(List<Payment> response) {
        return response.stream()
                .map(r -> "User_Id: " + r.getUserId() + ", Disbursement: " + r.getDisbursement())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private static BigDecimal calculateDisbursement(Payment r) {
        return r.getAmount().subtract(r.getAmount().multiply(BigDecimal.valueOf(.035))).setScale(2);
    }



}
