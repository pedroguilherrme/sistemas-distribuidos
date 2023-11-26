import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;


public class Cliente {
    public static void main(String[] args) {
        try {

            InterfaceRemota minhaInterface = (InterfaceRemota) Naming.lookup("rmi://localhost/ImplementacaoRemota");


            Scanner scanner = new Scanner(System.in);

            while (true) {

                System.out.println("Escolha uma opção:");
                System.out.println("1.Criar um documento ");
                System.out.println("2. Teste");
                System.out.println("3. Teste");


                int escolha = scanner.nextInt();

                switch (escolha) {
                    case 1:

                        String resultadoMetodoRemoto = minhaInterface.meuMetodoRemoto();
                        System.out.println("Resultado do método remoto: " + resultadoMetodoRemoto);
                        break;
                    case 2:

                        List<String> todosUsuarios = minhaInterface.getallusers();
                        System.out.println("Lista de todos os usuários:");
                        for (String usuario : todosUsuarios) {
                            System.out.println(usuario);
                        }
                        break;
                    case 3:

                        System.out.println("Saindo do cliente.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}