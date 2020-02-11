package managesys.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import managesys.security.LoginLogoutHandler;
import managesys.security.RestAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private LoginLogoutHandler loginLogoutHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 認証を除外するURIを指定する
        http
            .authorizeRequests()
            .mvcMatchers(securityProps().getExculdePath()).permitAll();

        // 認証機能が有効の場合
        if (securityProps().isEnabled()) {
            // USER権限を持つユーザのみ許可する
            http
                .authorizeRequests()
                .antMatchers(securityProps().getPath()).hasRole("USER");

            // 認証失敗時に401を返すように設定する
            http
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint);

            // CSRF対策を有効にする。
            http
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

            // 認証機能が無効の場合
        } else {
            // 全てのユーザを許可する
            http
                .authorizeRequests()
                .antMatchers("/**").permitAll();

            // CSRF対策を無効化する
            http.csrf().disable();
        }

        // CORSをフィルタベースで適用させる
        http
            .cors();

        // ログイン・ログアウト時の処理は、フォーム認証をベースに拡張する
        // ログイン時のハンドラを設定する
        // ログインURIは、誰でもアクセス可
        http
            .formLogin().loginPage(securityProps().getLoginPath())
            .usernameParameter("username")
            .passwordParameter("password")
            .successHandler(loginLogoutHandler)
            .failureHandler(loginLogoutHandler)
            .permitAll();

        // ログアウト時のハンドラを設定する
        // ログアウトURIは、誰でもアクセス可
        http
            .logout().logoutUrl(securityProps().getLogoutPath())
            .logoutSuccessHandler(loginLogoutHandler)
            .permitAll();
    }

    @Autowired
    public void configureAuthenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public LoginLogoutHandler loginLogoutHandler() {
        return new LoginLogoutHandler();
    }

    @Bean
    public SecurityProperties securityProps() {
        return new SecurityProperties();
    }
}
