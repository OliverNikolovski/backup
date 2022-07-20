CREATE TABLE public.users
(
    id                         bigserial primary key,
    username                   varchar(50) unique not null,
    password                   varchar(500)       not null,
    email                      varchar(100) unique,
    short_bio                  text,
    profile_picture            bytea,
    role                       varchar(50)        not null,
    is_account_non_expired     boolean default true,
    is_account_non_locked      boolean default true,
    is_credentials_non_expired boolean default true,
    is_enabled                 boolean default true
);


CREATE TABLE public.blog
(
    id                  bigserial primary key    not null,
    title               varchar(100)             not null,
    content             text                     not null,
    date_created        timestamp with time zone not null,
    number_of_likes     integer default 0,
    estimated_read_time integer                  not null,
    picture             bytea,
    user_id             bigserial                not null,
    foreign key (user_id) references users (id) on update cascade on delete cascade
);

CREATE TABLE public.comment
(
    id              bigserial primary key,
    content         text                     not null,
    date_created    timestamp with time zone not null,
    number_of_likes integer default 0,
    user_id         bigserial                not null,
    blog_id         bigserial                not null,
    foreign key (user_id) references users (id) on update cascade on delete cascade,
    foreign key (blog_id) references blog (id) on update cascade on delete cascade
);

CREATE TABLE public.tags
(
    blog_id bigserial    not null,
    tag     varchar(100) not null,
    primary key (blog_id, tag),
    foreign key (blog_id) references blog (id) on update cascade on delete cascade
);

CREATE TABLE public.bookmarks
(
    user_id      bigserial not null,
    blog_id      bigserial not null,
    date_created timestamp with time zone,
    primary key (user_id, blog_id),
    foreign key (user_id) references users (id) on update cascade on delete cascade,
    foreign key (blog_id) references blog (id) on update cascade on delete cascade
);

CREATE TABLE public.follows
(
    follower_id bigserial not null,
    followed_id bigserial not null,
    primary key (follower_id, followed_id),
    foreign key (follower_id) references users (id) on update cascade on delete cascade,
    foreign key (followed_id) references users (id) on update cascade on delete cascade
);
