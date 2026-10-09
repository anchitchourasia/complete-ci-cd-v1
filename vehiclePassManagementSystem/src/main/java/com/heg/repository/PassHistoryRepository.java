package com.heg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heg.model.PassHistory;


@Repository
public interface PassHistoryRepository extends JpaRepository<PassHistory, Long> {
   
	 List<PassHistory> findByPassNo(Long passNo);
	 
	 List<PassHistory> findByPassNoOrderByDateOfEntryDesc(Long passNo);
}
