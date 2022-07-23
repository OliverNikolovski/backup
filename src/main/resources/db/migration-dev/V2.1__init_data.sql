insert into users (username, password, role) values
('user', '$2a$10$noK43kdyb48ISFbp3ROzoOFwDk78E/pI6LhBm7lfGTIGrkOrqygni', 'ROLE_USER'),
('oliver', '$2a$10$W1gPUsAmycO1tXA03J7q/uXY/mauKXTmL77DhMBScryAy7cvu9xEm', 'ROLE_USER'),
('vasko', '$2a$10$Y.siGXW.OzwwnUklFX21uOKBLFG25inMQfiCjQjZY.z92L/G57rdy', 'ROLE_USER') on conflict do nothing;
