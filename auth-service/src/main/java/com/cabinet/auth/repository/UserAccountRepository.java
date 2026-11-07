package com.cabinet.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cabinet.auth.model.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

	Optional<UserAccount> findByEmail(String email);

	boolean existsByEmail(String email);
}
