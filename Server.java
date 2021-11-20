import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server {
    private int port;
    private String host;
    public DrawRemoteInterface drawRemoteMethods;
    public ServerRemoteInterface serverRemoteMethods;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void initRMI() {
        try {
            drawRemoteMethods = new DrawRemoteImplementation();
            serverRemoteMethods = new ServerRemoteImplementation(drawRemoteMethods);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("rmi://"+host+"/draw", drawRemoteMethods);
            registry.rebind("rmi://"+host+"/server", serverRemoteMethods);
            System.out.println("ok");
        } catch (RemoteException e) {
            System.out.println("RMI initialization Error");
        }
    }

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        if (port <= 1024 || port >= 49151) {
            System.out.println("Port Number Invalid");
            System.exit(-1);
        }

        Server server = new Server(host, port);
        server.initRMI();
    }
}
