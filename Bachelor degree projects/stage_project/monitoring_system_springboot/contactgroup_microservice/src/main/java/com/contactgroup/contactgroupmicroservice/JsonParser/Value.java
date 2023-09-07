package com.contactgroup.contactgroupmicroservice.JsonParser;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonIgnoreProperties
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Value {

    @JsonProperty("title")
    String alias;

    @JsonProperty("href")
    String name;



    public String getNameFromUrl(){

        return name.substring(72);
    }
}
