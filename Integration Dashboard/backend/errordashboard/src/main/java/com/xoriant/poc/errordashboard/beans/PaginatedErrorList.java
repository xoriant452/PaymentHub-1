package com.xoriant.poc.errordashboard.beans;

import java.util.List;

import com.xoriant.poc.errordashboard.dao.DashboardInfo;

public class PaginatedErrorList {

	private List<DashboardInfo> dashboardInfo;
	private int pageNumber;
	private long totalElements;
	private int totalPages;
	private int pageSize;
	
	public PaginatedErrorList() {
		super();
	}
	
	public PaginatedErrorList(List<DashboardInfo> dashboardInfo, int pageNumber, long totalElements, int totalPages,
			int pageSize) {
		super();
		this.dashboardInfo = dashboardInfo;
		this.pageNumber = pageNumber;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.pageSize = pageSize;
	}
	public List<DashboardInfo> getDashboardInfo() {
		return dashboardInfo;
	}
	public void setDashboardInfo(List<DashboardInfo> dashboardInfo) {
		this.dashboardInfo = dashboardInfo;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
