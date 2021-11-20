import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ServerRemoteInterface extends Remote {
    int request(ClientRemoteInterface remote, String service) throws RemoteException;
    void managerClose() throws RemoteException;
    void userClose(String uid) throws RemoteException;
    void kickoff(String uid) throws RemoteException;
    void sendMessage(String text) throws RemoteException;
}
