package model;

import jakarta.servlet.http.Cookie;

public class CookieChecker {
	public static Cookie findLoginCookie(Cookie[] cookies) {
		if(cookies != null) {
			  for(Cookie cookie : cookies) {
			  	if(cookie.getName().equals("login")) {
			  		return cookie;
			  	}
			  }
			}
		return null;
	}
}
