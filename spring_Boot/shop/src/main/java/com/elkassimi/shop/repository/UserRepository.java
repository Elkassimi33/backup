package com.elkassimi.shop.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elkassimi.shop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByFullName(String fullName);

    List<User> findByIsEnable(Boolean isEnable);
}
