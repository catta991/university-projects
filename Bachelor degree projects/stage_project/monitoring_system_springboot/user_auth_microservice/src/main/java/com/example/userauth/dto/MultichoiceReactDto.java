package com.example.userauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MultichoiceReactDto {

    private String label;
    private String value;


    @Override
    public String toString() {
        return "MultichoiceReactDto{" +
                "label='" + label + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
