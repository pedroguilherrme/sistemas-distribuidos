import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

public interface NoteRemote extends Remote {
    void createNote(Connection connection) throws RemoteException, SQLException;
    Note getNoteById(Connection connection, long noteId) throws RemoteException, SQLException;
    void updateNote(Connection connection) throws RemoteException, SQLException;
    long getId() throws RemoteException;
    void setId(long id) throws RemoteException;
    String getTitle() throws RemoteException;
    void setTitle(String title) throws RemoteException;
    String getContent() throws RemoteException;
    void setContent(String content) throws RemoteException;
    long getDocumentId() throws RemoteException;
    void setDocumentId(long documentId) throws RemoteException;
    Timestamp getCreatedAt() throws RemoteException;
    void setCreatedAt(Timestamp createdAt) throws RemoteException;
    Timestamp getUpdatedAt() throws RemoteException;
    void setUpdatedAt(Timestamp updatedAt) throws RemoteException;
}