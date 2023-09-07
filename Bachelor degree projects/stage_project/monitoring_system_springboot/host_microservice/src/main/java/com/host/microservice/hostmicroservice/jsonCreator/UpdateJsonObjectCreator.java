package com.host.microservice.hostmicroservice.jsonCreator;

import com.host.microservice.hostmicroservice.dto.HostDto;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UpdateJsonObjectCreator {


    // devo sempre passare l'os dell host
    public String getUpdateJsonObject(HostDto hostDto){


        System.out.println(hostDto.getPlugins_tag() == null);

        JSONObject updateHost = new JSONObject();

       JSONObject updateAttributes = new JSONObject();

       if(hostDto.getAlias() != null && !hostDto.getAlias().equals(""))
           updateAttributes.put("alias", hostDto.getAlias());

       if(hostDto.getContactgroups() != null ) {
           JSONObject contactGroups = new JSONObject();
           contactGroups.put("groups", hostDto.getContactgroups());
           contactGroups.put("use", true);
           updateAttributes.put("contactgroups", contactGroups);
       }

        JSONObject labels = new JSONObject();
      // if(hostDto.getPlugins_tag()!= null) {
           labels.put("plugins_tag", hostDto.getPlugins_tag());
           labels.put("os_family", hostDto.getOs_family());
           labels.put("agent_install_mode", hostDto.getAgent_install_mode());
           updateAttributes.put("labels", labels);
       //}
        updateHost.put("update_attributes", updateAttributes);

       System.out.println(updateHost.toString());

        return  updateHost.toString();
    }



    public String getInstallAgentUpdateJsonObject(){

        JSONObject updateHost = new JSONObject();

        JSONObject updateAttributes = new JSONObject();

        updateAttributes.put("tag_agent", "cmk-agent");

        updateHost.put("update_attributes", updateAttributes);

        System.out.println(updateHost.toString());

        return  updateHost.toString();
    }

    public String addToMonitorJsonObject (String [] contactGroupsString){
        JSONObject updateHost = new JSONObject();

        JSONObject updateAttributes = new JSONObject();

        JSONObject contactGroups = new JSONObject();
        contactGroups.put("groups", contactGroupsString);
        contactGroups.put("use", true);
        updateAttributes.put("contactgroups", contactGroups);

        updateHost.put("update_attributes", updateAttributes);
        return  updateHost.toString();
    }


    public String setSSHCredentials(HostDto hostDto){

        JSONObject updateHost = new JSONObject();

        JSONObject updateAttributes = new JSONObject();

        JSONObject labels = new JSONObject();
        labels.put("plugins_tag", hostDto.getPlugins_tag());
        labels.put("os_family", hostDto.getOs_family());
        labels.put("agent_install_mode", hostDto.getAgent_install_mode());
        updateAttributes.put("labels", labels);

        updateHost.put("update_attributes", updateAttributes);

        updateHost.put("update_attributes", updateAttributes);
        return  updateHost.toString();
    }


}
