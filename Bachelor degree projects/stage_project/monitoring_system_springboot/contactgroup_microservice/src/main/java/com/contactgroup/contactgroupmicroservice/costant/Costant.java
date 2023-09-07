package com.contactgroup.contactgroupmicroservice.costant;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class Costant {

    @Value("${checkMk.siteIp}")
    private String siteIp;
    @Value("${checkMk.siteName}")
    private String siteName;
    @Value("${url.react}")
    private String REACT_URL;

   /* @Value("${single.user.info.url}")
    private String SingleUserInfoUrl;*/


    @Value("${single.user.info.docker.url}")
    private String SingleUserInfoUrl;


    public String getALL_CONTACT_GROUP_URL(){
        return "http://"+siteIp+"/"+siteName+"/check_mk/api/1.0/domain-types/contact_group_config/collections/all";
    }
    public String getSINGLE_USER_URL(){
        return "http://"+siteIp+"/"+siteName+"/check_mk/api/1.0/objects/user_config/";
    }

    public String getAPPLY_CHANGES_URL(){
        return "http://"+siteIp+"/"+siteName+"/check_mk/api/1.0/domain-types/activation_run/actions/activate-changes/invoke";

    }

    public String getDELETE_MODIFY_CONTACT_GROUP_URL(){
        return "http://"+siteIp+"/"+siteName+"/check_mk/api/1.0/objects/contact_group_config/";

    }


    public String getSingleUserInfoUrl(){
        return SingleUserInfoUrl;
    }
}
