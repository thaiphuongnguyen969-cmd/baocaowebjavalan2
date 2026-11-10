package com.fashion.backend.repository;

import com.fashion.backend.entity.User;
import com.fashion.backend.exception.ResourceNotFoundException;
import com.fashion.backend.repository.jpa.UserJpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class JpaUserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository users;

    public JpaUserRepositoryAdapter(UserJpaRepository users) {
        this.users = users;
    }

    @Override
    public User findByEmail(String email) {
        return users.findByEmailIgnoreCase(email).orElse(null);
    }

    @Override
    public User findByEmailIncludingPassword(String email) {
        return findByEmail(email);
    }

    @Override
    public boolean emailExists(String email) {
        return users.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean emailExistsExceptId(String email, int id) {
        return users.existsByEmailIgnoreCaseAndIdNot(email, id);
    }

    @Override
    public void create(String fullname, String email, String password, String phone, String address, String role, Integer status) {
        User user = new User(null, fullname, email, password, role, status);
        user.phone = phone;
        user.address = address;
        users.save(user);
    }

    @Override
    public void update(int id, String fullname, String email, String phone, String address, String role, Integer status) {
        User user = users.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        user.fullname = fullname;
        user.email = email;
        user.phone = phone;
        user.address = address;
        user.role = role;
        user.status = status;
        users.save(user);
    }

    @Override
    public void updatePassword(int id, String encodedPassword) {
        User user = users.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        user.password = encodedPassword;
        users.save(user);
    }

    @Override
    public void delete(int id) {
        users.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> findAllAdminRows() {
        return users.findAll(Sort.by("id").descending()).stream().map(user -> {
            Map<String, Object> row = new java.util.LinkedHashMap<>();
            row.put("id", user.id);
            row.put("fullname", user.fullname);
            row.put("email", user.email);
            row.put("phone", user.phone);
            row.put("address", user.address);
            row.put("role", user.role);
            row.put("status", user.status);
            row.put("created_at", user.createdAt);
            return row;
        }).toList();
    }
}
