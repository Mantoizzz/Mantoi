package com.forum.mantoi.sys.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUser implements UserDetails {

    private Long id;

    private String username;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private Boolean isAccountNonExpired = true;

    private Boolean isEnabled = true;

    private Boolean isCredentialsNonExpired = true;

    private Boolean isAccountNonLocked = true;


    public JwtUser(User user) {
        id = user.getId();
        username = getUsername();
        password = user.getPassword();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
