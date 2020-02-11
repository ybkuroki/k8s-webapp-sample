package managesys.common;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import managesys.model.Account;
import managesys.model.AccountAuthority;
import managesys.model.Category;
import managesys.model.Format;
import managesys.repository.BookRepository;

@Component
@ConditionalOnProperty(prefix = "extension.master.generator", name = "enabled", matchIfMissing = false)
public class MasterDataGenerator {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void initialize() {
        AccountAuthority authority = new AccountAuthority("USER");
        authority.save(bookRepository);

        Collection<AccountAuthority> authorities = new ArrayList<AccountAuthority>();
        authorities.add(authority);
        Account account = new Account("test", passwordEncoder.encode("test"), authorities);
        account.save(bookRepository);

        Category c1 = new Category("技術書");
        c1.save(bookRepository);

        Category c2 = new Category("小説");
        c2.save(bookRepository);

        Category c3 = new Category("雑誌");
        c3.save(bookRepository);

        Format f1 = new Format("書籍");
        f1.save(bookRepository);

        Format f2 = new Format("電子書籍");
        f2.save(bookRepository);
    }

}
