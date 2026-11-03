-- Desativa temporariamente as chaves estrangeiras para limpeza
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
    (12, 'Avenida Pedro Álvares Cabral', '500', 'São Paulo', 'SP', '04094-050');

-- 2. Inserir Usuários
INSERT INTO tb_usuario (id, nome, email, login, senha, data_ultima_alteracao)
VALUES 
    (1, 'Carlos Silva', 'carlos.dono@email.com', 'carlosdono', 'senha123', CURRENT_TIMESTAMP),
    (2, 'Ana Oliveira', 'ana.dona@email.com', 'anadona', 'senha123', CURRENT_TIMESTAMP),
    (3, 'Roberto Santos', 'roberto.dono@email.com', 'robertodono', 'senha123', CURRENT_TIMESTAMP),
    (4, 'Mariana Costa', 'mariana.dona@email.com', 'marianadona', 'senha123', CURRENT_TIMESTAMP),
    (5, 'Lucas Pereira', 'lucas.cliente@email.com', 'lucascliente', 'senha123', CURRENT_TIMESTAMP),
    (6, 'Fernanda Lima', 'fernanda.cliente@email.com', 'fernandacliente', 'senha123', CURRENT_TIMESTAMP),
    (7, 'Gabriel Souza', 'gabriel.cliente@email.com', 'gabrielcliente', 'senha123', CURRENT_TIMESTAMP),
    (8, 'Beatriz Alves', 'beatriz.cliente@email.com', 'beatrizcliente', 'senha123', CURRENT_TIMESTAMP);

-- 3. Inserir Restaurantes
INSERT INTO tb_restaurante (id, nome, horario_funcionamento_inicio, horario_funcionamento_fim, tipo_cozinha, id_usuario_dono, id_endereco)
VALUES 
    (1, 'Trattoria Bella Italia', '11:30:00', '23:00:00', 2, 1, 9),
    (2, 'Sushiman Prime', '18:00:00', '23:30:00', 3, 2, 10),
    (3, 'Fogo & Brasa Churrascaria', '11:00:00', '22:00:00', 1, 3, 11),
    (4, 'Sabor Arabe', '11:30:00', '21:30:00', 7, 4, 12);

-- 4. Inserir itens no cardapio
INSERT INTO tb_item_cardapio (id, nome, descricao, preco, caminho_imagem, tipo_disponibilidade, id_restaurante)
VALUES
-- RESTAURANTE 1: Trattoria Bella Italia
(1, 'Spaghetti à Carbonara', 'Massa italiana com molho de ovos, parmesão, pancetta e pimenta-do-reino.', 42.90, '/images/cardapio/carbonara.jpg', 4, 1),
(2, 'Pizza Margherita', 'Pizza artesanal com molho de tomate, mussarela, manjericão fresco e azeite.', 55.90, '/images/cardapio/margherita.jpg', 3, 1),
(3, 'Lasagna Bolonhesa', 'Lasanha artesanal com camadas de massa, molho bolonhesa e queijo gratinado.', 48.90, '/images/cardapio/lasanha.jpg', 4, 1),
(4, 'Risotto de Funghi', 'Risoto cremoso preparado com cogumelos frescos e parmesão.', 52.90, '/images/cardapio/risotto-funghi.jpg', 1, 1),
(5, 'Tiramisu', 'Sobremesa italiana com biscoito champagne, café, creme de mascarpone e cacau.', 24.90, '/images/cardapio/tiramisu.jpg', 2, 1),

-- RESTAURANTE 2: Sushiman Prime
(6, 'Combo Sushi Premium', 'Seleção especial com 20 peças de sushi, sashimi e uramaki.', 89.90, '/images/cardapio/combo-sushi.jpg', 4, 2),
(7, 'Temaki de Salmão', 'Cone de alga nori recheado com arroz japonês, salmão fresco e cream cheese.', 32.90, '/images/cardapio/temaki-salmao.jpg', 3, 2),
(8, 'Hot Roll', 'Enrolado de arroz e salmão empanado e frito, servido com molho especial.', 36.90, '/images/cardapio/hot-roll.jpg', 4, 2),
(9, 'Sashimi de Salmão', 'Fatias selecionadas de salmão fresco, servidas com wasabi e gengibre.', 45.90, '/images/cardapio/sashimi.jpg', 1, 2),
(10, 'Harumaki de Legumes', 'Rolinho primavera crocante recheado com legumes selecionados.', 22.90, '/images/cardapio/harumaki.jpg', 2, 2),

-- RESTAURANTE 3: Fogo & Brasa Churrascaria
(11, 'Picanha na Brasa', 'Picanha grelhada na brasa, acompanhada de arroz, farofa e vinagrete.', 79.90, '/images/cardapio/picanha.jpg', 4, 3),
(12, 'Costela Assada', 'Costela bovina assada lentamente na brasa, macia e suculenta.', 69.90, '/images/cardapio/costela.jpg', 3, 3),
(13, 'Espetinho de Frango', 'Espetinho de peito de frango temperado e grelhado na brasa.', 18.90, '/images/cardapio/espetinho-frango.jpg', 2, 3),
(14, 'Alcatra Completa', 'Alcatra grelhada acompanhada de arroz, fritas e salada.', 64.90, '/images/cardapio/alcatra.jpg', 4, 3),
(15, 'Coração de Galinha', 'Corações de frango temperados e assados na brasa.', 29.90, '/images/cardapio/coracao.jpg', 1, 3),

-- RESTAURANTE 4: Sabor Arabe
(16, 'Combo de Esfihas', 'Combo com 6 esfihas abertas de carne, queijo e frango.', 39.90, '/images/cardapio/esfihas.jpg', 4, 4),
(17, 'Kibe Frito', 'Kibe tradicional recheado com carne temperada e especiarias.', 12.90, '/images/cardapio/kibe.jpg', 3, 4),
(18, 'Shawarma de Frango', 'Pão sírio recheado com frango temperado, salada e molho de alho.', 32.90, '/images/cardapio/shawarma.jpg', 4, 4),
(19, 'Homus', 'Pasta tradicional de grão-de-bico com tahine, limão e azeite.', 25.90, '/images/cardapio/homus.jpg', 2, 4),
(20, 'Falafel', 'Bolinho de grão-de-bico com ervas e especiarias, acompanhado de molho tahine.', 28.90, '/images/cardapio/falafel.jpg', 1, 4);

-- 5. Inserir Tipos de Usuário
INSERT INTO tb_tipo_usuario (id, nome, tipo, id_restaurante)
VALUES 
    -- Restaurante 1 (Trattoria)
    (1, 'Proprietário', 1, 1),
    (2, 'Gerente de Atendimento', 1, 1),
    (3, 'Cliente VIP', 2, 1),
    (13, 'Cliente Comum', 2, 1),

    -- Restaurante 2 (Sushiman)
    (4, 'Proprietário', 1, 2),
    (5, 'Sushiman Chefe', 1, 2),
    (6, 'Cliente', 2, 2),

    -- Restaurante 3 (Fogo & Brasa)
    (7, 'Proprietário', 1, 3),
    (8, 'Maitre', 1, 3),
    (9, 'Cliente Regular', 2, 3),

    -- Restaurante 4 (Sabor Arabe)
    (10, 'Proprietário', 1, 4),
    (11, 'Gerente Geral', 1, 4),
    (12, 'Cliente', 2, 4);

-- 6. Vincular Usuários aos Tipos na tabela intermediária
INSERT INTO tb_usuario_tipo_usuario (id_usuario, id_tipo_usuario)
VALUES 
    -- Proprietários
    (1, 1),
    (2, 4),
    (3, 7),
    (4, 10),
    -- Clientes
    (5, 3),
    (6, 6),
    (7, 9),
    (8, 12);

-- 7. Reiniciar o auto-incremento dos IDs para os próximos registros
ALTER TABLE tb_endereco ALTER COLUMN id RESTART WITH 13;
ALTER TABLE tb_usuario ALTER COLUMN id RESTART WITH 9;
ALTER TABLE tb_restaurante ALTER COLUMN id RESTART WITH 5;
ALTER TABLE tb_tipo_usuario ALTER COLUMN id RESTART WITH 14;
ALTER TABLE tb_item_cardapio ALTER COLUMN id RESTART WITH 21;