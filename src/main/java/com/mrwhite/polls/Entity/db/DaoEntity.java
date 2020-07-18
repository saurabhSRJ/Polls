package com.mrwhite.polls.Entity.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@MappedSuperclass //Designates a class whose mapping information is applied to the entities that inherit from it.
// A mapped superclass has no separate table defined for it.
@Data
@JsonIgnoreProperties(value = {"createdAt", "lastUpdatedOn"}, allowGetters = true)
@EntityListeners(AuditingEntityListener.class)
public class DaoEntity {
    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "created_by", updatable = false)
    @CreatedBy
    protected String createdBy;

    @Column(name = "last_updated_by")
    @LastModifiedBy
    protected String lastUpdatedBy;

    @Column(name = "deleted")
    @NotNull
    protected Long deleted;

    @Column(name = "created_at", updatable = false)
    @NotNull
    @CreatedDate
    protected Date createdAt;

    @LastModifiedDate
    @Column(name = "last_updated_at")
    @NotNull
    protected Date lastUpdatedAt;

    @PrePersist
    public void beforeInsert(){
        this.deleted = 0L;
    }
}
