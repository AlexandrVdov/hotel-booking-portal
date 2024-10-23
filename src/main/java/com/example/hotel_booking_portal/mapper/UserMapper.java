package com.example.hotel_booking_portal.mapper;

import com.example.hotel_booking_portal.entity.User;
import com.example.hotel_booking_portal.entity.UserRole;
import com.example.hotel_booking_portal.web.model.request.UpsertUserRequest;
import com.example.hotel_booking_portal.web.model.response.UpdateUserResponse;
import com.example.hotel_booking_portal.web.model.response.UserListResponse;
import com.example.hotel_booking_portal.web.model.response.UserResponse;
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
