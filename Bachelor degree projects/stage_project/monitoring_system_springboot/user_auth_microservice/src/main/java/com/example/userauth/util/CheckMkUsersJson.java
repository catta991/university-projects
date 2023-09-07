package com.example.userauth.util;

import com.example.userauth.dto.CrudUserDto;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@NoArgsConstructor

@Component
public class CheckMkUsersJson {

    private final Costant costant = new Costant();

    public String applyChangesCheckMk() {

        JSONObject applyChanges = new JSONObject();
        applyChanges.put("redirect", false);

        JSONArray sites = new JSONArray();
        sites.put("catta");
        applyChanges.put("sites", sites);
        applyChanges.put("force_foreign_changes", false);

        String applyChangesString = applyChanges.toString();
        return applyChangesString;

    }


    public String saveUserCheckMk(String fullName, CrudUserDto crudUserDto, boolean creation) {

        JSONObject jsonObject = new JSONObject();

        if (creation)
            jsonObject.put("username", crudUserDto.getUsername());
        // jsonObject.put("username", "catta");
        jsonObject.put("fullname", fullName);

        if (crudUserDto.getPassword()!= null && !crudUserDto.getPassword().equals("")) {
            JSONObject authOption = new JSONObject();

            authOption.put("auth_type", "password");
            authOption.put("password", crudUserDto.getPassword());

            jsonObject.put("auth_option", authOption);
        }


        JSONObject contactOption = new JSONObject();
        contactOption.put("email", crudUserDto.getEmail());

        jsonObject.put("contact_options", contactOption);

        JSONArray roles = new JSONArray();
        if (crudUserDto.getRoleName().equals("ROLE_ADMIN"))
            roles.put("admin");
        else
            roles.put("user");

        if(!crudUserDto.getRoleName().equals("ROLE_SUPER_ADMIN"))
        jsonObject.put("roles", roles);


        JSONArray authSite = new JSONArray();
        authSite.put("catta");
        jsonObject.put("authorized_sites", authSite);



        jsonObject.put("contactgroups", crudUserDto.getContactGroups());


        String addUserJsonString = jsonObject.toString();

        return addUserJsonString;

    }


}
