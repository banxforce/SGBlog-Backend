package org.example.utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WebUtils {
    /**
     * 将字符串渲染到客户端
     * 
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static void renderString(HttpServletResponse response, String string) {
        try(PrintWriter writer = response.getWriter()) {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            writer.print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置下载头
     * @param filename 文件类型
     */
    public static void setDownLoadHeader(String filename,  HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }
}