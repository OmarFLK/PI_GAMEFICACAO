package frontend;

import javax.swing.JOptionPane;
import backend.Usuario;
import backend.UsuarioDAO;

public class LoginTela extends javax.swing.JFrame {

    public LoginTela() {
        super("Sistema Acadêmico - Quiz");
        initComponents();
        this.setLocationRelativeTo(null);
    }

    // <editor-fold defaultstate="collapsed" desc="Código Gerado pelo NetBeans">
    private void initComponents() {
        loginTextField = new javax.swing.JTextField();
        senhaPasswordField = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        loginTextField.setBorder(javax.swing.BorderFactory.createTitledBorder("Digite seu e-mail"));
        senhaPasswordField.setBorder(javax.swing.BorderFactory.createTitledBorder("Digite sua senha"));

        jButton1.setText("Sair");
        jButton1.addActionListener(evt -> this.dispose());

        jButton2.setText("Login");
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jButton1)
                        .addGap(64, 64, 64)
                        .addComponent(jButton2))
                    .addComponent(senhaPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addComponent(loginTextField))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(loginTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(senhaPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(41, 41, 41))
        );
        pack();
    }
    // </editor-fold>

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        String email = loginTextField.getText();
        String senha = new String(senhaPasswordField.getPassword());

        // CHAMA O BANCO DE DADOS
        UsuarioDAO dao = new UsuarioDAO();
        Usuario logado = dao.efetuarLogin(email, senha);

        if (logado != null) {
            JOptionPane.showMessageDialog(this, "Bem vindo, " + logado.getNome() + "!");
            // poderia abrir a próxima tela:
        } else {
            JOptionPane.showMessageDialog(this, "E-mail ou senha incorretos!", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Variáveis
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JTextField loginTextField;
    private javax.swing.JPasswordField senhaPasswordField;
}