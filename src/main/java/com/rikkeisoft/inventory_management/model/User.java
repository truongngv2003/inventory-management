package com.rikkeisoft.inventory_management.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "fullname", nullable = false)
	private String fullname;
	
	@Column(name = "position", nullable = false)
	private String position;

	@CreationTimestamp
	@Column(name = "creation_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@UpdateTimestamp
	@Column(name = "update_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted = false;
}