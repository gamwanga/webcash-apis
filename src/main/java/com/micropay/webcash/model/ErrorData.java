package com.micropay.webcash.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorData {

    private String code;
    private String error;
    private String message;
}
