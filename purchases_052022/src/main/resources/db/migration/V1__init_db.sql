create table buyers
(
    id bigserial not null primary key,
    firstname varchar(255) not null,
    lastname varchar(255) not null
);

create table products
(
    id bigserial not null primary key,
    name varchar(255) not null,
    price numeric(7,2) not null
);

create table purchases
(
    id bigserial not null primary key,
    buyer_id bigint not null references buyers (id) on delete cascade,
    product_id bigint not null references products (id) on delete cascade,
    buy_date date not null
);

create index on buyers (lastname);
create index on products (name);
create index on purchases (id, buyer_id, product_id);
