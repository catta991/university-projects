package com.contactgroup.contactgroupmicroservice.JsonParser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@JsonIgnoreProperties
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserParser {
    @JsonProperty("id")
    private String username;
    private Extensions extensions;


}
