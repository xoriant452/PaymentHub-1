package com.xoriant.poc.errordashboard.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.poc.errordashboard.dao.DashboardInfo;
import com.xoriant.poc.errordashboard.repository.DashboardInfoRepository;
import com.xoriant.poc.errordashboard.utility.CSVHelper;

@Service
public class CSVService {

	@Autowired
	DashboardInfoRepository repository;
	
	@Autowired
	PopulateDashboardService dashboardService;

	public ByteArrayInputStream load(String applicationName,String transactionName, String fromDate, String toDate) {
		List<DashboardInfo> dashboardInfo = (List<DashboardInfo>) dashboardService.getDashboardInfoList(repository, applicationName, transactionName, fromDate, toDate);

		ByteArrayInputStream in = CSVHelper.dashboardInfoToCSV(dashboardInfo);
		return in;
	}

}
