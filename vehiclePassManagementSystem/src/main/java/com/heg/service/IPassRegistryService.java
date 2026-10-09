////package com.heg.service;
////
////import java.io.IOException;
////import java.util.List;
////import java.util.Map;
////
////import org.springframework.web.multipart.MultipartFile;
////
////import com.heg.dto.PassRegistryRequestDTO;
////import com.heg.dto.PassRegistryResponseDTO;
////import com.heg.dto.StatusUpdateRequestDTO;
////
////public interface IPassRegistryService {
////	 
////	PassRegistryResponseDTO  save(PassRegistryRequestDTO dto,
////            Map<String, MultipartFile> files) throws IOException;
////	
////	List<PassRegistryResponseDTO> getAll();
////	
////	PassRegistryResponseDTO update(
////	        Long id,
////	        PassRegistryRequestDTO request,
////	        Map<String, MultipartFile> files) throws IOException;
////	
////	PassRegistryResponseDTO updateStatus(
////	        Long id,
////	        StatusUpdateRequestDTO request
////	);
////	    
////}
//
//
//
//
//
//
//
//package com.heg.service;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.web.multipart.MultipartFile;
//
//import com.heg.dto.PassRegistryRequestDTO;
//import com.heg.dto.PassRegistryResponseDTO;
//import com.heg.dto.StatusUpdateRequestDTO;
//
//public interface IPassRegistryService {
// 
//    PassRegistryResponseDTO save(
//            PassRegistryRequestDTO dto,
//            Map<String, MultipartFile> files) throws IOException;
//
//    List<PassRegistryResponseDTO> getAll();
//
//    PassRegistryResponseDTO update(
//            Long id,
//            PassRegistryRequestDTO request,
//            Map<String, MultipartFile> files) throws IOException;
//
//    PassRegistryResponseDTO updateStatus(
//            Long id,
//            StatusUpdateRequestDTO request
//    );
//
//    byte[] getDocumentBytes(Long documentId) throws IOException;
//
//    String getDocumentFileName(Long documentId);
//
//    String getDocumentContentType(Long documentId);
//}






package com.heg.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.heg.dto.PassRegistryListDTO;
import com.heg.dto.PassRegistryRequestDTO;
import com.heg.dto.PassRegistryResponseDTO;
import com.heg.dto.StatusUpdateRequestDTO;

public interface IPassRegistryService {

    PassRegistryResponseDTO save(
            PassRegistryRequestDTO dto,
            Map<String, MultipartFile> files) throws IOException;

    List<PassRegistryResponseDTO> getAll();

    PassRegistryResponseDTO update(
            Long id,
            PassRegistryRequestDTO request,
            Map<String, MultipartFile> files) throws IOException;

    PassRegistryResponseDTO updateStatus(
            Long id,
            StatusUpdateRequestDTO request
    );

    byte[] getDocumentBytes(Long documentId) throws IOException;

    String getDocumentFileName(Long documentId);

    String getDocumentContentType(Long documentId) throws IOException;
    
    
    
    List<PassRegistryListDTO> getPassRegistryDetails();
    
    public PassRegistryResponseDTO getPassById(Long id);
    
    
    
    
}
