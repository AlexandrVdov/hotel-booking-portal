package com.example.web.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFilter {

    @NotNull(message = "Не указан параметр кол-во на страницу")
    private Integer pageSize;

    @NotNull(message = "Не указан параметр номер страницы")
    private Integer pageNumber;
}
