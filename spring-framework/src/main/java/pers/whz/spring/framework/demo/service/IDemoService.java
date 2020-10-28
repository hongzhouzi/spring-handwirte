package pers.whz.spring.framework.demo.service;

/**
 * @author hongzhou.wei
 * @date 2020/10/21
 */
public interface IDemoService {

    String get(String name);

    int update(String name) throws Exception;

}
