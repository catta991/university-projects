package com.example.userauth.mapper;

import com.example.userauth.dto.SingleUserDto;
import com.example.userauth.JsonParser.UserParser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class SingleUserDtoMapper {

    ModelMapper mapper = new ModelMapper();

    public ModelMapper getSingleUserDtopMapper() {

        TypeMap<UserParser, SingleUserDto> propertyMapper = this.mapper.createTypeMap(UserParser.class, SingleUserDto.class);
        propertyMapper.addMapping(UserParser::getUsername, SingleUserDto::setUsername);
        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getExtensions().getFullname(), SingleUserDto::setName));
        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getExtensions().getRoles(), SingleUserDto::setRoles));
        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getExtensions().getContactgroups(), SingleUserDto::setContactGroups));
        propertyMapper.addMappings(mapper -> mapper.map(src -> src.getExtensions().getContact_options().getEmail(),
                SingleUserDto::setEmail));

        return mapper;
    }


}
