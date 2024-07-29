package model;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

public class TopCreaterForLeaderBoard {
	public static void genTop(List<LeaderBoard> top, HttpSession session, String login) {
		List<LeaderBoard> genTop = new ArrayList<>();
		LeaderBoard currentPlayer = null;
		Long topNumber = 0L;
		for(LeaderBoard leaderBoard : top) {
			topNumber++;
			
			leaderBoard.setParsedTotalSpentMoney(PriceParser.parseToPrice(leaderBoard.getTotalSpentMoney()));
			if(topNumber == 1) {
				leaderBoard.setTopNumber(topNumber);
				session.setAttribute("topOne", leaderBoard);
			}
			
			if(topNumber > 1 && topNumber <= 30L) {
				leaderBoard.setTopNumber(topNumber);
				genTop.add(leaderBoard);
			}
			
			if(leaderBoard.getAccount().getLogin().equals(login)) {
				leaderBoard.setTopNumber(topNumber);
				currentPlayer = leaderBoard;
			}
			
			if(topNumber == 30L && !((String) session.getAttribute("status")).equalsIgnoreCase("SignIn") && currentPlayer == null) {
				break;
			}
		}
		session.setAttribute("top", genTop);
		if(currentPlayer != null) {
			session.setAttribute("currentPlayerTopNumber", currentPlayer.getTopNumberByPattern());
			session.setAttribute("currentPlayerLogin", login);
			session.setAttribute("currentPlayerTotalSpentMoney", currentPlayer.getParsedTotalSpentMoney());
		} else {
			session.setAttribute("currentPlayerTopNumber", "00.");
			session.setAttribute("currentPlayerLogin", "You Need To Sign In");
			session.setAttribute("currentPlayerTotalSpentMoney", "0$");
		}
	}
}
