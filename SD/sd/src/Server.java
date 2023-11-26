import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        try {
            // Inicializa o RMI Registry na porta 1099
            LocateRegistry.createRegistry(1099);

            // Cria uma instância da implementação remota
           InterfaceRemota minhaImplementacao = new ImplementacaoRemota();

            // Registra a implementação remota com um nome no RMI Registry
            Naming.rebind("ImplementacaoRemota", minhaImplementacao);

            System.out.println("Servidor pronto...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}