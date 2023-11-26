import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        try {
        	// Crie e exiba a janela de login
            SwingUtilities.invokeLater(() -> {
                try {
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}