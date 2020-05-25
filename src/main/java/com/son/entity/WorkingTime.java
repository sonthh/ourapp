package com.son.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "working_time")
public class WorkingTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Date startWorkDate;

    @Column
    private Date signContractDate;

    @Column
    private String workType;

    @Column
    private String note;

    @Column
    private Boolean isStopWork;

    @Column
    private String reasonStopWork;

    @Column
    private Date stopWorkDate;
}
