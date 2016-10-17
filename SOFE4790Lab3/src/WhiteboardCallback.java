import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WhiteboardCallback extends Remote {
    void callBack(int version) throws RemoteException;
}
