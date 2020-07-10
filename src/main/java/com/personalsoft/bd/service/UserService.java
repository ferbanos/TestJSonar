package com.personalsoft.bd.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalsoft.bd.model.db.UserEntity;
import com.personalsoft.bd.model.dto.UserDto;
import com.personalsoft.bd.repository.UserDao;

@Service
public class UserService {
	@Autowired
	UserDao userDao;
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	public List<UserEntity> list() {

		return (List<UserEntity>) userDao.findAll();
	}

	public UserEntity create(UserDto user) {
		UserEntity userEntity = new UserEntity();

		userEntity.setName(user.getName());
		userEntity.setEmail(user.getEmail());
		userEntity.setAge(user.getAge());

		return userDao.save(userEntity);
	}

	public UserEntity update(UserDto user, Integer id) {
		// Manejo de datos opcionales
		UserEntity userEntityResponse = new UserEntity();		
		UserEntity userEntity = userDao.findById(id).orElse(null);

		if (userEntity != null && userEntity.getAge() >= 25) {
			if (!userEntity.getEmail().equals(user.getEmail())) return null;
			
			if (userEntity.getName().equals(user.getName())) return null;
			
			if (userEntity.getAge().equals(user.getAge())) return null;
			
			userEntity.setName(user.getName());
			userEntity.setAge(user.getAge());

			userEntityResponse = userDao.save(userEntity);			
		} else {
			logger.error("El usuario no se puede actualizar");
		}

		return userEntityResponse;
	}
}
