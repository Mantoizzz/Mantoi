package com.forum.mantoi.sys.repository;

import com.forum.mantoi.sys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("update User u set u.username=:username,u.email=:email,u.avatar=:avatar,u.introduction=:intro where u.id=:id")
    void updateUser(String username, String email, String avatar, String intro, long id);
}
