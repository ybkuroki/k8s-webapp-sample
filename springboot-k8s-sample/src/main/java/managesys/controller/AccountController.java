package managesys.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import managesys.model.Account;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @GetMapping("/loginStatus")
    public boolean loginStatus() {
        return true;
    }

    @GetMapping("/loginAccount")
    public Account loginAccount(@AuthenticationPrincipal(expression = "account") Account account) {
        return account;
    }

}
