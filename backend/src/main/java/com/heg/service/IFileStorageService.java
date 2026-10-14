//package com.heg.service;
//
//import java.io.IOException;
//
//import org.springframework.web.multipart.MultipartFile;
//
//public interface IFileStorageService {
//	String upload(MultipartFile file) throws IOException;
//}






package com.heg.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {
    String upload(MultipartFile file) throws IOException;

    byte[] download(String fileName) throws IOException;

    String getContentType(String fileName) throws IOException;
}