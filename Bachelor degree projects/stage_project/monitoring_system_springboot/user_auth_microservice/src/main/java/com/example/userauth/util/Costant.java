package com.example.userauth.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;



@Component

@NoArgsConstructor


public class Costant {

    @Value("${checkMk.siteIp}")
    private String siteIp;
    @Value("${checkMk.siteName}")
    private String siteName;
    @Value("${url.react}")
    private String REACT_URL;

   // private  String ALL_SAVE_USER_URL = "http://"+siteIp+"/"+siteName+"/check_mk/api/1.0/domain-types/user_config/collections/all";
   // private  String APPLY_CHANGES_URL= "http://"+siteIp+"/"+siteName+"/check_mk/api/1.0/domain-types//activation_run/actions/activate-changes/invoke";
    //private  String DELETE_MODIFY_USER_URL = "http://"+siteIp+"/"+siteName+"/check_mk/api/1.0/objects/user_config/";


    public String getALL_SAVE_USER_URL() {
        return "http://"+siteIp+"/"+siteName+"/check_mk/api/1.0/domain-types/user_config/collections/all";

    }


   public String getSINGLE_USER_URL(){
       return "http://"+siteIp+"/"+siteName+"/check_mk/api/1.0/objects/user_config/";
   }

    public String getAPPLY_CHANGES_URL(){
        return "http://"+siteIp+"/"+siteName+"/check_mk/api/1.0/domain-types/activation_run/actions/activate-changes/invoke";

    }

    public String getDELETE_MODIFY_USER_URL(){
        return "http://"+siteIp+"/"+siteName+"/check_mk/api/1.0/objects/user_config/";

    }

    public String getREACT_URL(){
        return  REACT_URL;
    }

    public String getSiteName(){
        return siteName;
    }
}
