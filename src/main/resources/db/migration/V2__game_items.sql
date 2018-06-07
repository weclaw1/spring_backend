create table game_item (
    id bigserial PRIMARY KEY, 
    item_name varchar(255), 
    hit_points int4,
    mana int4,
    strength int4, 
    defense int4
);

create table game_character_game_item (
    game_character_id int8 not null references game_character(id),
    game_item_id int8 not null references game_item(id)
);