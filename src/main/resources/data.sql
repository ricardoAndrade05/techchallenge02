INSERT INTO tb_endereco (id, logradouro, numero, cidade, estado, cep)
VALUES 
    -- Endereços dos Usuários (1 a 8)
    (1, 'Rua das Flores', '100', 'São Paulo', 'SP', '01001-000'),
    (2, 'Avenida Paulista', '1500', 'São Paulo', 'SP', '01310-100'),
    (3, 'Rua Augusta', '500', 'São Paulo', 'SP', '01305-000'),
    (4, 'Alameda Santos', '200', 'São Paulo', 'SP', '01418-000'),
    (5, 'Rua Oscar Freire', '900', 'São Paulo', 'SP', '01426-001'),
    (6, 'Avenida Faria Lima', '3000', 'São Paulo', 'SP', '01451-000'),
    (7, 'Rua dos Pinheiros', '400', 'São Paulo', 'SP', '05422-000'),
    (8, 'Avenida Rebouças', '1200', 'São Paulo', 'SP', '05402-000'),
    
    -- Endereços dos Restaurantes (9 a 12)
    (9, 'Rua Haddock Lobo', '1300', 'São Paulo', 'SP', '01414-002'),
    (10, 'Avenida Brigadeiro Luís Antônio', '2000', 'São Paulo', 'SP', '01318-002'),
    (11, 'Rua Consolação', '800', 'São Paulo', 'SP', '01301-000'),
    (12, 'Avenida Pedro Álvares Cabral', '500', 'São Paulo', 'SP', '04094-050')
ON CONFLICT (id) DO NOTHING;

INSERT INTO tb_usuario (id, nome, email, login, senha, data_ultima_alteracao, id_tipo_usuario)
VALUES 
    (1, 'Carlos Silva', 'carlos.dono@email.com', 'carlosdono', 'senha123', NOW(), NULL),
    (2, 'Ana Oliveira', 'ana.dona@email.com', 'anadona', 'senha123', NOW(), NULL),
    (3, 'Roberto Santos', 'roberto.dono@email.com', 'robertodono', 'senha123', NOW(), NULL),
    (4, 'Mariana Costa', 'mariana.dona@email.com', 'marianadona', 'senha123', NOW(), NULL)
ON CONFLICT (id) DO NOTHING;

INSERT INTO tb_restaurante (id, nome, horario_funcionamento_inicio, horario_funcionamento_fim, tipo_cozinha, id_usuario_dono, id_endereco)
VALUES 
    (1, 'Trattoria Bella Italia', '11:30:00', '23:00:00', 2, 1, 9),
    (2, 'Sushiman Prime', '18:00:00', '23:30:00', 3, 2, 10),
    (3, 'Fogo & Brasa Churrascaria', '11:00:00', '22:00:00', 1, 3, 11),
    (4, 'Sabor Arabe', '11:30:00', '21:30:00', 7, 4, 12)
ON CONFLICT (id) DO NOTHING;

INSERT INTO tb_tipo_usuario (id, nome, tipo, id_restaurante)
VALUES 
    -- Restaurante 1 (Trattoria)
    (1, 'Proprietário', 1, 1),
    (2, 'Gerente de Atendimento', 1, 1),
    (3, 'Cliente VIP', 2, 1),

    -- Restaurante 2 (Sushiman)
    (4, 'Proprietário', 1, 2),
    (5, 'Sushiman Chefe', 1, 2),
    (6, 'Cliente', 2, 2),

    -- Restaurante 3 (Fogo & Brasa)
    (7, 'Proprietário', 1, 3),
    (8, 'Maitre', 1, 3),
    (9, 'Cliente Regular', 2, 3),

    -- Restaurante 4 (Sabor do Sertão)
    (10, 'Proprietário', 1, 4),
    (11, 'Gerente Geral', 1, 4),
    (12, 'Cliente', 2, 4)
ON CONFLICT (id) DO NOTHING;

UPDATE tb_usuario SET id_tipo_usuario = 1 WHERE id = 1;
UPDATE tb_usuario SET id_tipo_usuario = 4 WHERE id = 2;
UPDATE tb_usuario SET id_tipo_usuario = 7 WHERE id = 3;
UPDATE tb_usuario SET id_tipo_usuario = 10 WHERE id = 4;

INSERT INTO tb_usuario (id, nome, email, login, senha, data_ultima_alteracao, id_tipo_usuario)
VALUES 
    (5, 'Lucas Pereira', 'lucas.cliente@email.com', 'lucascliente', 'senha123', NOW(), 3),
    (6, 'Fernanda Lima', 'fernanda.cliente@email.com', 'fernandacliente', 'senha123', NOW(), 6),
    (7, 'Gabriel Souza', 'gabriel.cliente@email.com', 'gabrielcliente', 'senha123', NOW(), 9),
    (8, 'Beatriz Alves', 'beatriz.cliente@email.com', 'beatrizcliente', 'senha123', NOW(), 12)
ON CONFLICT (id) DO NOTHING;

SELECT setval('tb_endereco_id_seq', (SELECT MAX(id) FROM tb_endereco));
SELECT setval('tb_usuario_id_seq', (SELECT MAX(id) FROM tb_usuario));
SELECT setval('tb_restaurante_id_seq', (SELECT MAX(id) FROM tb_restaurante));
SELECT setval('tb_tipo_usuario_id_seq', (SELECT MAX(id) FROM tb_tipo_usuario));

