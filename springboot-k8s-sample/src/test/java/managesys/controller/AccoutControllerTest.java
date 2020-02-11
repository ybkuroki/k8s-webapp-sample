package managesys.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.jayway.jsonpath.JsonPath;

import managesys.WebTestSupport;
import managesys.model.Account;
import managesys.model.AccountAuthority;
import managesys.security.AccountUserDetails;
import managesys.service.AccountUserDetailsService;

@RunWith(SpringRunner.class)
public class AccoutControllerTest extends WebTestSupport {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AccountUserDetailsService accountUserDetailsService;

    private AccountUserDetails accountUserDetails;

    @Before
    public void setup() {
        createAccountUserDetails();
        when(accountUserDetailsService.loadUserByUsername("test")).thenReturn(accountUserDetails);
    }

    @Test
    public void LoginHttp200OK() {
        ResponseEntity<Object> token = loginStatus("", "");
        ResponseEntity<Object> res = login("test", "test", getCsrfToken(token));

        assertThat(res.getStatusCode(), is(HttpStatus.OK));
        assertThat(getSessionId(res), notNullValue());
    }

    @Test
    public void LoginHttp400Error() {
        ResponseEntity<Object> token = loginStatus("", "");
        ResponseEntity<Object> res = login("test", "abcdefg", getCsrfToken(token));

        assertThat(res.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(getSessionId(res), is(""));
    }

    @Test
    public void LogoutHttp200OK() {
        ResponseEntity<Object> token = loginStatus("", "");
        ResponseEntity<Object> res = login("test", "test", getCsrfToken(token));
        res = logout(getSessionId(res), getCsrfToken(token));

        assertThat(res.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void LoginStatusHttp200OK() {
        ResponseEntity<Object> token = loginStatus("", "");
        ResponseEntity<Object> res = login("test", "test", getCsrfToken(token));
        ResponseEntity<Object> sres = loginStatus(getSessionId(res), getCsrfToken(token));

        assertThat(sres.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void LoginStatusHttp401Error() {
        ResponseEntity<Object> res = loginStatus("", "");

        assertThat(res.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void LoginAccountHttp200OK() {
        ResponseEntity<Object> token = loginStatus("", "");
        ResponseEntity<Object> res = login("test", "test", getCsrfToken(token));
        ResponseEntity<String> sres = loginAccount(getSessionId(res), getCsrfToken(token));

        assertThat(sres.getStatusCode(), is(HttpStatus.OK));
        assertThat(JsonPath.read(sres.getBody(), "$.name"), is("test"));
        assertThat(sres.getBody().indexOf("password"), is(-1));
    }

    @Test
    public void LoginAccountHttp401Error() {
        ResponseEntity<String> res = loginAccount("", "");

        assertThat(res.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    private void createAccountUserDetails() {
        AccountAuthority authority = new AccountAuthority("USER");
        Collection<AccountAuthority> authorities = new ArrayList<AccountAuthority>();
        authorities.add(authority);
        accountUserDetails = new AccountUserDetails(new Account("test", passwordEncoder.encode("test"), authorities));
    }
}
