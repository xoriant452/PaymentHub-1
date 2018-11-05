package com.citi.paymenthub.kafka.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.citi.paymenthub.kafka.service.KafkaConsumerService;
import com.citi.paymenthub.kafka.service.PaymentTopicMessageService;

/**
 * Kafka Transaction Controller
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018 
 * @version 1.0
 */

@RestController
@EnableAutoConfiguration
public class PaymentMessageController {

	@Autowired
	private KafkaConsumerService consumer;
	
	@Autowired
	private PaymentTopicMessageService paymentTopicMessageService;
	
	private static final String MESSAGE="message";
	
	@PostMapping(value = "/paymentMessages", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String,Object> getPaymentMessages(@RequestBody String criteriaData) {
		return paymentTopicMessageService.fetchPaymentDataOnFilter(criteriaData);
	}
	
	@GetMapping(value = "/fetchSinglePaymentMessage")
	public @ResponseBody Map<String, String> getSinglePaymentMessage(@RequestParam("topicName") String topicName,@RequestParam("offsetId") String errorMessageId) {
		Map<String, String> errorMessageMap= new HashMap<String, String>();
			errorMessageMap.put(MESSAGE,(String) consumer.getMessageByOffset(topicName, errorMessageId).get(MESSAGE));
		return errorMessageMap;
	}
	
	
}
