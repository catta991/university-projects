package com.oauth.implementation.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Getter

// classe di appoggio per parsare un oggetto JSON
public class JsonParser {


    private String message;

}