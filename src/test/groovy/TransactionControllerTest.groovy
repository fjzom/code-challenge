import com.example.clip.controller.TransactionController
import com.example.clip.model.Payment
import com.example.clip.model.User
import com.example.clip.repository.PaymentRepository
import com.example.clip.request.PaymentRequest
import spock.lang.Specification


class TransactionControllerTest extends Specification {
    PaymentRepository repo = Mock()
    TransactionController transactionController = new TransactionController(repo);

    def "valid create happypath"() {
        given:
            String userId = "1"
            Double amount = 123.50
            PaymentRequest paymentRequest = new PaymentRequest(userId, amount)
        when:
            transactionController.create(paymentRequest)
        then:
            noExceptionThrown()
    }
    def "valid usersPaymentList happypath"() {
        given:
            User user = new User()
            Payment payment = new Payment(1L, 123.50, "1", "test", 99.99, user)
            List<Payment> paymentList = Arrays.asList(payment)
            repo.findAll() >> paymentList
        when:
            List<Payment> result =    transactionController.usersPaymentList()
        then:
            result.size() == 1
            noExceptionThrown()
    }
    def "valid disbursement happypath"() {
        given:
            User user = new User()
            Payment payment = new Payment(1L, new BigDecimal(123.50), "1", "PROCESSED", 0.00, user)
            List<Payment> paymentList = Arrays.asList(payment)
            repo.findAllByStatus("NEW") >> paymentList
            repo.save() >> _
        when:
            Set<Payment> result =    transactionController.disbursement()
        then:
            result.size() == 1
            noExceptionThrown()
    }
    def "valid report happypath"() {
        given:
            User user = new User()
            Payment payment = new Payment(1L, new BigDecimal(123.50), "1", "PROCESSED", 0.00, user)
            List<Payment> paymentList = Arrays.asList(payment)
            repo.findAllByStatus("NEW") >> paymentList
            repo.save() >> _
        when:
            Set<Payment> result =    transactionController.disbursement()
        then:
            result.size() == 1
            noExceptionThrown()
    }
    def "valid generateReportLst happypath"() {
        given:
            User user = new User()
            user.setId(1)
            user.setName("test")
            Payment payment = new Payment(1L, new BigDecimal(123.50), "1", "PROCESSED", 0.00, user)
            List<Payment> paymentList = Arrays.asList(payment)
        when:
            Set<Payment> result =    transactionController.generateReportLst(paymentList)
        then:
            result.size() == 1
            noExceptionThrown()
    }

    def "valid obtainNewPaymentsAmountSumListByUserId happypath"() {
        given:
            User user = new User()
            user.setId(1)
            user.setName("test")
            Payment payment = new Payment(1L, new BigDecimal(123.50), "1", "PROCESSED", 0.00, user)
            List<Payment> paymentList = Arrays.asList(payment)
        when:
            BigDecimal result =    transactionController.obtainNewPaymentsAmountSumListByUserId(paymentList, "1")
        then:
            result == 0
            noExceptionThrown()
    }
    def "valid obtainNewPaymentsSumByUserId happypath"() {
        given:
            User user = new User()
            user.setId(1)
            user.setName("test")
            Payment payment = new Payment(1L, new BigDecimal(123.50), "1", "PROCESSED", 0.00, user)
            List<Payment> paymentList = Arrays.asList(payment)
        when:
            BigDecimal result =    transactionController.obtainNewPaymentsSumByUserId(paymentList, "1")
        then:
            result == 0
            noExceptionThrown()
    }
    def "valid obtainUserNameByUserId happypath"() {
        given:
            User user = new User()
            user.setId(1)
            user.setName("test")
            Payment payment = new Payment(1L, new BigDecimal(123.50), "1", "PROCESSED", 0.00, user)
            List<Payment> paymentList = Arrays.asList(payment)
        when:
            String result =    transactionController.obtainUserNameByUserId(paymentList, "1")
        then:
            result == "test"
            noExceptionThrown()
    }
    def "valid getUserDisbursementLst happypath"() {
        given:
            User user = new User()
            user.setId(1)
            user.setName("test")
            Payment payment = new Payment(1L, new BigDecimal(123.50), "1", "PROCESSED", 0.00, user)
            List<Payment> paymentList = Arrays.asList(payment)
        when:
            Set<String> result =    transactionController.getUserDisbursementLst(paymentList)
        then:
            result.size() == 1
            noExceptionThrown()
    }
    def "valid calculateDisbursement happypath"() {
        given:
            User user = new User()
            user.setId(1)
            user.setName("test")
            Payment payment = new Payment(1L, new BigDecimal(123.50), "1", "PROCESSED", 0.00, user)
            List<Payment> paymentList = Arrays.asList(payment)
        when:
            BigDecimal result =    transactionController.calculateDisbursement(paymentList)
        then:
            result == 119.17750
            noExceptionThrown()
    }

}