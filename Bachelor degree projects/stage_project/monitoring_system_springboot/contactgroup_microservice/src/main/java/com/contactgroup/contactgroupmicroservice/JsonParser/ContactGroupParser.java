package com.contactgroup.contactgroupmicroservice.JsonParser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ContactGroupParser {

    @JsonProperty("value")
    Value[] value;
}
