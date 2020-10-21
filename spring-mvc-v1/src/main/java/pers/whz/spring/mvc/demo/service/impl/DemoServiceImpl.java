package pers.whz.spring.mvc.demo.service.impl;

import pers.whz.spring.mvc.annotation.MyService;
import pers.whz.spring.mvc.demo.service.IDemoService;

/**
 * @author hongzhou.wei
 * @date 2020/10/21
 */
@MyService
public class DemoServiceImpl implements IDemoService {
    @Override
    public String get(String name) {
        return "My name is " + name + ",from service.";
    }
}
