package db;

import scrapper.Info;

public class DBTester {

	public static void main(String[] args) {
		LocalDBHandler dbHandler = new LocalDBHandler();
		Info info = new Info("https://www.linkedin.com/sales/profile/AAEAABesfSMBkXdMJ8Ddo2No8aZf4TsDVf9CZC8,067m,NAME_SEARCH?moduleKey=peopleSearchResults",
				"Farid", "Miah", "", "", "New York","public health", "CEO", "BEO", "10-50");
		dbHandler.insert(info);
//		dbHandler.insert("https://www.linkedin.com/sales/profile/AAEAABesfSMBkXdMJ8Ddo2No8aZf4TsDVf9CZC8,067m,NAME_SEARCH?moduleKey=peopleSearchResults", "Farid", "Miah", "Upwork");
//		dbHandler.insert("https://www.linkedin.com/sales/profile/AAEAACSd2CQBq5uBq8l4xk0hr7S_ibyF1Elb2AA,_GhY,NAME_SEARCH?moduleKey=peopleSearchResults", "Md", "Monir Khondoker", "Upwork");
//		dbHandler.insert("https://www.linkedin.com/sales/profile/AAEAACU8wMABUeAxd3qJhRbq1IU5_nD9ivq92vQ,_V-D,NAME_SEARCH?moduleKey=peopleSearchResults", "Md", "RASEL", "Upwork");
//		dbHandler.insert("https://www.linkedin.com/sales/profile/AAEAAABmEMABPIfO4QbP0NB4HE0TpuLFhWqc4TE,s6ma,NAME_SEARCH?moduleKey=peopleSearchResults", "Gina", "Melani Caulkins", "Upwork");
		dbHandler.selectAll();
		dbHandler.closeConnection();
	}

}
