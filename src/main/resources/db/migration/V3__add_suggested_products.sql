create table suggested_product
(
    email_id int not null,
    product_name varchar(32) not null,
    constraint suggested_product_pk_email_id_product_name primary key (email_id, product_name),
    constraint suggested_product_email_id_fk foreign key (email_id) references email (id)
);