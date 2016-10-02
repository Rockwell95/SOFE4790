import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Election extends Remote{
    boolean vote(String candidate, int voterNum) throws RemoteException, FileNotFoundException, UnsupportedEncodingException;

    int result(String candidate, int numOfVotes) throws IOException;
}
