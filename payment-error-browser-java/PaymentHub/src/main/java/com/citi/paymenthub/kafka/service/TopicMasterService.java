package com.citi.paymenthub.kafka.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citi.paymenthub.kafka.model.TopicMaster;
import com.citi.paymenthub.kafka.repository.TopicMasterRepository;

/**
 * Topic Master Service
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018 
 * @version 1.0
 */

@Service
public class TopicMasterService {

	
	@Autowired
	private TopicMasterRepository topicMasterRepository;

	Logger logger = LogManager.getLogger(TopicMasterService.class);

	/**
	 * This Method returns the all topic name.
	 * @return
	 */
	public List<String> getAllTopics() {
		List<TopicMaster> topicMasterList = null;
		List<String> topicList=new ArrayList<String>();
		try {
			logger.debug("Enter into 'getAllTopics' method.");
			topicMasterList = topicMasterRepository.findAll();
			for(TopicMaster tm:topicMasterList) {
				topicList.add(tm.getTopicName());
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return topicList;
	}
	
}
