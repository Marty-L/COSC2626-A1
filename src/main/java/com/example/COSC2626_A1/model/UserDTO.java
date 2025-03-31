package com.example.COSC2626_A1.model;

import lombok.Data;

@Data //Provides getter and setter for all fields (Lombok)
public class UserDTO {
    private String email;
    private String user_name;
    private String password;
}
