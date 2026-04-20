package backend.Seguranca;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaService {

    // Transforma a senha em Hash antes de salvar no banco
    public static String hashSenha(String senhaPlana) {
        return BCrypt.hashpw(senhaPlana, BCrypt.gensalt(12));
    }

    // Verifica se a senha digitada no login bate com o Hash do banco
    public static boolean verificarSenha(String senhaPlana, String senhaHashed) {
        return BCrypt.checkpw(senhaPlana, senhaHashed);
    }
}