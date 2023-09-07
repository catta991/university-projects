package com.contactgroup.contactgroupmicroservice.controller;


import com.contactgroup.contactgroupmicroservice.Dto.SingleContactGroupDto;
import com.contactgroup.contactgroupmicroservice.JsonObjectCreator.ApplyChangesObject;
import com.contactgroup.contactgroupmicroservice.JsonParser.ContactGroupParser;
import com.contactgroup.contactgroupmicroservice.JsonParser.Value;
import com.contactgroup.contactgroupmicroservice.costant.Costant;
import com.contactgroup.contactgroupmicroservice.mapper.ModelMapperUserDto;
import com.contactgroup.contactgroupmicroservice.utils.UtilsFunction;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/contactGroup")
@AllArgsConstructor
public class ContactgroupController {


    private final RestTemplate restTemplate;
    private final ModelMapperUserDto singleUserDtoMapper = new ModelMapperUserDto();
    private final ModelMapper modelMapperSingleUserDto = singleUserDtoMapper.getSingleUserDtopMapper();

    private ApplyChangesObject applyChangesObject;
    private final Costant costant;
    private final UtilsFunction utilsFunction;





    //ok per chiamata diretta
    @GetMapping("/allUserContactGroups")
    public ResponseEntity<?> getAllUserContactGroups(@RequestHeader("Authorization") String auth) {


       // String[] headerContent = auth.split(" ");
        List<SingleContactGroupDto> dtos = new ArrayList<>();
        HashMap<String, String> respMex = new HashMap<>();
        HttpEntity<?> body = new HttpEntity<>(null, utilsFunction.getHeaders(auth));

        try {
            System.out.println(costant.getALL_CONTACT_GROUP_URL());
            ResponseEntity<ContactGroupParser> resp = restTemplate.exchange(costant.getALL_CONTACT_GROUP_URL(),
                    HttpMethod.GET, body, ContactGroupParser.class);
            List<Value> values = new ArrayList<>(Arrays.asList(resp.getBody().getValue()));

           /* if (!isSuperadmin) {

                HttpEntity<?> singleUserBody = new HttpEntity<>(null, utilsFunction.getHeaders(auth));

                ResponseEntity<SingleUserDto> userLogged = restTemplate.exchange(costant.getSingleUserInfoUrl() + headerContent[1]
                        ,
                        HttpMethod.GET, singleUserBody, SingleUserDto.class);
                SingleUserDto userDto = userLogged.getBody();

                System.out.println(userDto);

                List<String> userContactGroup = Arrays.asList(userDto.getContactGroups());

                for (Value val : values) {
                    if (userContactGroup.contains(val.getNameFromUrl()))
                        dtos.add(new SingleContactGroupDto(val.getNameFromUrl(), val.getAlias()));
                }

            } else {*/

                for (Value val : values) {
                    if(!val.getNameFromUrl().equals("all"))
                    dtos.add(new SingleContactGroupDto(val.getNameFromUrl(), val.getAlias()));
                }


            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } catch (Exception e) {
            respMex.put("error", e.getMessage());
            return new ResponseEntity<>(respMex, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

// ok per chiamata diretta
    @DeleteMapping("/delete/{contactgroup}")
    public ResponseEntity<?> deleteContactGroup(@RequestHeader("Authorization") String auth, @PathVariable("contactgroup") String name) {

        System.out.println(name);

        System.out.println(costant.getDELETE_MODIFY_CONTACT_GROUP_URL() + name);

        HttpEntity<?> body = new HttpEntity<>(null, utilsFunction.getHeaders(auth));
        HttpEntity<?> applyChangesBody = new HttpEntity<>(applyChangesObject.applyChangesCheckMk(), utilsFunction.getHeaders(auth));

        HashMap<String, String> resp = new HashMap<>();
        try {
            restTemplate.exchange(costant.getDELETE_MODIFY_CONTACT_GROUP_URL() + name, HttpMethod.DELETE, body, String.class);

            restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), applyChangesBody, String.class);


            resp.put("message", "contact group correctly deleted");

            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            resp.put("error", e.getMessage());
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }




// ok chiamata diretta
    @PutMapping("/modifyContactGroup")
    public ResponseEntity<?> modifyContactGroup(@RequestHeader("Authorization") String auth, @RequestBody SingleContactGroupDto singleContactGroupDto){

        //HttpHeaders headers = utilsFunction.getHeaders(auth).getHeaders();
        HashMap<String, String> resp = new HashMap<>();

        System.out.println(singleContactGroupDto);

        HttpEntity<?> body = new HttpEntity<>(null, utilsFunction.getHeaders(auth));
        try {

            System.out.println(costant.getDELETE_MODIFY_CONTACT_GROUP_URL()+singleContactGroupDto.getName());

            ResponseEntity<?> response = restTemplate.exchange(costant.getDELETE_MODIFY_CONTACT_GROUP_URL()+singleContactGroupDto.getName(),HttpMethod.GET ,body,
                    Object.class);
          String etag = response.getHeaders().getETag();
          

            HttpEntity<?> reqBody=  new HttpEntity<>(utilsFunction.modifyContactGroupString(singleContactGroupDto.getAlias()), utilsFunction.getHeadersWithEtag(auth, etag));

            System.out.println(reqBody.getBody());

            restTemplate.exchange(costant.getDELETE_MODIFY_CONTACT_GROUP_URL() + singleContactGroupDto.getName(), HttpMethod.PUT, reqBody, String.class);

            HttpEntity<?> activateChangesBody = new HttpEntity<>(applyChangesObject.applyChangesCheckMk(), utilsFunction.getHeaders(auth));

            restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), activateChangesBody, String.class);

            resp.put("message", "contact group correctly updated");

            return new ResponseEntity<>(resp, HttpStatus.OK);
        }catch (Exception e){
            resp.put("message", e.getMessage());
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

    }

    // ok chiamata diretta
    @PostMapping("/new/contactgroup")
    public ResponseEntity<?> newContactGroup(@RequestHeader("Authorization") String auth,
                                             @RequestBody SingleContactGroupDto singleContactGroupDto)
    {
        HashMap<String, String> resp = new HashMap<>();
        String[] headerContent = auth.split(" ");

        System.out.println(singleContactGroupDto);

      try {
          HttpEntity<?> reqBody = new HttpEntity<>(utilsFunction.createNewContactGroupString(singleContactGroupDto), utilsFunction.getHeaders(auth));

         restTemplate.exchange(costant.getALL_CONTACT_GROUP_URL(), HttpMethod.POST, reqBody, String.class);

         

          HttpEntity<?> activateChangesBody = new HttpEntity<>(applyChangesObject.applyChangesCheckMk(), utilsFunction.getHeaders(auth));
          restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), activateChangesBody, String.class);

          resp.put("message", "contact group correctly created");

          return new ResponseEntity<>(resp, HttpStatus.OK);
      }catch (Exception e){
          resp.put("error", e.getMessage());
          return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
      }

    }
}
