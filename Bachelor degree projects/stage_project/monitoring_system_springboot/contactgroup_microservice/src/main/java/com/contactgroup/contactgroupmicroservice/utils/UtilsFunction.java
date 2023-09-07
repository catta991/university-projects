package com.contactgroup.contactgroupmicroservice.utils;


import com.contactgroup.contactgroupmicroservice.Dto.SingleContactGroupDto;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class UtilsFunction {

    public HttpHeaders getHeaders(String auth){



        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", auth);

        return  headers;

    }






    public HttpHeaders getHeadersWithEtag(String auth, String tag){

        String[] headerContent = auth.split(" ");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer "+ headerContent[1]+" "+headerContent[2]);
        headers.add("If-Match", tag);

        return  headers;

    }




public String modifyContactGroupString(String newAlias){


    JSONObject jsonObject = new JSONObject();

    jsonObject.put("alias", newAlias);

    String jsonObjectString = jsonObject.toString();

    return  jsonObjectString;
}


public String createNewContactGroupString(SingleContactGroupDto singleContactGroupDto){
    JSONObject jsonObject = new JSONObject();

    jsonObject.put("name", singleContactGroupDto.getName());

    jsonObject.put("alias", singleContactGroupDto.getAlias());

    String jsonObjectString = jsonObject.toString();

    return  jsonObjectString;
}


}


