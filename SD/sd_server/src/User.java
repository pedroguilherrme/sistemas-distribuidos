import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class User extends UnicastRemoteObject implements UserRemote {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
    private String username;
    private String name;
    private String password;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public User() throws RemoteException {
        super();
    }

    public User(String username, String name, String password) throws RemoteException {
    	super();
    	
        this.username = username;
        this.name = name;
        this.password = password;
    }
    
    public boolean authenticateUser(String username, String password) throws RemoteException, SQLException {
        Connection connection = null;
        try {
            // Obtenha a conex�o com o banco de dados (substitua com sua l�gica de conex�o)
            connection = DatabaseConnector.getConnection();

            // Consulta para autenticar o usu�rio
            String query = "SELECT * FROM public.users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Usu�rio autenticado com sucesso
                        User authenticatedUser = new User();
                        authenticatedUser.setId(resultSet.getLong("id"));
                        authenticatedUser.setUsername(resultSet.getString("username"));
                        authenticatedUser.setName(resultSet.getString("name"));
                        authenticatedUser.setPassword(resultSet.getString("password"));
                        authenticatedUser.setCreatedAt(resultSet.getTimestamp("created_at"));
                        authenticatedUser.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error during user authentication.", e);
        }

        // Usu�rio n�o autenticado
        return false;
    }

    public User getUserById(Connection connection, long userId) throws RemoteException, SQLException {
        String query = "SELECT * FROM public.users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setName(resultSet.getString("name"));
                    user.setPassword(resultSet.getString("password"));
                    user.setCreatedAt(resultSet.getTimestamp("created_at"));
                    user.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                    return user;
                }
            }
        }
        return null;
    }

    public void updateUser(Connection connection) throws RemoteException, SQLException {
        String query = "UPDATE public.users SET username = ?, name = ?, password = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, this.username);
            preparedStatement.setString(2, this.name);
            preparedStatement.setString(3, this.password);
            preparedStatement.setLong(4, this.id);
            preparedStatement.executeUpdate();
        }
    }

    public List<Document> getOwnedDocuments(Connection connection) throws RemoteException, SQLException {
        List<Document> documents = new ArrayList<>();
        String query = "SELECT * FROM public.documents WHERE document_owner_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, this.id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Document document = new Document();
                    document.setId(resultSet.getLong("id"));
                    document.setTitle(resultSet.getString("title"));
                    document.setCreatedAt(resultSet.getTimestamp("created_at"));
                    document.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                    documents.add(document);
                }
            }
        }
        return documents;
    }

    public List<Document> getSharedDocuments(Connection connection) throws RemoteException, SQLException {
        List<Document> documents = new ArrayList<>();
        String query = "SELECT d.* FROM public.documents d " +
                       "JOIN public.users_documents ud ON d.id = ud.document_id " +
                       "WHERE ud.user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, this.id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Document document = new Document();
                    document.setId(resultSet.getLong("id"));
                    document.setTitle(resultSet.getString("title"));
                    document.setCreatedAt(resultSet.getTimestamp("created_at"));
                    document.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                    documents.add(document);
                }
            }
        }
        return documents;
    }

    public void shareDocument(Connection connection, long documentId) throws RemoteException, SQLException {
        String query = "INSERT INTO public.users_documents(user_id, document_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, this.id);
            preparedStatement.setLong(2, documentId);
            preparedStatement.executeUpdate();
        }
    }

    public User getUserByUsername(Connection connection, String username) throws RemoteException, SQLException {
        String query = "SELECT * FROM public.users WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setName(resultSet.getString("name"));
                    user.setPassword(resultSet.getString("password"));
                    user.setCreatedAt(resultSet.getTimestamp("created_at"));
                    user.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                    return user;
                }
            }
        }
        return null;
    }

    public void createUser(Connection connection) throws RemoteException, SQLException {
        String query = "INSERT INTO public.users(username, name, password) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, this.username);
            preparedStatement.setString(2, this.name);
            preparedStatement.setString(3, this.password);
            preparedStatement.executeUpdate();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}