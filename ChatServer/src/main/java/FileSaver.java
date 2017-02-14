

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileSaver extends Remote{
	public void add(String author, String message) throws RemoteException;
	
	public String[] getAll() throws RemoteException;
}
