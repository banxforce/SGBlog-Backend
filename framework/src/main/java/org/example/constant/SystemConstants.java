package org.example.constant;

import io.swagger.v3.oas.models.security.SecurityScheme;

public class SystemConstants {
    /**
     * 文章是草稿
     */
    public static final String ARTICLE_STATUS_DRAFT = "1";
    /**
     * 文章是正常分布状态
     */
    public static final String ARTICLE_STATUS_NORMAL = "0";
    /**
     * 热门文章当前页码
     */
    public static final int HOT_ARTICLE_PAGE_NUMBER = 1;
    /**
     * 热门文章单页大小
     */
    public static final int HOT_ARTICLE_PAGE_SIZE = 10;
    /**
     * 类别状态为正常
     */
    public static final String CATEGORY_STATUS_NORMAL = "0";
    /**
     * 类别状态为不可用
     */
    public static final String CATEGORY_STATUS_DISABLE = "1";
    /**
     * 友链审核通过
     */
    public static final String LINK_STATUS_PASS = "0";
    /**
     * 为根评论
     */
    public static final Long COMMENT_ROOT_ID = -1L;
    /**
     * 评论类型: 文章评论
     */
    public static final String ARTICLE_CONTENT = "0";
    /**
     * 评论类型: 友链评论
     */
    public static final String LINK_CONTENT = "1";
    /**
     * 菜单类型为菜单
     */
    public static final String MENU_TYPE_MENU = "C";
    /**
     * 菜单类型为按钮
     */
    public static final String MENU_TYPE_BUTTON = "F";
    /**
     * 菜单类型为目录
     */
    public static final String MENU_TYPE_DIRECTORY = "M";
    /**
     * 菜单状态正常
     */
    public static final String MENU_STATUS_NORMAL = "0" ;
    /**
     * 超级管理员
     */
    public static final Long SUPER_ADMIN = 1L;
    /**
     * 父菜单
     */
    public static final Long MENU_PARENT = 0L;
    /**
     * 用户账户类型为管理员
     */
    public static final String USER_TYPE_ADMIN = "1";

}