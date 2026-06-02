# 🧪 QuimLab: Game Educativo de Química

> Projeto Integrador Interdisciplinar desenvolvido em parceria com a ETEC Júlio de Mesquita, com foco em gamificação, ensino de Química e apoio ao aprendizado de alunos ingressantes do Ensino Médio Técnico.

![Java](https://img.shields.io/badge/JAVA-POO-C1121F?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/JAVA%20SWING-INTERFACE-111111?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MYSQL-DATABASE-C1121F?style=for-the-badge&logo=mysql&logoColor=white)
![Railway](https://img.shields.io/badge/RAILWAY-CLOUD%20DATABASE-111111?style=for-the-badge&logo=railway&logoColor=white)
![Maven](https://img.shields.io/badge/MAVEN-BUILD-C1121F?style=for-the-badge&logo=apachemaven&logoColor=white)
![JDBC](https://img.shields.io/badge/JDBC-CONNECTION-111111?style=for-the-badge)
![POO](https://img.shields.io/badge/PROGRAMA%C3%87%C3%83O%20ORIENTADA%20A%20OBJETOS-CORE-C1121F?style=for-the-badge)
![Desktop](https://img.shields.io/badge/DESKTOP-APPLICATION-111111?style=for-the-badge)
![ETEC](https://img.shields.io/badge/ETEC-J%C3%9ALIO%20DE%20MESQUITA-C1121F?style=for-the-badge)

---

## 📌 Sobre o Projeto

O **QuimLab** é um jogo educativo desenvolvido para auxiliar estudantes de Química na identificação de vidrarias, utensílios laboratoriais e sistemas experimentais utilizados em aulas práticas.

O projeto tem como objetivo transformar o aprendizado técnico em uma experiência mais interativa, visual e gamificada, facilitando a adaptação dos alunos ingressantes do 1º ano do curso técnico em Química às práticas presenciais em laboratório.

A aplicação foi desenvolvida em **Java**, utilizando os princípios de **Programação Orientada a Objetos (POO)**, com interface gráfica construída em **Java Swing** e persistência de dados em **MySQL**, hospedado em nuvem por meio da plataforma **Railway**.

---

## 🎯 Público-Alvo

O projeto é voltado principalmente para:


- alunos ingressantes do 1º ano do curso técnico em Química;
- estudantes do Ensino Médio Técnico;
- professores que desejam acompanhar o desempenho dos alunos;
- turmas que precisam reforçar a identificação de materiais e sistemas de laboratório.

---

## 🧠 Objetivo

O objetivo principal do **QuimLab** é unir educação, tecnologia e gamificação para tornar o aprendizado de Química mais acessível, dinâmico e engajador.

A proposta busca apoiar o aluno na construção de familiaridade com o ambiente laboratorial antes ou durante as práticas presenciais, reduzindo dificuldades iniciais e fortalecendo a aprendizagem por meio de desafios, rankings e estatísticas de desempenho.

---

## 🛠️ Tecnologias Utilizadas

| Categoria | Tecnologia |
|---|---|
| Linguagem principal | Java |
| Paradigma | Programação Orientada a Objetos |
| Interface gráfica | Java Swing |
| Banco de dados | MySQL |
| Banco em nuvem | Railway |
| Conexão com banco | JDBC |
| Gerenciador de dependências | Maven |
| Plataforma | Desktop |
| Controle de versão | Git e GitHub |

---

## 🚀 Funcionalidades Principais

### 👨‍🎓 Área do Aluno

- Login de aluno;
- acesso ao modo jogador;
- resolução de perguntas de Química;
- visualização de desempenho individual;
- estatísticas do aluno;
- ranking da própria turma;
- acompanhamento de pontuação, acertos, erros e aproveitamento.

### 👨‍🏫 Área do Professor

- Login de professor;
- painel administrativo;
- gerenciamento de perguntas;
- gerenciamento de usuários;
- visualização de estatísticas das turmas;
- acompanhamento de rankings por turma;
- análise geral de desempenho dos alunos.

### 🎮 Gamificação

- Sistema de pontuação;
- ranking de alunos;
- estatísticas de aproveitamento;
- acompanhamento de desempenho;
- incentivo à competição saudável;
- aprendizado por meio de desafios.

---

## 📋 Requisitos Funcionais

- [x] Autenticação de alunos e professores;
- [x] interface desktop em Java Swing;
- [x] conexão com banco de dados MySQL;
- [x] banco de dados hospedado em nuvem via Railway;
- [x] modo jogador para resolução de questões;
- [x] painel do professor;
- [x] gerenciamento de perguntas;
- [x] gerenciamento de usuários;
- [x] estatísticas individuais do aluno;
- [x] estatísticas gerais das turmas;
- [x] ranking da turma;
- [x] organização visual por perfis de usuário.

---

## ⚙️ Requisitos Não Funcionais

- Interface intuitiva e adaptada para estudantes do Ensino Médio;
- execução em ambiente desktop;
- organização do código com Programação Orientada a Objetos;
- separação entre interface, lógica e persistência;
- uso de banco de dados relacional;
- conexão remota com banco em nuvem;
- layout com boa legibilidade;
- contraste adequado entre textos, botões e fundos;
- estrutura preparada para expansão futura.

---

## 🗄️ Banco de Dados

O sistema utiliza **MySQL** como banco de dados principal, com hospedagem em nuvem pela plataforma **Railway**.

O banco é responsável por armazenar informações como:

1. **Usuários**
   - alunos;
   - professores;
   - dados de login;
   - tipo de conta.

2. **Perguntas**
   - enunciados;
   - alternativas;
   - respostas corretas;
   - dados associados ao quiz.

3. **Desempenho**
   - pontuação dos alunos;
   - quantidade de acertos;
   - quantidade de erros;
   - aproveitamento;
   - ranking.

4. **Turmas**
   - organização dos alunos por ano;
   - desempenho coletivo;
   - estatísticas por turma.

---

## 🧱 Arquitetura do Projeto

O projeto segue uma estrutura orientada a objetos, separando responsabilidades entre:

- **Telas:** responsáveis pela interface gráfica em Java Swing;
- **Modelos:** representam entidades como usuário, aluno, professor, pergunta e desempenho;
- **DAO/Repositórios:** responsáveis pela comunicação com o banco de dados;
- **Serviços:** concentram regras e operações principais;
- **Utilitários:** classes auxiliares para conexão, tema visual, componentes e validações.

Essa organização facilita manutenção, expansão e leitura do código.

---

## 🖥️ Interface

A interface do QuimLab foi construída com **Java Swing**, seguindo uma estética baseada em:

- preto;
- branco;
- cinza claro;
- detalhes em vermelho inspirados na identidade visual da ETEC;
- cards arredondados;
- botões padronizados;
- telas separadas para aluno e professor;
- gráficos e rankings para acompanhamento do desempenho.

---

## 📊 Estatísticas e Rankings

O sistema possui telas voltadas ao acompanhamento pedagógico, incluindo:

### Estatísticas do Aluno

- pontuação total;
- quantidade de questões respondidas;
- acertos;
- erros;
- aproveitamento;
- posição no ranking;
- comparação com média da turma.

### Estatísticas das Turmas

- dados do Primeiro Ano;
- dados do Segundo Ano;
- dados do Terceiro Ano;
- média geral;
- maior nota;
- menor nota;
- aproveitamento médio;
- ranking por turma.

---

## 📁 Estrutura Geral

```txt
QuimLab/
├── src/
│   └── main/
│       ├── java/
│       │   └── ...
│       └── resources/
├── pom.xml
├── README.md
└── ...
