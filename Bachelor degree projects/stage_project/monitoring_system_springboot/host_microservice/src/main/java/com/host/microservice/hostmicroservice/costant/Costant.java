package com.host.microservice.hostmicroservice.costant;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
@NoArgsConstructor
public class Costant {


    @Value("${checkMk.siteIp}")
    private String siteIp;
    @Value("${checkMk.siteName}")
    private String siteName;
    @Value("${single.user.info.url}")
    private String SingleUserInfoUrl;

    @Value("${Ansible.server.ip}")
    private String AnsibleServerIp;

    @Value("${Ansible.server.port}")
    private String AnsibleServerPort;


    public String getCRUD_HOST_URL(){
        return "http://"+siteIp +"/"+siteName+"/check_mk/api/1.0/objects/host_config/";
    }

    public String getAPPLY_CHANGES_URL(){
        return "http://"+siteIp+"/"+siteName+"/check_mk/api/1.0/domain-types/activation_run/actions/activate-changes/invoke";

    }

    public String getSINGLE_USER_INFO_URL(){
        return SingleUserInfoUrl;
    }

    public String getALL_HOSTS_URL(){
       return "http://"+siteIp+"/"+siteName+"/check_mk/api/v0/domain-types/host_config/collections/all";
    }


    public String getALL_HOST_WITH_QUERY_URL(){
       return  "http://"+siteIp+"/"+siteName+"/check_mk/api/v0/domain-types/host/collections/all?query={query}";
    }



    public String getINSTALL_AGENT_BASE_URL(){
        return "http://"+siteIp+"/"+siteName+"/";
    }


    public String getINSTALL_PLUGINS_LINUX_BASE_URL() {
        return "http://"+siteIp+"/"+siteName+"/check_mk/agents/plugins/";
    }

    public String getINSTALL_PLUGINS_WINDOWS_BASE_URL() {
        return "http://"+siteIp+"/"+siteName+"/check_mk/windows/agents/plugins/";
    }


    public String getINSTALL_AGENT_URL(){
        return "http://"+AnsibleServerIp+":" +AnsibleServerPort+"/install/agent";
    }


    public String getINSTALL_PLUGINS_URL(){
        return "http://"+AnsibleServerIp+":"+AnsibleServerPort+"/install/plugin";
    }



    public String getSERVICE_DISCOVERY_URL(String hostName){
        return "http://"+siteIp+"/"+siteName+"/check_mk/api/v0/objects/host/" + hostName + "/actions/discover_services/invoke";
    }


    public String getUSERNAME_PASSWORD_SSH(){
        return "http://"+AnsibleServerIp+":"+AnsibleServerPort+"/add/credentials";
    }


    public String getUSERNAME_KEY_SSH(){
        return "http://"+AnsibleServerIp+":"+AnsibleServerPort+"/add/key";
    }
}
