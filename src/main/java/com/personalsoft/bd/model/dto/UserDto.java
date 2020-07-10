package com.personalsoft.bd.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class UserDto {
	
	private Integer id;
	
	@Min(18)
	private Integer age;
	
	@Size(min= 10, max = 50)
	private String name;
	
	private String email;
}
