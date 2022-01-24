package com.micropay.webcash.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Table(name = "mbl_credit_app")
@Entity
@Data
public class CreditApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_app_id", nullable = false)
    private Integer id;

    @Column(name = "credit_type", nullable = false, length = 20)
    private String creditType;

    @Column(name = "start_date", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    @Column(name = "repay_term", nullable = false)
    private Integer repayTerm;

    @Column(name = "repay_period", nullable = false, length = 15)
    private String repayPeriod;

    @Column(name = "nxt_pmt_amt")
    private BigDecimal nextPmtAmt;

    @Column(name = "status")
    private String status;

    @Column(name = "cust_id", nullable = false)
    private Integer custId;

    @Column(name = "created_by", nullable = false, length = 10)
    private String createdBy;

    @Column(name = "create_date", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createDate;

    @Column(name = "modified_by", length = 30)
    private String modifiedBy;

    @Column(name = "modified_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date modifiedDate;

    @Column(name = "row_version", nullable = false)
    private Integer rowVersion;

    @Column(name = "appl_amt", nullable = false, precision = 6)
    private BigDecimal applAmt;

}