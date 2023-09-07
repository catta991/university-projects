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
public class Labels {

    private String os_family;

    private String plugins_tag;

    private String agent_install_mode;
}
