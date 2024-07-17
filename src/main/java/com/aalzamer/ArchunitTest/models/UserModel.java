package com.aalzamer.ArchunitTest.models;

import com.aalzamer.ArchunitTest.enums.Gender;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
@Entity(name = "custom_user")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String firstName;
    private String lastName;
    private String gender;
}
