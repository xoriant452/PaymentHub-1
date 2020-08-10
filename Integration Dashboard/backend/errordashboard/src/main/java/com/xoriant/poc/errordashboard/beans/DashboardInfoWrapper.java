package com.xoriant.poc.errordashboard.beans;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.xoriant.poc.errordashboard.dao.DashboardInfo;

@Component
public class DashboardInfoWrapper {

	private DashboardInfo dashboardInfo;
	private MultipartFile file;

	public DashboardInfoWrapper() {
		super();
	}

	public DashboardInfoWrapper(DashboardInfo dashboardInfo, MultipartFile file) {
		super();
		this.dashboardInfo = dashboardInfo;
		this.file = file;
	}

	public DashboardInfo getDashboardInfo() {
		return dashboardInfo;
	}

	public void setDashboardInfo(DashboardInfo dashboardInfo) {
		this.dashboardInfo = dashboardInfo;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
