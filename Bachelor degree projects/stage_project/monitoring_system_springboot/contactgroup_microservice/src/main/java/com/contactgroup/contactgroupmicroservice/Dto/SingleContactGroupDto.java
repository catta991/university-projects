package com.contactgroup.contactgroupmicroservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class SingleContactGroupDto {

    private String name;
    private String alias;
}
