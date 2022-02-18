package com.micropay.webcash.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Table(name = "loan_repayment")
@Entity
@Data
public class LoanRepayment {
    @Column(name = "repayment_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer repaymentId;

    @Column(name = "acct_no", nullable = false, length = 20)
    private String acctNo;

    @Column(name = "reference_no", nullable = false, length = 30)
    private String referenceNo;

    @Column(name = "amount", nullable = false, precision = 12, scale = 6)
    private Double amount;

    @Column(name = "trans_date", nullable = false)
    private Timestamp transDate;

    @Column(name = "channel_code", length = 30)
    private String channelCode;

}