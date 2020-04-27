package com.son.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.son.model.Gender;
import com.son.util.jpa.StringListConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

//@JsonSerialize(using = UserJsonSerializer.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    public enum Status {
        ACTIVE, INACTIVE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column
    private String fullName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @Column
    private Date birthDay;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(unique = true)
    private String identification;

    @Column
    private String address;

    @Column
    private String avatar;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false, columnDefinition = "bit(1) default 1")
    private Boolean shouldSendNotification = true;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> notificationTypes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "roleId")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "roleId"})}
    )
    private List<Role> roles;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof User) {
            User user = (User) obj;
            return user.getId().equals(id);
        }

        return false;
    }
}
