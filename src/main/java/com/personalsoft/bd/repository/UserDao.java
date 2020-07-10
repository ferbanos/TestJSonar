package com.personalsoft.bd.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.personalsoft.bd.model.db.UserEntity;

@Repository
public interface UserDao extends CrudRepository<UserEntity, Integer>{

}
