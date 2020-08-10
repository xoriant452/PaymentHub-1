package com.xoriant.poc.errordashboard.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.xoriant.poc.errordashboard.beans.PaginatedErrorList;
import com.xoriant.poc.errordashboard.dao.DashboardInfo;
import com.xoriant.poc.errordashboard.repository.DashboardInfoRepository;

@Service
public class PopulateDashboardService implements IPopulateDashboardService {

	private static final Logger logger = LoggerFactory.getLogger(PopulateDashboardService.class);

	public PaginatedErrorList getAggregatedFilteredErrorList(DashboardInfoRepository repository, int page, int size,
			String colName, int order, String applicationName, String transactionName, String fromDate, String toDate) {

		Page<DashboardInfo> errorList = null;

		if ((null == applicationName && null == transactionName && null == fromDate && null == toDate)
				|| (applicationName == "" && transactionName == "" && fromDate == "" && toDate == "")) {
			if (order == 0) {
				errorList = repository.findAll(PageRequest.of(page, size, Sort.by(colName).ascending()));
				logger.info("Extracted records in Ascending order for: " + colName);
			} else {
				errorList = repository.findAll(PageRequest.of(page, size, Sort.by(colName).descending()));
				logger.info("Extracted records in Descending order for: " + colName);
			}
		} else {
			if ((null == transactionName && null == fromDate && null == toDate)
					|| (transactionName == "" && fromDate == "" && toDate == "")) {
				errorList = repository.findErrorByApplicationName(applicationName, PageRequest.of(page, size));
				logger.info("Filtering records by application name::" + applicationName);
			} else if ((null == applicationName && null == fromDate && null == toDate)
					|| (applicationName == "" && fromDate == "" && toDate == "")) {
				errorList = repository.findErrorByTransactionName(transactionName, PageRequest.of(page, size));
				logger.info("Filtering records by Transaction name::" + transactionName);
			} else if ((null == applicationName && null == transactionName)
					|| (applicationName == "" && transactionName == "")) {
				errorList = repository.findErrorByDateRange(fromDate, toDate, PageRequest.of(page, size));
				logger.info("Filtering records by Date::");
			} else if ((null == transactionName) || (transactionName == "")) {
				errorList = repository.findErrorByApplicationNameFiltersbyDate(applicationName, fromDate, toDate,
						PageRequest.of(page, size));
				logger.info("Filtering records by Aplication name and Date::");
			} else if ((null == applicationName) || (applicationName == "")) {
				errorList = repository.findErrorByTransactiionNameFiltersbyDate(transactionName, fromDate, toDate,
						PageRequest.of(page, size));
				logger.info("Filtering records by Transaction name and Date::");
			} else if ((null == fromDate && null == toDate) || (fromDate == "" && toDate == "")) {
				errorList = repository.findErrorByAggregatedFilters(applicationName, transactionName,
						PageRequest.of(page, size));
				logger.info("Filtering records by Transaction name and Aplication name::");
			} else {
				errorList = repository.findErrorByAggregatedFiltersbyDate(applicationName, transactionName, fromDate,
						toDate, PageRequest.of(page, size));
				logger.info("Filtering records by aggregating Application name, transaction name and Date::"
						+ applicationName + " " + transactionName);
			}
		}

		
		
		PaginatedErrorList pErrorList = new PaginatedErrorList(errorList.getContent(), errorList.getNumber(),
				errorList.getTotalElements(), errorList.getTotalPages(), errorList.getSize());

		return pErrorList;

	}

	public List<DashboardInfo> getDashboardInfoList(DashboardInfoRepository repository, String applicationName,
			String transactionName, String fromDate, String toDate) {
		int page = 0;
		int size = 100000;
		Page<DashboardInfo> perrorList = null;
		if ((null == applicationName && null == transactionName && null == fromDate && null == toDate)
				|| (applicationName == "" && transactionName == "" && fromDate == "" && toDate == "")) {
			perrorList = repository.findAll(PageRequest.of(page, size));
		} else {
			if ((null == transactionName && null == fromDate && null == toDate)
					|| (transactionName == "" && fromDate == "" && toDate == "")) {
				perrorList = repository.findErrorByApplicationName(applicationName, PageRequest.of(page, size));
				logger.info("Filtering records by application name::" + applicationName);
			} else if ((null == applicationName && null == fromDate && null == toDate)
					|| (applicationName == "" && fromDate == "" && toDate == "")) {
				perrorList = repository.findErrorByTransactionName(transactionName, PageRequest.of(page, size));
				logger.info("Filtering records by Transaction name::" + transactionName);
			} else if ((null == applicationName && null == transactionName)
					|| (applicationName == "" && transactionName == "")) {
				perrorList = repository.findErrorByDateRange(fromDate, toDate, PageRequest.of(page, size));
				logger.info("Filtering records by Date::");
			} else if ((null == transactionName) || (transactionName == "")) {
				perrorList = repository.findErrorByApplicationNameFiltersbyDate(applicationName, fromDate, toDate,
						PageRequest.of(page, size));
				logger.info("Filtering records by Aplication name and Date::");
			} else if ((null == applicationName) || (applicationName == "")) {
				perrorList = repository.findErrorByTransactiionNameFiltersbyDate(transactionName, fromDate, toDate,
						PageRequest.of(page, size));
				logger.info("Filtering records by Transaction name and Date::");
			} else if ((null == fromDate && null == toDate) || (fromDate == "" && toDate == "")) {
				perrorList = repository.findErrorByAggregatedFilters(applicationName, transactionName,
						PageRequest.of(page, size));
				logger.info("Filtering records by Transaction name and Aplication name::");
			} else {
				perrorList = repository.findErrorByAggregatedFiltersbyDate(applicationName, transactionName, fromDate,
						toDate, PageRequest.of(page, size));
				logger.info("Filtering records by aggregating Application name, transaction name and Date::"
						+ applicationName + " " + transactionName);
			}
		}

		List<DashboardInfo> errorList = perrorList.getContent();

		return errorList;
	}

}
