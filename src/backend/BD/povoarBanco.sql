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