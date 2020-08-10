package com.xoriant.poc.errordashboard.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "errordetails")
public class DashboardInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String applicationName;
	private String customerName;
	private String transactionName;
	private String error_timestamp;
	private String fileUploaded;
	private String sourceSystem;
	private String targetSystem;
	private Integer total;
	private Integer success;
	private Integer failed;
	private String errorRecord;
	private Date lastUpdated;
	
	private String userName;

	public DashboardInfo() {
		super();
	}

	public DashboardInfo(String applicationName, String customerName, String transactionName, String error_timestamp,
			String fileUploaded, String sourceSystem, String targetSystem, Integer total, Integer success,
			Integer failed, String errorRecord, String userName) {
		super();
		this.applicationName = applicationName;
		this.customerName = customerName;
		this.transactionName = transactionName;
		this.error_timestamp = error_timestamp;
		this.fileUploaded = fileUploaded;
		this.sourceSystem = sourceSystem;
		this.targetSystem = targetSystem;
		this.total = total;
		this.success = success;
		this.failed = failed;
		this.errorRecord = errorRecord;
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getapplicationName() {
		return applicationName;
	}

	public void setapplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getTransactionName() {
		return transactionName;
	}

	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}

	public String getTimestamp() {
		return error_timestamp;
	}

	public void setTimestamp(String error_timestamp) {
		this.error_timestamp = error_timestamp;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getTargetSystem() {
		return targetSystem;
	}

	public void setTargetSystem(String targetSystem) {
		this.targetSystem = targetSystem;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public Integer getFailed() {
		return failed;
	}

	public void setFailed(Integer failed) {
		this.failed = failed;
	}

	public String getErrorRecord() {
		return errorRecord;
	}

	public void setErrorRecord(String errorRecord) {
		this.errorRecord = errorRecord;
	}

	public Date getLast_updated() {
		return lastUpdated;
	}

	public void setLast_updated(Date last_updated) {
		this.lastUpdated = last_updated;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getFileUploaded() {
		return fileUploaded;
	}

	public void setFileUploaded(String fileUploaded) {
		this.fileUploaded = fileUploaded;
	}

	@Override
	public String toString() {
		return "DashboardInfo [id=" + id + ", applicationName=" + applicationName + ", customerName=" + customerName
				+ ", transactionName=" + transactionName + ", error_timestamp=" + error_timestamp + ", fileUploaded="
				+ fileUploaded + ", sourceSystem=" + sourceSystem + ", targetSystem=" + targetSystem + ", total="
				+ total + ", success=" + success + ", failed=" + failed + ", errorRecord=" + errorRecord
				+ ", lastUpdated=" + lastUpdated + ", userName=" + userName + "]";
	}

}
