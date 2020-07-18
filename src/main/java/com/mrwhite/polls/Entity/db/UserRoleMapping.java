package com.mrwhite.polls.Entity.db;

import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.AuditOverride;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = DaoEntity.class)
@Where(clause = "deleted = false")
@Table(name = "user_role_mapping", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","role_id","deleted"}, name = "uniqueUserRoleMapping"))
//applies unique constraint on multiple columns, similar to composite key
@ToString(callSuper = true)
public class UserRoleMapping extends DaoEntity{
    @NotNull
    @Column(name = "user_id")
    private Long userId;
    @NotNull
    @Column(name = "role_id")
    private Long roleId;
}
