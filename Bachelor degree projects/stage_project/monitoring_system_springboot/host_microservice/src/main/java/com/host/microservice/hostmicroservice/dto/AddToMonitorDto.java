package com.host.microservice.hostmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddToMonitorDto {

    private String  hostName;

    private String [] contactGroups;
}
