package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import dao.DAOFactory;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.AccountValidator;
import model.CookieChecker;
import model.JedisEntityDeleter;
                             
@Controller
public class AuthontificationController {
	@GetMapping("/registration")
	public String registration(HttpServletRequest request, Model model) {
		Cookie[] cookies = request.getCookies();
		Cookie login = CookieChecker.findLoginCookie(cookies);
		if(login != null) {
			return "redirect:/main";
		}
		HttpSession session = request.getSession();
		session.setAttribute("loginErrors", "");
		session.setAttribute("passwordErrors", "");
		session.setAttribute("repeatedPasswordErrors", "");
		model.addAttribute("personalSession", session);
		model.addAttribute("account", new Account());
		return "registration";
	}
	
	@PostMapping("/registration")
	public String checkRegistration(@ModelAttribute("account") Account account, @Autowired AccountValidator accountValidator, HttpServletRequest request, HttpServletResponse response, Model model) {
		accountValidator.validateLogin(account.getLogin());
		accountValidator.checkIsLoginExists(account.getLogin());
		accountValidator.validatePassword(account.getPassword());
		accountValidator.validateRepeatedPassword(account.getPassword(), account.getRepeatedPassword());
		HttpSession session = request.getSession();
		session.setAttribute("loginErrors", accountValidator.getLoginErrors());
		session.setAttribute("passwordErrors", accountValidator.getPasswordErrors());
		session.setAttribute("repeatedPasswordErrors", accountValidator.getRepeatedPasswordErrors());
		model.addAttribute("personalSession", session);
		if(accountValidator.isHasErrors()) {
			model.addAttribute("account", account);
			return "registration";
		}
		DAOFactory.getAccountDAO().addAccount(account);
		JedisEntityDeleter.del("top");
		Cookie login = new Cookie("login", account.getLogin());
		login.setMaxAge(60 * 60 * 24 * 7);
		response.addCookie(login);
		return "redirect:/main";
	}
	
	@GetMapping("/signin")
	public String signin(HttpServletRequest request, Model model) {
		Cookie[] cookies = request.getCookies();
		Cookie login = CookieChecker.findLoginCookie(cookies);
		if(login != null) {
			return "redirect:/main";
		}
		HttpSession session = request.getSession();
		session.setAttribute("loginErrors", "");
		session.setAttribute("passwordErrors", "");
		model.addAttribute("personalSession", session);
		model.addAttribute("account", new Account());
		return "signin";
	}
	
	@PostMapping("/signin")
	public String checkSignin(@ModelAttribute("account") Account account, @Autowired AccountValidator accountValidator,HttpServletRequest request, HttpServletResponse response, Model model) {
		accountValidator.isAccountExists(account.getLogin(), account.getPassword());
		accountValidator.validatePassword(account.getPassword());
		HttpSession session = request.getSession();
		session.setAttribute("loginErrors", accountValidator.getLoginErrors());
		session.setAttribute("passwordErrors", accountValidator.getPasswordErrors());
		model.addAttribute("personalSession", session);
		if(accountValidator.isHasErrors()) {
			model.addAttribute("account", account);
			return "signin";
		}
		Cookie login = new Cookie("login", account.getLogin());
		login.setMaxAge(60 * 60 * 24 * 7);
		response.addCookie(login);
		return "redirect:/main";
	}
	
	@DeleteMapping("/signout")
	public void signout(HttpServletResponse response) {
		Cookie cookie = new Cookie("login", null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	@GetMapping("/signout")
	public void signoutGet(HttpServletResponse response) {
		Cookie cookie = new Cookie("login", null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
