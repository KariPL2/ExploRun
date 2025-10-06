package com.example.ExploRun.mapper;


import com.example.ExploRun.dto.UserDTO;
import com.example.ExploRun.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

  public abstract UserDTO toUserDTO(User user);

}
