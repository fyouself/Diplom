create table ROLES
(
    id   int not null primary key,
    role varchar(255) not null
);

create table USERS
(
    id       int generated always as identity primary key,
    login    varchar(50)  not null,
    password varchar(255) not null,
    role_id int not null REFERENCES ROLES (id)
);


create table FILES
(
    id       int generated always as identity primary key,
    name     varchar(255) not null,
    files_id int  not null references USERS (id)
);

insert into ROLES(id, role)
values (1, 'admin'),
       (2, 'user');

insert into USERS(login, password, role_id)
values ('user1', '$2a$12$GFCqXPF5bL7yCkvNt90aNeBhG1C0zUpw3pXMJjuPjVB0LzwpkmhrK', 2),
       ('user2', '$2a$12$3BhQ3WGtynP5g6e5bOC9guiSssKBFw9GL7UbL3m5zD6OmHJ.8D3qW', 2),
       ('user3', '$2a$12$3BhQ3WGtynP5g6e5bOC9guiSssKBFw9GL7UbL3m5zD6OmHJ.8D3qW', 2);

