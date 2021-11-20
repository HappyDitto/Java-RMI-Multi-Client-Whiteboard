import javax.swing.*;
import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DrawRemoteInterface extends Remote {

    ImageIcon getCanvas() throws RemoteException;

    void drawText(String t, int x, int y, Color color) throws RemoteException;

    void drawRectangle(int x, int y, int width, int height, Color color) throws RemoteException;

    void drawLine(int x1, int y1, int x2, int y2, Color color) throws RemoteException;

    void drawCircle(int x, int y, int radius, Color color) throws RemoteException;

    void drawOval(int x, int y, int width, int height, Color color) throws RemoteException;

    void clear() throws RemoteException;
}
