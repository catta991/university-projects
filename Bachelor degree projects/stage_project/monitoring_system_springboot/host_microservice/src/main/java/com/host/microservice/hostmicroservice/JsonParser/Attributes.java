package com.host.microservice.hostmicroservice.JsonParser;

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
public class Attributes {

    private Contactgroups contactgroups;

    private MetaData meta_data;

    private String ipaddress;

    private String tag_agent;

    private Labels labels;
}
