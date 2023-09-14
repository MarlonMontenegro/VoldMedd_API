CREATE TABLE pacientes
(

    id           bigint       not null auto_increment,
    nombre       varchar(100) not null,
    email        varchar(100) not null unique,
    numero       varchar(20),
    documento    varchar(6)   not null,
    calle        varchar(100) not null,
    complemento  varchar(100),
    ciudad       varchar(100) not null,

    primary key (id)
);