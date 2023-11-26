import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DocumentRemote extends Remote {
    void createDocument(Connection connection) throws RemoteException, SQLException;
    Document getDocumentById(Connection connection, long documentId) throws RemoteException, SQLException;
    void updateDocument(Connection connection, long userId, List<Note> newNotes) throws RemoteException, SQLException;
    List<Note> getNotes(Connection connection) throws RemoteException, SQLException;
    List<Document> getAllDocuments(Connection connection) throws RemoteException, SQLException;
    List<Document> getDocumentsByUserId(Connection connection, long userId) throws RemoteException, SQLException;
}