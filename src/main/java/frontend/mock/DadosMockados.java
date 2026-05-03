package frontend.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import frontend.util.Navegador;

public class DadosMockados {

    public static class UsuarioMock {
        private String nome;
        private String email;
        private String tipo;
        private String detalhe;

        public UsuarioMock(String nome, String email, String tipo, String detalhe) {
            this.nome = nome;
            this.email = email;
            this.tipo = tipo;
            this.detalhe = detalhe;
        }

        public String getNome() {
            return nome;
        }

        public String getEmail() {
            return email;
        }

        public String getTipo() {
            return tipo;
        }

        public String getDetalhe() {
            return detalhe;
        }
    }

    public static class PerguntaMock {
        private String enunciado;
        private String imagemDescricao;
        private String dificuldade;
        private List<String> alternativas;
        private int correta;

        public PerguntaMock(String enunciado, String imagemDescricao, String dificuldade, List<String> alternativas, int correta) {
            this.enunciado = enunciado;
            this.imagemDescricao = imagemDescricao;
            this.dificuldade = dificuldade;
            this.alternativas = alternativas;
            this.correta = correta;
        }

        public String getEnunciado() {
            return enunciado;
        }

        public String getImagemDescricao() {
            return imagemDescricao;
        }

        public String getDificuldade() {
            return dificuldade;
        }

        public List<String> getAlternativas() {
            return alternativas;
        }

        public int getCorreta() {
            return correta;
        }
    }

    public static class RankingMock {
        private String nome;
        private String turma;
        private int pontuacao;

        public RankingMock(String nome, String turma, int pontuacao) {
            this.nome = nome;
            this.turma = turma;
            this.pontuacao = pontuacao;
        }

        public String getNome() {
            return nome;
        }

        public String getTurma() {
            return turma;
        }

        public int getPontuacao() {
            return pontuacao;
        }
    }

    public static class EstatisticaMock {
        private String titulo;
        private String valor;
        private String detalhe;

        public EstatisticaMock(String titulo, String valor, String detalhe) {
            this.titulo = titulo;
            this.valor = valor;
            this.detalhe = detalhe;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getValor() {
            return valor;
        }

        public String getDetalhe() {
            return detalhe;
        }
    }

    public static class PerguntaCadastroMock {
        private String enunciado;
        private String dificuldade;
        private String status;

        public PerguntaCadastroMock(String enunciado, String dificuldade, String status) {
            this.enunciado = enunciado;
            this.dificuldade = dificuldade;
            this.status = status;
        }

        public String getEnunciado() {
            return enunciado;
        }

        public String getDificuldade() {
            return dificuldade;
        }

        public String getStatus() {
            return status;
        }
    }

    public static UsuarioMock getAlunoMockado() {
        return new UsuarioMock(
            "Guilherme Aluno",
            "guilherme@aluno.com",
            Navegador.TIPO_ALUNO,
            "1º ano B | Perfil de aluno"
        );
    }

    public static UsuarioMock getProfessorMockado() {
        return new UsuarioMock(
            "Maria do Socorro",
            "socorro@etec.sp.gov.br",
            Navegador.TIPO_PROFESSOR,
            "Química Geral e Experimental | Perfil de professor"
        );
    }

    public static List<PerguntaMock> getPerguntasGameplay() {
        List<PerguntaMock> perguntas = new ArrayList<>();
        perguntas.add(new PerguntaMock(
            "Qual nome deste objeto usado para aquecer líquidos e fazer titulações?",
            "Imagem ilustrativa: vidro cônico com base larga e gargalo estreito.",
            "FACIL",
            Arrays.asList("Proveta", "Erlenmeyer", "Bureta", "Bastão de vidro"),
            1
        ));
        perguntas.add(new PerguntaMock(
            "Em uma filtração simples, qual peça auxilia a separação sólido-líquido?",
            "Imagem ilustrativa: funil apoiado sobre um frasco coletor.",
            "MEDIO",
            Arrays.asList("Funil analítico", "Cadinho", "Kitassato", "Pipeta"),
            0
        ));
        perguntas.add(new PerguntaMock(
            "Qual material de porcelana é indicado para evaporação e secagem de sólidos?",
            "Imagem ilustrativa: recipiente raso de porcelana sobre placa de aquecimento.",
            "DIFICIL",
            Arrays.asList("Béquer", "Cápsula de porcelana", "Balão volumétrico", "Condensador"),
            1
        ));
        return perguntas;
    }

    public static List<RankingMock> getRankingGeral() {
        return Arrays.asList(
            new RankingMock("Aluno X", "1º ano A", 980),
            new RankingMock("Aluno Y", "1º ano B", 930),
            new RankingMock("Aluno Z", "2º ano A", 910),
            new RankingMock("Aluno W", "3º ano A", 885),
            new RankingMock("Aluno H", "1º ano C", 860)
        );
    }

    public static List<EstatisticaMock> getEstatisticasAluno() {
        return Arrays.asList(
            new EstatisticaMock("Acertos", "42", "Último mês"),
            new EstatisticaMock("Erros", "11", "Último mês"),
            new EstatisticaMock("Partidas", "9", "Sessões realizadas"),
            new EstatisticaMock("Pontuação", "1.280", "Pontuação acumulada")
        );
    }

    public static List<EstatisticaMock> getEstatisticasProfessor() {
        return Arrays.asList(
            new EstatisticaMock("Turmas acompanhadas", "3", "Visão geral"),
            new EstatisticaMock("Alunos ativos", "84", "Últimos 30 dias"),
            new EstatisticaMock("Questões testadas", "26", "Banco atual"),
            new EstatisticaMock("Média geral", "78%", "Desempenho das turmas")
        );
    }

    public static List<PerguntaCadastroMock> getPerguntasGerenciar() {
        return Arrays.asList(
            new PerguntaCadastroMock("Qual vidraria é um Erlenmeyer?", "FACIL", "Ativa"),
            new PerguntaCadastroMock("Qual peça condensa os vapores na destilação simples?", "MEDIO", "Ativa"),
            new PerguntaCadastroMock("Qual material é indicado para secagem de sólidos?", "DIFICIL", "Rascunho")
        );
    }
}
