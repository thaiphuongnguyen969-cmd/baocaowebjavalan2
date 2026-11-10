package com.fashion.backend.service.impl;

import com.fashion.backend.exception.BadRequestException;
import com.fashion.backend.entity.User;
import com.fashion.backend.repository.UserRepository;
import com.fashion.backend.service.UserService;
import com.fashion.backend.util.AccountNormalizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Map<String, Object>> findAllUsers() {
        return userRepository.findAllAdminRows();
    }

    @Override
    public Map<String,Object> profile(String email) {
        User user=requireUser(email);
        return Map.of("id",user.id(),"fullname",user.fullname(),"email",user.email(),
            "phone",user.phone==null?"":user.phone,"address",user.address==null?"":user.address,
            "role",user.role(),"status",user.status());
    }

    @Override
    public void updateProfile(String email,Map<String,Object> body) {
        User user=requireUser(email);
        String fullname=String.valueOf(body.getOrDefault("fullname","")).trim();
        String account=AccountNormalizer.normalize(String.valueOf(body.getOrDefault("email",email)));
        if(fullname.isEmpty()||account.isEmpty()) throw new BadRequestException("Họ tên và tài khoản không được để trống");
        if(userRepository.emailExistsExceptId(account,user.id())) {
            throw new BadRequestException("Email hoặc tài khoản đã được sử dụng");
        }
        userRepository.update(user.id(),fullname,account,
            String.valueOf(body.getOrDefault("phone","")),String.valueOf(body.getOrDefault("address","")),
            user.role(),user.status());
    }

    @Override
    public void changePassword(String email,Map<String,Object> body) {
        User user=requireUser(email);
        String current=String.valueOf(body.getOrDefault("currentPassword",""));
        String next=String.valueOf(body.getOrDefault("newPassword",""));
        if(current.isEmpty()||next.length()<6) throw new BadRequestException("Nhập mật khẩu hiện tại và mật khẩu mới tối thiểu 6 ký tự");
        if(!passwordEncoder.matches(current,user.password())) throw new BadRequestException("Mật khẩu hiện tại không đúng");
        userRepository.updatePassword(user.id(),passwordEncoder.encode(next));
    }

    @Override
    public void createUser(Map<String, Object> body) {
        String email = AccountNormalizer.normalize(String.valueOf(body.getOrDefault("email", "")));
        String password = String.valueOf(body.getOrDefault("password", ""));
        if (email.isEmpty() || password.length() < 6) {
            throw new BadRequestException("Email/tài khoản và mật khẩu (tối thiểu 6 ký tự) là bắt buộc");
        }
        if (userRepository.emailExists(email)) {
            throw new BadRequestException("Email hoặc tài khoản đã được sử dụng");
        }
        userRepository.create(String.valueOf(body.getOrDefault("fullname", "")), email,
            passwordEncoder.encode(password), String.valueOf(body.getOrDefault("phone", "")),
            String.valueOf(body.getOrDefault("address", "")),
            String.valueOf(body.getOrDefault("role", "customer")),
            statusValue(body.get("status")));
    }

    @Override
    public void updateUser(int id, Map<String, Object> body) {
        String email = AccountNormalizer.normalize(String.valueOf(body.getOrDefault("email", "")));
        String password = String.valueOf(body.getOrDefault("password", "")).trim();
        if (email.isEmpty()) throw new BadRequestException("Email hoặc tài khoản không được để trống");
        if (userRepository.emailExistsExceptId(email, id)) {
            throw new BadRequestException("Email hoặc tài khoản đã được sử dụng");
        }
        if (!password.isEmpty() && password.length() < 6) {
            throw new BadRequestException("Mật khẩu mới phải có ít nhất 6 ký tự");
        }
        userRepository.update(id, String.valueOf(body.getOrDefault("fullname", "")), email,
            String.valueOf(body.getOrDefault("phone", "")),
            String.valueOf(body.getOrDefault("address", "")),
            String.valueOf(body.getOrDefault("role", "customer")),
            statusValue(body.get("status")));
        if (!password.isEmpty()) {
            userRepository.updatePassword(id, passwordEncoder.encode(password));
        }
    }

    @Override
    public void deleteUser(int id) {
        userRepository.delete(id);
    }

    private Integer statusValue(Object value) {
        if (value == null || value.toString().isBlank()) return 1;
        try {
            int status = Integer.parseInt(value.toString());
            if (status != 0 && status != 1) throw new BadRequestException("status chỉ nhận 0 hoặc 1");
            return status;
        } catch (NumberFormatException ex) {
            throw new BadRequestException("status chỉ nhận 0 hoặc 1");
        }
    }

    private User requireUser(String email) {
        User user=userRepository.findByEmailIncludingPassword(email);
        if(user==null) throw new BadRequestException("Không tìm thấy người dùng");
        return user;
    }
}
