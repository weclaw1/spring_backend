create table application_user (
    id bigserial PRIMARY KEY, 
    admin boolean, 
    email varchar(255), 
    password varchar(255), 
    username varchar(255)
);

create table game_character (
    id bigserial PRIMARY KEY, 
    character_class varchar(255), 
    character_name varchar(255), 
    level int4, 
    application_user_id int8 not null references application_user(id)
);
