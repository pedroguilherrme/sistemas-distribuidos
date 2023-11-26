import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImplementacaoRemota extends UnicastRemoteObject implements InterfaceRemota {

    protected ImplementacaoRemota() throws RemoteException {
        super();
    }

    @Override
    public String meuMetodoRemoto() throws RemoteException {
        return "Olá do método remoto!";
    }

    @Override
    public List<String> getallusers() throws RemoteException {
        return new ArrayList<>(Arrays.asList("Elemento 1", "Elemento 2", "Elemento 3"));
    }

    @Override
    public String obterOpcoes() throws RemoteException {
        return "1. Criar Documento\n2. Criar uma nota em um documento;\nEditar uma nota em um documento;";
    }

    @Override
    public String executarOpcao(int escolha) throws RemoteException {
        switch (escolha) {
            case 1:
                return "Criando Documento";
            case 2:
                return "Criando nota 2";
            case 3:
                return "Editando Nota";
            default:
                return "Opção inválida. Tente novamente.";
        }
    }
}