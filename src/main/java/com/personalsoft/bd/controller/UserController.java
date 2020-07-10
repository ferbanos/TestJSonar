package com.personalsoft.bd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.personalsoft.bd.model.db.UserEntity;
import com.personalsoft.bd.model.dto.UserDto;
import com.personalsoft.bd.service.UserService;

@RestController
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService service;
	
	@GetMapping
	public ResponseEntity<List<UserEntity>> getAll () {
		
		return ResponseEntity.ok(service.list());		
	}
	
	@PostMapping
	@ResponseBody
	public UserEntity createUser(@Valid @RequestBody UserDto user)  {
		logger.info ("Creando un usuario User: {} ", user);
		
		if(user == null) 
			return null;		
		
		return service.create(user);		
	}
	
	@PutMapping("/{id}")
	@ResponseBody
	public UserEntity updateUser(@RequestBody UserDto user, @PathVariable("id") Integer id) {
		
		return service.update(user, id);
	}
	
	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable("id") Integer id) {
		
		return null;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
 }
