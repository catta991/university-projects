package com.contactgroup.contactgroupmicroservice.JsonObjectCreator;

import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class ApplyChangesObject {

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
}
