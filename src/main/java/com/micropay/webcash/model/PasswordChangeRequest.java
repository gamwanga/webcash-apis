package com.micropay.webcash.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PasswordChangeRequest {
    private Integer employeeId;
    private String userName;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
