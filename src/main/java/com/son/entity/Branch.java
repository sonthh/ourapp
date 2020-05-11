package com.son.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "branches")
public class Branch extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "branch", cascade = {CascadeType.REMOVE})
    @JsonIgnoreProperties({"branch"})
    private List<Department> departments;

    @PreRemove
    private void preRemove() {
        departments.forEach(department -> department.setBranch(null));
    }
}
