package com.cesde.eventhub.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
	
	private UUID id;
	private String name;
	private String roles;
    private String lastName;
    private String email;
    private String document;
    private String phone;
}
