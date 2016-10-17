import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WhiteboardCallbackImpl extends UnicastRemoteObject implements WhiteboardCallback{
    public WhiteboardCallbackImpl() throws RemoteException {
        super();
    }

    @Override
    public void callBack(int version) throws RemoteException {
        String returnMessage = "Someone has added a new shape, we now have a new version number! v" + version + ".0";
        System.out.println(returnMessage);
    }
}
