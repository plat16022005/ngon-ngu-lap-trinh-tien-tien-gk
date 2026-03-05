package com.khoithinhvuong.dev.config;

import jakarta.persistence.EntityManager;

@FunctionalInterface
public interface JpaWork<T> {
    T execute(EntityManager em);
}
