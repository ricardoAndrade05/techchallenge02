-- Desativa temporariamente as chaves estrangeiras
SET REFERENTIAL_INTEGRITY FALSE;

DELETE FROM tb_usuario_tipo_usuario;
DELETE FROM tb_usuario;
DELETE FROM tb_tipo_usuario;
DELETE FROM tb_restaurante;
DELETE FROM tb_endereco;

-- Religa as chaves estrangeiras
SET REFERENTIAL_INTEGRITY TRUE;

-- 1. Inserir Endereços
INSERT INTO tb_endereco (id, logradouro, numero, cidade, estado, cep) 
VALUES (1, 'Rua das Flores', '123', 'Maringá', 'PR', '87020-010');

-- 2. Inserir Usuários (sem a coluna id_tipo_usuario)
INSERT INTO tb_usuario (id, email, nome, login, senha, data_ultima_alteracao) 
VALUES (1, 'admin@saboritaliano.com', 'Carlos Dono', 'carlos.dono', '123456', '2026-10-13T10:00:00');

INSERT INTO tb_usuario (id, email, nome, login, senha, data_ultima_alteracao) 
VALUES (2, 'cliente@email.com', 'Ana Cliente', 'ana.cliente', '123456', '2026-10-13T10:05:00');

-- 3. Inserir o Restaurante referenciando o id_usuario_dono (id = 1)
INSERT INTO tb_restaurante (id, nome, horario_funcionamento_inicio, horario_funcionamento_fim, tipo_cozinha, id_endereco, id_usuario_dono) 
VALUES (1, 'Sabor Italiano', '10:00:00', '23:00:00', 2, 1, 1);

-- 4. Inserir Tipos de Usuário vinculados ao Restaurante
INSERT INTO tb_tipo_usuario (id, nome, tipo, id_restaurante) 
VALUES (1, 'Gerente do Estabelecimento', 1, 1);

INSERT INTO tb_tipo_usuario (id, nome, tipo, id_restaurante) 
VALUES (2, 'Cliente Frequente', 2, 1);

INSERT INTO tb_tipo_usuario (id, nome, tipo, id_restaurante) 
VALUES (3, 'Cliente Usual', 2, 1);

-- 5. Vincular Usuários aos seus Tipos na tabela intermediária (tb_usuario_tipo_usuario)
-- Carlos (id = 1) como Gerente (id = 1)
INSERT INTO tb_usuario_tipo_usuario (id_usuario, id_tipo_usuario) 
VALUES (1, 1);

-- Ana Cliente (id = 2) como Cliente Frequente (id = 2)
INSERT INTO tb_usuario_tipo_usuario (id_usuario, id_tipo_usuario) 
VALUES (2, 2);

ALTER TABLE tb_tipo_usuario ALTER COLUMN id RESTART WITH 4;
ALTER TABLE tb_usuario ALTER COLUMN id RESTART WITH 3;