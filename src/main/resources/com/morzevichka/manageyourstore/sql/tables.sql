CREATE TABLE workers (
    id NUMBER GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR2(100) NOT NULL,
    second_name VARCHAR2(100) NOT NULL,
    username VARCHAR2(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR2(255) CHECK( ROLE IN ('EMPLOYEE', 'ADMIN')) NOT NULL,
    salary NUMBER(10, 2) CHECK ( salary >= 0 ),
    register_date TIMESTAMP NOT NULL,
    last_login TIMESTAMP,
    profile_picture BLOB,
    CONSTRAINT workers_pk PRIMARY KEY (id)
); -- done

CREATE TABLE categories (
    id NUMBER GENERATED ALWAYS AS IDENTITY,
    name VARCHAR2(100) NOT NULL UNIQUE,
    CONSTRAINT categories_pk PRIMARY KEY (id)
); -- done

CREATE TABLE products (
    article NUMBER GENERATED ALWAYS AS IDENTITY,
    name VARCHAR2(100) NOT NULL UNIQUE,
    barcode VARCHAR2(13) UNIQUE NOT NULL,
    category_id NUMBER,
    cost NUMBER NOT NULL,
    last_edit_date TIMESTAMP,
    picture BLOB,
    CONSTRAINT products_pk PRIMARY KEY (article),
    CONSTRAINT fk_products__categories FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
); -- done

CREATE TABLE cards (
    id NUMBER GENERATED ALWAYS AS IDENTITY,
    name VARCHAR2(50) NOT NULL UNIQUE,
    cashback NUMBER(10, 2),
    CONSTRAINT cards_pk PRIMARY KEY (id)
); -- done

CREATE TABLE clients (
    id NUMBER GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR2(100) NOT NULL,
    second_name VARCHAR2(100),
    phone_number VARCHAR2(20) UNIQUE,
    email VARCHAR2(100) UNIQUE,
    card_id NUMBER,
    bonus NUMBER,
    status VARCHAR(20) CHECK ( status IN ('ACTIVE', 'BANNED') ),
    CONSTRAINT clients_pk PRIMARY KEY (id),
    CONSTRAINT fk_clients__cards FOREIGN KEY (card_id) REFERENCES cards(id)
); -- done

CREATE TABLE orders
(
    id            NUMBER GENERATED ALWAYS AS IDENTITY,
    order_sum     NUMBER(10, 2) NOT NULL,
    worker_id     NUMBER NOT NULL,
    client_id     NUMBER,
    purchase_time TIMESTAMP NOT NULL,
    CONSTRAINT orders_pk PRIMARY KEY (id),
    CONSTRAINT fk_orders_employee__workers FOREIGN KEY (worker_id) REFERENCES workers (id),
    CONSTRAINT fk_orders_client__clients FOREIGN KEY (client_id) REFERENCES clients (id)
); -- done

CREATE TABLE order_items (
    id NUMBER GENERATED ALWAYS AS IDENTITY,
    order_id NUMBER NOT NULL,
    product_article NUMBER NOT NULL,
    quantity NUMBER NOT NULL,
    CONSTRAINT order_items_pk PRIMARY KEY (id),
    CONSTRAINT fk_order_items_order__orders FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_items_product__products FOREIGN KEY (product_article) REFERENCES products(article)
); -- done