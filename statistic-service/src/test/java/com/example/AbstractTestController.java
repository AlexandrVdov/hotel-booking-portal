package com.example;

import com.example.web.request.UpsertUserRequest;
import com.example.web.response.UpdateUserResponse;
import com.example.web.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Testcontainers
public abstract class AbstractTestController {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected static PostgreSQLContainer postgreSQLContainer;

    static {
        DockerImageName postgres = DockerImageName.parse("postgres:12.3");

        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres)
                .withReuse(true);

        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();

        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);
    }

    @Autowired
    protected WebApplicationContext context;

    protected UserResponse createUserResponse(Long id, String username) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(id);
        userResponse.setUsername(username);
        userResponse.setEmail(username.toLowerCase() + "@mail.ru");
        userResponse.setRole("ROLE_USER");

        return userResponse;
    }

    protected UpdateUserResponse createUpdateUserResponse(Long id, String username) {
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        updateUserResponse.setId(id);
        updateUserResponse.setUsername(username);
        updateUserResponse.setEmail(username.toLowerCase() + "@mail.ru");

        return updateUserResponse;
    }

    protected UpsertUserRequest createUpsertUserRequest(Long id, String username) {
        UpsertUserRequest upsertUserRequest = new UpsertUserRequest();
        upsertUserRequest.setUsername(username);
        upsertUserRequest.setPassword("123");
        if (username == null) {
            return upsertUserRequest;
        }
        upsertUserRequest.setEmail(username.toLowerCase() + "@mail.ru");

        return upsertUserRequest;
    }
}
