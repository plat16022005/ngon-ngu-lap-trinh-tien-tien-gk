package com.khoithinhvuong.dev.repository;

import com.khoithinhvuong.dev.model.UserAccount;
import jakarta.persistence.EntityManager;

public interface UserAccountRepository {
    public UserAccount findByUserName(String username);
    public void create(UserAccount userAccount);
}
