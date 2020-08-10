package com.xoriant.poc.errordashboard.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import com.xoriant.poc.errordashboard.dao.DashboardInfo;

public class CSVHelper {
	public static String TYPE = "text/csv";
	static String[] HEADERs = { "Application", "Customer", "Transaction", "Source", "Target", "Total", "Success",
			"Failed", "File Uploaded", "Error Record", "User", "Uploaded On" };
	
	
	public static ByteArrayInputStream dashboardInfoToCSV(List<DashboardInfo> dashboardInfoList) {
	    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

	    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
	        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
	    	csvPrinter.printRecord(Arrays.asList(HEADERs));
	      for (DashboardInfo dashboardInfo : dashboardInfoList) {
	        List<String> data = Arrays.asList(
	              dashboardInfo.getapplicationName(),
	              dashboardInfo.getCustomerName(),
	              dashboardInfo.getTransactionName(),
	              dashboardInfo.getSourceSystem(),
	              dashboardInfo.getTargetSystem(),
	              String.valueOf(dashboardInfo.getTotal()),
	              String.valueOf(dashboardInfo.getSuccess()),
	              String.valueOf(dashboardInfo.getFailed()),
	              String.valueOf(dashboardInfo.getFileUploaded()),
	              dashboardInfo.getErrorRecord(),
	              dashboardInfo.getUserName(),
	              dashboardInfo.getTimestamp()
	            );

	        csvPrinter.printRecord(data);
	      }

	      csvPrinter.flush();
	      return new ByteArrayInputStream(out.toByteArray());
	    } catch (IOException e) {
	      throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
	    }
	  }
}
