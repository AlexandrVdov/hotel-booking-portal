package com.example.mapper;

import com.example.event.RegistrationEvent;
import com.example.domain.User;
import com.example.domain.UserRole;
import com.example.web.request.UpsertUserRequest;
import com.example.web.response.UpdateUserResponse;
import com.example.web.response.UserListResponse;
import com.example.web.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requestToUser(UpsertUserRequest request);

    @Mapping(source = "userId", target = "id")
    User requestToUser(Long userId, UpsertUserRequest request);

    @Mapping(source = "id", target = "userId")
    RegistrationEvent userToRegistrationEvent(User user);

    UserResponse userToResponse(User user);

    UpdateUserResponse userToUpdateResponse(User user);

    default UserListResponse userListToUserListResponse(List<User> users) {
        UserListResponse response = new UserListResponse();

        response.setUsers(users.stream()
                .map(this::userToResponse).collect(Collectors.toList()));

        return response;
    }

    default String mapRolesToString(UserRole role) {
        return role.getAuthority().toString();
    }
}
