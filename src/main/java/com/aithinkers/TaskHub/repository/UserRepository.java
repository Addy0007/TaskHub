package com.aithinkers.TaskHub.repository;

import com.aithinkers.TaskHub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    //Here we used Optional to Null pointer exception
    /* Here in DB has two statements possibilities
    1.If the User with email exists return user
    2.no user with that email exist so return nothing
      here we can expect the null pointer exception
      Instead to overcome JPA allows to warp the result in Optional<user> because optional is an container Object
      that may have or may not have a non-null value*/

    Optional<User> findByEmail(String email);
    //used When you need to CHECK Existence,Not fetch the actual user
    boolean existsByEmail(String email);
}
