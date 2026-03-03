package com.cesde.eventhub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

	@NotBlank
	private String name;

	@NotBlank
	private String lastName;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String document;

	@NotBlank
	private String phone;


}
