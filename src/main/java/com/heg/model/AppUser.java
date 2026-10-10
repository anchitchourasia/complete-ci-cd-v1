package com.heg.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "VPMS_AUTHORITY")
public class AppUser {
	@Id
    @Column(name = "USERNAME", length = 20)
    private String username;

    @Column(name = "PASSWORD", nullable = false,length = 100)
    private String password;

    @Column(name = "ROLE", nullable = false, length = 10)
    private String role;

    @Column(name = "STATUS", nullable = false,length = 10)
    private String status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AppUser(String username, String password, String role, String status) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.status = status;
	}

	public AppUser() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
