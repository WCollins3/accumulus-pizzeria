create table email
(
    id int not null,
    email varchar(64) NOT NULL,
    constraint email_pk__id primary key (id),
    constraint email_pk__email unique (email)
);

create table "topping"
(
    email_id int not null,
    topping varchar(32) not null,
    constraint topping_pk_email_id_topping primary key (email_id, topping),
    constraint topping_email_id_fk foreign key (email_id) references email (id)
);