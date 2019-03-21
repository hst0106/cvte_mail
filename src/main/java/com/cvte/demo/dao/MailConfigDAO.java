package com.cvte.demo.dao;

import com.cvte.demo.pojo.MailConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: huangshuntong
 * @Date: 2019/3/21 20:29
 * @description
 */
public interface MailConfigDAO extends JpaRepository<MailConfig,Integer> {
}
