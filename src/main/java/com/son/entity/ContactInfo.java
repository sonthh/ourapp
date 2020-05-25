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
@Table(name = "contact_info")
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String homeNumber;

    @Column
    private String skype;

    @Column
    private String facebook;

    @Column
    private String urgentContact;

    @Column
    private String relation;

    @Column
    private String urgentPhoneNumber;

    @Column
    private String address;

    @Column
    private String village;

    @Column
    private String district;

    @Column
    private String province;

}
