package com.host.microservice.hostmicroservice.lql;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class LqlQueryCreator {







    public String filterHostByContactgroupQuery(String[] contactGroups){

        String query = "\"op\": \"and\", \"expr\": [{\"op\": \"or\", \"expr\": [";

        String arguments = "";

        for(int i=0; i<contactGroups.length-1; i++){
            arguments+="{\"op\": \"<=\", \"left\": \"contact_groups\", \"right\": \""+contactGroups[i]+"\"}, ";
        }


        arguments+="{\"op\": \"<=\", \"left\": \"contact_groups\", \"right\": \""+contactGroups[contactGroups.length-1]+"\"}]}, ";


        return  query+arguments;


    }

    //state 0 UP   state 1 DOWN  state 2 UNREACH
    public String filterHostByContactgroupAndState0Query(String[] contactGroups){

        String stateQuery = "{\"op\": \"=\", \"left\": \"state\", \"right\": \"0\"}";

        return filterHostByContactgroupQuery(contactGroups)+stateQuery+"]";
    }

    public String filterHostByContactgroupAndState1Query(String[] contactGroups){

        String stateQuery = "{\"op\": \"=\", \"left\": \"state\", \"right\": \"1\"}";

        return filterHostByContactgroupQuery(contactGroups)+stateQuery+"]";
    }

    public String filterHostByContactgroupAndState2Query(String[] contactGroups){

        String stateQuery = "{\"op\": \"=\", \"left\": \"state\", \"right\": \"2\"}";

        return filterHostByContactgroupQuery(contactGroups)+stateQuery+"]";
    }


    public String filterHostByStatus0(){
        return "\"op\": \"=\", \"left\": \"state\", \"right\": \"0\"";
    }

    public String filterHostByStatus1(){
        return "\"op\": \"=\", \"left\": \"state\", \"right\": \"1\"";
    }

    public String filterHostByStatus2(){
        return "\"op\": \"=\", \"left\": \"state\", \"right\": \"2\"";
    }

}
