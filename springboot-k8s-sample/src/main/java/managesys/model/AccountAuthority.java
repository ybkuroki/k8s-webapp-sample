package managesys.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;

import managesys.repository.AbstractRepository;

@Entity(name = "AUTHORITY_MASTER")
public class AccountAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String authority = "";

    public AccountAuthority() {}

    public AccountAuthority(String authority) {
        setAuthority(authority);
    }

    public int getId() {
        return id;
    }

    public void setAuthority(String authority) {
        if (authority.startsWith("ROLE_")) {
            this.authority = authority;
        } else {
            this.authority = "ROLE_" + authority;
        }
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void save(AbstractRepository repo) {
        repo.save(AccountAuthority.class, this);
    }
}
