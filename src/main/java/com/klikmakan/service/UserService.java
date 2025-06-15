package com.klikmakan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klikmakan.model.User;
import com.klikmakan.model.Role;
import com.klikmakan.repository.UserRepository;
import com.klikmakan.repository.RoleRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    public boolean registerUser(User user, String roleId) {
        System.out.println("Cek username: " + user.getUsername());

        if (userRepo.existsByUsername(user.getUsername())) {
            System.out.println("Username sudah ada");
            return false;
        }

        // Cari role berdasarkan id (ADMIN atau USER)
        Role foundRole = roleRepo.findById(roleId).orElse(null);
        if (foundRole == null) {
            System.out.println("Role tidak ditemukan di DB!");
            return false;
        }

        // Ubah: gunakan username sebagai ID
        user.setId(user.getUsername());

        // Simpan password langsung (catatan: nanti perlu dienkripsi!)
        user.setPassword(user.getPassword());

        // Set role
        user.setRole(foundRole);

        // Simpan user
        userRepo.save(user);
        return true;
    }
}