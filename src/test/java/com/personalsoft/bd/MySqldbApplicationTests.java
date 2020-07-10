package com.personalsoft.bd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.net.URI;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalsoft.bd.controller.UserController;
import com.personalsoft.bd.model.db.UserEntity;
import com.personalsoft.bd.model.dto.UserDto;
import com.personalsoft.bd.repository.UserDao;
import com.personalsoft.bd.service.UserService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes=MySqldbApplication.class)
@WebMvcTest({UserController.class, UserService.class})
class MySqldbApplicationTests {
	private static final Logger logger = LoggerFactory.getLogger(MySqldbApplicationTests.class);
	private ObjectMapper mapper = new ObjectMapper();
	private static final Integer ID_USER= 1;
	@Autowired
	UserController controller;
	
	@Autowired
	MockMvc mock;
	
	@MockBean
	UserDao dao;
	
	UserDto user;
	
	@BeforeEach
	void contextLoads() {
		user = UserDto.builder().name("Fernando Banos Lozano").age(18).email("correo@correo.com").build();
	}
	
	@Test
	void user_UT01_CreateUserSuccess_ReturnOk_User() throws Exception {
		logger.info("User_UT01_CreateUserSuccess_ReturnOkAndAnUser");
		
		// GIVEN
		UserEntity entityResponse = UserEntity.builder().id(ID_USER).age(18).name("Fernando Banos Lozano").email("correo@correo.com").build();
		
//		user.setAge(15);
		when(dao.save(any(UserEntity.class))).thenReturn(entityResponse);
		
		// WHEN
		MvcResult jsonResponse = getResultPost(user);
		String userJson = jsonResponse.getResponse().getContentAsString();
		UserEntity entity = mapper.readValue(userJson, UserEntity.class);
		
		// THEN
		assertNotNull(entity.getId());		// valida que se haya creado al tener el id diferente de null
		assertTrue(user.getAge()>=18);		// valida que la edad sea mayor o igual a 18
			
		// valida que la información del response sea igual a la información del request
		assertEquals(user.getName(),  entity.getName());	
		assertEquals(user.getEmail(), entity.getEmail());
		assertEquals(user.getAge(),   entity.getAge());		
	}
	
//	@Test
	void user_UT02_UpdateUserSuccess_ReturnOk_User() throws Exception {
		logger.info("user_UT02_UpdateUserSuccess_ReturnOk_User");

		// GIVEN
		UserEntity entityResponse = UserEntity.builder().id(ID_USER).name("Fernando Banos Lozano").age(25).email("correo@correo.com").build();
		
		UserEntity entityResponseBD = new UserEntity();
		entityResponseBD.setId(ID_USER);
		entityResponseBD.setName("Fernando Banos");
		entityResponseBD.setEmail("correo@correo.com");
		
		user.setAge(25);
		
		when(dao.findById(any(Integer.class))).thenReturn(Optional.of(entityResponseBD));
		when(dao.save(any(UserEntity.class))).thenReturn(entityResponse);		
		
		// WHEN
		MvcResult jsonResponse = getResultPut(user, ID_USER);
		String userJson = jsonResponse.getResponse().getContentAsString();
		UserEntity entity = mapper.readValue(userJson, UserEntity.class);

		// THEN		
		// validando lo que hay en BD
		assertNotNull(entityResponseBD);			// valida que la rta de la consulta a BD sea diferente de null
		assertTrue(entityResponseBD.getAge() >=25);	// valida que la edad en BD sea mayor a 25
		
		assertNotEquals(user.getName(), entityResponseBD.getName());	
		assertNotEquals(user.getAge(),  entityResponseBD.getAge());	
						
		// validando que la información del request sea igual al response
		assertEquals(user.getEmail(), entity.getEmail());
		assertEquals(user.getName(),  entity.getName());	
		assertEquals(user.getAge(),   entity.getAge());				
	}
	
	@Test
	void user_UT02_UpdateUserSuccess_AgeBD_menor_25() throws Exception {
		logger.info("user_UT02_UpdateUserSuccess_ReturnERROR_User");
		
		// GIVEN
		UserEntity entityResponse = UserEntity.builder().id(ID_USER).name("Fernando Alvarez").age(25).email("correo@correo.com").build();
		UserEntity entityResponseBD = UserEntity.builder().id(ID_USER).name("Fernando Banos").age(18).email("correo@correo.com").build();

		user.setAge(25);
		
		when(dao.save(any(UserEntity.class))).thenReturn(entityResponse);
		when(dao.findById(any(Integer.class))).thenReturn(Optional.of(entityResponseBD));
		
		// WHEN
		MvcResult jsonResponse = getResultPut(user, ID_USER);
		String userJson = jsonResponse.getResponse().getContentAsString();
		UserEntity entity = mapper.readValue(userJson, UserEntity.class);

		// THEN		
		// validando lo que hay en BD
		assertNotNull(entityResponseBD);				// valida que la rta de la consulta a BD sea diferente de null
		assertFalse(entityResponseBD.getAge()>=25);		// valida que la edad en BD no sea mayor a 25
		
		assertNotEquals(user.getName(), entityResponseBD.getName());	
		assertNotEquals(user.getAge(),  entityResponseBD.getAge());	
						
		// validando que la información del request sea igual al response
		assertTrue(StringUtils.isEmpty(entity.getEmail())); 
		assertTrue(StringUtils.isEmpty(entity.getName()));	
		assertNull(entity.getAge());				
	}
	
//	@Test
	void user_UT02_UpdateUserSuccess_Null_User() throws Exception {
		logger.info("user_UT02_UpdateUserSuccess_Null_User");
		
		// GIVEN
		UserEntity entityResponse = UserEntity.builder().id(1).name("Fernando Alvarez").age(25).email("correo@correo.com").build();
		UserEntity entityResponseBD = null;

		user.setAge(25);
		
		when(dao.findById(ID_USER)).thenReturn(Optional.of(entityResponseBD));
		when(dao.save(any(UserEntity.class))).thenReturn(entityResponse);		
		
		// WHEN
		MvcResult jsonResponse = getResultPut(user, ID_USER);
		String userJson = jsonResponse.getResponse().getContentAsString();

		// THEN	
		assertNull(userJson);	// valida que la rta de la consulta a BD sea null
	}
	
	private MvcResult getResultPost(UserDto requestObject) throws Exception {
		String json = mapper.writeValueAsString(requestObject);

		return this.mock.perform(post("/")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andReturn();
	}
	
	private MvcResult getResultPut(UserDto requestObject, Integer id) throws Exception {
		String json = mapper.writeValueAsString(requestObject);
		
		StringBuilder url = new StringBuilder("/");
		url.append(id);
		
		return this.mock.perform(put(url.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andReturn();
	}

}
