package com.heptagon.thirema.dataprocessor.repository;

import com.heptagon.thirema.commons.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
