import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class ShapeListServant extends UnicastRemoteObject implements ShapeList {
	private static final long serialVersionUID = 1L;
	private Vector<Shape> theList;
	private int version;
	private HashMap<Integer, WhiteboardCallback> callbacks = new HashMap<>();
	private ArrayList<Integer> usedInts = new ArrayList<>();

	public ShapeListServant() throws RemoteException{
		theList = new Vector<Shape>();
		version = 0;
	}

	public Shape newShape(GraphicalObject g) throws RemoteException{
		version++;
		Shape s = new ShapeServant(g, version);
		theList.addElement(s);
		for (Integer key : callbacks.keySet()) {
			WhiteboardCallback clientCallback = callbacks.get(key);
			clientCallback.callBack(version);
		}
		return s;
	}

	public Vector<Shape> allShapes() throws RemoteException{
		return theList;
	}

	public int getVersion() throws RemoteException{
		return version;
	}

	@Override
	public int register(WhiteboardCallback callback) throws RemoteException {
		int callBackId = 0;
		if(usedInts.size() > 0){
			callBackId = usedInts.get(usedInts.size() - 1) + 1; // will default to 0
		}

		usedInts.add(callBackId);
		callbacks.put(callBackId, callback);
		System.out.println("Successfully registered callback with reference \"" + callback + "\" with callback ID " + callBackId + ".");
		return callBackId;
	}

	@Override
	public int unregister(int callbackId) throws RemoteException {
		WhiteboardCallback callback = callbacks.remove(callbackId);
		boolean unregistered = usedInts.remove(Integer.valueOf(callbackId)); // Deletes  callbackId from the list, not the int at index callbackId
		if(unregistered){
			System.out.println("Successfully unregistered callback with reference \"" + callback + "\" with callback ID " + callbackId + ".");
			return 0;
		}
		else{
			System.err.println("Error unregistering callback with reference \"" + callback + "\" with callback ID " + callbackId + ".");
			return -1; //UNREGISTER ERROR
		}
	}
}
