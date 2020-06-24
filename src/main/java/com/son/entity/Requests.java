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
@Table(name = "requests")
public class Requests extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String type;

    @Column
    private String info;

    @Column
    private String reason;

    @Column
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiverId")
    private User receiver;

    @Column
    private Double amount;

    @Column
    private Date startDate;

    @Column
    private Date decidedDate;

    @Column
    private Date Date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personnelId")
    private Personnel personnel;
}
