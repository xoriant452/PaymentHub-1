package com.xoriant.poc.errordashboard.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.xoriant.poc.errordashboard.exception.FileStorageException;

@Service
public class FileStorageService {

	private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

	@Value("${file.upload-dir}")
	String uploadDir;

	public void storeFile(MultipartFile file) {
		Path fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
		try {
			Files.createDirectories(fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the upload directory on server.", ex);
		}

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Path targetLocation = fileStorageLocation.resolve(fileName);
		try {
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			logger.info("Saving the received Error File at:: " + targetLocation.toString());
		} catch (IOException e) {
			logger.error("Failed to store file::" + e);
		}
	}

	public Resource loadFileAsResource(String fileName) {
		Path fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
		Resource resource=null;
		try {
			Path filePath =fileStorageLocation.resolve(fileName).normalize();
			resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				logger.error("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			logger.error("File not found " + fileName, ex);
		}
		
		return resource;
	}

}
