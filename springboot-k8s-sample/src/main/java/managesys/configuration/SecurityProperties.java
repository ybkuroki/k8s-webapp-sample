package managesys.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "extension.security")
public class SecurityProperties {

    /** 認証対象のURI */
    private String[] path = new String[] { "/api/**" };
    /** 認証除外対象のURI */
    private String[] exculdePath = new String[] { "/api/health" };
    /** ログインURI */
    private String loginPath = "/api/account/login";
    /** ログアウトURI */
    private String logoutPath = "/api/account/logout";
    /** 認証機能有効可否 */
    private boolean enabled = true;

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public String[] getExculdePath() {
        return exculdePath;
    }

    public void setExculdePath(String[] exculdePath) {
        this.exculdePath = exculdePath;
    }

    public String getLoginPath() {
        return loginPath;
    }

    public void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }

    public String getLogoutPath() {
        return logoutPath;
    }

    public void setLogoutPath(String logoutPath) {
        this.logoutPath = logoutPath;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
