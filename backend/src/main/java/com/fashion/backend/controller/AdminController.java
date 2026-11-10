package com.fashion.backend.controller;

import com.fashion.backend.service.UserService;
import com.fashion.backend.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:8090", "http://127.0.0.1:8090"})
public class AdminController {
    private final UserService userService;
    private final DashboardService dashboardService;

    public AdminController(UserService userService, DashboardService dashboardService) {
        this.userService = userService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/users")
    public List<Map<String, Object>> users() {
        return userService.findAllUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {
        userService.createUser(body);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of("message", "Tạo người dùng thành công"));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Map<String, Object> body) {
        userService.updateUser(id, body);
        return ResponseEntity.ok(Map.of("message", "Cập nhật người dùng thành công"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "Đã xóa người dùng"));
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return dashboardService.summary();
    }
}
