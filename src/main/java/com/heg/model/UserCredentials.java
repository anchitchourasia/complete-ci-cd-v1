package com.heg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "VPMS_USER_CREDENTIALS")
public class UserCredentials {

    @Id
    @Column(name = "EMP_NO")
    private Long empNo;

    @Column(name = "PASSWORD_HASH")
    private byte[] passwordHash;

    @Column(name = "ROLE", length = 30)
    private String role;

    public UserCredentials() {}

    public UserCredentials(Long empNo, byte[] passwordHash, String role) {
        this.empNo = empNo;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public Long getEmpNo() {
        return empNo;
    }

    public void setEmpNo(Long empNo) {
        this.empNo = empNo;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}