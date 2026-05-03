INSERT INTO usuario (nomeUsuario, emailUsuario, senha, tipo) VALUES 
('Maria do Socorro', 'socorro@etec.sp.gov.br', 'etec123', 'PROFESSOR'),
('Guilherme Aluno', 'guilherme@aluno.com', '123456', 'ALUNO');

INSERT INTO ajuda (nome, descricao) VALUES 
('Eliminar 50%', 'Remove duas alternativas incorretas'),
('Dica do Técnico', 'Exibe uma dica textual sobre a composição do material');

INSERT INTO perguntas (enunciado, imagemURL, dificuldade, criadoPor) VALUES 
('Qual das vidrarias abaixo é um Erlenmeyer, utilizado para titulações e aquecimento de líquidos?', 'url_imagem_erlenmeyer.jpg', 'FACIL', 1);

SET @pergunta_id1 = LAST_INSERT_ID();
INSERT INTO Alternativa (idPergunta, Texto, correta) VALUES 
(@pergunta_id1, 'Becker', 0),
(@pergunta_id1, 'Erlenmeyer', 1),
(@pergunta_id1, 'Proveta', 0),
(@pergunta_id1, 'Balão Volumétrico', 0);

INSERT INTO perguntas (enunciado, imagemURL, dificuldade, criadoPor) VALUES 
('Em um sistema de destilação simples, qual peça é fundamental para condensar os vapores?', 'url_sistema_destilacao.jpg', 'MEDIO', 1);

SET @pergunta_id2 = LAST_INSERT_ID();
INSERT INTO Alternativa (idPergunta, Texto, correta) VALUES 
(@pergunta_id2, 'Bico de Bunsen', 0),
(@pergunta_id2, 'Condensador', 1),
(@pergunta_id2, 'Kitassato', 0),
(@pergunta_id2, 'Coluna de Fracionamento', 0);

INSERT INTO perguntas (enunciado, dificuldade, criadoPor) VALUES 
('Qual destes materiais de porcelana é o mais indicado para a secagem de sólidos ou evaporação de soluções em chapa aquecedora?', 'DIFICIL', 1);

SET @pergunta_id3 = LAST_INSERT_ID();
INSERT INTO Alternativa (idPergunta, Texto, correta) VALUES 
(@pergunta_id3, 'Almofariz', 0),
(@pergunta_id3, 'Cápsula de Porcelana', 1),
(@pergunta_id3, 'Cadinho', 0),
(@pergunta_id3, 'Funil de Büchner', 0);



-- PERGUNTA 1 (FACIL)
INSERT INTO perguntas (idPergunta, enunciado, ImagemURL, dificuldade, criadoPor, ativa) 
VALUES (1, 'Qual das vidrarias abaixo é um Erlenmeyer, utilizado para titulações e aquecimento?', 'url_erlenmeyer.jpg', 'FACIL', 1, 1);
INSERT INTO Alternativa (idPergunta, Texto, correta) VALUES (1, 'Béquer', 0), (1, 'Erlenmeyer', 1), (1, 'Proveta', 0), (1, 'Balão Volumétrico', 0);

-- PERGUNTA 2 (FACIL)
INSERT INTO perguntas (idPergunta, enunciado, ImagemURL, dificuldade, criadoPor, ativa) 
VALUES (2, 'Qual o nome deste material de porcelana usado para triturar substâncias sólidas?', 'url_almofariz.jpg', 'FACIL', 1, 1);
INSERT INTO Alternativa (idPergunta, Texto, correta) VALUES (2, 'Cápsula de Porcelana', 0), (2, 'Almofariz e Pistilo', 1), (2, 'Cadinho', 0), (2, 'Funil de Buchner', 0);

-- PERGUNTA 3 (FACIL)
INSERT INTO perguntas (idPergunta, enunciado, ImagemURL, dificuldade, criadoPor, ativa) 
VALUES (3, 'EPI indispensável para proteger os olhos contra respingos no laboratório:', NULL, 'FACIL', 1, 1);
INSERT INTO Alternativa (idPergunta, Texto, correta) VALUES (3, 'Luvas de Amianto', 0), (3, 'Máscara Cirúrgica', 0), (3, 'Óculos de Segurança', 1), (3, 'Protetor Auricular', 0);

-- PERGUNTA 4 (MEDIO)
INSERT INTO perguntas (idPergunta, enunciado, ImagemURL, dificuldade, criadoPor, ativa) 
VALUES (4, 'Para medir exatamente 25,00 mL de uma solução, qual vidraria oferece maior precisão?', 'url_pipeta.jpg', 'MEDIO', 1, 1);
INSERT INTO Alternativa (idPergunta, Texto, correta) VALUES (4, 'Béquer de 50mL', 0), (4, 'Proveta de 25mL', 0), (4, 'Pipeta Volumétrica', 1), (4, 'Bastão de Vidro', 0);

-- PERGUNTA 5 (MEDIO)
INSERT INTO perguntas (idPergunta, enunciado, ImagemURL, dificuldade, criadoPor, ativa) 
VALUES (5, 'Qual vidraria é adequada para preparar soluções com volumes rigorosamente exatos?', 'url_balao_vol.jpg', 'MEDIO', 1, 1);
INSERT INTO Alternativa (idPergunta, Texto, correta) VALUES (5, 'Erlenmeyer', 0), (5, 'Balão Volumétrico', 1), (5, 'Béquer', 0), (5, 'Condensador', 0);

-- PERGUNTA 6 (DIFICIL)
INSERT INTO perguntas (idPergunta, enunciado, ImagemURL, dificuldade, criadoPor, ativa) 
VALUES (6, 'Ao diluir um ácido concentrado em água, qual o procedimento correto?', NULL, 'DIFICIL', 1, 1);
INSERT INTO Alternativa (idPergunta, Texto, correta) VALUES (6, 'Adicionar a água sobre o ácido.', 0), (6, 'Adicionar o ácido sobre a água lentamente.', 1), (6, 'Misturar ambos rapidamente.', 0), (6, 'Aquecer a água antes da mistura.', 0);

-- PERGUNTA 7 (DIFICIL)
INSERT INTO perguntas (idPergunta, enunciado, ImagemURL, dificuldade, criadoPor, ativa) 
VALUES (7, 'Qual destes equipamentos é utilizado para filtração a vácuo?', 'url_buchner.jpg', 'DIFICIL', 1, 1);
INSERT INTO Alternativa (idPergunta, Texto, correta) VALUES (7, 'Funil Comum', 0), (7, 'Funil de Buchner e Kitassato', 1), (7, 'Papel de Filtro Simples', 0), (7, 'Dessecador', 0);