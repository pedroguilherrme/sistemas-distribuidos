import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceRemota extends Remote {
    // Métodos remotos
    String meuMetodoRemoto() throws RemoteException;
    List<String> getallusers() throws RemoteException;

    // Métodos relacionados ao menu
    String obterOpcoes() throws RemoteException;
    String executarOpcao(int escolha) throws RemoteException;
}