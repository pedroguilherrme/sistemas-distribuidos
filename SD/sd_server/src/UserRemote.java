import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface UserRemote extends Remote {
	boolean authenticateUser(String username, String password) throws RemoteException, SQLException;
    User getUserById(Connection connection, long userId) throws RemoteException, SQLException;
    void updateUser(Connection connection) throws RemoteException, SQLException;
    List<Document> getOwnedDocuments(Connection connection) throws RemoteException, SQLException;
    List<Document> getSharedDocuments(Connection connection) throws RemoteException, SQLException;
    void shareDocument(Connection connection, long documentId) throws RemoteException, SQLException;
    User getUserByUsername(Connection connection, String username) throws RemoteException, SQLException;
    void createUser(Connection connection) throws RemoteException, SQLException;
    long getId() throws RemoteException;
    String getUsername() throws RemoteException;
    String getName() throws RemoteException;
    String getPassword() throws RemoteException;
    Timestamp getCreatedAt() throws RemoteException;
    Timestamp getUpdatedAt() throws RemoteException;
    void setId(long id) throws RemoteException;
    void setUsername(String username) throws RemoteException;
    void setName(String name) throws RemoteException;
    void setPassword(String password) throws RemoteException;
    void setCreatedAt(Timestamp createdAt) throws RemoteException;
    void setUpdatedAt(Timestamp updatedAt) throws RemoteException;
}