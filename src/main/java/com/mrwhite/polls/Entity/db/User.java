package com.mrwhite.polls.Entity.db;

import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Where;
import org.hibernate.envers.AuditOverride;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = 0")
@ToString(callSuper = true)
@AuditOverride(forClass = DaoEntity.class)
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email","deleted"}, name = "uniqueEmail")
    })
public class User extends DaoEntity {
    @Column(name = "name")
    @NotNull(message = "Name cannot be null")
    @Size(max = 40)
    private String name;

    @Column(name = "email")
    @NotNull
    @Email
    private String email;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "password")
    @NotNull
    private String password;
}
