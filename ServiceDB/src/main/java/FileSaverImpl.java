
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileSaverImpl extends UnicastRemoteObject implements FileSaver {

	private static final long serialVersionUID = 2342341L;
	private String PATH = "messagesHistory.log";

	protected FileSaverImpl() throws RemoteException {
		super();
	}

	public void add(String author, String message) {
		try {
			StringBuilder line = new StringBuilder();
			line.append(author);
			line.append(": ");
			line.append(message);
			line.append("\n");

			Files.write(Paths.get(PATH), line.toString().getBytes(), StandardOpenOption.CREATE,
					StandardOpenOption.APPEND);
			System.out.println("entry saved");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String[] getAll() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
