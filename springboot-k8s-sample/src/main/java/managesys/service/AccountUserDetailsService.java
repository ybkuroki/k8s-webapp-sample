package managesys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import managesys.model.Account;
import managesys.repository.BookRepository;
import managesys.security.AccountUserDetails;

@Service
public class AccountUserDetailsService implements UserDetailsService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = Account.findByUsername(bookRepository, username)
                                .orElseThrow(() -> new UsernameNotFoundException("user not found."));

        return new AccountUserDetails(account);
    }

}
