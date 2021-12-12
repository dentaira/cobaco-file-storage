INSERT INTO user_account(id, email, name, password, role) VALUES(gen_random_uuid(), 'local@domain', 'denden', 'password', 'USER');
COMMIT;
