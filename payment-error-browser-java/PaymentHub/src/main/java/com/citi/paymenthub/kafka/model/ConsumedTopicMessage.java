package com.citi.paymenthub.kafka.model;
/**
 * Error Message Master Service
 * 
 * @author Yogesh Mohite
 * @CreationDate 26/10/2018
 * @version 1.0
 */
public class ConsumedTopicMessage implements Comparable<ConsumedTopicMessage>{

	private Long messageKey;
	private String message;
	private Long offset;
	private Long timeStamp;
	private Integer partitionNo;
	private String uter;
	private Boolean isProcessed;
	
	public Long getMessageKey() {
		return messageKey;
	}
	public void setMessageKey(Long messageKey) {
		this.messageKey = messageKey;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getOffset() {
		return offset;
	}
	public void setOffset(Long offset) {
		this.offset = offset;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Integer getPartitionNo() {
		return partitionNo;
	}
	public void setPartitionNo(Integer partitionNo) {
		this.partitionNo = partitionNo;
	}
	
	public String getUter() {
		return uter;
	}
	public void setUter(String uter) {
		this.uter = uter;
	}
	
	public Boolean getIsProcessed() {
		return isProcessed;
	}
	
	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}
	
	public int compareTo(ConsumedTopicMessage ctm){  
		if(this.offset==ctm.offset)  
		return 0;  
		else if(ctm.offset>this.offset)  
		return 1;  
		else  
		return -1;  
		}  
	
	
}
