package com.forum.mantoi.sys.model;

import com.forum.mantoi.sys.dao.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author DELL
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUser implements UserDetails {

    private Long id;

    private String username;

    private String email;

    private String password;

    private Role role;

    private Collection<? extends GrantedAuthority> authorities;

    private Boolean isAccountNonExpired;

    private Boolean isEnabled;

    private Boolean isCredentialsNonExpired;

    private Boolean isAccountNonLocked;


    public JwtUser(User user) {
        id = user.getId();
        email = user.getEmail();
        role = user.getRole();
        username = user.getUsername();
        password = user.getPassword();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
