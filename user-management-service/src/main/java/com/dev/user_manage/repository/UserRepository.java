package com.dev.user_manage.repository;

import com.dev.user_manage.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported= false)
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByUsername(String username);
}
