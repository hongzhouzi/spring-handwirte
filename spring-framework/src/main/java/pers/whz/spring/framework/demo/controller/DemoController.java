package pers.whz.spring.framework.demo.controller;

import pers.whz.spring.framework.annotation.Autowired;
import pers.whz.spring.framework.annotation.Controller;
import pers.whz.spring.framework.annotation.RequestMapping;
import pers.whz.spring.framework.annotation.RequestParam;
import pers.whz.spring.framework.demo.service.IDemoService;
import pers.whz.spring.framework.webmvc.component.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping("/model")
    public ModelAndView model(@RequestParam("name") String name) {
        String result = demoService.get(name);
        Map<String, Object> model = new HashMap<>();
        model.put("name", name);
        model.put("data", result);
        model.put("token", "123456");
        return new ModelAndView("demo.html", model);
    }

    @RequestMapping("/update")
    public ModelAndView update(@RequestParam("name") String name) {
        try {
            int result = demoService.update(name);
            return null;
        }catch (Throwable e){
            Map<String,String> model = new HashMap<>();
            model.put("detail",e.getCause().getMessage());
            model.put("stackTrace", Arrays.toString(e.getStackTrace()));
            return new ModelAndView("500",model);
        }
    }

}
