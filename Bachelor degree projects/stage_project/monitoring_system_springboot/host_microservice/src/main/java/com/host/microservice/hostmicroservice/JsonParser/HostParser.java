package com.host.microservice.hostmicroservice.JsonParser;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonIgnoreProperties
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HostParser {

    @JsonProperty("id")
    private String hostName;

    @JsonProperty("title")
    private String alias;

    private Extensions extensions;


}
