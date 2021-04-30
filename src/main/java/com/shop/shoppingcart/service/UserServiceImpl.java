package com.shop.shoppingcart.service;

import com.shop.shoppingcart.model.User;
import com.shop.shoppingcart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    public User findById(long id) {
        return userRepository.findById(id);
    }
}
