

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DBServerMain {

	public static void main(String[] args) throws RemoteException {
		Registry reg = LocateRegistry.createRegistry(9999);
		FileSaver saver = new FileSaverImpl();
		reg.rebind("toFile", saver);
		System.err.println("Data base service started");
	}

}
