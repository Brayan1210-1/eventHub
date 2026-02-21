package com.cesde.eventhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaLoginDTO {
 
	private String refreshToken;
	private String accessToken;
}
