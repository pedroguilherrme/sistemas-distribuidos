import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.Objects;

public class Note extends UnicastRemoteObject implements NoteRemote {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
    private String title;
    private String content;
    private long documentId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Note() throws RemoteException {
        super();
    }

    public Note(String title, String content, long documentId) throws RemoteException {
    	super();
    	
        this.title = title;
        this.content = content;
        this.documentId = documentId;
    }

    public void createNote(Connection connection) throws RemoteException, SQLException {
        String query = "INSERT INTO public.notes(title, content, document_id, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, this.title);
            preparedStatement.setString(2, this.content);
            preparedStatement.setLong(3, this.documentId);
            preparedStatement.setTimestamp(4, getCurrentTimestamp());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getLong(1);
                }
            }
        }
    }

    public Note getNoteById(Connection connection, long noteId) throws RemoteException, SQLException {
        String query = "SELECT * FROM public.notes WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, noteId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Note note = new Note();
                    note.setId(resultSet.getLong("id"));
                    note.setTitle(resultSet.getString("title"));
                    note.setContent(resultSet.getString("content"));
                    note.setDocumentId(resultSet.getLong("document_id"));
                    note.setCreatedAt(resultSet.getTimestamp("created_at"));
                    note.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                    return note;
                }
            }
        }
        return null;
    }

    public void updateNote(Connection connection) throws RemoteException, SQLException {
        String query = "UPDATE public.notes SET title = ?, content = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, this.title);
            preparedStatement.setString(2, this.content);
            preparedStatement.setLong(3, this.id);
            preparedStatement.executeUpdate();
        }
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(long documentId) {
        this.documentId = documentId;
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

    private Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id && documentId == note.documentId && title.equals(note.title) && content.equals(note.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, documentId);
    }
}
