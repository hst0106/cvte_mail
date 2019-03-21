package com.cvte.demo.service;

import com.cvte.demo.common.ServerResponse;
import com.cvte.demo.pojo.Mail;

import java.util.concurrent.Future;

/**
 * @Author: huangshuntong
 * @Date: 2019/3/21 9:48
 * @description
 */
public interface AsyncService {
    /*
    * 执行异步任务
    * */

    Future<Integer> executeAsync(Mail mail);
}
