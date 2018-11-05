package com.citi.paymenthub.kafka.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.citi.paymenthub.kafka.costant.ConstantUtils;
import com.citi.paymenthub.kafka.model.ErrorMessageTrans;
import com.citi.paymenthub.kafka.model.TopicMessagesMaster;
import com.citi.paymenthub.kafka.repository.ErrorMessageTransRepository;
import com.citi.paymenthub.kafka.repository.TopicMessagesMasterRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Error Message Master Service
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */

@Service
public class ErrorMessageMasterService {

	@Autowired
	KafkaConsumerService kafkaConsumerService;

	@Autowired
	private TopicMessagesMasterRepository TopicMessagesMasterRepository;

	@Autowired
	private ErrorMessageTransRepository errorMessageTransRepository;

	Logger logger = LogManager.getLogger(ErrorMessageMasterService.class);

	public boolean saveMessages(List<TopicMessagesMaster> tpList) {
		TopicMessagesMasterRepository.saveAll(tpList);
		return true;
	}

	/**
	 * This method sends record/s for review.
	 * @param ids
	 * @return
	 */
	public Map<String, String> sendRecordsForApproval(String ids) {
		Map<String, String> messageMap = new HashMap<String, String>();
		try {
			logger.debug("Enter into 'sendRecordsForApproval' method.");
			if (null != ids && !ConstantUtils.BLANK.equalsIgnoreCase(ids.trim())) {
				List<String> idList = convertJsonStringToList(ids);
				if (!CollectionUtils.isEmpty(idList)) {
					List<ErrorMessageTrans> transRecordList = new ArrayList<ErrorMessageTrans>();
					for (String offsetId : idList) {
						ErrorMessageTrans errorMessageTrans = kafkaConsumerService
								.getRecordByOffset(ConstantUtils.ERRORMESSAGETOPIC, offsetId);
						if(errorMessageTrans.getUter()!=null && !errorMessageTrans.getUter().equals(ConstantUtils.BLANK))
						transRecordList.add(errorMessageTrans);
					}
					if (!CollectionUtils.isEmpty(transRecordList)) {
						transRecordList = errorMessageTransRepository.saveAll(transRecordList);
					}
				}
				messageMap.put(ConstantUtils.MESSAGE, "Record/s submitted successfully and records sent for approval.");
			} else {
				logger.error("JSONArray is empty.");
				messageMap.put(ConstantUtils.MESSAGE, "Something went wrong.Please contact to Admin..!!!");
			}
		} catch (Exception e) {
			messageMap.put(ConstantUtils.MESSAGE, "Something went wrong.Please contact to Admin..!!!");
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
	private List<String> convertJsonStringToList(String idList)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(idList, new TypeReference<List<String>>() {
		});
	}

}
