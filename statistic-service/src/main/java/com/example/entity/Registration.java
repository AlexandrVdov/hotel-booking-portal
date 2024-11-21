package com.example.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "registrations")
public class Registration {

    @Id
    private String id;

    @Field("user_id")
    private Long userId;
}
