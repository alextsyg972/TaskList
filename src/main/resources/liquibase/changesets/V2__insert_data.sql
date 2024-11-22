insert into users (name, username, password)
values ('John Doe', 'johndoe@gmail.com', '$2a$12$LPJ1MFO5mpyIkM.dJcwHcuc67chnQZVKTxHtyNTdq8eoX3URUU/j6'),
       ('Mike Smith', 'mikesmith@yahoo.com', '$2a$12$LPJ1MFO5mpyIkM.dJcwHcuc67chnQZVKTxHtyNTdq8eoX3URUU/j6');

insert into tasks (title, description, status, expiration_date)
values ('Buy cheese', null, 'TODO', '2023-01-29 12:00:00'),
       ('Do homework', 'Math, Physics, Literature', 'IN_PROGRESS', '2023-01-31 00:00:00'),
       ('Clean rooms', null, 'DONE', null),
       ('Call Mike', 'Ask about meeting', 'TODO', '2023-02-01 00:00:00');

insert into users_tasks (task_id, user_id)
values (1, 2),
       (2, 2),
       (3, 2),
       (4, 1);

insert into users_roles (user_id, role)
values (1, 'role_admin'),
       (1, 'role_user'),
       (2, 'role_user');