package com.host.microservice.hostmicroservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@AllArgsConstructor
@Data
@ToString
public class HostDto {

    private String hostName;

    private String alias;

    private String ipaddress;

    private String tag_agent;

    private String os_family;

    private String plugins_tag;

    private String agent_install_mode;

    private String[] contactgroups;

    private String status;

    private String creation_date;

    private String username;

    private String password;

    private String key;

    public HostDto() {

    }

    public HostDto(String hostName) {
        this.hostName = hostName;
    }




    public HostDto(String plugins_tag, String os_family, String agent_install_mode) {
        this.plugins_tag = plugins_tag;
        this.os_family = os_family;
        this.agent_install_mode = agent_install_mode;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HostDto hostDto = (HostDto) o;
        return Objects.equals(hostName, hostDto.hostName);
    }


}
