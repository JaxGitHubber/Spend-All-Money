package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DAOFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;
import model.CookieChecker;
import model.Item;
import model.ItemsCountBarrier;
import model.ItemsHistory;
import model.ItemsStorage;
import model.JedisEntityDeleter;
import model.JsonListParser;
import model.LeaderBoard;
import model.People;
import model.Person;
import model.PriceParser;
import model.SessionFiller;
import model.ShoppingCart;
import model.TopCreaterForLeaderBoard;
import redis.clients.jedis.Jedis;
        
@Controller
public class WebController {
	@Autowired
	private ApplicationContext appliactionContext;
	@Autowired
	private People people;
	private Cookie loginCookie;
	  
	@GetMapping("/main")
	public String getMain(@Autowired SessionFiller sessionFiller, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		this.loginCookie = CookieChecker.findLoginCookie(request.getCookies());
		if(this.loginCookie != null) {
			if((String) session.getAttribute("status") == null || !((String) session.getAttribute("status")).equalsIgnoreCase("SignIn")) {
				sessionFiller.setApplicationContext(appliactionContext);
				sessionFiller.fillAccountSession(this.loginCookie.getValue(), session); 
			}
			model.addAttribute("login", this.loginCookie.getValue());
			model.addAttribute("isSignIn", true);
		} else {
			if((String) session.getAttribute("status") == null || !((String) session.getAttribute("status")).equalsIgnoreCase("SignOut")) {
				sessionFiller.setApplicationContext(appliactionContext);
				sessionFiller.fillStartSession(session); 
			}
			model.addAttribute("isSignIn", false);
		}
		
		model.addAttribute("personalSession", session);
		model.addAttribute("people", people);
		return "main";  
	}
	
	@GetMapping("/shoppingcart")
	public String getShoppingCart(Model model, HttpSession session) {
		try {
		session.setAttribute("state", ((Person) session.getAttribute("person")).getState());
		session.setAttribute("fixedState", ((Person) session.getAttribute("person")).getFixedState());
		model.addAttribute("personalSession", session); 
		if(((ShoppingCart) session.getAttribute("shoppingCart")).getItems().size() == 0) {
			return "emptyShoppingCart"; 
		}
		return "shoppingCart";
		} catch(Exception e) {
			return "redirect:/main";
		}
	}
	
	@GetMapping("/gratitude")
	public String gratitude(Model model, HttpSession session) {
		try {
		session.setAttribute("state", ((Person) session.getAttribute("person")).getState());
		model.addAttribute("personalSession", session); 
		return "gratitude";
		} catch(Exception e) {
			return "redirect:/main";
		}
	}
	
	@GetMapping("/leaderboard")
	public String getLeaderBoard(HttpSession session, Model model) {
		Jedis jedis = new Jedis("localhost", 6379);
		List<LeaderBoard> top;
		List<String> jsonTop = jedis.lrange("top", 0, -1);
				if(jsonTop.size() == 0) {
					top = DAOFactory.getLeaderBoardDAO().getAllSortedTop();
					jedis.rpush("top", JsonListParser.parseTopToJson(top).toArray(new String[0]));
				} else {
					top = JsonListParser.parseJsonToTop(jsonTop);
				}
		jedis.close();
		if(top.size() == 0) {
			return "emptyLeaderBoard";
		}
		String login = "";
		if(this.loginCookie != null) { 
			login = loginCookie.getValue();
		}
		TopCreaterForLeaderBoard.genTop(top, session, login);
		model.addAttribute("personalSession", session);
		return "leaderBoard";
	}
	
	@PostMapping("/leaderboard")
	public String increaseTotalSpentMoneyForAccount(@RequestBody String totalSpentMoney) {
		Long leaderBoardID = DAOFactory.getAccountDAO().getAccount(this.loginCookie.getValue()).getLeaderboard().getId();
		Long saveTotalSpentMoney = DAOFactory.getLeaderBoardDAO().getTotalSpentMoney(leaderBoardID);
		Long newtotalSpentMoney = saveTotalSpentMoney + PriceParser.parseToLong(totalSpentMoney);
		DAOFactory.getLeaderBoardDAO().updateTotalSpentMoney(leaderBoardID, newtotalSpentMoney);
		JedisEntityDeleter.del("top");
		return "success";
	}
	
	@PostMapping("/item") 
	public String addItem(@RequestBody String itemJson, HttpSession session) {
		Item item = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			item = mapper.readValue(itemJson, Item.class);
			((ShoppingCart) session.getAttribute("shoppingCart")).addItem(item); 
			((ItemsStorage) session.getAttribute("storage")).incrementItem(item);
		} catch (JsonProcessingException e) {
			e.printStackTrace();       
		} 
		return "success"; 
	}
	
	@DeleteMapping("/item")
	public String deleteItem(@RequestBody String itemJson, HttpSession session) {
		Item item = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			item = mapper.readValue(itemJson, Item.class);
			((ShoppingCart) session.getAttribute("shoppingCart")).removeItem(item);
			((ItemsStorage) session.getAttribute("storage")).decrementItem(item);
		} catch (JsonProcessingException e) {
			e.printStackTrace(); 
		} 
		return "success";
	} 
	
	@PostMapping("/items/barrier")
	public String updateCountBarriers(HttpSession session) {
		((ShoppingCart) session.getAttribute("shoppingCart")).updateCountBarriers((ItemsCountBarrier) session.getAttribute("countBarrier"));
		return "success";
	}
	
	@PostMapping("/items/barrier/clear")
	public String clearBarriers(HttpSession session) {
		((Person) session.getAttribute("person")).setItemsCountBarrier(new ItemsCountBarrier());
		session.setAttribute("countBarrier", ((Person) session.getAttribute("person")).getItemsCountBarrier());
		return "success";
	}
	
	@DeleteMapping("/items") 
	public String deleteAllItems(HttpSession session) {
		((ShoppingCart) session.getAttribute("shoppingCart")).clear();   
		((ItemsStorage) session.getAttribute("storage")).clear();
		return "success";
	}
	
	@DeleteMapping("/whaleitem") 
	public String deleteWhaleItem(@RequestBody String itemName, HttpSession session) {
		((ShoppingCart) session.getAttribute("shoppingCart")).removeItem(itemName);
		((ItemsStorage) session.getAttribute("storage")).deleteItem(itemName);
		return "success";
	}
	
	@PostMapping("/state")  
	public String saveState(@RequestBody String moneyInfo, HttpSession session) {
		String[] money = moneyInfo.split(";");
		((Person) session.getAttribute("person")).setState(money[0]); 
		((Person) session.getAttribute("person")).setSpentMoney(money[1]);
		if(this.loginCookie != null) {
			DAOFactory.getPersonDAO().updatePerson(DAOFactory.getAccountDAO().getAccount(this.loginCookie.getValue()), (Person) session.getAttribute("person"));
		}
		return "success";
	}
	
	@PostMapping("/history")
	public String history(HttpSession session) {  
		((ItemsHistory) session.getAttribute("history")).addItems(((ShoppingCart) session.getAttribute("shoppingCart")).getItems());
		return "success";
	}
	
	@DeleteMapping("/history")
	public String clearHistroy(HttpSession session) {
		((ItemsHistory) session.getAttribute("history")).clear();       
		return "success";
	}
	
	@PutMapping("/person")
	public String changePerson(@RequestBody String fullName, HttpSession session) {
		session.setAttribute("person", ((People) session.getAttribute("people")).getPerson(fullName));
		Person person = (Person) session.getAttribute("person");
		session.setAttribute("countBarrier", person.getItemsCountBarrier());
		session.setAttribute("storage", person.getItemsStorage());
		session.setAttribute("shoppingCart", person.getShoppingCart());
		return "success";
	}
}
