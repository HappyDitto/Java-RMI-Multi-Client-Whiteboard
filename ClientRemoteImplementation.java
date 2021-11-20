import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import static javax.swing.JOptionPane.YES_NO_OPTION;

public class ClientRemoteImplementation extends UnicastRemoteObject implements ClientRemoteInterface {
    private JFrame gui;
    private JList list;
    private JTextPane textPane;
    private String uid;
    private String username;
    private Client client;

    public ClientRemoteImplementation(String uid, String username, Client client) throws RemoteException {
        this.uid = uid;
        this.username = username;
        this.client = client;
    }

    @Override
    public void setList(JList list) throws RemoteException {
        this.list = list;
    }

    @Override
    public void setTextPane(JTextPane pane) throws RemoteException {
        this.textPane = pane;
    }

    @Override
    public void setChatMessage(String message) throws RemoteException {
        this.textPane.setText(message);
    }

    @Override
    public void setGui(JFrame gui) throws RemoteException {
        this.gui = gui;
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public String getUsername() throws RemoteException {
        return username;
    }

    public int onRequest(String username) throws RemoteException {
        return JOptionPane.showConfirmDialog(gui,"User " + username +
                " want to join the canvas", "Request", YES_NO_OPTION );
    }

    @Override
    public void print(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public void onManagerClose() throws RemoteException {
        client.showPopup("Manager has closed the canvas, all connection will be refused. " +
                "Please close the program");
    }

    @Override
    public void updateUserList(ArrayList<String> usernames) throws RemoteException {
        list.setListData(usernames.toArray());
        list.updateUI();
    }

    @Override
    public void onKickoff() throws RemoteException {
        JOptionPane.showMessageDialog(gui, "You are kicked off by the manager");
        System.exit(0);
    }
}
