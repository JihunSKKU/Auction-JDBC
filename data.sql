INSERT INTO users (user_id, user_password, is_admin) VALUES
('db19313611', 'wlgns123@', TRUE),
('user1', 'pw1', FALSE),
('user2', 'pw2', FALSE),
('user3', 'pw3', FALSE);

INSERT INTO categories (category_id, category_name) VALUES
(0, 'Electronics'),
(1, 'Books'),
(2, 'Home'),
(3, 'Clothing'),
(4, 'Sporting Goods'),
(5, 'Other Categories');

INSERT INTO conditions (condition_id, condition_name) VALUES
(0, 'New'),
(1, 'Like-new'),
(2, 'Used (Good)'),
(3, 'Used (Acceptable)');

INSERT INTO items (category_id, description_, condition_id, seller_id, buy_it_now_price, date_posted, bid_closing_date, item_status) VALUES
(0, 'Galaxy S21', 0, 'user1', 300000, '2023-10-27 12:00:00', '2023-11-03 12:00:00', FALSE),
(0, 'MacBook Pro', 1, 'user3', 1200000, '2023-10-28 00:00:00', '2023-11-02 13:10:00', TRUE),
(0, 'Galaxy Z Fold3', 2, 'db19313611', 500000, '2023-10-28 18:00:00', '2023-10-31 15:30:00', TRUE),
(0, 'iPad Air5', 1, 'user3', 400000, '2023-10-29 21:00:00', '2023-11-05 21:00:00', FALSE),
(0, 'Nintendo switch', 3, 'user2', 1000000, '2023-10-25 18:00:00', '2023-11-01 12:00:00', TRUE),
(1, 'Elementary Number Theory by Kenneth H. Rosen', 1, 'user1', 20000, '2023-10-28 00:00:00', '2023-11-03 00:00:00', FALSE),
(1, 'Principles of Mathematical Analysis by Walter Rudin', 3, 'user2', 10000, '2023-10-28 00:00:00', '2023-11-01 16:10:00', TRUE),
(1, 'Atomic Habits by James Clear', 1, 'user3', 15000, '2023-10-30 00:00:00', '2023-11-05 00:00:00', FALSE),
(1, 'Abstract Algebra by David S. Dummit and Richard M. Foote', 1, 'db19313611', 30000, '2023-10-25 12:00:00', '2023-11-06 00:00:00', FALSE),
(1, 'Funny book by Jihun', 0, 'db19313611', 50000, '2023-10-27 12:00:00', '2023-11-08 12:00:00', FALSE),
(2, 'Mirror', 1, 'user3', 30000, '2023-10-26 12:00:00', '2023-11-03 12:00:00', FALSE),
(2, 'Lighting Fixture', 2, 'user1', 15000, '2023-10-25 12:00:00', '2023-11-04 00:00:00', FALSE),
(2, 'Towel', 0, 'user2', 3000, '2023-10-28 18:00:00', '2023-11-06 00:00:00', FALSE),
(2, 'Desk', 1, 'db19313611', 50000, '2023-10-27 18:00:00', '2023-11-05 00:00:00', FALSE),
(2, 'Chair', 2, 'user1', 30000, '2023-10-29 12:00:00', '2023-11-08 12:00:00', FALSE),
(3, 'Black T-Shirts', 0, 'user1', 30000, '2023-10-27 15:00:00', '2023-11-01 12:00:00', TRUE),
(3, 'Nike Sneakers', 2, 'user1', 100000, '2023-10-27 18:00:00', '2023-11-03 18:00:00', FALSE),
(3, 'White Shirts', 1, 'user2', 20000, '2023-10-28 12:00:00', '2023-11-04 12:00:00', FALSE),
(3, 'Red Dress', 2, 'user3', 50000, '2023-10-15 18:00:00', '2023-10-30 13:20:00', TRUE),
(3, 'Dark Jean', 1, 'user2', 30000, '2023-10-18 12:00:00', '2023-11-06 00:00:00', FALSE),
(4, 'Soccer Ball', 3, 'user3', 10000, '2023-10-27 18:00:00', '2023-11-03 19:00:00', FALSE),
(4, 'Baseball Glove', 2, 'user2', 25000, '2023-10-28 18:00:00', '2023-11-04 18:00:00', FALSE),
(4, 'Basketball', 3, 'user3', 10000, '2023-10-27 18:00:00', '2023-11-05 18:00:00', FALSE),
(4, '5kg Dumbbells', 2, 'user1', 15000, '2023-10-22 12:00:00', '2023-10-31 18:00:00', TRUE),
(4, 'Tennis racket', 1, 'db19313611', 40000, '2023-10-23 18:00:00', '2023-11-10 00:00:00', FALSE),
(5, 'Pencil', 0, 'user1', 500, '2023-10-19 21:00:00', '2023-11-03 21:00:00', FALSE),
(5, 'Grand Piano', 2, 'user2', 3000000, '2023-10-20 18:00:00', '2023-11-04 18:00:00', FALSE),
(5, 'Pen', 1, 'user1', 3000, '2023-10-21 18:00:00', '2023-11-05 18:00:00', FALSE),
(5, 'Violin', 2, 'user2', 500000, '2023-10-28 17:00:00', '2023-11-07 00:00:00', FALSE),
(5, 'TNT', 0, 'db19313611', 10000000, '2023-10-24 18:00:00', '2023-11-15 18:00:00', FALSE),

(0, 'Test Phone 1', 1, 'user1', 300000, '2023-11-02 12:00:00', '2023-11-10 12:00:00', FALSE),
(0, 'Test Phone 2', 1, 'user2', 300000, '2023-11-02 12:00:00', '2023-11-15 12:00:00', FALSE),
(0, 'Test Phone 3', 1, 'user3', 300000, '2023-11-02 21:00:00', '2023-11-18 21:00:00', FALSE),

(0, 'Test Phone 4', 1, 'user1', 200000, '2023-10-01 18:00:00', '2023-10-20 21:00:00', TRUE),
(0, 'Test Phone 5', 1, 'user2', 200000, '2023-10-11 18:00:00', '2023-10-30 21:00:00', TRUE),
(0, 'Test Phone 6', 1, 'user3', 200000, '2023-10-21 18:00:00', '2023-11-10 21:00:00', FALSE);

INSERT INTO bids (item_id, bidder_id, bid_price, bid_date) VALUES
(2, 'user1', 300000, '2023-11-01 13:15:00'),
(2, 'user2', 500000, '2023-11-02 12:00:00'),
(2, 'db19313611', 1200000, '2023-11-02 13:10:00'),
(3, 'user2', 400000, '2023-10-31 15:30:00'),
(3, 'user3', 450000, '2023-11-01 11:45:00'),
(3, 'user2', 500000, '2023-11-01 12:00:00'),
(5, 'db19313611', 1000000, '2023-11-01 12:00:00'),
(7, 'user3', 8000, '2023-11-01 14:20:00'),
(7, 'user1', 10000, '2023-11-01 16:10:00'),
(10, 'user2', 30000, '2023-11-01 17:45:00'),
(10, 'user1', 45000, '2023-11-02 10:30:00'),
(11, 'user1', 5000, '2023-11-02 10:30:00'),
(16, 'user3', 25000, '2023-11-01 10:00:00'),
(16, 'user2', 30000, '2023-11-01 12:00:00'),
(18, 'db19313611', 15000, '2023-11-02 21:30:00'),
(19, 'user1', 50000, '2023-10-30 13:20:00'),
(23, 'user1', 8000, '2023-11-01 23:45:00'),
(23, 'user2', 9000, '2023-11-02 09:10:00'),
(24, 'user3', 15000, '2023-10-31 18:00:00'),
(27, 'user1', 1000000, '2023-10-30 18:20:00'),
(27, 'user3', 1500000, '2023-11-02 12:30:00'),
(30, 'user1', 10000, '2023-11-01 01:00:00');

INSERT INTO billings (item_id, buyer_id, purchase_date, amount_buyer) VALUES
(2, 'db19313611', '2023-11-02 13:10:00', 1200000),
(3, 'user2', '2023-10-31 15:30:00', 500000),
(5, 'db19313611', '2023-11-01 12:00:00', 1000000),
(7, 'user1', '2023-11-01 16:10:00', 10000),
(16, 'user2', '2023-11-01 12:00:00', 30000),
(19, 'user1', '2023-10-30 13:20:00', 50000),
(24, 'user3', '2023-10-31 18:00:00', 15000);