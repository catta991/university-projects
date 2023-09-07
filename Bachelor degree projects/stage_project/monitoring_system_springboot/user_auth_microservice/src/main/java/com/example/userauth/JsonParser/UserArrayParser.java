package com.example.userauth.JsonParser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonIgnoreProperties
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserArrayParser {

    private UserParser[] value;
}
