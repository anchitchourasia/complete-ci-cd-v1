//package com.heg.service;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//@Service
//public class FileStorageServiceImpl implements IFileStorageService{
//
//	@Value("${app.upload.dir}")
//	private String uploadPath;
//	
//	@Override
//	public String upload(MultipartFile file) throws IOException {
//		  
//		Files.createDirectories(Paths.get(uploadPath));
//		
//		 String originalName = file.getOriginalFilename();
//		 
//		 String extension = "";
//		 
//		 if(originalName.contains(".")) {
//	            extension = originalName.substring(originalName.lastIndexOf("."));
//	       }
//
//		 String generatedName =
//	                LocalDateTime.now()
//	                        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
//	                        + extension;
//
//	        Path target = Paths.get(uploadPath, generatedName);
//
//	        Files.copy(file.getInputStream(),
//	                target,
//	                StandardCopyOption.REPLACE_EXISTING);
//
//	        return generatedName;
//		  
//	
//	}
//
//}







package com.heg.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements IFileStorageService {

    @Value("${app.upload.dir}")
    private String uploadPath;

    @Override
    public String upload(MultipartFile file) throws IOException {

        Files.createDirectories(Paths.get(uploadPath));

        String originalName = file.getOriginalFilename();
        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        String generatedName =
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                        + extension;

        Path target = Paths.get(uploadPath, generatedName);

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return generatedName;
    }

    @Override
    public byte[] download(String fileName) throws IOException {
        Path filePath = Paths.get(uploadPath, fileName).normalize();

        if (!Files.exists(filePath)) {
            throw new RuntimeException("File not found: " + fileName);
        }

        return Files.readAllBytes(filePath);
    }

    @Override
    public String getContentType(String fileName) throws IOException {
        Path filePath = Paths.get(uploadPath, fileName).normalize();
        String contentType = Files.probeContentType(filePath);
        return contentType != null ? contentType : "application/octet-stream";
    }
}
