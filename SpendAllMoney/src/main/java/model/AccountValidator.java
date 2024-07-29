package model;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import dao.DAOFactory;
import lombok.Getter;

@Component
@Scope("prototype")
@Getter
public class AccountValidator {
	private boolean hasErrors = false;
	private String loginErrors = "";
	private String passwordErrors = "";
	private String repeatedPasswordErrors = "";
	private int minLoginSize = 3;
	private int minPasswordSize = 7;
	
	public void validateLogin(String login) {
		checkLoginSize(login);
		checkLoginOnPattern(login);
	}
	
	public void checkIsLoginExists(String login) {
		if(DAOFactory.getAccountDAO().isLoginExists(login)) {
			this.loginErrors += "this login already exists.";
			this.hasErrors = true;
		}
	}
	
	public void validatePassword(String password) {
		checkPasswordSize(password);
	}
	
	public void validateRepeatedPassword(String password, String repeatedPassword) {
		checkIsRepeatedPasswordSame(password, repeatedPassword);
	}
	
	public void isAccountExists(String login, String password) {
		String pw = DAOFactory.getAccountDAO().getPw(login);
		if(pw == null) {
			this.loginErrors += "this login doesn't exist. ";
			this.hasErrors = true;
		} else if(!BCrypt.checkpw(password, pw)) {
			this.passwordErrors = "this password is wrong. ";
			this.hasErrors = true;
		}
	}
	
	private void checkLoginSize(String login) {
		if(login.length() < this.minLoginSize) {
			this.loginErrors += "login size must be more then 2 letters. ";
			this.hasErrors = true;
		}
	}
	
	private void checkLoginOnPattern(String login) {
		String validLogin = login.replaceAll("[?*-=+;:.,<>^#%@|// /{/}/(/)~`№±§'\"]", "!");
		for(int i = 0; i < validLogin.length(); i++) {
			if(validLogin.charAt(i) == '!') {
				this.loginErrors += "login can't contain: ? * - = + ; : . , < > ^ # % @ | / { } ( ) ~ ` № ± § ' \" tab digits. ";
				this.hasErrors = true;
				break;
			}
		}
	}
	
	private void checkPasswordSize(String password) {
		if(password.length() < this.minPasswordSize) {
			this.passwordErrors = "password size must be more then 6 letters.";
			this.hasErrors = true;
		}
	}
	
	private void checkIsRepeatedPasswordSame(String password, String repeatedPasswrod) {
		if(!password.equals(repeatedPasswrod)) {
			this.repeatedPasswordErrors += "password can't be different.";
			this.hasErrors = true;
		}
	}
}
