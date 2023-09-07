package com.contactgroup.contactgroupmicroservice.mapper;

import com.contactgroup.contactgroupmicroservice.Dto.SingleUserDto;
import com.contactgroup.contactgroupmicroservice.JsonParser.UserParser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class ModelMapperUserDto {

    private  ModelMapper mapperCurrentUser = new ModelMapper();

    public ModelMapper getSingleUserDtopMapper() {

        TypeMap<UserParser, SingleUserDto> propertyMapp = this.mapperCurrentUser.createTypeMap(UserParser.class, SingleUserDto.class);
        propertyMapp.addMapping(UserParser::getUsername, SingleUserDto::setUsername);
        propertyMapp.addMappings(mapper -> mapper.map(src -> src.getExtensions().getFullname(), SingleUserDto::setName));
        propertyMapp.addMappings(mapper -> mapper.map(src -> src.getExtensions().getRoles(), SingleUserDto::setRoles));
        propertyMapp.addMappings(mapper -> mapper.map(src -> src.getExtensions().getContactgroups(), SingleUserDto::setContactGroups));
        propertyMapp.addMappings(mapper -> mapper.map(src -> src.getExtensions().getContact_options().getEmail(),
                SingleUserDto::setEmail));




        return mapperCurrentUser;
    }



}
