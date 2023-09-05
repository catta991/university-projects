package com.oauth.implementation.util;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Getter
public class KeyCloackJsonParser {

    private String access_token;
}
