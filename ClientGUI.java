import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

import static javax.swing.SwingUtilities.isRightMouseButton;

public class ClientGUI {
    private JButton line;
    private JButton circle;
    private JButton oval;
    private JButton rectangle;
    private JButton text;
    private JTextPane chatWindow;
    private JTextField chatInput;
    private JButton color;
    private JPanel canvasPanel;
    private JScrollPane userListPanel;
    private JToolBar toolbar;
    private JPanel content;
    private JList userList;
    private JButton Send;

    private Client client;

    private JFrame clientInterface;

    public ClientGUI(Client client){
        this.client = client;
        initGUI();
    }

    public void run() {
        EventQueue.invokeLater(() -> {
            try {
                clientInterface.pack();
                clientInterface.setVisible(true);
            } catch (Exception e) {
                System.out.println("Error when initialize GUI, please try again");
            }
        });
    }

    public JFrame getClientInterface() {
        return clientInterface;
    }

    public JList getUserList() {
        return userList;
    }

    public JTextPane getChatWindow() {
        return chatWindow;
    }

    public void initGUI(){
        this.clientInterface = new JFrame();
        clientInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientInterface.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.closeWindow();
            }
        });
        clientInterface.setContentPane(content);

        canvasPanel.setLayout(new GridLayout());
        WhiteBoardCanvas canvas = new WhiteBoardCanvas(client.getDrawRemoteObject(), client);
        canvasPanel.add(canvas);

        line.addActionListener(e -> canvas.setOption(GUIConstants.LINE));
        rectangle.addActionListener(e -> canvas.setOption(GUIConstants.RECTANGLE));
        circle.addActionListener(e -> canvas.setOption(GUIConstants.CIRCLE));
        oval.addActionListener(e -> canvas.setOption(GUIConstants.OVAL));
        text.addActionListener(e -> canvas.setOption(GUIConstants.TEXT));
        color.addActionListener(e -> {
                Color color = JColorChooser.showDialog(content,
                        "Choose a color", null);
                if (color != null) {
                    canvas.setColor(color);
                }
        });

        userList.addMouseListener( new MouseAdapter()
        {
            public void mousePressed(MouseEvent e) {
                if (!client.isManager()) {
                    return;
                }
                if (isRightMouseButton(e) ) {
                    String user = (String) userList.getSelectedValue();
                    System.out.println(user);
                    if(user != null){
                        // trigger kick off
                        String uid = user.substring(user.indexOf("@")+1);
                        client.kickoff(uid);
                    }
                }
            }
        });

        Send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = chatInput.getText();
                if (text != null)
                    client.sendMessage(text);
            }
        });
    }
}
