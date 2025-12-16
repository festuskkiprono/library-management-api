package com.festuskiprono.library.mappers;

import com.festuskiprono.library.dtos.RegisterUserRequest;
import com.festuskiprono.library.dtos.UpdateUserRequest;
import com.festuskiprono.library.dtos.UserDto;
import com.festuskiprono.library.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel ="spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest registerUserRequest);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
