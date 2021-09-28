package org.example.service;

import org.example.entity.User;

public interface UserService {
    User getUserByUserId(Integer id);
    Integer insertUserId(User user);
}
