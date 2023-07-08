/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic;

import cn.ntopic.service.impl.NTUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SpringSecurity 配置
 *
 * @author obullxl 2021年06月19日: 新增
 */
// @EnableWebSecurity(debug = true)
// @EnableGlobalMethodSecurity(prePostEnabled = true)
public class NTSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private NTUserServiceImpl ntUserService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.ntUserService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 如果有允许匿名的url，填在下面
//                .antMatchers().permitAll()
                .anyRequest().authenticated()
                .and()
                // 设置登陆页
                .formLogin()//.loginPage("/login")
                // 设置登陆成功页
                .defaultSuccessUrl("/").permitAll()
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout().permitAll();

        // 关闭CSRF跨域
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**");
    }

}
