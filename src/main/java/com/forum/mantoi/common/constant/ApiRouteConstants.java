package com.forum.mantoi.common.constant;

/**
 * URL路由常数
 *
 * @author DELL
 */
public interface ApiRouteConstants {

    String API_ALL = "/**";

    String API_AUTH_PREFIX = "/auth";

    String API_CAPTCHA = "/captcha";

    String API_SMS = "/sms/captcha";

    String API_REGISTER = "/register";

    String API_LOGIN = "/login";

    String API_LOGOUT = "/logout";

    String API_REGISTER_URL = "/auth/register";

    String API_LOGIN_URL = "/auth/login";

    String API_LOGOUT_URL = "/auth/logout";

    String API_USER_PROFILE = "/profile/{userId}";

    String API_POST_PREFIX = "/post";

    String API_ADD = "/add";

    String API_POST_DETAIL = "/detail/{postId}/{page}";

    String API_POST_LOAD_MORE = "/loadMore";

    String API_POST_DELETE = "/delete/{postId}";

    String API_LIKE = "/*/like";

    String API_USER_FOLLOW = "/follow";

    String API_USER_SUBSCRIBERS = "/subscribers/{curPage}";

    String API_USER_FANS = "/fans/{curPage}";

    String API_POST_ADD_COMMENT = "/{postId}/addComment";

    String API_POST_ADD_REPLY = "/{postId}/{commentId}/addReply";

    String API_ERROR = "/error";


}
