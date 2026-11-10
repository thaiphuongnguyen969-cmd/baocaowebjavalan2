package com.fashion.backend.repository;

import com.fashion.backend.entity.User;

import java.util.List;
import java.util.Map;

public interface UserRepository {
    User findByEmail(String email);
    User findByEmailIncludingPassword(String email);
    boolean emailExists(String email);
    boolean emailExistsExceptId(String email, int id);
    void create(String fullname, String email, String password, String phone, String address, String role, Integer status);
    void update(int id, String fullname, String email, String phone, String address, String role, Integer status);
    void updatePassword(int id, String encodedPassword);
    void delete(int id);
    List<Map<String, Object>> findAllAdminRows();
}
