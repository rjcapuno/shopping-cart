package com.shop.shoppingcart.repository;

import com.shop.shoppingcart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
}
