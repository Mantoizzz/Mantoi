package com.forum.mantoi.sys.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author DELL
 */

@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete");


    private final String permission;
}
