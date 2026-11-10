package com.fashion.backend.service;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<Map<String, Object>> findAllUsers();
    Map<String,Object> profile(String email);
    void updateProfile(String email,Map<String,Object> body);
    void changePassword(String email,Map<String,Object> body);
    void createUser(Map<String, Object> body);
    void updateUser(int id, Map<String, Object> body);
    void deleteUser(int id);
}
