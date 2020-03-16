package com.son.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.son.security.Credentials;
import com.son.util.jpa.StringListConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = { @JoinColumn(name = "userId") },
        inverseJoinColumns = { @JoinColumn(name = "roleId") },
        uniqueConstraints = { @UniqueConstraint(columnNames = {"userId", "roleId"}) }
    )
    private List<Role> roles;

    @Column(nullable = false, columnDefinition = "bit(1) default 1")
    private Boolean shouldSendNotification;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> notificationTypes;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }

        if (obj instanceof User) {
            User user = (User) obj;
            return user.getId().equals(id);
        }

        return false;
    }

    public boolean sameCredentials(Credentials credentials) {
        return credentials.getId().equals(id);
    }
}
