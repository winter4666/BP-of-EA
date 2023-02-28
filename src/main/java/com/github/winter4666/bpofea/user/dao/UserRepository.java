package com.github.winter4666.bpofea.user.dao;

import com.github.winter4666.bpofea.user.domain.model.User;
import com.github.winter4666.bpofea.user.domain.service.UserDao;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>, UserDao {
}
