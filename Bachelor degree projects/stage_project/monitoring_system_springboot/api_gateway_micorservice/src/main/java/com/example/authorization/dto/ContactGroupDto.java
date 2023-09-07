package com.example.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactGroupDto {


    private String name;
    private String alias;


    public ContactGroupDto(String name) {
        this.name = name;
    }
}
