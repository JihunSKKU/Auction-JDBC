drop table users CASCADE;
drop table categories CASCADE;
drop table conditions CASCADE;
drop table items CASCADE;
drop table bids CASCADE;
drop table billings CASCADE;

CREATE TABLE users (
    user_id             VARCHAR(255) PRIMARY KEY,
    user_password       VARCHAR(255) NOT NULL,
    is_admin            BOOLEAN DEFAULT FALSE
);

-- Electronics, Books, Home, Clothing, Sporting Goods, Other Categories
CREATE TABLE categories (
    category_id         SERIAL PRIMARY KEY,
    category_name       VARCHAR(255) NOT NULL
);

-- New, Like-new, Used (Good), Used (Acceptable)
CREATE TABLE conditions (
    condition_id        SERIAL PRIMARY KEY,
    condition_name      VARCHAR(255) NOT NULL
);

CREATE TABLE items (
    item_id             SERIAL PRIMARY KEY,
    category_id         INT REFERENCES categories(category_id) ON DELETE CASCADE,           
    description_        VARCHAR(255) NOT NULL,
    condition_id        INT REFERENCES conditions(condition_id),       
    seller_id           VARCHAR(255) REFERENCES users(user_id) ON DELETE CASCADE,
    buy_it_now_price    INT CHECK(buy_it_now_price > 0),
    bid_closing_date    TIMESTAMP NOT NULL,
    date_posted         TIMESTAMP NOT NULL,
    item_status         BOOLEAN DEFAULT FALSE      -- bid ends or not
);

CREATE TABLE bids (
    item_id             INT REFERENCES items(item_id) ON DELETE CASCADE,
    bidder_id           VARCHAR(255) REFERENCES users(user_id) ON DELETE CASCADE,
    bid_price           INT CHECK(bid_price > 0),
    bid_date            TIMESTAMP NOT NULL,
    PRIMARY KEY (item_id, bid_price)
);

CREATE TABLE billings (
    item_id             INT PRIMARY KEY REFERENCES items(item_id) ON DELETE CASCADE,
    -- seller_id           VARCHAR(255) REFERENCES users(user_id) ON DELETE CASCADE,
    buyer_id            VARCHAR(255) REFERENCES users(user_id) ON DELETE CASCADE,
    purchase_date       TIMESTAMP NOT NULL,
    amount_buyer        INT NOT NULL   -- amount due buyers need to pay
    -- amount_seller       INT NOT NULL    -- amount of money sellers need to get paid
);