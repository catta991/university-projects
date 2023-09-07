package com.host.microservice.hostmicroservice.controller;


import com.host.microservice.hostmicroservice.costant.Costant;
import com.host.microservice.hostmicroservice.dto.AddToMonitorDto;
import com.host.microservice.hostmicroservice.dto.HostDto;
import com.host.microservice.hostmicroservice.dto.PluginsDto;
import com.host.microservice.hostmicroservice.dto.SingleUserDto;
import com.host.microservice.hostmicroservice.errorUtil.ErrorUtilObject;
import com.host.microservice.hostmicroservice.JsonParser.HostParser;
import com.host.microservice.hostmicroservice.jsonCreator.ApplyChangeJsonObject;
import com.host.microservice.hostmicroservice.jsonCreator.UpdateJsonObjectCreator;
import com.host.microservice.hostmicroservice.mapper.ModelMapperHostDto;
import com.host.microservice.hostmicroservice.thread.*;
import com.host.microservice.hostmicroservice.util.UtilFunctions;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/host")
@AllArgsConstructor
public class HostsController {


    private RestTemplate restTemplate;


    private UtilFunctions utilFunctions;


    private UpdateJsonObjectCreator updateJsonObjectCreator;

    private ApplyChangeJsonObject applyChangeJsonObject;


    private final ModelMapperHostDto modelMapperHostDto = new ModelMapperHostDto();
    private final ModelMapper modelMapperHost = modelMapperHostDto.getHostDtopMapper();
    private final Costant costant;



    @GetMapping("/monitored/hosts")
    public ResponseEntity<?> getUserMonitoredHost(@RequestHeader("Authorization") String auth) {

        ErrorUtilObject downHostThreadError = new ErrorUtilObject(false, "");
        ErrorUtilObject upHostThreadError = new ErrorUtilObject(false, "");
        ErrorUtilObject unreachHostThreadError = new ErrorUtilObject(false, "");
        ErrorUtilObject getSingleUserThreadError = new ErrorUtilObject(false, "");
        ErrorUtilObject getAllHostThreadError = new ErrorUtilObject(false, "");


        String[] headerParts = auth.split(" ");
        HttpEntity<?> body = new HttpEntity<>(null, utilFunctions.getHeaders(auth));
        SingleUserDto user = new SingleUserDto();
        List<HostDto> allHostDtos = new ArrayList<>();
        CountDownLatch allHostAndSigleUserLatch = new CountDownLatch(2);
        GetSingleUserThread getSingleUserThread = new GetSingleUserThread(getSingleUserThreadError, user, headerParts[1], body, allHostAndSigleUserLatch, costant.getSINGLE_USER_INFO_URL());
        System.out.println(costant.getALL_HOSTS_URL());
        String allHostUrl = costant.getALL_HOSTS_URL();
        GetAllHostsThread getAllHostsThread = new GetAllHostsThread(body, allHostAndSigleUserLatch, getAllHostThreadError, allHostDtos, costant.getALL_HOSTS_URL());


        try {

            getSingleUserThread.start();
            getAllHostsThread.start();

            allHostAndSigleUserLatch.await();

            if (getSingleUserThreadError.isError())
                return new ResponseEntity<>(getSingleUserThreadError.getMessage(), HttpStatus.BAD_REQUEST);
            else if (getAllHostThreadError.isError())
                return new ResponseEntity<>(getAllHostThreadError.getMessage(), HttpStatus.BAD_REQUEST);

            System.out.println(user);
            System.out.println(allHostDtos);


            boolean isSuperadmin = false;
            String[] contactGroups = user.getContactGroups();

            if (contactGroups.length == 1 && contactGroups[0].equals("all"))
                isSuperadmin = true;


            ArrayList<HostDto> response = new ArrayList<>();
            if (contactGroups != null && contactGroups.length != 0) {
                ArrayList<HostDto> upHostsDto = new ArrayList<>();
                ArrayList<HostDto> downHostsDto = new ArrayList<>();
                ArrayList<HostDto> unreacHostsDto = new ArrayList<>();

                CountDownLatch filterLatch = new CountDownLatch(3);

                FilterHostUpThread filterHostUpThread = new FilterHostUpThread("hostupThread", filterLatch, contactGroups, upHostsDto, (ArrayList<HostDto>) allHostDtos, modelMapperHost, body, upHostThreadError, isSuperadmin, costant.getALL_HOST_WITH_QUERY_URL());
                FilterHostDownThread filterHostDownThread = new FilterHostDownThread("hostDownThread", filterLatch, contactGroups, downHostsDto, (ArrayList<HostDto>) allHostDtos, modelMapperHost, body, downHostThreadError, isSuperadmin, costant.getALL_HOST_WITH_QUERY_URL());
                FilterHostUnreachThread filterHostUnreachThread = new FilterHostUnreachThread("hostUnreachThread", filterLatch, contactGroups, unreacHostsDto, (ArrayList<HostDto>) allHostDtos, modelMapperHost, body, unreachHostThreadError, isSuperadmin, costant.getALL_HOST_WITH_QUERY_URL());


                filterHostDownThread.start();
                filterHostUpThread.start();
                filterHostUnreachThread.start();
                filterLatch.await();


                if (downHostThreadError.isError())
                    return new ResponseEntity<>(downHostThreadError.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                else if (upHostThreadError.isError())
                    return new ResponseEntity<>(upHostThreadError.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                else if (unreachHostThreadError.isError())
                    return new ResponseEntity<>(unreachHostThreadError.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

                System.out.println(upHostsDto);

                response.addAll(upHostsDto);
                response.addAll(downHostsDto);
                response.addAll(unreacHostsDto);
            }

            System.out.println(response);

            return new ResponseEntity<>(response, HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    // se accede il super admin per gli host

    @GetMapping("unmonitored/hosts")
    public  ResponseEntity<?> getUnmonitoredHost(@RequestHeader("Authorization") String auth)
    {

        ErrorUtilObject downHostThreadError = new ErrorUtilObject(false, "");
        ErrorUtilObject upHostThreadError = new ErrorUtilObject(false, "");
        ErrorUtilObject unreachHostThreadError = new ErrorUtilObject(false, "");
        ErrorUtilObject getSingleUserThreadError = new ErrorUtilObject(false, "");
        ErrorUtilObject getAllHostThreadError = new ErrorUtilObject(false, "");


        String[] headerParts = auth.split(" ");
        HttpEntity<?> body = new HttpEntity<>(null, utilFunctions.getHeaders(auth));
        SingleUserDto user = new SingleUserDto();
        ArrayList<HostDto> userHostsDtos;
       ArrayList<HostDto> allHostDtos = new ArrayList<>();
        CountDownLatch allHostAndSigleUserLatch = new CountDownLatch(2);
        GetSingleUserThread getSingleUserThread = new GetSingleUserThread(getSingleUserThreadError, user, headerParts[1], body, allHostAndSigleUserLatch, costant.getSINGLE_USER_INFO_URL());
        GetAllHostsThread getAllHostsThread = new GetAllHostsThread(body, allHostAndSigleUserLatch, getAllHostThreadError, allHostDtos, costant.getALL_HOSTS_URL());


        try {

               getSingleUserThread.start();
               getAllHostsThread.start();

                allHostAndSigleUserLatch.await();

                if (getSingleUserThreadError.isError())
                    return new ResponseEntity<>(getSingleUserThreadError.getMessage(), HttpStatus.BAD_REQUEST);
                else if (getAllHostThreadError.isError())
                    return new ResponseEntity<>(getAllHostThreadError.getMessage(), HttpStatus.BAD_REQUEST);

                userHostsDtos = (ArrayList<HostDto>) getUserMonitoredHost(auth).getBody();

                System.out.println("user host "+userHostsDtos);


                String[] contactGroups = user.getContactGroups();

                ArrayList<HostDto> upHostsDto = new ArrayList<>();
                ArrayList<HostDto> downHostsDto = new ArrayList<>();
                ArrayList<HostDto> unreacHostsDto = new ArrayList<>();

                CountDownLatch filterLatch = new CountDownLatch(3);

                FilterHostUpThread filterHostUpThread = new FilterHostUpThread("hostupThread", filterLatch, contactGroups, upHostsDto, (ArrayList<HostDto>) allHostDtos, modelMapperHost, body, upHostThreadError, true, costant.getALL_HOST_WITH_QUERY_URL());
                FilterHostDownThread filterHostDownThread = new FilterHostDownThread("hostDownThread", filterLatch, contactGroups, downHostsDto, (ArrayList<HostDto>) allHostDtos, modelMapperHost, body, downHostThreadError, true, costant.getALL_HOST_WITH_QUERY_URL());
                FilterHostUnreachThread filterHostUnreachThread = new FilterHostUnreachThread("hostUnreachThread", filterLatch, contactGroups, unreacHostsDto, (ArrayList<HostDto>) allHostDtos, modelMapperHost, body, unreachHostThreadError, true, costant.getALL_HOST_WITH_QUERY_URL());


                filterHostDownThread.start();
                filterHostUpThread.start();
                filterHostUnreachThread.start();
                filterLatch.await();


                if (downHostThreadError.isError())
                    return new ResponseEntity<>(downHostThreadError.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                else if (upHostThreadError.isError())
                    return new ResponseEntity<>(upHostThreadError.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                else if (unreachHostThreadError.isError())
                    return new ResponseEntity<>(unreachHostThreadError.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);


            ArrayList<HostDto> response = new ArrayList<>();
                response.addAll(upHostsDto);
                response.addAll(downHostsDto);
                response.addAll(unreacHostsDto);

                response.removeAll(userHostsDtos);


                System.out.println(response);

                return new ResponseEntity<>(response, HttpStatus.OK);

            }catch(Exception e ){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

    }


    @DeleteMapping("/delete/{hostname}")

    public ResponseEntity<?> deleteHost(@RequestHeader("Authorization") String auth,
                                        @PathVariable("hostname") String hostName) {

        HashMap<String, String> rispMex = new HashMap<>();

        HttpEntity<?> body = new HttpEntity<>(applyChangeJsonObject.getApplyChangeJsonObject(), utilFunctions.getHeaders(auth));

        try {


            restTemplate.exchange(costant.getCRUD_HOST_URL() + hostName,
                    HttpMethod.DELETE, body, String.class);


            restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), body, String.class);

            rispMex.put("message", "host correctly deleted");

            return new ResponseEntity<>(rispMex, HttpStatus.OK);

        } catch (Exception e) {
            rispMex.put("error", e.getMessage());
            return new ResponseEntity<>(rispMex, HttpStatus.BAD_REQUEST);
        }


    }

    @PutMapping("/update/{hostname}")
    public ResponseEntity<?> updateHost(@RequestHeader("Authorization") String auth, @PathVariable("hostname") String hostname,
                                        @RequestBody HostDto hostInfo) {

        HttpEntity<?> body = new HttpEntity<>(applyChangeJsonObject.getApplyChangeJsonObject(), utilFunctions.getHeaders(auth));
        HashMap<String, String> resp = new HashMap<>();
        try {

            System.out.println(costant.getCRUD_HOST_URL() + hostname);

            System.out.println(hostInfo);

            ResponseEntity<Object> singleHost = restTemplate.exchange(costant.getCRUD_HOST_URL() + hostname,
                    HttpMethod.GET, body, Object.class);

            HttpEntity<?> updateBodyRequest = new HttpEntity<>(updateJsonObjectCreator.getUpdateJsonObject(hostInfo), utilFunctions.getHeadersWithETag(auth, singleHost.getHeaders().getETag()));

            System.out.println(singleHost.getHeaders().getETag());
            System.out.println(hostInfo.getContactgroups());

            restTemplate.exchange(costant.getCRUD_HOST_URL() + hostname, HttpMethod.PUT, updateBodyRequest, String.class);

            restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), body, String.class);

            resp.put("message", "host correctly updated");

            return new ResponseEntity<>(resp, HttpStatus.OK);

        } catch (Exception e) {
            resp.put("error", e.getMessage());
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @PostMapping("/install/agent/{hostname}")
    public ResponseEntity<?> installAgent(@RequestHeader("Authorization") String auth, @PathVariable("hostname") String hostname) {


        HttpEntity<?> body = new HttpEntity<>(null, utilFunctions.getHeaders(auth));
        HttpEntity<?> applyChangeBody = new HttpEntity<>(applyChangeJsonObject.getApplyChangeJsonObject(), utilFunctions.getHeaders(auth));

        HashMap<String, String> resp = new HashMap<>();

        try {

            ResponseEntity<HostParser> singleHost = restTemplate.exchange(costant.getCRUD_HOST_URL() + hostname,
                    HttpMethod.GET, body, HostParser.class);

            HostDto hostDto = modelMapperHost.map(singleHost.getBody(), HostDto.class);

            String ipAddress = hostDto.getIpaddress();
            String baseUrl = costant.getINSTALL_AGENT_BASE_URL();
            restTemplate.exchange(costant.getINSTALL_AGENT_URL()+ "?ip={ipAddress}&link={baseUrl}", HttpMethod.GET,
                    body, Object.class, ipAddress, baseUrl);


            HttpEntity<?> updateBodyRequest = new HttpEntity<>(updateJsonObjectCreator.getInstallAgentUpdateJsonObject(),
                    utilFunctions.getHeadersWithETag(auth, singleHost.getHeaders().getETag()));


            restTemplate.exchange(costant.getCRUD_HOST_URL() + hostname, HttpMethod.PUT, updateBodyRequest, String.class);


            JSONObject refreshServices = new JSONObject();
            refreshServices.put("mode", "refresh");


            HttpEntity<?> refresh = new HttpEntity<>(refreshServices.toString(), utilFunctions.getHeaders(auth));

            System.out.println("prima di service discovery");
            restTemplate.exchange(costant.getSERVICE_DISCOVERY_URL(hostname),
                    HttpMethod.POST, refresh, String.class);


            restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), applyChangeBody, String.class);


            System.out.println("dopo discovery e cambiamenti");
            ResponseEntity<HostParser> singleHostSecondCall = restTemplate.exchange(costant.getCRUD_HOST_URL() + hostname,
                    HttpMethod.GET, body, HostParser.class);

            System.out.println("dopo richiesta host");

            HttpEntity<?> updateBodyRequestAfterChange = new HttpEntity<>(updateJsonObjectCreator.getInstallAgentUpdateJsonObject(),
                    utilFunctions.getHeadersWithETag(auth, singleHostSecondCall.getHeaders().getETag()));

            restTemplate.exchange(costant.getCRUD_HOST_URL() + hostname, HttpMethod.PUT, updateBodyRequestAfterChange, String.class);

            System.out.println("dopo seconda modifica");

            restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), applyChangeBody, String.class);
            restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), applyChangeBody, String.class);

            System.out.println("fine");

            resp.put("message", "agent correctly installed");
            return new ResponseEntity<>(resp, HttpStatus.OK);

        } catch (Exception e) {
            resp.put("error", e.getMessage());
            System.out.println(e.getMessage());
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }


    }


    @PostMapping("/install/plugin/{hostname}")
    public ResponseEntity<?> installPlugins(@RequestHeader("Authorization") String auth, @PathVariable("hostname") String hostname,
                                            @RequestBody PluginsDto pluginsDto) {

        HttpEntity<?> body = new HttpEntity<>(null, utilFunctions.getHeaders(auth));
        HttpEntity<?> applyChangeBody = new HttpEntity<>(applyChangeJsonObject.getApplyChangeJsonObject(), utilFunctions.getHeaders(auth));

        HashMap<String, String> resp = new HashMap<>();

        System.out.println(pluginsDto);
        try {

            if (pluginsDto.getPluginsName() == null || pluginsDto.getPluginsName().length == 0) {
                resp.put("error", "no plugins selected");
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
            } else {

                ResponseEntity<HostParser> singleHost = restTemplate.exchange(costant.getCRUD_HOST_URL() + hostname,
                        HttpMethod.GET, body, HostParser.class);

                HostDto hostDto = modelMapperHost.map(singleHost.getBody(), HostDto.class);

                /*if (hostDto.getTag_agent() == null) {
                    resp.put("error", "cmk_agent not installed");
                    return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
                }*/

                String ipAddress = hostDto.getIpaddress();

                ArrayList<String> installedPlugins = new ArrayList<>();
                List<String> alreadyInstalledPlugins = Arrays.asList(hostDto.getPlugins_tag().split(","));

                for (int i = 0; i < pluginsDto.getPluginsName().length; i++) {
                    if (!alreadyInstalledPlugins.contains(pluginsDto.getPluginsName()[i])) {
                        String baseUrl = "";
                        if (hostDto.getOs_family().equals("linux"))
                            baseUrl = costant.getINSTALL_PLUGINS_LINUX_BASE_URL() + pluginsDto.getPluginsName()[i];
                        else
                            baseUrl = costant.getINSTALL_PLUGINS_WINDOWS_BASE_URL() + pluginsDto.getPluginsName()[i];

                        restTemplate.exchange(costant.getINSTALL_PLUGINS_URL()+"?ip={ipAddress}&link={baseUrl}", HttpMethod.GET,
                                body, Object.class, ipAddress, baseUrl);

                        installedPlugins.add(pluginsDto.getPluginsName()[i]);
                    }


                }

                if (installedPlugins.size() == 0) {
                    resp.put("error", "no plugin installed");
                    return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
                }

                String pluginTags = "";


                if (hostDto.getPlugins_tag().equals("null"))
                    pluginTags = String.join(",", installedPlugins);
                else
                    pluginTags = hostDto.getPlugins_tag() + "," + String.join(",", installedPlugins);

                //System.out.println(hostDto.getPlugins_tag()+","+pluginTags);
                HostDto hostDtoUpdate = new HostDto(pluginTags, hostDto.getOs_family(), hostDto.getAgent_install_mode());

                System.out.println(hostDtoUpdate);


                JSONObject refreshServices = new JSONObject();
                refreshServices.put("mode", "new");

                HttpEntity<?> refresh = new HttpEntity<>(refreshServices.toString(), utilFunctions.getHeaders(auth));

                System.out.println("prima di service discovery");
                restTemplate.exchange(costant.getSERVICE_DISCOVERY_URL(hostname),
                        HttpMethod.POST, refresh, String.class);


                updateHost(auth, hostname, hostDtoUpdate);



                //restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), applyChangeBody, String.class);

                resp.put("message", "plugins correctly installed");
                return new ResponseEntity<>(resp, HttpStatus.OK);

            }
        } catch (Exception e) {
            System.out.println("in catch");
            resp.put("error", e.getMessage());
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }


    }


    @PostMapping("/add/to/monitoring")
    public ResponseEntity<?> addToMonitor(@RequestHeader("Authorization") String auth,
                                          @RequestBody AddToMonitorDto[] addToMonitorDto){

        HttpEntity<?> body = new HttpEntity<>(null, utilFunctions.getHeaders(auth));
        HttpEntity<?> applyChangeBody = new HttpEntity<>(applyChangeJsonObject.getApplyChangeJsonObject(), utilFunctions.getHeaders(auth));

        HashMap<String, String> resp = new HashMap<>();
        try {
            if(addToMonitorDto == null || addToMonitorDto.length == 0){

                resp.put("error", "no host selected");
                return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }


            for(int i=0; i< addToMonitorDto.length; i++) {

                String hostname = addToMonitorDto[i].getHostName();

                ResponseEntity<Object> singleHost = restTemplate.exchange(costant.getCRUD_HOST_URL() +hostname,
                        HttpMethod.GET, body, Object.class);



                HttpEntity<?> updateBodyRequest = new HttpEntity<>(updateJsonObjectCreator.addToMonitorJsonObject(addToMonitorDto[i].getContactGroups()),
                        utilFunctions.getHeadersWithETag(auth, singleHost.getHeaders().getETag()));


                restTemplate.exchange(costant.getCRUD_HOST_URL() + hostname, HttpMethod.PUT, updateBodyRequest, String.class);



            }

            restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), applyChangeBody, String.class);
            resp.put("message", "hosts correctly updated");

            return new ResponseEntity<>(resp, HttpStatus.OK);

        } catch (Exception e) {
            resp.put("error", e.getMessage());
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }

    @PostMapping("/set/SSH/credentials")
    public ResponseEntity<?> setSSHCredentials(@RequestHeader("Authorization") String auth,
                                               @RequestBody HostDto hostDto){


        HttpEntity<?> body = new HttpEntity<>(null, utilFunctions.getHeaders(auth));
        HttpEntity<?> applyChangeBody = new HttpEntity<>(applyChangeJsonObject.getApplyChangeJsonObject(), utilFunctions.getHeaders(auth));

        HashMap<String, String> resp = new HashMap<>();
        try {

            System.out.println(costant.getCRUD_HOST_URL() + hostDto.getHostName());

            System.out.println(hostDto);

            ResponseEntity<Object> singleHost = restTemplate.exchange(costant.getCRUD_HOST_URL() + hostDto.getHostName(),
                    HttpMethod.GET, body, Object.class);

            HttpEntity<?> updateBodyRequest = new HttpEntity<>(updateJsonObjectCreator.setSSHCredentials(hostDto), utilFunctions.getHeadersWithETag(auth, singleHost.getHeaders().getETag()));


            restTemplate.exchange(costant.getCRUD_HOST_URL() + hostDto.getHostName(), HttpMethod.PUT, updateBodyRequest, String.class);

            restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), applyChangeBody, String.class);


            String ipAddress = hostDto.getIpaddress();
            String username = hostDto.getUsername();

            if(hostDto.getAgent_install_mode()!= null && hostDto.getAgent_install_mode().equals("password")){
                String password = hostDto.getPassword();
                restTemplate.exchange(costant.getUSERNAME_PASSWORD_SSH()+"?ip={ipAddress}&username={username}&password={password}",
                        HttpMethod.GET,
                        body, Object.class, ipAddress, username, password);
            } else if(hostDto.getAgent_install_mode()!= null && hostDto.getAgent_install_mode().equals("key")){
                String key = hostDto.getKey();
                restTemplate.exchange(costant.getUSERNAME_KEY_SSH()+"?ip={ipAddress}&username={username}&key={key}",
                        HttpMethod.GET,
                        body, Object.class, ipAddress, username, key);

            }else{
                resp.put("error", "Unknown SSH authentication method");
                return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            resp.put("message", "SSH authentication mode correctly updated");
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }catch (Exception e){
            resp.put("error", e.getMessage());
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
