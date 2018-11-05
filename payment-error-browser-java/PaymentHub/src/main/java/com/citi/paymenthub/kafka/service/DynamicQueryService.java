package com.citi.paymenthub.kafka.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.citi.paymenthub.kafka.costant.ConstantUtils;
import com.citi.paymenthub.kafka.model.TopicMessagesMaster;

/**
 * Dynamic Query Service
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */
@Component
public class DynamicQueryService {

	private static Logger logger = LogManager.getLogger(DynamicQueryService.class);
	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * This method create the dynamic query for Payment Browser based on UI filters.
	 * 
	 * @param dynamicQuery
	 * @param pageNumber
	 * @param pageSize
	 * @return Map of record list and page information.
	 */
	public Map<String, Object> paymentMessagesFilterQuery(TopicMessagesMaster dynamicQuery, int pageNumber,
			int pageSize) {
		final Query query = new Query();
		final List<Criteria> criteria = new ArrayList<>();
		Map<String, Object> paginationMap = new HashMap<String, Object>();
		try {
			if (dynamicQuery.getUter() != null && !dynamicQuery.getUter().equalsIgnoreCase(ConstantUtils.BLANK)) {
				criteria.add(Criteria.where(ConstantUtils.MESSAGEUTER).is(dynamicQuery.getUter().trim()));
			}
			if (dynamicQuery.getTopicName() != null
					&& !dynamicQuery.getTopicName().equalsIgnoreCase(ConstantUtils.BLANK)) {
				criteria.add(Criteria.where(ConstantUtils.TOPICNAME).is(dynamicQuery.getTopicName()));
			}
			if (dynamicQuery.getMsgTimestamp() != null && dynamicQuery.getMsgTimestamp() != 0l) {
				criteria.add(Criteria.where(ConstantUtils.MSGTIMESTAMP).gte(dynamicQuery.getMsgTimestamp()));
				Long lastMiliSecondOfDay = dynamicQuery.getMsgTimestamp() + 86400000;
				criteria.add(Criteria.where(ConstantUtils.MSGTIMESTAMP).lte(lastMiliSecondOfDay));
			}
			if (dynamicQuery.getMsgData() != null && !dynamicQuery.getMsgData().equalsIgnoreCase(ConstantUtils.BLANK)) {
				criteria.add(Criteria.where(ConstantUtils.MSGDATA).regex(toLikeRegex(dynamicQuery.getMsgData())));
			}
			if (!criteria.isEmpty()) {
				query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
			}
			long recorCount = mongoTemplate.count(query, TopicMessagesMaster.class);
			long number = (recorCount - (pageNumber * pageSize));
			long skipRec = number >= 0l ? number : 0;
			long totalPages = recorCount / pageSize;
			long lastPage = recorCount % pageSize;
			long totalNumberOfPages = lastPage == 0 ? totalPages : (totalPages + 1);
			if (pageNumber == totalPages && lastPage > 0) {
				query.limit((int) (recorCount % pageSize));
			} else {
				query.limit(pageSize);
			}
			query.skip(skipRec);
			logger.debug("Builded Query :" + query);
			List<TopicMessagesMaster> topicMessagesMasterList = mongoTemplate.find(query, TopicMessagesMaster.class);
			Collections.sort(topicMessagesMasterList);
			paginationMap.put(ConstantUtils.TOTALPAGES, totalNumberOfPages);
			paginationMap.put(ConstantUtils.PAGESIZE, pageSize);
			paginationMap.put(ConstantUtils.CONTENT, topicMessagesMasterList);
			paginationMap.put(ConstantUtils.TOTALELEMENTS, recorCount);
			paginationMap.put(ConstantUtils.PAGENUMBER, pageNumber - 1);
		} catch (Exception e) {
			logger.error(e);
		}
		return paginationMap;
	}

	private String toLikeRegex(String source) {
		return source.replaceAll("\\*", ".*");
	}

}
