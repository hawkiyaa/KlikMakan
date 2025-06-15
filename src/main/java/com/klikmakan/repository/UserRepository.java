package com.klikmakan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.klikmakan.model.User; // Sesuaikan path-nya sesuai lokasi User.java

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    User findByUsername(String username);
}
