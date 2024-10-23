package com.example.hotel_booking_portal.controller;

import com.example.hotel_booking_portal.AbstractTestController;
import com.example.hotel_booking_portal.StringTestUtils;
import com.example.hotel_booking_portal.entity.RoleType;
import com.example.hotel_booking_portal.entity.UserRole;
import com.example.hotel_booking_portal.exception.AlreadyExistsException;
import com.example.hotel_booking_portal.exception.EntityNotFoundException;
import com.example.hotel_booking_portal.service.UserService;
import com.example.hotel_booking_portal.web.controller.UserController;
import com.example.hotel_booking_portal.web.model.request.UpsertUserRequest;
import com.example.hotel_booking_portal.web.model.request.UserFilter;
import com.example.hotel_booking_portal.web.model.response.UpdateUserResponse;
import com.example.hotel_booking_portal.web.model.response.UserListResponse;
import com.example.hotel_booking_portal.web.model.response.UserResponse;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest extends AbstractTestController {

    @MockBean
    private UserService userService;

    @Test
    public void whenFindAll_thenReturnAllUsers() throws Exception {

        List<UserResponse> userResponses = new ArrayList<>();
        userResponses.add(createUserResponse(1L, "User1"));
        userResponses.add(createUserResponse(2L, "User2"));

        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setUsers(userResponses);

        UserFilter filter = new UserFilter(5, 1);

        Mockito.when(userService.findAll(filter))
                .thenReturn(userListResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/user")
                        .param("pageSize", "5")
                        .param("pageNumber", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/find_all_users_response.json");

        Mockito.verify(userService, Mockito.times(1)).findAll(filter);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenGetUserById_thenReturnUserById() throws Exception {
        UserResponse userResponse = createUserResponse(1L, "User");

        Mockito.when(userService.findById(1L)).thenReturn(userResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/find_user_by_id_response.json");

        Mockito.verify(userService, Mockito.times(1)).findById(1L);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateUser_thenReturnNewUser() throws Exception {
        UpsertUserRequest request = createUpsertUserRequest(1L, "NewUser");
        UpdateUserResponse response = createUpdateUserResponse(1L, "NewUser");
        UserRole role = UserRole.from(RoleType.ROLE_USER);

        Mockito.when(userService.save(request, role)).thenReturn(response);

        String actualResponse = mockMvc.perform(post("/api/v1/user")
                        .param("roleType", RoleType.ROLE_USER.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/create_user_response.json");

        Mockito.verify(userService, Mockito.times(1)).save(request, role);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenUpdateUser_thenReturnUpdatedUser() throws Exception {
        UpsertUserRequest request = createUpsertUserRequest(1L, "User");
        UpdateUserResponse response = createUpdateUserResponse(1L, "User");

        Mockito.when(userService.update(1L, request)).thenReturn(response);

        String actualResponse = mockMvc.perform(put("/api/v1/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/update_user_response.json");

        Mockito.verify(userService, Mockito.times(1)).update(1L, request);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenDeleteUserById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/user/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void whenFindByIdNotExistedUser_thenReturnError() throws Exception {
        Mockito.when(userService.findById(500L))
                .thenThrow(new EntityNotFoundException("Пользователь не найден с id 500"));

        var response = mockMvc.perform(get("/api/v1/user/500"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource(
                "response/find_user_by_id_not_found_response.json");

        Mockito.verify(userService, Mockito.times(1)).findById(500L);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateUserWithEmptyName_thenReturnError() throws Exception {

        var response = mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUpsertUserRequest(1L, null))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String actualResponse = response.getContentAsString();
        String expectedResponse = StringTestUtils.readStringFromResource(
                "response/empty_user_name_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateUserWithExistsName_thenReturnError() throws Exception {
        UpsertUserRequest request = createUpsertUserRequest(1L, "ExistingUser");
        UserRole role = UserRole.from(RoleType.ROLE_USER);

        Mockito.when(userService.save(request, role))
                .thenThrow(new AlreadyExistsException("Пользователь с таким именем уже зарегистрирован"));

        var response = mockMvc.perform(post("/api/v1/user")
                        .param("roleType", RoleType.ROLE_USER.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");

        String expectedResponse = StringTestUtils
                .readStringFromResource("response/already_exists_response.json");
        String actualResponse = response.getContentAsString();

        Mockito.verify(userService, Mockito.times(1)).save(request, role);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
}
