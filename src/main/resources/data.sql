INSERT INTO blog(title, content, date_created, estimated_read_time, user_id)
VALUES ('Title 1', 'Content 1', '2016-06-22 19:10:25-07', 5, 1),
       ('Title 2', 'Content 2', '2013-06-22 19:10:25-07', 2, 2),
       ('Title 3', 'Content 3', '2015-06-22 19:10:25-07', 9, 3),
       ('Title 4', 'Content 4', '2016-06-22 19:10:25-07', 7, 1),
       ('Title 5', 'Content 5', '2018-06-22 19:10:25-07', 5, 1),
       ('Title 6', 'Content 6', '2008-06-22 19:10:25-07', 6, 2),
       ('Title 7', 'Content 7', '2005-06-22 19:10:25-07', 4, 3),
       ('Title 8', 'Content 8', '2021-06-22 19:10:25-07', 2, 3),
       ('Title 9', 'Content 9', '2020-06-22 19:10:25-07', 3, 2),
       ('Title 10', 'Content 10', '2019-06-22 19:10:25-07', 14, 1)
ON CONFLICT DO NOTHING;

INSERT INTO bookmarks(user_id, blog_id, date_created)
VALUES (1, 1, '2016-06-22 19:10:25-07'),
       (1, 3, '2016-06-22 19:10:25-07'),
       (1, 5, '2016-06-22 19:10:25-07'),
       (1, 7, '2016-06-22 19:10:25-07'),
       (1, 9, '2016-06-22 19:10:25-07'),
       (2, 2, '2016-06-22 19:10:25-07'),
       (2, 4, '2016-06-22 19:10:25-07'),
       (2, 6, '2016-06-22 19:10:25-07'),
       (2, 8, '2016-06-22 19:10:25-07'),
       (2, 10, '2016-06-22 19:10:25-07'),
       (3, 1, '2016-06-22 19:10:25-07'),
       (3, 2, '2016-06-22 19:10:25-07'),
       (3, 3, '2016-06-22 19:10:25-07'),
       (3, 4, '2016-06-22 19:10:25-07')
ON CONFLICT DO NOTHING;


--SPORT
--POLITICS
--ENTERTAINMENT
--NEWS
INSERT INTO tags(blog_id, tag)
VALUES (1, 'SPORT'),
       (2, 'SPORT'),
       (2, 'ENTERTAINMENT'),
       (3, 'POLITICS'),
       (4, 'NEWS'),
       (4, 'SPORT'),
       (5, 'NEWS'),
       (5, 'POLITICS'),
       (5, 'ENTERTAINMENT'),
       (6, 'NEWS'),
       (6, 'ENTERTAINMENT'),
       (7, 'POLITICS'),
       (8, 'SPORT'),
       (9, 'SPORT'),
       (9, 'ENTERTAINMENT'),
       (10, 'POLITICS')
ON CONFLICT DO NOTHING;






-- INSERT INTO likes_blog(user_id, blog_id) VALUES
-- (1, 2),
-- (1, 4),
-- (1, 6),
-- (1, 8),
-- (1, 10),
-- (2, 1),
-- (2, 3),
-- (2, 5),
-- (2, 7),
-- (2, 9),
-- (3, 10),
-- (3, 9),
-- (3, 8),
-- (3, 7) ON CONFLICT DO NOTHING;

