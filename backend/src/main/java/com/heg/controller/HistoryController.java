package com.heg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heg.dto.PassHistoryResponseDTO;
import com.heg.service.IPassHistoryService;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private final IPassHistoryService historyService;
    
    public HistoryController(IPassHistoryService service) {
        this.historyService = service;
    }

    @GetMapping("/{passNo}")
    public ResponseEntity<List<PassHistoryResponseDTO>>
            getHistory(@PathVariable Long passNo) {
System.out.println("History COntroller called :: ");
        return ResponseEntity.ok(
        		historyService.findHistoryByPassNo(passNo));

    }
    
}