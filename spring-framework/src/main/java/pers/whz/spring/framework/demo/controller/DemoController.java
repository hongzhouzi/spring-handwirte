package pers.whz.spring.framework.demo.controller;

import pers.whz.spring.framework.annotation.Autowired;
import pers.whz.spring.framework.annotation.Controller;
import pers.whz.spring.framework.annotation.RequestMapping;
import pers.whz.spring.framework.annotation.RequestParam;
import pers.whz.spring.framework.demo.service.IDemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hongzhou.wei
 * @date 2020/10/21
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private IDemoService demoService;

    @RequestMapping("/query")
    public void query(HttpServletRequest req, HttpServletResponse resp,
                      @RequestParam("name") String name) {
        String result = demoService.get(name);
        try {
            resp.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
