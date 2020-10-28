package pers.whz.spring.framework.aop.config;

import lombok.Data;

/**
 * aop配置-实体类
 *
 * @author hongzhou.wei
 * @date 2020/10/28
 */
@Data
public class AopConfig {
    /**
     * 切点
     */
    private String pointCut;
    /**
     * 切面类
     */
    private String aspectClass;

    private String aspectBefore;
    private String aspectAfter;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;
}
