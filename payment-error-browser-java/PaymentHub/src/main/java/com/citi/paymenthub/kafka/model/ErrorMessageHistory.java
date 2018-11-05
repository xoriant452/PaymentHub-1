package com.citi.paymenthub.kafka.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Error Message Master Service
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */
@Document(collection = "Error_Topic_Message_History")
public class ErrorMessageHistory implements Serializable {

	private static final long serialVersionUID = -1319367407575995075L;
	@Id
	private String id;
	private String errorMsgId;
	private Long offsetId;
	private String uter;
	private String errorMsgData;
	private String status;
	private String topicName;
	private String requestedDate;
	private String requestedBy;
	private String approvedBy;
	private String approvedDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getErrorMsgId() {
		return errorMsgId;
	}

	public void setErrorMsgId(String errorMsgId) {
		this.errorMsgId = errorMsgId;
	}

	public Long getOffsetId() {
		return offsetId;
	}

	public void setOffsetId(Long offsetId) {
		this.offsetId = offsetId;
	}

	public String getUter() {
		return uter;
	}

	public void setUter(String uter) {
		this.uter = uter;
	}

	public String getErrorMsgData() {
		return errorMsgData.substring(0, 180);
	}

	public String getWholeErrorMsgData(boolean flag) {
		return errorMsgData;
	}

	public void setErrorMsgData(String errorMsgData) {
		this.errorMsgData = errorMsgData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
}
