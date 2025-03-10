ALTER TABLE tb_user ADD CONSTRAINT user_email_unique UNIQUE (email);

INSERT INTO public.tb_user_content(id, photo) VALUES (1, null);
INSERT INTO public.tb_role(id, role) VALUES (1, 'ADMIN');
INSERT INTO public.tb_role(id, role) VALUES (2, 'MANAGER');
INSERT INTO public.tb_role(id, role) VALUES (3, 'USER');

INSERT INTO public.tb_user(
	date_birth, id, id_user_content_fk, first_name, gender, last_name, email, password)
	VALUES ('1-1-2000', 1, 1, 'ADMIN', 'MALE', '', 'admin@admin', '$2a$10$z3WjyrPg2MLZFoHOxgePY.LdE7JgpDkfyzti5h5gZn3tm8yCKfRrO');

INSERT INTO public.tb_user_role(user_id, role_id) VALUES (1, 1);