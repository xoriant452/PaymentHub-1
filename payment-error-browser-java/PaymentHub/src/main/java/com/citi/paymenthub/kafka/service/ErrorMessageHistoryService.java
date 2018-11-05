package com.citi.paymenthub.kafka.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.citi.paymenthub.kafka.model.ErrorMessageHistory;
import com.citi.paymenthub.kafka.repository.ErrorMessageHistoryRepository;

/**
 * Error Message History Service
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */
@Service
public class ErrorMessageHistoryService {

	@Autowired
	private ErrorMessageHistoryRepository errorMessageHistoryRepository;

	Logger logger = LogManager.getLogger(ErrorMessageHistoryService.class);

	/**
	 * This method returns the All history record of replayed messages.
	 * 
	 * @param page
	 * @return Page<ErrorMessageHistory>
	 */
	public Page<ErrorMessageHistory> getAllHistoryRecords(Pageable page) {
		Page<ErrorMessageHistory> errorMessageHistory = null;
		try {
			logger.debug("Enter into 'getAllHistoryRecords' method.");
			errorMessageHistory = (Page<ErrorMessageHistory>) errorMessageHistoryRepository.findAll(page);
		} catch (Exception e) {
			logger.error(e);
		}
		return errorMessageHistory;
	}

	/**
	 * method fetch the message according to _id.
	 * @param errorMessageId
	 * @return String
	 */
	public String getSingleErrorMessage(String errorMessageId) {
		logger.debug("Enter into 'getSingleErrorMessage' method of ErrorMessageHistoryService class.");
		ErrorMessageHistory record = (ErrorMessageHistory) errorMessageHistoryRepository
				.findByIdSingleErrorMessage(errorMessageId).get(0);
		return record.getWholeErrorMsgData(true);
	}
}
