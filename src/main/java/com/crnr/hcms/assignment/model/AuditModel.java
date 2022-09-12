package com.crnr.hcms.assignment.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Namadev
 * 
 * @version 1.0
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditModel {
		
	@CreatedDate
	@Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

	@LastModifiedDate
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;
    
    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @PrePersist
    private void beforeSaving() {
    	createdDate = getTimeStamp();
        updatedDate = getTimeStamp();
        isDeleted = false;
    }

    @PreUpdate
    private void beforeUpdating() {
    	updatedDate = getTimeStamp();
    }
    
    private static LocalDateTime getTimeStamp() {
		return LocalDateTime.now(ZoneId.of("UTC"));
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
     
}