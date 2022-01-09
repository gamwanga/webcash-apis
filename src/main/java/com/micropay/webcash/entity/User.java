package com.micropay.webcash.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "user_ref", indexes = {
        @Index(name = "user_ref_un", columnList = "login_id", unique = true)
})
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "login_id", length = 20)
    private String loginId;

    @Column(name = "employee_number", length = 30)
    private String employeeNumber;

    @Column(name = "cust_no", length = 30)
    private String customerNo;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender", length = 1)
    private String gender;

    @Column(name = "allow_multi_session", length = 1)
    private String allowMultipleSessions;

    @Column(name = "email")
    private String emailAddress;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "security_role_id")
    private Integer securityRoleId;

    @Column(name = "start_dt")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    @Column(name = "end_dt")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    @Column(name = "failed_login_attempt")
    private Integer failedLoginAttempts;

    @Column(name = "status")
    private String status;

    @Column(name = "effective_dt")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date effectiveDt;

    @Column(name = "password_changed_flag", length = 1)
    private String passwordChangedFlag;

    @Column(name = "password_expiry_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date passwordExpiryDate;

    @Column(name = "allow_branch_change", length = 1)
    private String allowBranchChange;

    @Column(name = "last_logon_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date lastLogonDate;

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

    @Transient
    private Boolean edit;

}