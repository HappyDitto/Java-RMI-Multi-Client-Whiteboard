import javax.swing.*;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

public class Client {
    private String address;
    private int port;
    private String username;
    private String uid;
    private boolean isManager;
    private ClientGUI ui;
    private DrawRemoteInterface drawRemoteInterface;
    private ServerRemoteInterface serverRemoteInterface;
    private ClientRemoteInterface remoteClient;

    public Client(String service, String address, int port ,String username) {
        this.address = address;
        this.port = port;
        this.uid = UUID.randomUUID().toString();
        this.username = username;
        this.isManager = service.equals("CreateWhiteboard");
    }

    public void initRMI() {
        try {
            Registry registry = LocateRegistry.getRegistry(this.port);
            drawRemoteInterface = (DrawRemoteInterface) registry.lookup("rmi://"+address+"/draw");
            serverRemoteInterface = (ServerRemoteInterface) registry.lookup("rmi://"+address+"/server");
            remoteClient = new ClientRemoteImplementation(uid, username, this);
        } catch (Exception e) {
            System.out.println("RMI connection fail");
            System.exit(-1);
        }
    }

    public void initGUI() {
        this.ui = new ClientGUI(this);
    }

    public ClientGUI getGUi() {
        return ui;
    }

    public String getUid() {
        return uid;
    }

    public void setRemoteGUI(JFrame gui) {
        try {
            remoteClient.setGui(gui);
        } catch (RemoteException exception) {
            System.out.println("RMI connection fail, please restart program");
            System.exit(-1);
        }
    }

    public DrawRemoteInterface getDrawRemoteObject() {
        return drawRemoteInterface;
    }

    public int sendRequest(String service) {
        try {
            return serverRemoteInterface.request(remoteClient, service);
        } catch (RemoteException exception) {
            System.out.println("RMI connection fail");
            System.exit(-1);
        }
        return UserConstants.REJECTED;
    }

    public void parseResult(int result) {
        switch (result) {
            case UserConstants.REJECTED -> {
                System.out.println("Manager rejected your request");
                System.exit(0);
            }
            case UserConstants.MANAGER_EXIST -> {
                System.out.println("There has already been a manager");
                System.exit(0);
            }
            case UserConstants.MANAGER_NOT_EXIST -> {
                System.out.println("Cannot find the whiteboard, try CreateWhiteboard to create one");
                System.exit(0);
            }
        }
    }

    public void setRemoteUserList(JList userList) {
        try {
            remoteClient.setList(userList);
        } catch (RemoteException exception) {
            System.out.println("RMI connection fail, please restart program");
            System.exit(-1);
        }
    }

    public void setRemoteTextPane(JTextPane textPane) {
        try {
            remoteClient.setTextPane(textPane);
        } catch (RemoteException exception) {
            System.out.println("RMI connection fail, please restart program");
            System.exit(-1);
        }
    }

    public void closeWindow() {
        try {
            if (isManager) {
                ui.getClientInterface().setVisible(false);
                drawRemoteInterface.clear();
                serverRemoteInterface.managerClose();
            } else {
                serverRemoteInterface.userClose(uid);
            }
        } catch (RemoteException e) {
            //e.printStackTrace();
        }
    }
    public boolean isManager() {
        return isManager;
    }

    public void kickoff(String uid) {
        try {
            if (!uid.equals(this.uid))
                serverRemoteInterface.kickoff(uid);
        } catch (RemoteException ignored) {

        }
    }

    public void showPopup(String message) {
        JOptionPane.showMessageDialog(ui.getClientInterface(), message);
    }

    public void sendMessage(String text) {
        String message = username + ": " + text  + "\n";
        try {
            serverRemoteInterface.sendMessage(message);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(ui.getClientInterface(),"Cannot connect to server");
        }
    }

    public static void main(String[] args) {
        String service = args[0];
        if (!service.equals("JoinWhiteboard") && !service.equals("CreateWhiteboard")) {
            System.out.println("Service Invalid");
            System.exit(-1);
        }
        String host = args[1];
        int port = Integer.parseInt(args[2]);
        if (port <= 1024 || port >= 49151) {
            System.out.println("Port Number Invalid");
            System.exit(-1);
        }
        String username = args[3];

        Client client = new Client(service, host, port, username);
        client.initRMI();
        client.initGUI();
        client.setRemoteGUI(client.getGUi().getClientInterface());
        client.setRemoteUserList(client.getGUi().getUserList());
        client.setRemoteTextPane(client.getGUi().getChatWindow());
        int result = client.sendRequest(service);
        client.parseResult(result);
        client.getGUi().run();
    }
}
