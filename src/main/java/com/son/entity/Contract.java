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
@Table(name = "contracts")
public class Contract extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personnelId")
    private Personnel personnel;

    @Column
    private String contractType;

    @Column
    private String taxType;

    @Column
    private String contractNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "signerId")
    private User signer;

    @Column
    private String workType;

    @Column
    private Date validDate;

    @Column
    private Date expiredDate;

    @Column
    private Integer probationTime;

    @Column
    private Integer salary;

    @Column
    private String workAt;

    @Column
    private Date startWorkDate;

    @Column
    private String note;
}
