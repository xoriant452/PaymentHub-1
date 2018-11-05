package com.citi.paymenthub.kafka.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.citi.paymenthub.kafka.common.CommonUtils;
import com.citi.paymenthub.kafka.costant.ConstantUtils;
import com.citi.paymenthub.kafka.model.ErrorMessageHistory;
import com.citi.paymenthub.kafka.model.ErrorMessageTrans;
import com.citi.paymenthub.kafka.repository.ErrorMessageHistoryRepository;
import com.citi.paymenthub.kafka.repository.ErrorMessageTransRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Error Message Trans Service
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */
@Service
public class ErrorMessageTransService {

	@Autowired
	private ErrorMessageTransRepository errorMessageTransRepository;

	@Autowired
	private ErrorMessageHistoryRepository errorMessageHistoryRepository;

	@Autowired
	private ProducerService producerService;

	private Logger logger = LogManager.getLogger(ErrorMessageTransService.class);

	/**
	 * Method returns the pending approval records on the basis of page details.
	 * 
	 * @param page
	 * @return Page<ErrorMessageTrans>
	 */
	public Page<ErrorMessageTrans> getAllPendingApprovalMsgs(Pageable page) {
		Page<ErrorMessageTrans> errorMessageTrans = null;
		try {
			logger.debug("Enter into 'getAllPendingApprovalMsgs' method.");
			errorMessageTrans = (Page<ErrorMessageTrans>) errorMessageTransRepository.findAll(page);
		} catch (Exception e) {
			logger.error(e);
		}
		return errorMessageTrans;
	}

	/**
	 * If method get status as approved then error message goes for replay otherwise
	 * it will be discarded from transaction collection.
	 * 
	 * @param ids
	 * @return message
	 */
	public Map<String, String> updateApproveRejectStatus(String ids) {
		Map<String, String> messageMap = new HashMap<String, String>();
		try {
			logger.debug("Enter into 'updateStatusErrorMsg' method.");
			String status = null;
			if (null != ids && !ConstantUtils.BLANK.equalsIgnoreCase(ids.trim())) {
				Map<String, Object> approvalMap = convertJsonStringToList(ids);
				@SuppressWarnings("unchecked")
				List<String> idList = (List<String>) approvalMap.get(ConstantUtils.LIST);
				status = approvalMap.get(ConstantUtils.STATUS).toString();
				if (!CollectionUtils.isEmpty(idList) && null != status
						&& !ConstantUtils.BLANK.equalsIgnoreCase(status.trim())) {
					List<ErrorMessageHistory> historyRecordList = null;
					List<ErrorMessageTrans> recordList = (List<ErrorMessageTrans>) errorMessageTransRepository
							.findAllById(idList);
					if (ConstantUtils.STATUS_A.equalsIgnoreCase(status)) {
						for (ErrorMessageTrans errorMessageTrans : recordList) {
							logger.debug("Replay Functionality called.");
							producerService.errorMessageReplayCall("EU.PAYHUB.MESSAGE.REPLAYED",
									errorMessageTrans.getWholeErrorMsgData(true));
						}
					}
					if (!CollectionUtils.isEmpty(recordList)) {
						historyRecordList = errorMessageHistoryRepository
								.saveAll(getErrorMessageHistList(recordList, status));
					}
					if (!CollectionUtils.isEmpty(historyRecordList)) {
						errorMessageTransRepository.deleteAll(recordList);
					}
				}
				messageMap.put("message", "Record/s " + status == ConstantUtils.STATUS_A ? ConstantUtils.APPROVED
						: ConstantUtils.REJECTED + " successfully.");
			} else {
				logger.error("Provided JSON does not contains id's list or status.");
				messageMap.put("message", "Something went wrong.Please call to Admin..!!!");
			}
		} catch (Exception e) {
			messageMap.put("message", "Something went wrong.Please contact to Admin..!!!");
			logger.error(e);
		}
		return messageMap;
	}

	/**
	 * Method returns the list from the JSON string.
	 * @param idList
	 * @return id's list
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private Map<String, Object> convertJsonStringToList(String idList)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(idList, new TypeReference<Map<String, Object>>() {
		});
	}

	/**
	 * Method creates the ErrorMessageHistory object for saving into the collection.
	 * 
	 * @param messageList
	 * @param status
	 * @return list of newly created ErrorMessageHistory instances
	 */
	private List<ErrorMessageHistory> getErrorMessageHistList(List<ErrorMessageTrans> messageList, String status) {
		List<ErrorMessageHistory> emtList = new ArrayList<ErrorMessageHistory>();
		logger.debug(" Enter into 'getErrorMessageHistList' method.");
		for (ErrorMessageTrans errorMessageTrans : messageList) {
			try {
				ErrorMessageHistory errorMessageHist = (ErrorMessageHistory) CommonUtils
						.getInstance(ErrorMessageHistory.class);
				errorMessageHist.setOffsetId(errorMessageTrans.getOffsetId());
				errorMessageHist.setErrorMsgId(errorMessageTrans.getErrorMsgId());
				errorMessageHist.setUter(errorMessageTrans.getUter());
				errorMessageHist.setErrorMsgData(errorMessageTrans.getWholeErrorMsgData(true));
				errorMessageHist.setStatus(status);
				errorMessageHist.setRequestedDate(errorMessageTrans.getRequestedDate());
				errorMessageHist.setRequestedBy(errorMessageTrans.getRequestedBy());
				errorMessageHist.setTopicName(errorMessageTrans.getTopicName());
				errorMessageHist.setApprovedBy(ConstantUtils.SYSTEM_ADMIN_CK);
				errorMessageHist.setApprovedDate(LocalDate.now().toString());
				emtList.add(errorMessageHist);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return emtList;
	}

	/**
	 * method fetch the message according to _id.
	 * 
	 * @param errorMessageId
	 * @return String
	 */
	public String getSingleErrorMessage(String errorMessageId) {
		ErrorMessageTrans record = (ErrorMessageTrans) errorMessageTransRepository
				.findByIdSingleErrorMessage(errorMessageId).get(0);
		return record.getWholeErrorMsgData(true);
	}

	/**
	 * Method fetch the list of UTER from Transaction and History collection of
	 * mongoDB.
	 * 
	 * @return list of UTER
	 */
	public List<String> transactionAndHistoryUterList() {
		List<String> offsetList = new ArrayList<String>();
		List<ErrorMessageTrans> errorMessageTransList = errorMessageTransRepository.findAll();
		List<ErrorMessageHistory> errorMessageHistoryList = errorMessageHistoryRepository.findAll();
		for (ErrorMessageTrans transOffset : errorMessageTransList) {
			offsetList.add(transOffset.getUter());
		}
		for (ErrorMessageHistory historyOffset : errorMessageHistoryList) {
			offsetList.add(historyOffset.getUter());
		}
		return offsetList;
	}
}
