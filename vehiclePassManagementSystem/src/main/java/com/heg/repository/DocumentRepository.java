package com.heg.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heg.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
  
}