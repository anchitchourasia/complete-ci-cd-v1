package com.heg.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.heg.dto.PassRegistryListDTO;
import com.heg.dto.PassRegistryRequestDTO;
import com.heg.dto.PassRegistryResponseDTO;
import com.heg.dto.StatusUpdateRequestDTO;
import com.heg.service.IPassRegistryService;

@RestController
@RequestMapping("/api/passes")
public class PassRegistryController {

    @Autowired
    private IPassRegistryService service;

    private static final String API_KEY = "VPMS_SECRET_KEY_2026";

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hii VPMS");
    }

    @PostMapping(
        value = "/save",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<PassRegistryResponseDTO> save(
            @RequestPart("request") PassRegistryRequestDTO request,
            MultipartHttpServletRequest multipartRequest)
            throws IOException {

        System.out.println(request);
        System.out.println("Status : " + request.getStatus());
        System.out.println("Contractor Code : " + request.getContractorCode());

        Map<String, MultipartFile> files = multipartRequest.getFileMap();
        System.out.println("Files received : " + files.keySet());

        return ResponseEntity.ok(service.save(request, files));
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PassRegistryResponseDTO> update(
            @PathVariable Long id,
            @RequestPart("request") PassRegistryRequestDTO request,
            MultipartHttpServletRequest multipartRequest)
            throws IOException {

        Map<String, MultipartFile> files = multipartRequest.getFileMap();

        System.out.println("========== FILES ==========");

        files.forEach((k, v) -> {
            System.out.println(k + " -> " + v.getOriginalFilename());
        });

        return ResponseEntity.ok(
                service.update(id, request, files)
        );
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllPasses(
            @RequestHeader(value = "x-api-key", required = false) String apiKey) {

        if (!API_KEY.equals(apiKey)) {
            return ResponseEntity.status(401).body("Invalid API Key");
        }

        List<PassRegistryResponseDTO> passes = service.getAll();
        return ResponseEntity.ok(passes);
    }
    
    
    
    @GetMapping("/listV1")
    public List<PassRegistryListDTO> getAllPasses() {
        return service.getPassRegistryDetails();
    }
    
    
    @GetMapping("/list/{id}")
    public ResponseEntity<?> getPassById(
            @PathVariable Long id,
            @RequestHeader(value = "x-api-key", required = false) String apiKey) {

        if (!API_KEY.equals(apiKey)) {
            return ResponseEntity.status(401).body("Invalid API Key");
        }

        PassRegistryResponseDTO pass = service.getPassById(id);
        return ResponseEntity.ok(pass);
    }
    

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequestDTO request) {

        return ResponseEntity.ok(service.updateStatus(id, request));
    }
    
    
    
    

//    @GetMapping("/documents/download/{documentId}")
//    public ResponseEntity<?> downloadDocument(
//            @PathVariable Long documentId,
//            @RequestHeader(value = "x-api-key", required = false) String apiKey) throws IOException {
//
//        if (!API_KEY.equals(apiKey)) {
//            return ResponseEntity.status(401).body("Invalid API Key");
//        }
//
//        byte[] fileData = service.getDocumentBytes(documentId);
//        String fileName = service.getDocumentFileName(documentId);
//        String contentType = service.getDocumentContentType(documentId);
//
//        ByteArrayResource resource = new ByteArrayResource(fileData);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//                .contentType(
//                        (contentType != null && !contentType.isBlank())
//                                ? MediaType.parseMediaType(contentType)
//                                : MediaType.APPLICATION_OCTET_STREAM
//                )
//                .contentLength(fileData.length)
//                .body(resource);
//    }
    
    
    @GetMapping("/{id}")
    public PassRegistryResponseDTO getPassById(
            @PathVariable Long id){

        return service.getPassById(id);
    }
    
    
    @GetMapping("/documents/download/{documentId}")
    public ResponseEntity<?> downloadDocument(
            @PathVariable Long documentId,
            @RequestHeader(value = "x-api-key", required = false) String apiKey) throws IOException {

        if (!API_KEY.equals(apiKey)) {
            return ResponseEntity.status(401).body("Invalid API Key");
        }

        byte[] fileData = service.getDocumentBytes(documentId);
        String fileName = service.getDocumentFileName(documentId);
        String contentType = service.getDocumentContentType(documentId);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(fileData);
    }
}