package managesys;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WebTestSupport {

    @Autowired
    private TestRestTemplate rest;

    public ResponseEntity<Object> login(String username, String password, String token) {
        return formPost("/api/account/login", "username=" + username + "&password=" + password, "", token);
    }

    public ResponseEntity<Object> loginStatus(String session, String token) {
        return get("/api/account/loginStatus", session, Object.class, token);
    }

    public ResponseEntity<String> loginAccount(String session, String token) {
        return get("/api/account/loginAccount", session, String.class, token);
    }

    public ResponseEntity<Object> logout(String session, String token) {
        return formPost("/api/account/logout", "", session, token);
    }

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        ResponseEntity<Object> token = loginStatus("", "");
        ResponseEntity<Object> res = login("test", "test", getCsrfToken(token));

        ResponseEntity<T> sres = get(url, getSessionId(res), responseType, getCsrfToken(token));

        return sres;
    }

    public ResponseEntity<Object> post(String url, String body) {
        ResponseEntity<Object> token = loginStatus("", "");
        ResponseEntity<Object> res = login("test", "test", getCsrfToken(token));

        ResponseEntity<Object> sres = jsonPost(url, body, getSessionId(res), getCsrfToken(token));

        return sres;
    }

    public ResponseEntity<Object> formPost(String url, String body, String session, String token) {
        RequestEntity<Object> req = RequestEntity.post(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Cookie", "XSRF-TOKEN=" + token + "; " + session).header("X-XSRF-TOKEN", token).body(body);
        return rest.exchange(req, Object.class);
    }

    public ResponseEntity<Object> jsonPost(String url, String body, String session, String token) {
        RequestEntity<Object> req = RequestEntity.post(URI.create(url))
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Cookie", "XSRF-TOKEN=" + token + "; " + session).header("X-XSRF-TOKEN", token).body(body);
        return rest.exchange(req, Object.class);
    }

    public <T> ResponseEntity<T> get(String url, String session, Class<T> responseType, String token) {
        RequestEntity<Void> req = RequestEntity.get(URI.create(url))
                .header("Cookie", "XSRF-TOKEN=" + token + "; " + session).header("X-XSRF-TOKEN", token).build();
        return rest.exchange(req, responseType);
    }

    public String getSessionId(ResponseEntity<Object> res) {
        if (res.getStatusCode() == HttpStatus.OK) {
            String strCookie = res.getHeaders().get("Set-Cookie").get(2);
            return strCookie.substring(0, strCookie.indexOf(";"));
        }
        return "";
    }

    public String getCsrfToken(ResponseEntity<Object> res) {
        if (res.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            String strCookie = res.getHeaders().get("Set-Cookie").get(0);
            String strToken = strCookie.substring(0, strCookie.indexOf(";"));
            return strToken.substring(strToken.indexOf("=") + 1, strToken.length());
        }
        return "";
    }

}
