-- 1. Inserir Endereços
INSERT INTO tb_endereco (id, logradouro, numero, cidade, estado, cep) 
VALUES (1, 'Rua das Flores', '123', 'Maringá', 'PR', '87020-010');

-- 2. Inserir o Usuário que será o Dono (inicialmente sem tipo_usuario se for opcional, ou com um tipo genérico)
INSERT INTO tb_usuario (id, email, nome, login, senha, data_ultima_alteracao, id_tipo_usuario) 
VALUES (1, 'admin@saboritaliano.com', 'Carlos Dono', 'carlos.dono', '123456', '2026-10-13T10:00:00', NULL);

-- 3. Inserir o Restaurante já referenciando o id_usuario_dono (id = 1)
INSERT INTO tb_restaurante (id, nome, horario_funcionamento_inicio, horario_funcionamento_fim, tipo_cozinha, id_endereco, id_usuario_dono) 
VALUES (1, 'Sabor Italiano', '10:00:00', '23:00:00', 2, 1, 1);

-- 4. Inserir Tipos de Usuário vinculados ao Restaurante
INSERT INTO tb_tipo_usuario (id, nome, tipo, id_restaurante) 
VALUES (1, 'Gerente do Estabelecimento', 1, 1);

INSERT INTO tb_tipo_usuario (id, nome, tipo, id_restaurante) 
VALUES (2, 'Cliente Frequente', 2, 1);

-- 5. Atualizar o Dono ou inserir novos usuários vinculados aos cargos do restaurante
UPDATE tb_usuario SET id_tipo_usuario = 1 WHERE id = 1; -- Carlos vira Gerente/Dono

INSERT INTO tb_usuario (id, email, nome, login, senha, data_ultima_alteracao, id_tipo_usuario) 
VALUES (2, 'cliente@email.com', 'Ana Cliente', 'ana.cliente', '123456', '2026-10-13T10:05:00', 2);