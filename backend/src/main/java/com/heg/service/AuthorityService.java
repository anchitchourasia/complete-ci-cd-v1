package com.heg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heg.dto.AuthorityDTO;
import com.heg.repository.AuthorityRepository;

@Service
public class AuthorityService {

	@Autowired
    private AuthorityRepository authorityRepository;

    public List<AuthorityDTO> findByEmpCode(String empCode) {
        return authorityRepository.findByEmpCode(empCode);
    }

}
