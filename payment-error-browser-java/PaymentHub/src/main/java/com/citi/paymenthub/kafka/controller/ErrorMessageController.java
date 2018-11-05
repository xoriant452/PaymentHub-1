package com.citi.paymenthub.kafka.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.citi.paymenthub.kafka.common.CommonUtils;
import com.citi.paymenthub.kafka.costant.ConstantUtils;
import com.citi.paymenthub.kafka.model.ErrorMessageHistory;
import com.citi.paymenthub.kafka.model.ErrorMessageTrans;
import com.citi.paymenthub.kafka.service.ErrorMessageHistoryService;
import com.citi.paymenthub.kafka.service.ErrorMessageMasterService;
import com.citi.paymenthub.kafka.service.ErrorMessageTransService;
import com.citi.paymenthub.kafka.service.KafkaConsumerService;
import com.citi.paymenthub.kafka.service.TopicMasterService;

/**
 * Kafka Transaction Controller
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */

@RestController
@EnableAutoConfiguration
public class ErrorMessageController {

	@Autowired
	private KafkaConsumerService consumer;

	@Autowired
	private ErrorMessageHistoryService errorMessageHistoryService;

	@Autowired
	private ErrorMessageMasterService errorMessageMasterService;

	@Autowired
	private ErrorMessageTransService errorMessageTransService;

	@Autowired
	private TopicMasterService topicMasterService;

	@GetMapping(value = "/errorMessages")
	public Map<String, Object> getErrorMessages(@RequestParam("fromTimestamp") Long fromTimestamp,
			@RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageSize") Integer pageSize)
			throws Exception {
		Map<String, Object> ls = consumer.runConsumerOnTimestamp(fromTimestamp, pageNumber, pageSize);
		return ls;
	}

	@PutMapping(value = "/replayFunctionality", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, String> replayFunctionality(@RequestBody String idList) {
		return errorMessageMasterService.sendRecordsForApproval(idList);
	}

	@GetMapping(value = "/historyReportData", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<ErrorMessageHistory> getHistoryReportData(Pageable page) {
		return errorMessageHistoryService.getAllHistoryRecords(page);
	}

	@GetMapping(value = "/pendingApprovalErrorMessages", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Page<ErrorMessageTrans> getPendingApprovalMessages(Pageable page) {
		return errorMessageTransService.getAllPendingApprovalMsgs(page);
	}

	@GetMapping(value = "/filterDates")
	public Map<Long, String> getFilterDates() throws Exception {
		return CommonUtils.getLastSevenDays();
	}

	@PutMapping(value = "/reviewPendingRecords", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, String> approveRecords(@RequestBody String idList) {
		return errorMessageTransService.updateApproveRejectStatus(idList);
	}

	@GetMapping(value = "/singleErrorMessage")
	public @ResponseBody Map<String, String> getSingleErrorMessage(@RequestParam("reportName") String reportName,
			@RequestParam("errorMessageId") String errorMessageId) {
		Map<String, String> errorMessageMap = new HashMap<String, String>();
		if (ConstantUtils.MASTER.equalsIgnoreCase(reportName)) {
			errorMessageMap.put(ConstantUtils.MESSAGE, (String) consumer
					.getMessageByOffset(ConstantUtils.ERRORMESSAGETOPIC, errorMessageId).get(ConstantUtils.MESSAGE));
		} else if (ConstantUtils.TRANS.equalsIgnoreCase(reportName)) {
			errorMessageMap.put(ConstantUtils.MESSAGE, errorMessageTransService.getSingleErrorMessage(errorMessageId));
		} else {
			errorMessageMap.put(ConstantUtils.MESSAGE,
					errorMessageHistoryService.getSingleErrorMessage(errorMessageId));
		}
		return errorMessageMap;
	}

	@GetMapping(value = "/receiveMessages")
	public void getReceiveMessages() throws Exception {
		consumer.receiveMessage();
	}

	@GetMapping(value = "/topicList")
	public @ResponseBody List<String> getCountryByRegionId() {
		return topicMasterService.getAllTopics();
	}
}
