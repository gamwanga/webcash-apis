package com.micropay.webcash.utils;
import com.micropay.webcash.model.TxnResult;

public class CommonResponse {
    public  static TxnResult getUndefinedError() {
        return TxnResult.builder().code("-99")
                .message("A system related error has occurred").build();
    }
    public  static TxnResult getDBUndefinedError() {
        return TxnResult.builder().code("-99")
                .message("A system related error has occurred").build();
    }
}
