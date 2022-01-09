package com.micropay.webcash.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "user_roles")
@Entity
@Data
public class UserRole {
    @Column(name = "user_role_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer userRoleId;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "allow_trans_posting", nullable = false, length = 1)
    private String allowTransPosting;

    @Column(name = "allow_charge_override", nullable = false, length = 1)
    private String allowChargeOverride;

    @Column(name = "allow_exch_rate_override", nullable = false, length = 1)
    private String allowExchRateOverride;

    @Column(name = "allow_teller_trans", nullable = false, length = 1)
    private String allowTellerTrans;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "created_by", nullable = false, length = 10, updatable = false)
    private String createdBy;

    @Column(name = "create_dt", nullable = false, updatable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createDate;

    @Column(name = "modified_by", length = 10, insertable = false)
    private String modifiedBy;

    @Column(name = "modify_dt", insertable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date modifyDate;

    @Column(name = "uuid", length = 36)
    private String uuid;

    @Transient
    private Boolean edit;
}