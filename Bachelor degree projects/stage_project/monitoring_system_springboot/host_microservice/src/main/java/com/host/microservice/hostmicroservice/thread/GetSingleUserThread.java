package com.host.microservice.hostmicroservice.thread;

import com.host.microservice.hostmicroservice.costant.Costant;
import com.host.microservice.hostmicroservice.dto.SingleUserDto;
import com.host.microservice.hostmicroservice.errorUtil.ErrorUtilObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;

public class GetSingleUserThread extends Thread{

    private Thread t;
    private ErrorUtilObject errorHandler;
    private SingleUserDto singleUserDto;
    private String userName;
    private HttpEntity<?> body;
    private CountDownLatch latch;
    private String url;

    private RestTemplate restTemplate= new RestTemplate();



    public GetSingleUserThread(ErrorUtilObject errorHandler, SingleUserDto singleUserDto, String userName,
                               HttpEntity<?> body, CountDownLatch latch, String url){
        this.errorHandler = errorHandler;
        this.singleUserDto = singleUserDto;
        this.userName = userName;
        this.body=body;
        this.latch=latch;
        this.url = url;
    }




    public void run() {



        try{
            ResponseEntity<SingleUserDto> singleUserDtoResponseEntity = restTemplate.exchange(url+userName,
                    HttpMethod.GET, body, SingleUserDto.class);

            singleUserDto.setContactGroups(singleUserDtoResponseEntity.getBody().getContactGroups());

            latch.countDown();


        }catch (Exception e){

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
