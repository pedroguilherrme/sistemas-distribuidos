import java.rmi.Naming;

public class RemoteService {
    private static UserRemote userRemote;
    private static DocumentRemote documentRemote;
    private static NoteRemote noteRemote;

    static {
        try {
            // Inicializa��o dos objetos remotos
            userRemote = (UserRemote) Naming.lookup("rmi://localhost/UserRemote");
            documentRemote = (DocumentRemote) Naming.lookup("rmi://localhost/DocumentRemote");
            noteRemote = (NoteRemote) Naming.lookup("rmi://localhost/NoteRemote");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserRemote getUserRemote() {
        return userRemote;
    }

    public static DocumentRemote getDocumentRemote() {
        return documentRemote;
    }

    public static NoteRemote getNoteRemote() {
        return noteRemote;
    }
}