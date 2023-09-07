package com.host.microservice.hostmicroservice.JsonParser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class HostsArray {

    private List<HostParser> value;
}
