package com.contactgroup.contactgroupmicroservice.JsonParser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonIgnoreProperties
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ContactOptions {
    private String email;
}
