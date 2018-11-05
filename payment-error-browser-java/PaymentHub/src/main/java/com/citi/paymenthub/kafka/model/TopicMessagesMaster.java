package com.citi.paymenthub.kafka.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Topic Messages Master
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */
@Document(collection = "Topics_Message_Master")
public class TopicMessagesMaster implements Serializable, Comparable<TopicMessagesMaster> {

	private static final long serialVersionUID = 3025640504956812138L;
	@Id
	private String id;
	private String topicName;
	private String uter;
	private Long offsetId;
	private Integer msgId;
	private String msgData;
	private Long msgTimestamp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getUter() {
		return uter;
	}

	public void setUter(String uter) {
		this.uter = uter;
	}

	public long getOffsetId() {
		return offsetId;
	}

	public void setOffsetId(long offsetId) {
		this.offsetId = offsetId;
	}

	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	public String getMsgData() {
		return msgData;
	}

	public void setMsgData(String msgData) {
		this.msgData = msgData;
	}

	public Long getMsgTimestamp() {
		return msgTimestamp;
	}

	public void setMsgTimestamp(Long msgTimestamp) {
		this.msgTimestamp = msgTimestamp;
	}

	public void setOffsetId(Long offsetId) {
		this.offsetId = offsetId;
	}

	public int compareTo(TopicMessagesMaster ctm) {
		if (ctm.getId().equalsIgnoreCase(this.id))
			return 0;
		else
			return ctm.getId().compareTo(this.id);

	}
}
