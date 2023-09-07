package com.contactgroup.contactgroupmicroservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString

public class SingleUserDto {
    private String username;
    private String name;
    private String email;
    private String[] roles;
    private String[] contactGroups;
}
