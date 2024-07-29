package dao;

public class DAOFactory {
	public static AccountDAO getAccountDAO() {
		return new AccountDAO();
	}
	
	public static PersonDAO getPersonDAO() {
		return new PersonDAO();
	}
	
	public static LeaderBoardDAO getLeaderBoardDAO() {
		return new LeaderBoardDAO();
	}
}
