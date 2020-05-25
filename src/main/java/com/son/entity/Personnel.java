package com.son.entity;

import com.son.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "personnel")
public class Personnel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String fullName;

    @Column
    private String phoneNumber;

    @Column
    private String email;

    @Column
    private Date birthDay;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne
    @JoinColumn(name = "identificationId")
    private Identification identification;

    @OneToOne
    @JoinColumn(name = "bankInfoId")
    private BankInfo bankInfo;

    @OneToOne
    @JoinColumn(name = "passportId")
    private Passport passport;

    @Column
    private String position;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departmentId")
    private Department department;

    @OneToOne
    @JoinColumn(name = "workingTimeId")
    private WorkingTime workingTime;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany
    @JoinColumn(name = "personnelId")
    private List<Qualification> qualifications;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany
    @JoinColumn(name = "personnelId")
    private List<Certification> certifications;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany
    @JoinColumn(name = "personnelId")
    private List<WorkHistory> workHistories;

    @OneToOne
    @JoinColumn(name = "contactInfoId")
    private ContactInfo contactInfo;

    @OneToOne
    @JoinColumn(name = "healthyStatusId")
    private HealthyStatus healthyStatus;

    @OneToOne
    @JoinColumn(name = "salaryId")
    private Salary salary;

    @OneToOne
    @JoinColumn(name = "additionalInfoId")
    private AdditionalInfo additionalInfo;

}
