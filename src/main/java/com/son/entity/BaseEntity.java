package com.son.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.son.handler.UserJsonSerializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {
    @CreatedBy
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "createdBy")
    @JsonSerialize(using = UserJsonSerializer.class)
    protected User createdBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    protected Date createdDate;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lastModifiedBy")
    @JsonSerialize(using = UserJsonSerializer.class)
    protected User lastModifiedBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    protected Date lastModifiedDate;
}
