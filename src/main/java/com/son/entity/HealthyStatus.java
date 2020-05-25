package com.son.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "healthy_status")
public class HealthyStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Double height;

    @Column
    private Double weight;

    @Column
    private String bloodGroup;

    @Column
    private String healthyStatus;

    @Column
    private String lastCheckDate;

}
