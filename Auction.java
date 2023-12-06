import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.sql.Types;
import java.text. *;
import java.util. *;

public class Auction {
	private static Scanner scanner = new Scanner(System.in);
	private static String username;
	private static Connection conn;

	enum Category {
		ELECTRONICS, 
		BOOKS,
		HOME,
		CLOTHING,
		SPORTINGGOODS,
		OTHERS
	}
	enum Condition {
		NEW,
		LIKE_NEW,
		GOOD,
		ACCEPTABLE
	}
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private static boolean LoginMenu() {
		String userpass, isAdmin;

		System.out.print("----< User Login >\n" +
				" ** To go back, enter 'back' in user ID.\n" +
				"     user ID: ");
		try{
			username = scanner.next();
			scanner.nextLine();

			if(username.equalsIgnoreCase("back")){
				return false;
			}

			System.out.print("     password: ");
			userpass = scanner.next();
			scanner.nextLine();
		}catch (java.util.InputMismatchException e) {
			System.out.println("Error: Invalid input is entered. Try again.\n");
			username = null;
			return false;
		}

		/* Your code should come here to check ID and password */
		String sql = "SELECT * FROM users WHERE user_id = ? AND user_password = ?";
        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setString(1, username);
            pStmt.setString(2, userpass);

            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
					System.out.println("You are successfully logged in.\n");
					return true;
                } else {
					/* If Login Fails */
					username = null;
					System.out.println("Error: Incorrect user name or password\n");
					return false; 
                }
            }
        } catch (SQLException e) {
			username = null;
            System.out.println("SQLException: " + e);
            return false;
        }	
	}

	private static boolean SellMenu() {
		Category category = null;
		Condition condition = null;
		String description;
		LocalDateTime dateTime;
		char choice;
		int price;
		boolean flag_catg = true, flag_cond = true;

		do{
			System.out.println(
					"----< Sell Item >\n" +
					"---- Choose a category.\n" +
					"    1. Electronics\n" +
					"    2. Books\n" +
					"    3. Home\n" +
					"    4. Clothing\n" +
					"    5. Sporting Goods\n" +
					"    6. Other Categories\n" +
					"    P. Go Back to Previous Menu"
					);

			try {
				choice = scanner.next().charAt(0);;
			}catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.\n");
				continue;
			}

			flag_catg = true;

			switch ((int) choice){
				case '1':
					category = Category.ELECTRONICS;
					continue;
				case '2':
					category = Category.BOOKS;
					continue;
				case '3':
					category = Category.HOME;
					continue;
				case '4':
					category = Category.CLOTHING;
					continue;
				case '5':
					category = Category.SPORTINGGOODS;
					continue;
				case '6':
					category = Category.OTHERS;
					continue;
				case 'p':
				case 'P':
					return false;
				default:
					System.out.println("Error: Invalid input is entered. Try again.\n");
					flag_catg = false;
					continue;
			}
		}while(!flag_catg);

		do{
			System.out.println(
					"---- Select the condition of the item to sell.\n" +
					"   1. New\n" +
					"   2. Like-new\n" +
					"   3. Used (Good)\n" +
					"   4. Used (Acceptable)\n" +
					"   P. Go Back to Previous Menu"
					);

			try {
				choice = scanner.next().charAt(0);;
				scanner.nextLine();
			}catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.\n");
				continue;
			}

			flag_cond = true;

			switch (choice) {
				case '1':
					condition = Condition.NEW;
					break;
				case '2':
					condition = Condition.LIKE_NEW;
					break;
				case '3':
					condition = Condition.GOOD;
					break;
				case '4':
					condition = Condition.ACCEPTABLE;
					break;
				case 'p':
				case 'P':
					return false;
				default:
					System.out.println("Error: Invalid input is entered. Try again.\n");
					flag_cond = false;
					continue;
			}
		}while(!flag_cond);

		try {
			System.out.println("---- Description of the item (one line): ");
			description = scanner.nextLine();
			System.out.println("---- Buy-It-Now price: ");

			while (!scanner.hasNextInt()) {
				scanner.next();
				System.out.println("Invalid input is entered. Please enter Buy-It-Now price: ");
			}

			price = scanner.nextInt();
			scanner.nextLine();

			System.out.print("---- Bid closing date and time (YYYY-MM-DD HH:MM): ");
			// you may assume users always enter valid date/time
			String date = scanner.nextLine();  /* "2023-03-04 11:30"; */
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			dateTime = LocalDateTime.parse(date, formatter);
		}catch (Exception e) {
			System.out.println("Error: Invalid input is entered. Going back to the previous menu.\n");
			return false;
		}

		/* TODO: Your code should come here to store the user inputs in your database */
		String sql = "INSERT INTO items (category_id, description_, condition_id, seller_id, buy_it_now_price, bid_closing_date, date_posted) VALUES (?, ?, ?, ?, ?, ?, NOW())";

		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setInt(1, category.ordinal());
            pStmt.setString(2, description);
            pStmt.setInt(3, condition.ordinal());
            pStmt.setString(4, username);
            pStmt.setInt(5, price);
            pStmt.setTimestamp(6, Timestamp.valueOf(dateTime));

            pStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e);
            return false;
        }

		System.out.println("Your item has been successfully listed.\n");
		return true;
	}

	private static boolean SignupMenu() {
		/* 2. Sign Up */
		String new_username, userpass, isAdmin;
		System.out.print("----< Sign Up >\n" + 
				" ** To go back, enter 'back' in user ID.\n" +
				"---- user name: ");
		try {
			new_username = scanner.next();
			scanner.nextLine();
			if(new_username.equalsIgnoreCase("back")){
				return false;
			}
			System.out.print("---- password: ");
			userpass = scanner.next();
			scanner.nextLine();
			System.out.print("---- In this user an administrator? (Y/N): ");
			isAdmin = scanner.next();
			scanner.nextLine();
		} catch (java.util.InputMismatchException e) {
			System.out.println("Error: Invalid input is entered. Please select again.\n");
			return false;
		}

		/* TODO: Your code should come here to create a user account in your database */
		boolean is_admin;
		if (isAdmin.equalsIgnoreCase("y"))
			is_admin = true;
		else if (isAdmin.equalsIgnoreCase("n"))
			is_admin = false;
		else {
			System.out.println("Error: Invalid input is entered. Please enter again.\n");
			return false;
		}
		String sql = "INSERT INTO users (user_id, user_password, is_admin) VALUES (?, ?, ?)";
		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setString(1, new_username);
            pStmt.setString(2, userpass);
            pStmt.setBoolean(3, is_admin);

            int rowInserted = pStmt.executeUpdate();
            if (rowInserted > 0) {
                System.out.println("Your account has been successfully created.\n");
				return true;
            } else {
                System.out.println("Failed to create your account. Try again.\n");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e);
            return false;
        }		
	}

	private static boolean AdminMenu() {
		/* 3. Login as Administrator */
		char choice;
		String adminname, adminpass;
		String keyword, seller;
		System.out.print("----< Login as Administrator >\n" +
				" ** To go back, enter 'back' in user ID.\n" +
				"---- admin ID: ");

		try {
			adminname = scanner.next();
			scanner.nextLine();
			if(adminname.equalsIgnoreCase("back")){
				return false;
			}
			System.out.print("---- password: ");
			adminpass = scanner.nextLine();

			// TODO: check the admin's account and password.
			String sql = "SELECT * FROM users WHERE user_id = ? AND user_password = ? AND is_admin = TRUE";

			try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
				pStmt.setString(1, adminname);
				pStmt.setString(2, adminpass);

				try (ResultSet rs = pStmt.executeQuery()) {
					if (rs.next()) {
						// login successed.
					} else {
						// login failed. go back to the previous menu.
						System.out.println("Error: Incorrect admin ID or password.\n");
						return false;
					}
				}
			} catch (SQLException ee) {
				System.out.println("SQLException: " + ee);
				return false;
    		}

		} catch (java.util.InputMismatchException e) {
			System.out.println("Error: Invalid input is entered. Try again.\n");
			return false;
		}

		// boolean login_success = true;

		// if(!login_success){
		// 	// login failed. go back to the previous menu.
		// 	return false;
		// }

		do {
			System.out.println(
					"----< Admin menu > \n" +
					"    1. Print Sold Items per Category \n" +
					"    2. Print Account Balance for Seller \n" +
					"    3. Print Seller Ranking \n" +
					"    4. Print Buyer Ranking \n" +
					"    P. Go Back to Previous Menu"
					);

			try {
				choice = scanner.next().charAt(0);;
				scanner.nextLine();
			} catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.\n");
				continue;
			}

			if (choice == '1') {
				System.out.println( "< Select category > : \n" +
									"    Electronics, Books, Home, Clothing, Sporting Goods, Other Categories");
				System.out.print("----Enter Category to search : ");
				keyword = scanner.nextLine();

				/*TODO: Print Sold Items per Category */
				if(keyword.equalsIgnoreCase("Electronics")) {
					keyword = "Electronics";
				} else if(keyword.equalsIgnoreCase("Books")) {
					keyword = "Books";
				} else if(keyword.equalsIgnoreCase("Home")) {
					keyword = "Home";
				} else if(keyword.equalsIgnoreCase("Clothing")) {
					keyword = "Clothing";
				} else if(keyword.equalsIgnoreCase("Sporting Goods")) {
					keyword = "Sporting Goods";
				} else if(keyword.equalsIgnoreCase("Other Categories")) {
					keyword = "Other Categories";
				} else {
					System.out.println("Error: Invalid input is entered. Try again.\n");
					continue;
				}

				String sql = "SELECT i.description_, b.purchase_date, i.seller_id, b.buyer_id, b.amount_buyer " +
							"FROM billings b " +
							"INNER JOIN items i ON b.item_id = i.item_id " +
							"INNER JOIN categories c ON i.category_id = c.category_id " +
							"WHERE i.item_status = TRUE AND c.category_name = ? " +
							"ORDER BY b.purchase_date";

				try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
					pStmt.setString(1, keyword);

					try (ResultSet rset = pStmt.executeQuery()) {
						if(rset.next()) {
							System.out.println("sold item       | sold date       | seller ID   | buyer ID   | price | commissions");
							System.out.println("----------------------------------------------------------------------------------");
							do {
								String sold_item = rset.getString("description_");
								String sold_date = rset.getString("purchase_date");
								String seller_id = rset.getString("seller_id");
								String buyer_id = rset.getString("buyer_id");
								int price = rset.getInt("amount_buyer");
								int commission = price / 10;

								System.out.println(sold_item + " | " + sold_date + " | " + seller_id + " | " + buyer_id + " | " + price + " | " + commission);
							} while(rset.next());
						} else {
							System.out.println("No items found in the selected category.");
						}
						System.out.println();
					}
				} catch (SQLException e) {
					System.out.println("SQLException: " + e);
				}
				continue;
			} else if (choice == '2') {
				/*TODO: Print Account Balance for Seller */
				System.out.printf("---- Enter Seller ID to search : ");
				seller = scanner.next();
				scanner.nextLine();
				
				String sql = "SELECT i.description_, b.purchase_date, b.buyer_id, b.amount_buyer " +
							"FROM billings b " +
							"INNER JOIN items i ON b.item_id = i.item_id " +
							"WHERE i.item_status = TRUE AND i.seller_id = ? " +
							"ORDER BY b.purchase_date";

				try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
					pStmt.setString(1, seller);
					int seller_balance = 0;

					try (ResultSet rset = pStmt.executeQuery()) {
						if (rset.next()) {
							System.out.println("sold item       | sold date       | buyer ID   | price | commissions");
							System.out.println("--------------------------------------------------------------------");
							do {
								String sold_item = rset.getString("description_");
								String sold_date = rset.getString("purchase_date");
								String buyer_id = rset.getString("buyer_id");
								int price = rset.getInt("amount_buyer");
								int commission = price / 10;
								seller_balance += price - commission;

								System.out.println(sold_item + " | " + sold_date + " | " + buyer_id + " | " + price + " | " + commission);
							} while (rset.next());
							System.out.println("Seller Balance: " + seller_balance);
						} else {
							System.out.println("The entered user does not exist or there are no purchased items.");
						}
						System.out.println();
					}
				} catch (SQLException e) {
					System.out.println("SQLException: " + e);
				}

				continue;
			} else if (choice == '3') {
				/*TODO: Print Seller Ranking */
				System.out.println("seller ID   | # of items sold | Total Profit (excluding commissions)");
				System.out.println("--------------------------------------------------------------------");
				String sql = "SELECT i.seller_id, COUNT(b.item_id) AS num_items, SUM(b.amount_buyer) - SUM(b.amount_buyer / 10) AS total_profit " +
							"FROM billings b " +
							"INNER JOIN items i ON b.item_id = i.item_id " +
							"WHERE i.item_status = TRUE " +
							"GROUP BY i.seller_id " +
							"ORDER BY total_profit DESC";

				try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
					try(ResultSet rset = pStmt.executeQuery()) {					
						if (rset.next()) {
							do {
								String seller_id = rset.getString("seller_id");
								int num_items = rset.getInt("num_items");
								int total_profit = rset.getInt("total_profit");

								System.out.println(seller_id + " | " + num_items + " | " + total_profit);
							} while(rset.next());
						} else {
							System.out.println("No sellers found.");
						}
						System.out.println();
					}
				} catch (SQLException e) {
					System.out.println("SQLException: " + e);
				}

				continue;
			} else if (choice == '4') {
				/*TODO: Print Buyer Ranking */
				System.out.println("buyer ID   | # of items purchased | Total Money Spent ");
				System.out.println("------------------------------------------------------");
				
				String sql = "SELECT b.buyer_id, COUNT(b.item_id) AS num_items, SUM(b.amount_buyer) AS total_spent " +
                             "FROM billings b " +
							 "INNER JOIN items i ON b.item_id = i.item_id " +
                             "WHERE i.item_status = TRUE " +
                             "GROUP BY b.buyer_id " +
                             "ORDER BY total_spent DESC";

				try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
					try(ResultSet rset = pStmt.executeQuery()) {
						if (rset.next()) {
							do {
								String buyer_id = rset.getString("buyer_id");
								int num_items = rset.getInt("num_items");
								int total_spent = rset.getInt("total_spent");

								System.out.println(buyer_id + " | " + num_items + " | " + total_spent);
							} while (rset.next());
						} else {
							System.out.println("No buyers found.");
						}
						System.out.println();
					}
				} catch (SQLException e) {
					System.out.println("SQLException: " + e);
				}
				continue;
			} else if (choice == 'P' || choice == 'p') {
				return false;
			} else {
				System.out.println("Error: Invalid input is entered. Try again.");
				continue;
			}
		} while(true);
	}

	public static void CheckSellStatus(){
		/* TODO: Check the status of the item the current user is selling */
		System.out.println("item listed in Auction | bidder (buyer ID) | bidding price | bidding date/time");
		System.out.println("-------------------------------------------------------------------------------");
		
		String sql = "SELECT i.description_, b.bidder_id, b.bid_price, b.bid_date " +
					"FROM bids b " +
					"INNER JOIN items i ON b.item_id = i.item_id " +
					"WHERE i.seller_id = ? " +
					"ORDER BY b.bid_date";

		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setString(1, username);

			try (ResultSet rset = pStmt.executeQuery()) {
				if (rset.next()) {
					do {
						String item = rset.getString("description_");
						String bidder = rset.getString("bidder_id");
						int bidding_price = rset.getInt("bid_price");
						String bidding_date_time = rset.getString("bid_date");

						System.out.println(item + " | " + bidder + " | " + bidding_price + " | " + bidding_date_time);
					} while(rset.next());
				} else {
					System.out.println("No items listed in auction by the current user.");
				}
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e);
		}
	}

	public static boolean BuyItem(){
		Category category = null;
		Condition condition = null;
		char choice;
		int choice_item_id;
		int price;
		String keyword, seller, datePosted;
		boolean flag_catg = true, flag_cond = true;
		
		do {

			System.out.println( "----< Select category > : \n" +
					"    1. Electronics\n"+
					"    2. Books\n" + 
					"    3. Home\n" + 
					"    4. Clothing\n" + 
					"    5. Sporting Goods\n" +
					"    6. Other categories\n" +
					"    7. Any category\n" +
					"    P. Go Back to Previous Menu"
					);

			try {
				choice = scanner.next().charAt(0);;
				scanner.nextLine();
			} catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.");
				return false;
			}

			flag_catg = true;

			switch (choice) {
				case '1':
					category = Category.ELECTRONICS;
					break;
				case '2':
					category = Category.BOOKS;
					break;
				case '3':
					category = Category.HOME;
					break;
				case '4':
					category = Category.CLOTHING;
					break;
				case '5':
					category = Category.SPORTINGGOODS;
					break;
				case '6':
					category = Category.OTHERS;
					break;
				case '7':
					break;
				case 'p':
				case 'P':
					return false;
				default:
					System.out.println("Error: Invalid input is entered. Try again.");
					flag_catg = false;
					continue;
			}
		} while(!flag_catg);

		do {

			System.out.println(
					"----< Select the condition > \n" +
					"   1. New\n" +
					"   2. Like-new\n" +
					"   3. Used (Good)\n" +
					"   4. Used (Acceptable)\n" +
					"   P. Go Back to Previous Menu"
					);
			try {
				choice = scanner.next().charAt(0);;
				scanner.nextLine();
			} catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.");
				return false;
			}

			flag_cond = true;

			switch (choice) {
				case '1':
					condition = Condition.NEW;
					break;
				case '2':
					condition = Condition.LIKE_NEW;
					break;
				case '3':
					condition = Condition.GOOD;
					break;
				case '4':
					condition = Condition.ACCEPTABLE;
					break;
				case 'p':
				case 'P':
					return false;
				default:
					System.out.println("Error: Invalid input is entered. Try again.");
					flag_cond = false;
					continue;
				}
		} while(!flag_cond);

		try {
			System.out.println(" ** Enter 'any' if you want to see any items. ");
			System.out.printf("---- Enter keyword to search the description : ");
			keyword = scanner.next();
			scanner.nextLine();

			System.out.println(" ** Enter 'any' if you want to see items from any seller. ");
			System.out.printf("---- Enter Seller ID to search : ");
			seller = scanner.next();
			scanner.nextLine();

			System.out.println(" ** This will search items that have been posted after the designated date.");
			System.out.printf("---- Enter date posted (YYYY-MM-DD): ");
			datePosted = scanner.next();
			scanner.nextLine();
		} catch (java.util.InputMismatchException e) {
			System.out.println("Error: Invalid input is entered. Try again.");
			return false;
		}

		/* TODO: Query condition: item category */
		/* TODO: Query condition: item condition */
		/* TODO: Query condition: items whose description match the keyword (use LIKE operator) */
		/* TODO: Query condition: items from a particular seller */
		/* TODO: Query condition: posted date of item */
		String sql = "WITH max_bids AS " +
						"(SELECT item_id, MAX(bid_price) AS max_bid_price FROM bids GROUP BY item_id) " + 
					"SELECT i.item_id, i.description_, c.condition_name, i.seller_id, i.buy_it_now_price, b.bid_price, b.bidder_id, (i.bid_closing_date - NOW()) AS time_left, i.bid_closing_date " +
					"FROM items i " +
						"INNER JOIN conditions c ON i.condition_id = c.condition_id " +
						"LEFT JOIN max_bids mb ON i.item_id = mb.item_id " +
						"LEFT JOIN bids b ON i.item_id = b.item_id AND (mb.max_bid_price = b.bid_price OR mb.max_bid_price IS NULL) " +
					"WHERE " +
						"(i.category_id = ? OR ? IS NULL) AND " +
						"i.condition_id = ? AND i.item_status = FALSE AND " +
						"(LOWER(i.description_) LIKE LOWER(?) OR 'any' = ?) AND " +
						"(i.seller_id = ? OR 'any' = ?) AND " +
						"i.seller_id != ? AND " +
						"i.date_posted > TO_TIMESTAMP(?, 'YYYY-MM-DD') AND " +
						"i.bid_closing_date > NOW() " +
					"ORDER BY i.item_id ";
		ArrayList<Integer> item_id_list = new ArrayList<>();	
		
		/* TODO: List all items that match the query condition */
		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
			if (category != null) {
				pStmt.setInt(1, category.ordinal());
				pStmt.setInt(2, category.ordinal());
			} else {
				pStmt.setNull(1, Types.INTEGER);
				pStmt.setNull(2, Types.INTEGER);
			}
			pStmt.setInt(3, condition.ordinal());
			pStmt.setString(4, "%" + keyword + "%");
			pStmt.setString(5, keyword);
			pStmt.setString(6, seller);
			pStmt.setString(7, seller);
			pStmt.setString(8, username);
			pStmt.setString(9, datePosted);

			try (ResultSet rset = pStmt.executeQuery()) {
				if (rset.next()) {
					System.out.println("Item ID | Item description | Condition | Seller | Buy-It-Now | Current Bid | highest bidder | Time left | bid close");
					System.out.println("-------------------------------------------------------------------------------------------------------");
					do {
						int item_id = rset.getInt("item_id");
						item_id_list.add(item_id);
						String description = rset.getString("description_");
						String condition_name = rset.getString("condition_name");
						String seller_id = rset.getString("seller_id");
						int buy_it_now = rset.getInt("buy_it_now_price");
						int current_bid = rset.getInt("bid_price");
						String highest_bidder = rset.getString("bidder_id");
						String time_left = rset.getString("time_left");
						Timestamp bid_close = rset.getTimestamp("bid_closing_date");
						String bid_close_ = sdf.format(bid_close);

						System.out.println(item_id + " | " + description + " | " + condition_name + " | " + seller_id + " | " + buy_it_now + " | " + current_bid + " | " + highest_bidder + " | " + time_left + " | " + bid_close_);
					} while (rset.next());
				} else {
					System.out.println("There are no items matching the information you entered.\n");
					return false;
				}
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e);
			return false;
		}

		try {
			do {
				System.out.println("** To go back, enter '0' in Item ID.");
				System.out.printf("---- Select Item ID to buy or bid : ");
				choice_item_id = scanner.nextInt();
				scanner.nextLine();

				if(choice_item_id == 0) {
					System.out.println();
					return false;
				}

				else if (!item_id_list.contains(choice_item_id)) {
					System.out.println("Invalid Item ID. Please select a valid Item ID.");
					continue;
				}
				break;
			} while(true);

			System.out.printf("     Price: ");
			price = scanner.nextInt();
			scanner.nextLine();
		} catch (java.util.InputMismatchException e) {
			System.out.println("Error: Invalid input is entered. Try again.");
			return false;
		}

		/* TODO: Buy-it-now or bid: If the entered price is higher or equal to Buy-It-Now price, the bid ends. */
		/* Even if the bid price is higher than the Buy-It-Now price, the buyer pays the B-I-N price. */
		sql = "WITH max_bids AS " +
				"(SELECT item_id, MAX(bid_price) AS max_bid_price FROM bids GROUP BY item_id) " +
				"SELECT i.item_id, i.buy_it_now_price, b.bid_price, i.bid_closing_date " +
				"FROM items i " +
					"LEFT JOIN max_bids mb ON i.item_id = mb.item_id " +
					"LEFT JOIN bids b ON i.item_id = b.item_id AND (mb.max_bid_price = b.bid_price OR mb.max_bid_price IS NULL) " +
				"WHERE i.item_id = ?";

		try(PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setInt(1, choice_item_id);
			try (ResultSet rset = pStmt.executeQuery()) {
				if (rset.next()) {
					int item_id = rset.getInt("item_id");
					int buy_it_now = rset.getInt("buy_it_now_price");
					int current_bid = rset.getInt("bid_price");
            		Timestamp bid_close = rset.getTimestamp("bid_closing_date");
					Timestamp current_time = new Timestamp(System.currentTimeMillis());

					if (bid_close.before(current_time)) {
						System.out.println("Bid Ended.\n");
						return false;
					} 
					
					String insert_bid_sql = "INSERT INTO bids (item_id, bidder_id, bid_price, bid_date) VALUES (?, ?, ?, ?)";
					String insert_billing_sql = "INSERT INTO billings (item_id, buyer_id, purchase_date, amount_buyer) VALUES (?, ?, ?, ?)";
					String update_item_status_sql = "UPDATE items SET item_status = TRUE, bid_closing_date = ? WHERE item_id = ?";

					if (price >= buy_it_now) {
						price = buy_it_now;
					}

					if (price > current_bid) {
						// INSERT DATA IN BIDS TABLE 
						try (PreparedStatement bidStmt = conn.prepareStatement(insert_bid_sql)) {
							bidStmt.setInt(1, choice_item_id);
							bidStmt.setString(2, username);
							bidStmt.setInt(3, price);
							bidStmt.setTimestamp(4, current_time);

							bidStmt.executeUpdate();
						}

						/* TODO: if you won, print the following */
						if (price >= buy_it_now) {
							try(PreparedStatement billingStmt = conn.prepareStatement(insert_billing_sql);
							PreparedStatement itemStmt = conn.prepareStatement(update_item_status_sql)) {
								billingStmt.setInt(1, choice_item_id);
								billingStmt.setString(2, username);
								billingStmt.setTimestamp(3, current_time);
								billingStmt.setInt(4, price);
								billingStmt.executeUpdate();

								itemStmt.setTimestamp(1, current_time);
								itemStmt.setInt(2, choice_item_id);
								itemStmt.executeUpdate();
							}

							System.out.println("Congratulations, the item is yours now.\n");
						} else {
							/* TODO: if you are the current highest bidder, print the following */
							System.out.println("Congratulations, you are the highest bidder.\n");
						}
						return true;
					} else {
						System.out.println("Failed. You must offer a higher price than " + current_bid + "\n");
						return false;
					}
				} else {
					System.out.println("Error: Item not found.\n");
            		return false;
				}
			}
 		} catch (SQLException e) {
			System.out.println("SQLException: " + e);
			return false;
		}
	} 

	public static void CheckBuyStatus(){
		/* TODO: Check the status of the item the current buyer is bidding on */
		/* Even if you are outbidded or the bid closing date has passed, all the items this user has bidded on must be displayed */

		String sql = "WITH max_bids AS (" +
					"    SELECT item_id, MAX(bid_price) AS max_bid_price " +
					"    FROM bids " +
					"    GROUP BY item_id" +
					"), " +
					"max_bids_name AS (" +
					"    SELECT b.item_id, b.bid_price AS max_bid_price, b.bidder_id " +
					"    FROM max_bids mb " +
					"        INNER JOIN bids b ON mb.item_id = b.item_id AND mb.max_bid_price = b.bid_price" +
					"), " +
					"my_max_bid AS (" +
					"    SELECT item_id, MAX(bid_price) AS your_bid_price " +
					"    FROM bids " +
					"    WHERE bidder_id = ? " +
					"    GROUP BY item_id" +
					") " +
					"SELECT i.item_id, i.description_, mbn.bidder_id, mbn.max_bid_price, mmb.your_bid_price, i.bid_closing_date, i.item_status " +
					"FROM my_max_bid mmb " +
					"    INNER JOIN items i ON mmb.item_id = i.item_id " +
					"    LEFT JOIN max_bids_name mbn ON mmb.item_id = mbn.item_id " +
					"ORDER BY item_id";

		try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
			pStmt.setString(1, username);
			try (ResultSet rset = pStmt.executeQuery()) {
				if (rset.next()) {
					System.out.println("item ID   | item description   | highest bidder | highest bidding price | your bidding price | bid closing date/time");
					System.out.println("--------------------------------------------------------------------------------------------------------------------");
					do {
						int item_id = rset.getInt("item_id");
						String description = rset.getString("description_");
						String bidder_id = rset.getString("bidder_id");
						int max_bid_price = rset.getInt("max_bid_price");
						int your_bid_price = rset.getInt("your_bid_price");
						Timestamp bid_close = rset.getTimestamp("bid_closing_date");
						String bid_close_ = sdf.format(bid_close);
						Timestamp current_time = new Timestamp(System.currentTimeMillis());
						
						boolean item_status = rset.getBoolean("item_status");

						if (bid_close.before(current_time)) {
							System.out.println(item_id + " | " + description + " | " + bidder_id + " | " + max_bid_price +" | " + your_bid_price + " | Bid Ended.");
							if(!item_status) {
								// Update tables
								String insert_billing_sql = "INSERT INTO billings (item_id, buyer_id, purchase_date, amount_buyer) VALUES (?, ?, ?, ?)";
								String update_item_status_sql = "UPDATE items SET item_status = TRUE, bid_closing_date = ? WHERE item_id = ?";
								
								try(PreparedStatement billingStmt = conn.prepareStatement(insert_billing_sql);
								PreparedStatement itemStmt = conn.prepareStatement(update_item_status_sql)) {
									billingStmt.setInt(1, item_id);
									billingStmt.setString(2, username);
									billingStmt.setTimestamp(3, bid_close);
									billingStmt.setInt(4, your_bid_price);
									billingStmt.executeUpdate();
									
									itemStmt.setTimestamp(1, current_time);
									itemStmt.setInt(2, item_id);
									itemStmt.executeUpdate();
								}
							}
						} else {
							System.out.println(item_id + " | " + description + " | " + bidder_id + " | " + max_bid_price +" | " + your_bid_price + " | " + bid_close_);
						}
					} while (rset.next());
				} else {
					System.out.println("There are currently no items being bid on.");
					return;
				}
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		System.out.println("");
	}

	public static void CheckAccount(){
		/* TODO: Check the balance of the current user.  */		
		String soldSql = "SELECT c.category_name, i.item_id, b.purchase_date, b.amount_buyer, b.buyer_id, (b.amount_buyer / 10) AS commission " +
						"FROM items i " +
						"INNER JOIN categories c ON i.category_id = c.category_id " +
						"INNER JOIN billings b ON i.item_id = b.item_id " +
						"WHERE i.seller_id = ?";
		int receive_price = 0;
						
		System.out.println("[Sold Items]");
		try (PreparedStatement pStmt = conn.prepareStatement(soldSql)) {
			pStmt.setString(1, username);
			try (ResultSet rset = pStmt.executeQuery()) {
				if (rset.next()) {
					System.out.println("item category  | item ID   | sold date | sold price  | buyer ID | commission  ");
					System.out.println("------------------------------------------------------------------------------");
					do {
						String category = rset.getString("category_name");
						int item_id = rset.getInt("item_id");
						Timestamp sold_date = rset.getTimestamp("purchase_date");
						String sold_date_ = sdf.format(sold_date);
						int sold_price = rset.getInt("amount_buyer");
						String buyer_id = rset.getString("buyer_id");
						int commission = rset.getInt("commission");
						receive_price += sold_price - commission;

						System.out.println(category + " | " + item_id + " | " + sold_date_ + " | " + sold_price + " | " + buyer_id + " | " + commission);
					} while (rset.next());
					System.out.println("\nAmount money the company owes to the user : " + receive_price);

				} else {
					System.out.println("There are no items sold.");
				}
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}


		String purchasedSql = "SELECT c.category_name, i.item_id, b.purchase_date, b.amount_buyer, i.seller_id " +
            "FROM items i " +
            "INNER JOIN categories c ON i.category_id = c.category_id " +
            "INNER JOIN billings b ON i.item_id = b.item_id " +
            "WHERE b.buyer_id = ?";
		int offer_price = 0; 

		System.out.println("\n[Purchased Items]");
		try (PreparedStatement pStmt = conn.prepareStatement(purchasedSql)) {
			pStmt.setString(1, username);
			try (ResultSet rset = pStmt.executeQuery()) {
				if (rset.next()) {
					System.out.println("item category  | item ID   | purchased date | purchased price  | seller ID ");
					System.out.println("--------------------------------------------------------------------------");
					do {
						String category = rset.getString("category_name");
						int item_id = rset.getInt("item_id");
						Timestamp purchased_date = rset.getTimestamp("purchase_date");
						String purchased_date_ = sdf.format(purchased_date);
						int purchased_price = rset.getInt("amount_buyer");
						String seller_id = rset.getString("seller_id");
						offer_price += purchased_price;

						System.out.println(category + " | " + item_id + " | " + purchased_date_ + " | " + purchased_price + " | " + seller_id);
					} while (rset.next());

					System.out.println("\nAmount money the user owes to the company : " + offer_price);
				} else {
					System.out.println("There are no purchased items.");
				}
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}

		System.out.println("");
	}

	public static void main(String[] args) {
		char choice;
		boolean ret;

		if(args.length<2){
			System.out.println("Usage: java Auction postgres_id password");
			System.exit(1);
		}

		try{
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/"+args[0], args[0], args[1]); 
            // Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/db19313611", "db19313611", "wlgns123@"); 
		}
		catch(SQLException e){
			System.out.println("SQLException : " + e);	
			System.exit(1);
		}

		do {
			username = null;
			System.out.println(
					"----< Login menu >\n" + 
					"----(1) Login\n" +
					"----(2) Sign up\n" +
					"----(3) Login as Administrator\n" +
					"----(Q) Quit"
					);

			try {
				choice = scanner.next().charAt(0);;
				scanner.nextLine();
			} catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.");
				continue;
			}

			try {
				switch ((int) choice) {
					case '1':
						ret = LoginMenu();
						if(!ret) continue;
						break;
					case '2':
						ret = SignupMenu();
						if(!ret) continue;
						break;
					case '3':
						ret = AdminMenu();
						if(!ret) continue;
					case 'q':
					case 'Q':
						System.out.println("Good Bye");
						/* TODO: close the connection and clean up everything here */
						conn.close();
						System.exit(0);
					default:
						System.out.println("Error: Invalid input is entered. Try again.");
				}
			} catch (SQLException e) {
				System.out.println("SQLException : " + e);	
			}
		} while (username==null || username.equalsIgnoreCase("back"));  

		// logged in as a normal user 
		do {
			System.out.println(
					"---< Main menu > :\n" +
					"----(1) Sell Item\n" +
					"----(2) Status of Your Item Listed on Auction\n" +
					"----(3) Buy Item\n" +
					"----(4) Check Status of your Bid \n" +
					"----(5) Check your Account \n" +
					"----(Q) Quit"
					);

			try {
				choice = scanner.next().charAt(0);;
				scanner.nextLine();
			} catch (java.util.InputMismatchException e) {
				System.out.println("Error: Invalid input is entered. Try again.");
				continue;
			}

			try{
				switch (choice) {
					case '1':
						ret = SellMenu();
						if(!ret) continue;
						break;
					case '2':
						CheckSellStatus();
						break;
					case '3':
						ret = BuyItem();
						if(!ret) continue;
						break;
					case '4':
						CheckBuyStatus();
						break;
					case '5':
						CheckAccount();
						break;
					case 'q':
					case 'Q':
						System.out.println("Good Bye");
						/* TODO: close the connection and clean up everything here */
						conn.close();
						System.exit(0);
				}
			} catch (SQLException e) {
				System.out.println("SQLException : " + e);	
				System.exit(1);
			}
		} while(true);
	} // End of main 
} // End of class