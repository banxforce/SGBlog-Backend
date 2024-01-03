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
     *将异常信息通过response写入响应体传递给前端
     *
     * @param response 渲染对象，用于向客户端发送响应
     * @param string 待渲染的字符串，将作为响应发送给客户端
     */
    public static void renderString(HttpServletResponse response, String string) {
        try (PrintWriter writer = response.getWriter()) {
            // 设置响应状态码为 200，表示成功
            response.setStatus(200);
            // 设置响应内容类型为 JSON
            response.setContentType("application/json");
            // 设置响应字符编码为 UTF-8，确保中文等字符正确显示
            response.setCharacterEncoding("utf-8");
            // 将字符串写入响应输出流
            writer.print(string);
        } catch (IOException e) {
            // 如果发生 I/O 异常，打印异常信息
            e.printStackTrace();
        }
    }



    /**
     * 设置下载头信息
     *
     * @param filename 要下载的文件名称，用于设置下载的文件名
     * @param response HttpServletResponse 对象，用于设置响应头信息
     * @throws UnsupportedEncodingException 如果编码不支持，抛出该异常
     */
    public static void setDownLoadHeader(String filename, HttpServletResponse response) throws UnsupportedEncodingException {
        // 设置响应内容类型为 Excel 文件
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // 设置响应字符编码为 UTF-8，确保处理中文等字符时正确显示
        response.setCharacterEncoding("utf-8");
        // 使用 URLEncoder 对文件名进行编码，避免中文乱码，并替换空格为 %20
        String encodedFileName = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        // 设置响应头的 Content-Disposition 属性，指示浏览器以下载方式处理响应
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
    }

}