package pers.whz.spring.mvc.demo.controller;

import pers.whz.spring.mvc.annotation.MyAutowired;
import pers.whz.spring.mvc.annotation.MyController;
import pers.whz.spring.mvc.annotation.MyRequestMapping;
import pers.whz.spring.mvc.annotation.MyRequestParam;
import pers.whz.spring.mvc.demo.service.IDemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hongzhou.wei
 * @date 2020/10/21
 */
@MyController
@MyRequestMapping("/demo")
public class DemoController {
    @MyAutowired
    private IDemoService demoService;

    @MyRequestMapping("/query")
    public void query(HttpServletRequest req, HttpServletResponse resp,
                      @MyRequestParam("name") String name) {
        String result = demoService.get(name);
        try {
            resp.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @MyRequestMapping("/append")
    public void append(HttpServletRequest req, HttpServletResponse resp,
                    @MyRequestParam("a") String a, @MyRequestParam("b") String b){
        try {
            resp.getWriter().write(a + " +append+ " + b + "=" + (a + b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @MyRequestMapping("/add")
    public void add(HttpServletRequest req, HttpServletResponse resp,
                    @MyRequestParam("a") Integer a, @MyRequestParam("b") Integer b){
        try {
            resp.getWriter().write(a + "+" + b + "=" + (a + b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
