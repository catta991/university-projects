package com.host.microservice.hostmicroservice.errorUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ErrorUtilObject {

    private boolean isError;
    private String message;
}
