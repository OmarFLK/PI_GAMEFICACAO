-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usuario` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `nomeUsuario` VARCHAR(100) NOT NULL,
  `emailUsuario` VARCHAR(100) NOT NULL,
  `senha` VARCHAR(255) NOT NULL,
  `tipo` ENUM('ALUNO', 'PROFESSOR') NULL,
  `dataCriacao` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idUsuario`),
  UNIQUE INDEX `email_UNIQUE` (`emailUsuario` ASC) VISIBLE
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `perguntas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `perguntas` (
  `idPergunta` INT NOT NULL AUTO_INCREMENT,
  `enunciado` VARCHAR(200) NOT NULL,
  `imagemURL` VARCHAR(255) NULL,
  `dificuldade` ENUM('FACIL', 'MEDIO', 'DIFICIL') NOT NULL,
  `criadoPor` INT NOT NULL,
  `dataCriacao` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ativa` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`idPergunta`),
  INDEX `criadoPor_idx` (`criadoPor` ASC) VISIBLE,
  CONSTRAINT `criadoPor`
    FOREIGN KEY (`criadoPor`)
    REFERENCES `usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `Alternativa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Alternativa` (
  `idAlternativa` INT NOT NULL AUTO_INCREMENT,
  `idPergunta` INT NOT NULL,
  `Texto` VARCHAR(200) NOT NULL,
  `imagem_URL` VARCHAR(225) NULL,
  `correta` TINYINT NOT NULL,
  PRIMARY KEY (`idAlternativa`),
  INDEX `idPergunta_idx` (`idPergunta` ASC) VISIBLE,
  CONSTRAINT `idPergunta`
    FOREIGN KEY (`idPergunta`)
    REFERENCES `perguntas` (`idPergunta`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `Partida`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Partida` (
  `idPartida` INT NOT NULL AUTO_INCREMENT,
  `idUsuario` INT NOT NULL,
  `Pontuacao` INT NOT NULL DEFAULT 0,
  `dataInicio` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dataFim` DATETIME NULL,
  PRIMARY KEY (`idPartida`),
  INDEX `idUsuario_idx` (`idUsuario` ASC) VISIBLE,
  CONSTRAINT `idUsuario`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `resposta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `resposta` (
  `idResposta` INT NOT NULL AUTO_INCREMENT,
  `idPergunta` INT NOT NULL,
  `idPartida` INT NOT NULL,
  `idAlternativa` INT NOT NULL,
  `correta` TINYINT NOT NULL,
  `tempoResposta` INT NULL,
  PRIMARY KEY (`idResposta`),
  INDEX `idPartida_idx` (`idPartida` ASC) VISIBLE,
  INDEX `idPergunta_idx` (`idPergunta` ASC) VISIBLE,
  INDEX `idAlternativa_idx` (`idAlternativa` ASC) VISIBLE,
  CONSTRAINT `idPartida`
    FOREIGN KEY (`idPartida`)
    REFERENCES `Partida` (`idPartida`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idPergunta`
    FOREIGN KEY (`idPergunta`)
    REFERENCES `perguntas` (`idPergunta`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idAlternativa`
    FOREIGN KEY (`idAlternativa`)
    REFERENCES `Alternativa` (`idAlternativa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `ajuda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ajuda` (
  `idAjuda` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NOT NULL,
  `descricao` VARCHAR(200) NULL,
  PRIMARY KEY (`idAjuda`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `usoDeAjuda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usoDeAjuda` (
  `idUsoDeAjuda` INT NOT NULL AUTO_INCREMENT,
  `idPartida` INT NOT NULL,
  `idAjuda` INT NOT NULL,
  PRIMARY KEY (`idUsoDeAjuda`),
  INDEX `idPartida_idx` (`idPartida` ASC) VISIBLE,
  INDEX `idAjuda_idx` (`idAjuda` ASC) VISIBLE,
  CONSTRAINT `idPartida`
    FOREIGN KEY (`idPartida`)
    REFERENCES `Partida` (`idPartida`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idAjuda`
    FOREIGN KEY (`idAjuda`)
    REFERENCES `ajuda` (`idAjuda`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `estatistica`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estatistica` (
  `idEstatistica` INT NOT NULL AUTO_INCREMENT,
  `idUsuario` INT NOT NULL,
  `totalAcertos` INT NOT NULL DEFAULT 0,
  `totalErros` INT NOT NULL DEFAULT 0,
  `tempoMedio` FLOAT NOT NULL,
  PRIMARY KEY (`idEstatistica`),
  INDEX `idUsuario_idx` (`idUsuario` ASC) VISIBLE,
  CONSTRAINT `idUsuario`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;