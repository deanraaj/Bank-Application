package com.dean.bank_application.repository;

import com.dean.bank_application.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    public boolean existsByEmail(String email);
    public boolean existsByAccountNumber(String accountNumber);

    public Users findByAccountNumber(String accountNumber);

    public Users findByEmail(String email);
}
