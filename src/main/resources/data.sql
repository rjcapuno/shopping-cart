INSERT INTO PRODUCTS (id, name, price, count_remaining) VALUES (1, 'pen', 7.50, 5);
INSERT INTO PRODUCTS (id, name, price, count_remaining) VALUES (2, 'paper', 20, 3);

INSERT INTO USERS (id, name, address, email, password)
VALUES (1, 'RJ', 'Laguna', 'ricjohncapuno@gmail.com', '$2y$12$ZB6hoUg6pjBjl/nhe5Ffserhy37BzHGZuzFO5YxDsj19pn2yx/cNi');
INSERT INTO USERS (id, name, address, email, password)
VALUES (2, 'Ric', 'Batangas', 'ric@gmail.com', '$2y$12$9W/7ra72HqmPM/S1MGY0xucr75ipxAXW87KgLCkkflrXKtPDtqqhy');

INSERT INTO CARTS (id, created_at, updated_at, user_id, total_price) VALUES (1, now(), now(), 1, 27.50);

INSERT INTO CART_PRODUCTS (cart_id, product_id) VALUES (1 ,1);
INSERT INTO CART_PRODUCTS (cart_id, product_id) VALUES (1 ,2);