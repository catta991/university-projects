package com.host.microservice.hostmicroservice.thread;

import com.host.microservice.hostmicroservice.dto.HostDto;
import com.host.microservice.hostmicroservice.errorUtil.ErrorUtilObject;
import com.host.microservice.hostmicroservice.JsonParser.HostsArray;
import com.host.microservice.hostmicroservice.mapper.ModelMapperHostDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class GetAllHostsThread extends Thread{

    private Thread t;


    private HttpEntity<?> body;
    private CountDownLatch latch;
    private ErrorUtilObject errorHandler;
    private List<HostDto> allHosts;
    private String url;

    private RestTemplate restTemplate= new RestTemplate();
    private final ModelMapperHostDto modelMapperHostDto = new ModelMapperHostDto();
    private final ModelMapper modelMapperHost = modelMapperHostDto.getHostDtopMapper();


    public GetAllHostsThread(HttpEntity<?> body, CountDownLatch latch, ErrorUtilObject errorHandler,
                             List<HostDto> allHosts, String url){
        this.body = body;
        this.latch=latch;
        this.errorHandler = errorHandler;
        this.allHosts = allHosts;
        this.url = url;
    }

    public void run(){

        try{

            System.out.println("url "+url);

            ResponseEntity<HostsArray> AllHostResp = restTemplate.exchange(
                    url,
                    HttpMethod.GET, body, HostsArray.class);


            List<HostDto> allHostDtos = AllHostResp.getBody().getValue()
                    .stream()
                    .map(host -> modelMapperHost.map(host, HostDto.class))
                    .collect(Collectors.toList());

            allHosts.addAll(allHostDtos);
            latch.countDown();
        }catch (Exception e ){
            errorHandler.setError(true);
            errorHandler.setMessage(e.getMessage());
            latch.countDown();
        }
    }



    public void start () {

        if (t == null) {
            t = new Thread (this);
            t.start ();
        }
    }
}
