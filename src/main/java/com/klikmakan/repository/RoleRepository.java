package com.klikmakan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.klikmakan.model.Role; // Pastikan path ini sesuai dengan lokasi class Role kamu

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByName(String name);
}