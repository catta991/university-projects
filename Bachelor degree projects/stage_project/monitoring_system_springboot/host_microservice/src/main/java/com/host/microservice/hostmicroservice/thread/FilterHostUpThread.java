package com.host.microservice.hostmicroservice.thread;

import com.host.microservice.hostmicroservice.dto.HostDto;
import com.host.microservice.hostmicroservice.errorUtil.ErrorUtilObject;
import com.host.microservice.hostmicroservice.JsonParser.HostsArray;
import com.host.microservice.hostmicroservice.lql.LqlQueryCreator;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class FilterHostUpThread extends Thread {

    private Thread t;
    private String threadName;
    private CountDownLatch latch;
    private String[] contactgroups;
    private ArrayList<HostDto> host;
    private ArrayList<HostDto> allHost;
    private ModelMapper mapper;
    private HttpEntity<?> body;
    private ErrorUtilObject errorObject;
    private boolean isSuperAdmin;
    private String url;

    private LqlQueryCreator lqlQueryCreator = new LqlQueryCreator();
    private RestTemplate restTemplate = new RestTemplate();


    public FilterHostUpThread(String name, CountDownLatch latch, String[] contactgroups, ArrayList<HostDto> host,
                              ArrayList<HostDto> allHost, ModelMapper mapper, HttpEntity<?> body, ErrorUtilObject errorObject,
                              boolean isSuperAdmin, String url) {
        threadName = name;
        this.latch = latch;
        this.contactgroups = contactgroups;
        this.host = host;
        this.allHost = allHost;
        this.mapper = mapper;
        this.body = body;
        this.errorObject = errorObject;
        this.isSuperAdmin = isSuperAdmin;
        this.url = url;

    }


    public void run() {

        try {



            String query = "";

            if(isSuperAdmin){
                query ="{" + lqlQueryCreator.filterHostByStatus0()+"}";
            }

            else {
                query = "{" + lqlQueryCreator.filterHostByContactgroupAndState0Query(contactgroups) + "}";

            }



            System.out.println("up query"+ query);
            System.out.println(url);



            ResponseEntity<HostsArray> filterHostUpResp = restTemplate.exchange(
                    url,
                    HttpMethod.GET, body, HostsArray.class, query);


            List<HostDto> dtos = filterHostUpResp.getBody().getValue()
                    .stream()
                    .map(host -> mapper.map(host, HostDto.class))
                    .collect(Collectors.toList());

            System.out.println("dtos " + dtos.size());

            for(HostDto hostDto: dtos){

                if(allHost.contains(hostDto)) {
                    HostDto dto = allHost.get(allHost.indexOf(hostDto));

                        dto.setStatus("UP");
                        host.add(dto);

                }
            }
            latch.countDown();
            System.out.println("Thread " +  threadName + " exiting.");
        }
         catch (Exception e) {
            latch.countDown();
            errorObject.setError(true);
            errorObject.setMessage(e.getMessage());
        }

    }

    public void start () {

        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}
