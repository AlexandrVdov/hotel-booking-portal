package com.example.hotel_booking_portal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "authorities")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private RoleType authority;

    @OneToMany(mappedBy = "role")
    @ToString.Exclude
    private List<User> users = new ArrayList<>();

    public static UserRole from(RoleType type) {
        UserRole role = new UserRole();
        role.setAuthority(type);

        return role;
    }
}
