import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import static javax.swing.JOptionPane.*;

public class ServerRemoteImplementation extends UnicastRemoteObject implements ServerRemoteInterface {
    private HashMap<String, ClientRemoteInterface> userMap;
    private String managerUID;
    private String chat;
    private DrawRemoteInterface remoteDraw;

    protected ServerRemoteImplementation(DrawRemoteInterface remoteDraw) throws RemoteException {
        userMap = new HashMap<>();
        managerUID = "";
        chat = "";
        this.remoteDraw = remoteDraw;
    }

    private synchronized int join(ClientRemoteInterface client, boolean accept) throws RemoteException {
        if (accept) {
            userMap.put(client.getUid(), client);
            System.out.println("Peer Join");
            updateUserList();
            client.setChatMessage(chat);
            return UserConstants.OK;
        }
        return UserConstants.REJECTED;
    }

    private synchronized int create(ClientRemoteInterface client) throws RemoteException {
        if (managerUID.equals("")) {
            String uid = client.getUid();
            userMap.put(uid, client);
            System.out.println("Manager create");
            managerUID = uid;
            updateUserList();
            return UserConstants.OK;
        }
        return UserConstants.MANAGER_EXIST;
    }

    @Override
    public int request(ClientRemoteInterface client, String service) throws RemoteException {
        if (service.equals("CreateWhiteboard")) {
            return create(client);
        } else if (service.equals("JoinWhiteboard")) {
            if (managerUID.equals("")) {
                return UserConstants.MANAGER_NOT_EXIST;
            }
            client.print("Waiting for manager to approve");
            ClientRemoteInterface manager = userMap.get(managerUID);
            int result = manager.onRequest(client.getUsername());
            return join(client, result != NO_OPTION);
        }
        return UserConstants.REJECTED;
    }

    @Override
    public synchronized void managerClose() throws RemoteException {
        userMap.remove(managerUID);
        managerUID = "";

        UnicastRemoteObject.unexportObject(this, true);
        UnicastRemoteObject.unexportObject(remoteDraw, true);
//        for (int i = userMap.size(); i > 0;) {
//            String uid = (String) userMap.keySet().toArray()[0];
//            ClientRemoteInterface client = userMap.remove(uid);
//            //client.onManagerClose();
//            i = userMap.size();
//        }
        userMap.clear();
    }

    @Override
    public synchronized void userClose(String uid) throws RemoteException {
        userMap.remove(uid);
        updateUserList();
    }

    @Override
    public synchronized void kickoff(String uid) throws RemoteException {
        ClientRemoteInterface client = userMap.get(uid);
        userMap.remove(uid);
        updateUserList();
        client.onKickoff();
    }

    @Override
    public synchronized void sendMessage(String text) throws RemoteException {
        chat += text;
        broadcastChat(chat);
    }

    private void broadcastChat(String text) throws RemoteException {
        for (ClientRemoteInterface client:
                userMap.values()) {
            client.setChatMessage(text);
        }
    }

    private ArrayList<String> getUserList() throws RemoteException {
        ArrayList<String> usernames = new ArrayList<>();
        for (ClientRemoteInterface client:
             userMap.values()) {
            usernames.add(client.getUsername()+"@"+client.getUid());
        }
        return usernames;
    }

    private void updateUserList() throws RemoteException {
        for (ClientRemoteInterface client:
                userMap.values()) {
            client.updateUserList(getUserList());
        }
    }

}
