package com.xoriant.poc.errordashboard.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xoriant.poc.errordashboard.beans.JwtRequest;
import com.xoriant.poc.errordashboard.beans.JwtResponse;
import com.xoriant.poc.errordashboard.beans.PaginatedErrorList;
import com.xoriant.poc.errordashboard.dao.DashboardInfo;
import com.xoriant.poc.errordashboard.exception.ErrorNotFoundException;
import com.xoriant.poc.errordashboard.repository.DashboardInfoRepository;
import com.xoriant.poc.errordashboard.service.CSVService;
import com.xoriant.poc.errordashboard.service.FileStorageService;
import com.xoriant.poc.errordashboard.service.JwtUserDetailsService;
import com.xoriant.poc.errordashboard.service.PopulateDashboardService;
import com.xoriant.poc.errordashboard.utility.JwtTokenUtil;

@CrossOrigin
@RestController
public class ErrorDashboardController {

	private static final Logger logger = LoggerFactory.getLogger(ErrorDashboardController.class);

	@Autowired
	private PopulateDashboardService dashboardService;
	@Autowired
	CSVService fileService;
	@Autowired
	FileStorageService fileStorageService;
	@Autowired
	private DashboardInfoRepository repository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Value("${file.upload-dir}")
	String uploadDir;

	@GetMapping("/getAllErrors")
	public List<DashboardInfo> getErrorList() {
		List<DashboardInfo> errorList = (List<DashboardInfo>) repository.findAll();
		return errorList;
	}

	@CrossOrigin
	@GetMapping(value = "/getPaginatedErrorList")
	public PaginatedErrorList getPaginatedErrorList(@RequestParam(name = "page") int page,
			@RequestParam(name = "size") int size,
			@RequestParam(name = "sortBy", required = false, defaultValue = "lastUpdated") String colName,
			@RequestParam(name = "order", required = false, defaultValue = "1") int order,
			@RequestParam(name = "applicationName", required = false) String applicationName,
			@RequestParam(name = "transactionName", required = false) String transactionName,
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(name = "toDate", required = false) String toDate) {

		PaginatedErrorList pErrorList = dashboardService.getAggregatedFilteredErrorList(repository, page, size, colName,
				order, applicationName, transactionName, fromDate, toDate);

		return pErrorList;
	}

	@CrossOrigin
	@PostMapping(value = "/saveError")
	public DashboardInfo saveError(@RequestBody DashboardInfo dashboardInfo) {
		logger.info("Saving error");
		dashboardInfo.setLast_updated((new Date()));
		DashboardInfo returndashBoardInfo = repository.save(dashboardInfo);

		return returndashBoardInfo;
	}

	@DeleteMapping("/errors/{id}")
	public ResponseEntity<?> deleteError(@PathVariable(value = "id") Long bookId) throws ErrorNotFoundException {
		DashboardInfo dashboardInfo = repository.findById(bookId).orElseThrow(() -> new ErrorNotFoundException(bookId));

		repository.delete(dashboardInfo);

		return ResponseEntity.ok().build();
	}

	@CrossOrigin
	@GetMapping("/exportRecords")
	public ResponseEntity<Resource> getFile(
			@RequestParam(name = "applicationName", required = false) String applicationName,
			@RequestParam(name = "transactionName", required = false) String transactionName,
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(name = "toDate", required = false) String toDate) {
		String filename = "dashboardInfo.csv";
		InputStreamResource file = new InputStreamResource(
				fileService.load(applicationName, transactionName, fromDate, toDate));
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
		return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.parseMediaType("application/csv"))
				.body(file);
	}

	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public void upload(@RequestPart MultipartFile file) {
		if (null != file) {
			fileStorageService.storeFile(file);
		} else {
			logger.info("No File provided in Request Body.");
		}
	}

	@PostMapping(value = "/saveErrorFile", consumes = "multipart/form-data")
	public DashboardInfo saveErrorFile(@RequestParam(name = "applicationName", required = false) String applicationName,
			@RequestParam(name = "customerName", required = false) String customerName,
			@RequestParam(name = "errorRecord", required = false) String errorRecord,
			@RequestParam(name = "failed", required = false) int failed,
			@RequestParam(name = "success", required = false) int success,
			@RequestParam(name = "total", required = false) int total,
			@RequestParam(name = "fileUploaded", required = false) String fileUploaded,
			@RequestParam(name = "sourceSystem", required = false) String sourceSystem,
			@RequestParam(name = "targetSystem", required = false) String targetSystem,
			@RequestParam(name = "error_timestamp", required = false) String error_timestamp,
			@RequestParam(name = "transactionName", required = false) String transactionName,
			@RequestParam(name = "userName", required = false) String userName, @RequestPart MultipartFile file) {
		logger.info("Saving error");
		DashboardInfo dashboardInfo = new DashboardInfo(applicationName, customerName, transactionName, error_timestamp,
				fileUploaded, sourceSystem, targetSystem, total, success, failed, errorRecord, userName);
		dashboardInfo.setLast_updated((new Date()));
		DashboardInfo returndashBoardInfo = repository.save(dashboardInfo);

		if (null != file) {
			fileStorageService.storeFile(file);
		} else {
			logger.info("No File provided in Request Body.");
		}

		return returndashBoardInfo;
	}

	@PostMapping(value = "/downloadLogs")
	public ResponseEntity<Resource> downloadLogs(HttpServletRequest request) {
		String fileName = "results.csv";

		Resource resource = fileStorageService.loadFileAsResource(fileName);
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename());

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).headers(httpHeaders)
				.body(resource);
	}

	// AUTH Methods

	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
