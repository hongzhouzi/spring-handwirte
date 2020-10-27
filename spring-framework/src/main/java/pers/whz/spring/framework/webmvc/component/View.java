package pers.whz.spring.framework.webmvc.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 视图-实体类
 *
 * @author hongzhou.wei
 * @date 2020/10/26
 */
public class View {
    private File viewFile;

    public View(File templateFile) {
        this.viewFile = templateFile;
    }

    /**
     * 自定义解析视图逻辑
     *
     * @param model
     * @param req
     * @param resp
     * @throws Exception
     */
    public void render(Map<String, ?> model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        StringBuffer sb = new StringBuffer();
        RandomAccessFile ra = new RandomAccessFile(this.viewFile, "r");

        String line = null;
        while (null != (line = ra.readLine())) {
            line = new String(line.getBytes("ISO-8859-1"), "utf-8");
            Pattern pattern = Pattern.compile("￥\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String paramName = matcher.group();
                paramName = paramName.replaceAll("￥\\{|\\}", "");
                Object paramValue = model.get(paramName);
                line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                matcher = pattern.matcher(line);
            }
            sb.append(line);
        }
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write(sb.toString());
    }

    /**
     * 处理特殊字符
     *
     * @param str 待处理字符串
     * @return 处理完特殊字符的字符串
     */
    public static String makeStringForRegExp(String str) {
        return str.replace("\\", "\\\\").replace("*", "\\*")
            .replace("+", "\\+").replace("|", "\\|")
            .replace("{", "\\{").replace("}", "\\}")
            .replace("(", "\\(").replace(")", "\\)")
            .replace("^", "\\^").replace("$", "\\$")
            .replace("[", "\\[").replace("]", "\\]")
            .replace("?", "\\?").replace(",", "\\,")
            .replace(".", "\\.").replace("&", "\\&");
    }
}
