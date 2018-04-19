create sequence hibernate_sequence start 1 increment 1
create table application_user (id int8 not null, admin boolean, email varchar(255), password varchar(255), username varchar(255), primary key (id))
create table game_character (id int8 not null, character_class varchar(255), character_name varchar(255), level int4, application_user_id int8 not null, primary key (id))
alter table if exists game_character add constraint FKqqs1v7b0t4y41f8a3iwb6nbxl foreign key (application_user_id) references application_user
create sequence hibernate_sequence start 1 increment 1
create table application_user (id int8 not null, admin boolean, email varchar(255), password varchar(255), username varchar(255), primary key (id))
create table game_character (id int8 not null, character_class varchar(255), character_name varchar(255), level int4, application_user_id int8 not null, primary key (id))
alter table if exists game_character add constraint FKqqs1v7b0t4y41f8a3iwb6nbxl foreign key (application_user_id) references application_user
create sequence hibernate_sequence start 1 increment 1
create table application_user (id int8 not null, admin boolean, email varchar(255), password varchar(255), username varchar(255), primary key (id))
create table game_character (id int8 not null, character_class varchar(255), character_name varchar(255), level int4, application_user_id int8 not null, primary key (id))
alter table if exists game_character add constraint FKqqs1v7b0t4y41f8a3iwb6nbxl foreign key (application_user_id) references application_user
create sequence hibernate_sequence start 1 increment 1
create table application_user (id int8 not null, admin boolean, email varchar(255), password varchar(255), username varchar(255), primary key (id))
create table game_character (id int8 not null, character_class varchar(255), character_name varchar(255), level int4, application_user_id int8 not null, primary key (id))
alter table if exists game_character add constraint FKqqs1v7b0t4y41f8a3iwb6nbxl foreign key (application_user_id) references application_user
create sequence hibernate_sequence start 1 increment 1
create table application_user (id int8 not null, admin boolean, email varchar(255), password varchar(255), username varchar(255), primary key (id))
create table game_character (id int8 not null, character_class varchar(255), character_name varchar(255), level int4, application_user_id int8 not null, primary key (id))
alter table if exists game_character add constraint FKqqs1v7b0t4y41f8a3iwb6nbxl foreign key (application_user_id) references application_user
