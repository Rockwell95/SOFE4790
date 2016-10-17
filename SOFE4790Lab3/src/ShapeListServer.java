import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ShapeListServer {
	public static void main(String args[]) {
		//args[0]: hostname of server
		String host = args[0];
		try {
			System.setProperty("java.rmi.server.hostname", host);
			ShapeList aList = new ShapeListServant();
			//ShapeList stub = (ShapeList) UnicastRemoteObject.exportObject(aList, 0);
			
			// Bind the remote object's stub in the registry 
			Registry registry;
			try {
				registry = LocateRegistry.getRegistry();
				registry.list();
			}catch (RemoteException e){
				System.out.println("RMI registry cannot be located at port "
					+ Registry.REGISTRY_PORT);
				registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
				System.out.println("RMI registry created at port "
					+ Registry.REGISTRY_PORT);
			}

			registry.rebind("ShapeList", aList);
			System.out.println("ShapeListServer ready");
		}catch(Exception e){
			System.err.println("ShapeListServer exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
