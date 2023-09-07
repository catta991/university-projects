package com.host.microservice.hostmicroservice.JsonParser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonIgnoreProperties
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Extensions {


    private Attributes attributes;
}
