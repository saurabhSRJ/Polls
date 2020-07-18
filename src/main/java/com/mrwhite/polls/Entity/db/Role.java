package com.mrwhite.polls.Entity.db;

import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.AuditOverride;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false")
@ToString(callSuper = true)
@Builder
@Table(name = "role", uniqueConstraints = @UniqueConstraint(columnNames = {"role_name","deleted"}, name = "uniqueRoleName"))
@AuditOverride(forClass = DaoEntity.class)
public class Role extends DaoEntity{
    @Column(name = "role_name")
    @NotNull
    private String roleName;
}
