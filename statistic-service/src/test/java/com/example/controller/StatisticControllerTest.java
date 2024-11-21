package com.example.controller;

import com.example.AbstractTestController;
import com.example.domain.RoleType;
import com.example.domain.UserRole;
import com.example.service.StatisticService;
import com.example.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class StatisticControllerTest extends AbstractTestController {

    @MockBean
    private StatisticService statisticService;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService.save(createUpsertUserRequest(1L, "User"), UserRole.from(RoleType.ROLE_USER));
        userService.save(createUpsertUserRequest(2L, "Admin"), UserRole.from(RoleType.ROLE_ADMIN));
    }

    @AfterEach
    public void afterEach() {
        userService.deleteById(1L);
        userService.deleteById(2L);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    public void whenDownloadStatistics_thenReturnCsv() throws Exception {

        byte[] csvData = "header1,header2\nvalue1,value2".getBytes();

        Mockito.when(statisticService.getStatisticAsCsv()).thenReturn(csvData);

        mockMvc.perform(get("/api/v1/statistic/download"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("text/csv; charset=UTF-8")))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statistics.csv"))
                .andExpect(content().bytes(csvData));

        Mockito.verify(statisticService, Mockito.times(1)).getStatisticAsCsv();
    }
}
