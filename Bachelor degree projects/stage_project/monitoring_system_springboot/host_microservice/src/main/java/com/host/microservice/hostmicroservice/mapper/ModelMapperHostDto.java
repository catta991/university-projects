package com.host.microservice.hostmicroservice.mapper;

import com.host.microservice.hostmicroservice.dto.HostDto;
import com.host.microservice.hostmicroservice.JsonParser.HostParser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class ModelMapperHostDto {

    private ModelMapper hostMapper = new ModelMapper();

    public ModelMapper getHostDtopMapper() {

        TypeMap<HostParser, HostDto> propertyMapp = this.hostMapper.createTypeMap(HostParser.class, HostDto.class);
        propertyMapp.addMapping(HostParser::getHostName, HostDto::setHostName);
        propertyMapp.addMapping(HostParser::getAlias, HostDto::setAlias);
        propertyMapp.addMappings(mapper -> mapper.map(src -> src.getExtensions().getAttributes().getIpaddress(), HostDto::setIpaddress));
        propertyMapp.addMappings(mapper -> mapper.map(src -> src.getExtensions().getAttributes().getTag_agent(), HostDto::setTag_agent));
        propertyMapp.addMappings(mapper -> mapper.map(src -> src.getExtensions().getAttributes().getContactgroups().getGroups(), HostDto::setContactgroups));
        propertyMapp.addMappings(mapper -> mapper.map(src->src.getExtensions().getAttributes().getLabels().getOs_family(), HostDto:: setOs_family));
        propertyMapp.addMappings(mapper -> mapper.map(src->src.getExtensions().getAttributes().getLabels().getPlugins_tag(), HostDto:: setPlugins_tag));
        propertyMapp.addMappings(mapper -> mapper.map(src->src.getExtensions().getAttributes().getLabels().getAgent_install_mode(), HostDto:: setAgent_install_mode));
        propertyMapp.addMappings(mapper -> mapper.map(src->src.getExtensions().getAttributes().getMeta_data().getOnlyDate(), HostDto:: setCreation_date));

        return hostMapper;
    }
}
