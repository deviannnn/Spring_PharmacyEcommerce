package vn.edu.tdtu.springecommerce.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.springecommerce.model.Account;
import vn.edu.tdtu.springecommerce.model.Cart;
import vn.edu.tdtu.springecommerce.model.Customer;
import vn.edu.tdtu.springecommerce.service.AccountService;
import vn.edu.tdtu.springecommerce.service.CartService;
import vn.edu.tdtu.springecommerce.service.CustomerService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;
    private PasswordEncoder passwordEncoder;
    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Customer> listAllCustomer() {
        return customerService.findAll();
    }

    @PostMapping("/register")
    public String register(@RequestParam("name") String name, @RequestParam("username") String username,
                           @RequestParam("password") String password, @RequestParam("repassword") String repassword, RedirectAttributes model) {
        model.addFlashAttribute("name", name);
        model.addFlashAttribute("username", username);
        model.addFlashAttribute("password", password);
        model.addFlashAttribute("repassword", repassword);
        if (name == "" || username == "" || password == "" || repassword == "") {
            model.addFlashAttribute("registerFail", "Sorry. Information is incomplete.");
            return "redirect:/register";
        } else if (accountService.findByUsername(username) != null) {
            model.addFlashAttribute("registerFail", "Sorry. This username has existed.");
            return "redirect:/register";
        } else if (password.length() < 6) {
            model.addFlashAttribute("registerFail", "Sorry. Password must have at least 6 characters.");
            return "redirect:/register";
        } else if (!password.equals(repassword)) {
            model.addFlashAttribute("registerFail", "Sorry. Confirm password didn't correct.");
            return "redirect:/register";
        } else {
            accountService.register(username, password, name);
            model.addFlashAttribute("registerSuccess", "Congratulations, you have successfully registered");
            return "redirect:/login";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        RedirectAttributes model, HttpSession session) {
        model.addFlashAttribute("username", username);
        model.addFlashAttribute("password", password);
        if (username == "" || password == "") {
            model.addFlashAttribute("loginFail", "Sorry. Information is incomplete.");
            return "redirect:/login";
        }
        Account loginAccount = accountService.login(username, password);
        if (loginAccount == null) {
            model.addFlashAttribute("loginFail", "Sorry. Username or password didn't correct.");
            return "redirect:/login";
        }
        int permission = loginAccount.getPermission();
        session.setAttribute("isLogged", true);
        session.setAttribute("permission", permission);
        if (permission >= 2) {
            return "redirect:/admin";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String today = formatter.format(date);

            Customer currentCustomer = customerService.findByAccount_Id(loginAccount.getId());
            currentCustomer.setLastActive(today);

            session.setAttribute("customer", currentCustomer);
            return "redirect:/";
        }
    }

    @PostMapping("/update/{id}")
    public String updatePermission(@PathVariable("id") int accountId, @RequestParam("permission") int permission) {
        accountService.updatePermit(accountId, permission);
        return "redirect:/admin/account";
    }

}
