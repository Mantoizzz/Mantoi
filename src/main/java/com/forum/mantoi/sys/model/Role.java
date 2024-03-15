package com.forum.mantoi.sys.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * @author DELL
 */

@Getter
@RequiredArgsConstructor
public enum Role {

    USER(Set.of(Permission.USER_READ)),

    VIP(Set.of(Permission.VIP_READ)),


    ADMIN(Set.of(Permission.ADMIN_READ,
            Permission.ADMIN_UPDATE, Permission.ADMIN_CREATE, Permission.ADMIN_DELETE
    ));


    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new java.util.ArrayList<>(getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
