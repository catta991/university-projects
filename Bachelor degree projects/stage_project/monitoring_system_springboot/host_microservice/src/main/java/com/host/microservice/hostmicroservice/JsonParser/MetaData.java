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

public class MetaData {

    private String created_at;


    public String getOnlyDate(){
        return created_at.substring(0, 10);
    }

}
