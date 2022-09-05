package com.example.clip.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.example.clip.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private  PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
		.authorizeRequests() 
		.antMatchers( "/api/clip/createPayload","/api/clip/usersPaymentList", "/api/clip/report")
        .permitAll()
        .antMatchers("/api/clip/disbursement").hasRole(ADMIN.name())
        .anyRequest()
		.authenticated()
		.and()
		.httpBasic();
		
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		UserDetails peterAdmin = User.builder()
				.username("peter")
				.password(passwordEncoder.encode("a"))
				.roles(ADMIN.name())
				.build();
			return new InMemoryUserDetailsManager(
					peterAdmin
					);
					
		
	}
}
