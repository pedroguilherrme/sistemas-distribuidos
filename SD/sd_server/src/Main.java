import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Main {
    public static void main(String[] args) {
        try {
            // Crie uma inst�ncia do servidor RMI
            LocateRegistry.createRegistry(1099);

            // Crie inst�ncias das implementa��es remotas
            UserRemote userRemote = new User();
            DocumentRemote documentRemote = new Document();
            NoteRemote noteRemote = new Note();

            // Associe os objetos remotos com um nome no registro RMI
            Naming.rebind("UserRemote", userRemote);
            Naming.rebind("DocumentRemote", documentRemote);
            Naming.rebind("NoteRemote", noteRemote);

            System.out.println("Servidor RMI pronto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}