package com.cvte.demo.dao;

import com.cvte.demo.pojo.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEmailDAO extends JpaRepository<UserEmail,Integer> {
}
