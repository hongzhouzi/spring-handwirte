package pers.whz.spring.framework.demo.service.impl;

import pers.whz.spring.framework.annotation.Service;
import pers.whz.spring.framework.demo.service.IDemoService;

/**
 * @author hongzhou.wei
 * @date 2020/10/21
 */
@Service
public class DemoServiceImpl implements IDemoService {
    @Override
    public String get(String name) {
        return "My name is " + name + ",from service.";
    }
}
