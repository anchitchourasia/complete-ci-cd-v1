//package com.heg.dto;
//
//import java.time.LocalDate;
//
//
//
//public class DocumentRequestDTO {
//
//    
//    private String documentType;
//
//    
//    private String documentNo;
//
//    
//    private LocalDate startDate;
//
//    
//    private LocalDate expiryDate;
//
//    // Multipart file key (e.g. rcFile, insuranceFile)
//    private String fileKey;
//
//    public DocumentRequestDTO() {
//    }
//
//    public String getDocumentType() {
//        return documentType;
//    }
//
//    public void setDocumentType(String documentType) {
//        this.documentType = documentType;
//    }
//
//    public String getDocumentNo() {
//        return documentNo;
//    }
//
//    public void setDocumentNo(String documentNo) {
//        this.documentNo = documentNo;
//    }
//
//    public LocalDate getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(LocalDate startDate) {
//        this.startDate = startDate;
//    }
//
//    public LocalDate getExpiryDate() {
//        return expiryDate;
//    }
//
//    public void setExpiryDate(LocalDate expiryDate) {
//        this.expiryDate = expiryDate;
//    }
//
//    public String getFileKey() {
//        return fileKey;
//    }
//
//    public void setFileKey(String fileKey) {
//        this.fileKey = fileKey;
//    }
//}










package com.heg.dto;

import java.time.LocalDate;

public class DocumentRequestDTO {

	 private Long documentId;

	    private String documentType;

	    private String documentNo;

	    private LocalDate startDate;

	    private LocalDate expiryDate;

	    // Multipart file key
	    private String fileKey;

	    // Uploaded file name (optional)
	    private String fileName;

	    // Existing stored file name (used during update)
	    private String existingFile;
	    
	    

    public String getExistingFile() {
			return existingFile;
		}

		public void setExistingFile(String existingFile) {
			this.existingFile = existingFile;
		}

	public DocumentRequestDTO() {
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}