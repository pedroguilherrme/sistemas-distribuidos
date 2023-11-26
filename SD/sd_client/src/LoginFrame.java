import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        super("Login");

        // Configura��es b�sicas do frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null); // Centraliza na tela
        setLayout(new BorderLayout());

        // Painel para os campos de entrada
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2));

        // Componentes
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        // Bot�o de login
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Adiciona os componentes ao frame
        add(inputPanel, BorderLayout.CENTER);
        add(loginButton, BorderLayout.SOUTH);

        // Torna o frame vis�vel
        setVisible(true);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            // Obtenha as refer�ncias remotas do servidor
            UserRemote userRemote = RemoteService.getUserRemote();

            // Autentica��o do usu�rio
            boolean authenticatedUser = userRemote.authenticateUser(username, password);

            if (authenticatedUser) {
                // Usu�rio autenticado com sucesso
                JOptionPane.showMessageDialog(this, "Login successful!");
                // Aqui voc� pode abrir a pr�xima tela ou realizar outras a��es necess�rias.
            } else {
                // Falha na autentica��o
                JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during login. Please try again later.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame();
            }
        });
    }
}
