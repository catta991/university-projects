package com.example.authorization.costant;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class Costant {


    @Value("${delete.contactgroup.url}")
    private String deleteUrl;

    @Value("${get.all.contactgroup.url}")
    private String allContactGroupUrl;

    @Value("${modify.contactgroup.url}")
    private String modifyContactGroupUrl;

    @Value("${create.contactgroup.url}")
    private String createContactGroupUrl;

    @Value("${login.url}")
    private String loginUrl;

    @Value("${checkMk.user.url}")
    private String checkMkUserUrl;

    @Value("${save.new.user.url}")
    private String saveNewUserUrl;

    @Value("${delete.user.url}")
    private String deleteUserUrl;

    @Value("${update.user.url}")
    private String updateUserUrl;

    @Value("${refresh.token.url}")
    private String refreshTokenUrl;

    @Value("${get.roles.url}")
    private String getRolesUrl;

    @Value("${single.user.info.url}")
    private String getSingleUserInformationUrl;

    @Value("${get.secret.code.url}")
    private String getSecretCodeUrl;

    @Value("${set.secret.code.url}")
    private String setSecretCodeUrl;

    // da agg getter
    @Value("${get.monitored.hosts.url}")
    private String getMonitoredHostUrl;

    @Value("${get.unmonitored.hosts.url}")
    private String getUnmonitoredHostUrl;

    @Value("${update.host.url}")
    private String getUpdateHostUrl;

    @Value("${delete.host.url}")
    private String getDeleteHostUrl;

    @Value("${install.agent.url}")
    private String getInstallAgentUrl;

    @Value("${install.plugins.url}")
    private String getInstallPluginsUrl;

    @Value("${add.host.to.monitoring.url}")
    private String getAddHostToMonitoringUrl;

    @Value("${set.SSH.credentials}")
    private String getSetSSHCredentialsUrl;




   /* @Value("${single.user.info.docker.url}")
    private String getSingleUserInformationUrl;*/

    public String getDeleteUrl() {
        return deleteUrl;
    }

    public String getAllContactGroupUrl() {
        return allContactGroupUrl;
    }

    public String getModifyContactGroupUrl() {
        return modifyContactGroupUrl;
    }

    public String getCreateContactGroupUrl() {
        return createContactGroupUrl;
    }


    public String getLoginUrl() {
        return loginUrl;
    }

    public String getCheckMkUserUrl() {
        return checkMkUserUrl;
    }

    public String getSaveNewUserUrl() {
        return saveNewUserUrl;
    }

    public String getDeleteUserUrl() {
        return deleteUserUrl;
    }

    public String getUpdateUserUrl() {
        return updateUserUrl;
    }

    public String getRefreshTokenUrl() {
        return refreshTokenUrl;
    }

    public String getGetRolesUrl() {
        return getRolesUrl;
    }

    public String getSingleUserInformationUrl() {
        return getSingleUserInformationUrl;
    }

    public String getGetSecretCodeUrl() {
        return getSecretCodeUrl;
    }

    public String getSetSecretCodeUrl() {
        return setSecretCodeUrl;
    }

    public String getGetSingleUserInformationUrl() {
        return getSingleUserInformationUrl;
    }

    public String getGetMonitoredHostUrl() {
        return getMonitoredHostUrl;
    }

    public String getGetUnmonitoredHostUrl() {
        return getUnmonitoredHostUrl;
    }

    public String getGetUpdateHostUrl() {
        return getUpdateHostUrl;
    }

    public String getGetDeleteHostUrl() {
        return getDeleteHostUrl;
    }

    public String getGetInstallAgentUrl() {
        return getInstallAgentUrl;
    }

    public String getGetInstallPluginsUrl() {
        return getInstallPluginsUrl;
    }

    public String getGetAddHostToMonitoringUrl() {
        return getAddHostToMonitoringUrl;
    }

    public String getGetSetSSHCredentialsUrl() {
        return getSetSSHCredentialsUrl;
    }
}
