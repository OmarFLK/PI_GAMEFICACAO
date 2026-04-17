SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS estatistica, usoDeAjuda, ajuda, resposta, Partida, Alternativa, perguntas, usuario;

CREATE TABLE usuario (
  idUsuario INT NOT NULL AUTO_INCREMENT,
  nomeUsuario VARCHAR(100) NOT NULL,
  emailUsuario VARCHAR(100) NOT NULL,
  senha VARCHAR(255) NOT NULL,
  tipo ENUM('ALUNO', 'PROFESSOR') NOT NULL,
  dataCriacao DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (idUsuario),
  UNIQUE (emailUsuario)
) ENGINE = InnoDB;

CREATE TABLE perguntas (
  idPergunta INT NOT NULL AUTO_INCREMENT,
  enunciado VARCHAR(500) NOT NULL,
  imagemURL VARCHAR(255) NULL,
  dificuldade ENUM('FACIL', 'MEDIO', 'DIFICIL') NOT NULL,
  criadoPor INT NOT NULL,
  ativa TINYINT DEFAULT 1,
  PRIMARY KEY (idPergunta),
  CONSTRAINT fk_perguntas_usuario FOREIGN KEY (criadoPor) REFERENCES usuario (idUsuario)
) ENGINE = InnoDB;

CREATE TABLE Alternativa (
  idAlternativa INT NOT NULL AUTO_INCREMENT,
  idPergunta INT NOT NULL,
  Texto VARCHAR(200) NOT NULL,
  imagemURL VARCHAR(255) NULL,
  correta TINYINT(1) NOT NULL,
  PRIMARY KEY (idAlternativa),
  CONSTRAINT fk_alternativa_pergunta FOREIGN KEY (idPergunta) REFERENCES perguntas (idPergunta) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE Partida (
  idPartida INT NOT NULL AUTO_INCREMENT,
  idUsuario INT NOT NULL,
  Pontuacao INT DEFAULT 0,
  dataInicio DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (idPartida),
  CONSTRAINT fk_partida_usuario FOREIGN KEY (idUsuario) REFERENCES usuario (idUsuario)
) ENGINE = InnoDB;

CREATE TABLE resposta (
  idResposta INT NOT NULL AUTO_INCREMENT,
  idPergunta INT NOT NULL,
  idPartida INT NOT NULL,
  idAlternativa INT NOT NULL,
  correta TINYINT NOT NULL,
  tempoResposta INT NULL,
  PRIMARY KEY (idResposta),
  CONSTRAINT fk_resp_partida FOREIGN KEY (idPartida) REFERENCES Partida (idPartida),
  CONSTRAINT fk_resp_pergunta FOREIGN KEY (idPergunta) REFERENCES perguntas (idPergunta),
  CONSTRAINT fk_resp_alternativa FOREIGN KEY (idAlternativa) REFERENCES Alternativa (idAlternativa)
) ENGINE = InnoDB;

CREATE TABLE ajuda (
  idAjuda INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(50) NOT NULL,
  descricao VARCHAR(200) NULL,
  PRIMARY KEY (idAjuda)
) ENGINE = InnoDB;

CREATE TABLE usoDeAjuda (
  idUsoDeAjuda INT NOT NULL AUTO_INCREMENT,
  idPartida INT NOT NULL,
  idAjuda INT NOT NULL,
  PRIMARY KEY (idUsoDeAjuda),
  CONSTRAINT fk_uso_partida FOREIGN KEY (idPartida) REFERENCES Partida (idPartida),
  CONSTRAINT fk_uso_ajuda FOREIGN KEY (idAjuda) REFERENCES ajuda (idAjuda)
) ENGINE = InnoDB;

CREATE TABLE estatistica (
  idEstatistica INT NOT NULL AUTO_INCREMENT,
  idUsuario INT NOT NULL,
  totalAcertos INT NOT NULL DEFAULT 0,
  totalErros INT NOT NULL DEFAULT 0,
  tempoMedio FLOAT NOT NULL,
  PRIMARY KEY (idEstatistica),
  CONSTRAINT fk_estat_usuario FOREIGN KEY (idUsuario) REFERENCES usuario (idUsuario)
) ENGINE = InnoDB;

SET FOREIGN_KEY_CHECKS=1;