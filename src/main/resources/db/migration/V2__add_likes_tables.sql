CREATE TABLE public.likes_blog
(
    user_id bigserial not null,
    blog_id bigserial not null,
    primary key (user_id, blog_id),
    foreign key (user_id) references users (id) on update cascade on delete cascade,
    foreign key (blog_id) references blog (id) on update cascade on delete cascade
);

CREATE TABLE public.likes_comment
(
    user_id bigserial not null,
    comment_id bigserial not null,
    primary key (user_id, comment_id),
    foreign key (user_id) references users (id) on update cascade on delete cascade,
    foreign key (comment_id) references comment (id) on update cascade on delete cascade
);