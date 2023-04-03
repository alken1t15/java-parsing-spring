CREATE TABLE product(
    id SERIAL,
    store_name VARCHAR(255),
    category VARCHAR(255),
    name VARCHAR(255),
    link_product VARCHAR(255),
    price INT4,
    date TIMESTAMP
);
CREATE TABLE shop(
    id SERIAL,
    store_name VARCHAR(255),
    category VARCHAR(255),
    link_page VARCHAR(255)
);

INSERT INTO shop (store_name, category, link_page) VALUES ('Белый ветер','Ноутбуки','https://shop.kz/noutbuki/filter/almaty-is-v_nalichii-or-ojidaem-or-dostavim/apply/');
INSERT INTO shop (store_name, category, link_page) VALUES ('Белый ветер','Смартфоны','https://shop.kz/smartfony/filter/almaty-is-v_nalichii-or-ojidaem-or-dostavim/apply/');
INSERT INTO shop (store_name, category, link_page) VALUES ('Белый ветер','Смарт-часы','https://shop.kz/smart-chasy/filter/almaty-is-v_nalichii-or-ojidaem-or-dostavim/apply/');
INSERT INTO shop (store_name, category, link_page) VALUES ('Белый ветер','Планшеты','https://shop.kz/planshety/filter/almaty-is-v_nalichii-or-ojidaem-or-dostavim/apply/');
INSERT INTO shop (store_name, category, link_page) VALUES ('Белый ветер','Телевизоры','https://shop.kz/televizory/filter/almaty-is-v_nalichii-or-ojidaem-or-dostavim/apply/');