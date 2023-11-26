import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Document extends UnicastRemoteObject implements DocumentRemote {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
    private String title;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private long documentOwnerId;
    private long editingUserId;

    public Document() throws RemoteException {
        super();
    }

    public Document(String title, long documentOwnerId) throws RemoteException {
    	super();
        this.title = title;
        this.setDocumentOwnerId(documentOwnerId);
    }

    public void createDocument(Connection connection) throws RemoteException, SQLException {
        String query = "INSERT INTO public.documents(title, document_owner_id, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, this.title);
            preparedStatement.setLong(2, this.documentOwnerId);
            preparedStatement.setTimestamp(3, getCurrentTimestamp());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getLong(1);
                }
            }
        }
    }

    public Document getDocumentById(Connection connection, long documentId) throws RemoteException, SQLException {
        String query = "SELECT * FROM public.documents WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, documentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Document document = new Document();
                    document.setId(resultSet.getLong("id"));
                    document.setTitle(resultSet.getString("title"));
                    document.setCreatedAt(resultSet.getTimestamp("created_at"));
                    document.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                    return document;
                }
            }
        }
        return null;
    }

    public void updateDocument(Connection connection, long userId, List<Note> newNotes) throws RemoteException, SQLException {
        // Verificar se o documento est� sendo editado por outro usu�rio
        if (this.editingUserId != 0 && this.editingUserId != userId) {
            throw new SQLException("O documento est� sendo editado por outro usu�rio.");
        }

        // Marcar o documento como editado pelo usu�rio atual
        String markEditingQuery = "UPDATE public.documents SET editing_user_id = ? WHERE id = ?";
        try (PreparedStatement markEditingStatement = connection.prepareStatement(markEditingQuery)) {
            markEditingStatement.setLong(1, userId);
            markEditingStatement.setLong(2, this.id);
            markEditingStatement.executeUpdate();
        }

        // Atualizar o conte�do do documento
        String updateDocumentQuery = "UPDATE public.documents SET title = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement updateDocumentStatement = connection.prepareStatement(updateDocumentQuery)) {
            updateDocumentStatement.setString(1, this.title);
            updateDocumentStatement.setLong(2, this.id);
            updateDocumentStatement.setTimestamp(3, getCurrentTimestamp());
            updateDocumentStatement.executeUpdate();
        }

        // Adicionar novas notas ao documento
        if (newNotes != null && !newNotes.isEmpty()) {
            for (Note newNote : newNotes) {
                newNote.setDocumentId(this.id);
                newNote.createNote(connection);
            }
        }

        // Resetar o editing_user_id para indicar que a edi��o foi conclu�da
        String resetEditingQuery = "UPDATE public.documents SET editing_user_id = NULL WHERE id = ?";
        try (PreparedStatement resetEditingStatement = connection.prepareStatement(resetEditingQuery)) {
            resetEditingStatement.setLong(1, this.id);
            resetEditingStatement.executeUpdate();
        }
    }

    public List<Note> getNotes(Connection connection) throws RemoteException, SQLException {
        List<Note> notes = new ArrayList<>();
        String query = "SELECT * FROM public.notes WHERE document_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, this.id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Note note = new Note();
                    note.setId(resultSet.getLong("id"));
                    note.setTitle(resultSet.getString("title"));
                    note.setContent(resultSet.getString("content"));
                    note.setDocumentId(resultSet.getLong("document_id"));
                    note.setCreatedAt(resultSet.getTimestamp("created_at"));
                    note.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                    notes.add(note);
                }
            }
        }
        return notes;
    }

    public List<Document> getAllDocuments(Connection connection) throws RemoteException, SQLException {
        List<Document> documents = new ArrayList<>();
        String query = "SELECT * FROM public.documents";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Document document = new Document();
                document.setId(resultSet.getLong("id"));
                document.setTitle(resultSet.getString("title"));
                document.setCreatedAt(resultSet.getTimestamp("created_at"));
                document.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                documents.add(document);
            }
        }
        return documents;
    }

    public List<Document> getDocumentsByUserId(Connection connection, long userId) throws RemoteException, SQLException {
        List<Document> documents = new ArrayList<>();
        String query = "SELECT d.* FROM public.documents d " +
                       "JOIN public.users_documents ud ON d.id = ud.document_id " +
                       "WHERE ud.user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

	public long getDocumentOwnerId() {
		return documentOwnerId;
	}

	public void setDocumentOwnerId(long documentOwnerId) {
		this.documentOwnerId = documentOwnerId;
	}
	
	private Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}