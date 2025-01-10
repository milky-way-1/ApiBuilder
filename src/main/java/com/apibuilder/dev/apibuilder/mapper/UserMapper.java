package com.apibuilder.dev.apibuilder.mapper;

import com.apibuilder.dev.apibuilder.dto.request.UserRegistrationRequest;
import com.apibuilder.dev.apibuilder.dto.response.UserResponse;
import com.apibuilder.dev.apibuilder.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserResponse toResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    public User toEntity(UserRegistrationRequest request) {
        return modelMapper.map(request, User.class);
    }

    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}