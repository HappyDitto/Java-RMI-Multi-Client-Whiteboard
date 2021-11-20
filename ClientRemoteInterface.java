import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientRemoteInterface extends Remote {
    void setGui(JFrame gui) throws RemoteException;

    void setList(JList list) throws RemoteException;

    void setTextPane(JTextPane pane) throws RemoteException;

    void setChatMessage(String message) throws RemoteException;

    String getUid() throws RemoteException;

    String getUsername() throws RemoteException;

    int onRequest(String username) throws RemoteException;

    void print(String message) throws RemoteException;

    void onManagerClose() throws RemoteException;

    void updateUserList(ArrayList<String> usernames) throws RemoteException;

    void onKickoff() throws RemoteException;
}
