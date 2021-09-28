package org.example.service;

import org.example.annotation.Service;
import org.example.annotation.ServiceScan;
import org.example.entity.User;

import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User getUserByUserId(Integer id) {
        Random random = new Random();
        User user = User.builder().userName(UUID.randomUUID().toString())
                .sex(random.nextBoolean())
                .id(id)
                .build();
        return user;
    }

    @Override
    public Integer insertUserId(User user) {
        System.out.println("插入数据成功：" + user);
        return 1;
    }
}
