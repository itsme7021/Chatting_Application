package chatting.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Server_window implements ActionListener {
    static JFrame f1 = new JFrame();
    private JLabel l1 , l2;
    private JPanel j1;
    JTextField t1;
    static JPanel t2;
    private JButton b1;
    static Box ver = Box.createVerticalBox();  //to take msg in a vertical box with sent msgs on the right and received ones on the left
    static ServerSocket ss; //there is gonna only one server and else are sockets
    static Socket s2;
    static DataInputStream d1;
    static DataOutputStream d2;
    boolean typing;

    Server_window()
    {
        //as we want our head color to be different from the rest of our window thus we have to create different panels
        j1 = new JPanel();
        j1.setLayout(null);
        j1.setBounds(0 ,0 , 450 , 70);  // as to be on Top
        j1.setBackground(new Color(7 , 94 , 84));
        f1.add(j1);
        //this is used to add icons to a frame, this gives the system resources
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("chatting/app/icons/3.png"));
        //i1 is used for the back icon on our head
        Image i2 = i1.getImage().getScaledInstance(30 , 30 , Image.SCALE_DEFAULT); //to adjust the image

        //now label doesnt take Image as parameter thus have to convert it into image icon
        ImageIcon i3 = new ImageIcon(i2);
        //icons cant be directly added to the frame so they has to added in a label first
        l1 = new JLabel(i3);

        //default layout is border layout we wish to create our own layout thus
        f1.setLayout(null);
        l1.setBounds(5 , 17 , 30 , 30);

        //now we are adding functionality to the back icon using mouse functions such that if back is pressed it exits the window
        l1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        j1.add(l1); //to add the component to our frame

        //adding the second icon
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("chatting/app/icons/1.png"));
        Image i5 = i4.getImage().getScaledInstance(60 , 60 , Image.SCALE_DEFAULT); //to adjust the image
        ImageIcon i6 = new ImageIcon(i5);
        l2 = new JLabel(i6);

        l2.setBounds(40 , 5 , 60 , 60);
        j1.add(l2); //to add the component to our frame

        //adding the 3rd icon
        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("chatting/app/icons/video.png"));
        Image i8 = i7.getImage().getScaledInstance(30 , 30 , Image.SCALE_DEFAULT); //to adjust the image
        ImageIcon i9 = new ImageIcon(i8);
        JLabel l4 = new JLabel(i9);

        l4.setBounds(305 , 20 , 30 , 30);
        j1.add(l4); //to add the component to our frame

        //adding the 4th icon
        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("chatting/app/icons/phone.png"));
        Image i11 = i10.getImage().getScaledInstance(35 , 30 , Image.SCALE_DEFAULT); //to adjust the image
        ImageIcon i12 = new ImageIcon(i11);
        JLabel l5 = new JLabel(i12);

        l5.setBounds(360 , 20 , 35 , 30);
        j1.add(l5); //to add the component to our frame

        //adding the 5th icon
        ImageIcon i13= new ImageIcon(ClassLoader.getSystemResource("chatting/app/icons/3icon.png"));
        Image i14 = i13.getImage().getScaledInstance(12 , 25 , Image.SCALE_DEFAULT); //to adjust the image
        ImageIcon i15 = new ImageIcon(i14);
        JLabel l6 = new JLabel(i15);

        l6.setBounds(410 , 20 , 13 , 25);
        j1.add(l6); //to add the component to our frame

        //adding name
        JLabel l3 = new JLabel("Gaitonde");
        l3.setFont(new Font("SAN_SERIF" , Font.BOLD ,18 ));
        l3.setBounds(110 , 15 , 100 , 18);
        l3.setForeground(Color.white); //used to set the colour of the particular label
        j1.add(l3);

        //adding active now status
        JLabel l7 = new JLabel("Active Now");
        l7.setFont(new Font("SAN_SERIF" , Font.PLAIN ,14 ));
        l7.setBounds(110 , 40 , 100 , 20);
        l7.setForeground(Color.white); //used to set the colour of the particular label
        j1.add(l7);

        Timer t = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!typing)
                {
                    l7.setText("Active Now");
                }
            }
        });

        t.setInitialDelay(2000);

        //making a text field to type our message and setting its font too
        t1 = new JTextField();
        t1.setBounds(5 , 655 , 310 , 38);
        t1.setFont( new Font("SAN_SERIF" , Font.PLAIN , 18));
        f1.add(t1);

        t1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                l7.setText("Typing...");
                t.stop();
                typing = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                typing= false;
                if(!t.isRunning())
                {
                    t.start();
                }
            }
        });

        //making a send button
        b1 = new JButton("Send");
        b1.setFont(new Font("SAN_SERIF" , Font.BOLD , 16));
        b1.setForeground(Color.white);
        b1.setBounds(320 , 655 , 125 , 38);
        b1.setBackground(new Color(7 , 94 , 84));
        b1.addActionListener(this);  //adding functionality to the send button by actionperformed
        f1.add(b1);

        //making text area to display messages
        t2 = new JPanel();
        t2.setBounds(5 , 75 , 440 , 575);
        t2.setFont(new Font("HELVETICA_NEUE_75_BOLD" , Font.PLAIN , 16));
        f1.add(t2);

        f1.setBounds(400 , 200 , 450 , 700);
        f1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f1.setUndecorated(true);  //this removes the window header that is the minimize close and other option on top of window as a bar
        f1.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae)
    {
        try {
            String s = t1.getText();
            JPanel p2 = format(s);

            //now our msg is getting in middle so to print it on right hand side
            t2.setLayout(new BorderLayout());
            JPanel p4 = new JPanel();
            p4.setLayout(new BorderLayout());
            p4.add(p2 , BorderLayout.LINE_END);
            ver.add(p4);
            ver.add(Box.createVerticalStrut(15)); //to add space between two messages
            t2.add(ver , BorderLayout.PAGE_START);   //gonna add at the start of the page
            d2.writeUTF(t1.getText());
            t1.setText("");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    //we are gonna append panels in panel for much different and better look
    public static JPanel format(String s)
    {
        JLabel j = new JLabel("<html><p style = \"width : 150px\">"+s+"</p></html>");   //we are using html for line break if the text is too big
        j.setFont(new Font("HELVETICA_NEUE_75_BOLD" , Font.PLAIN , 16));
        j.setBackground(new Color(37 , 211 , 102));
        j.setOpaque(true);
        j.setBorder(new EmptyBorder(15 , 15 ,15 ,50));   //gonna make a border around our panel of green colour

        //gonna add time to our msg

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat("HH:MM");

        JLabel j2 = new JLabel(sd.format(c.getTime()));
        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3 , BoxLayout.Y_AXIS));   //gonna add layout to it for better visual along y axis

        p3.add(j);
        p3.add(j2);

        return p3;
    }

    public static void main(String[] args) {
        new Server_window().f1.setVisible(true);
        String msg = "";
        try {
            ss = new ServerSocket(600109);   //it takes port as input and it can be anything until its not occupied
            while (true) {
                s2 = ss.accept();
                d1 = new DataInputStream(s2.getInputStream());
                d2 = new DataOutputStream(s2.getOutputStream());

                while(true)
                {
                    //this is gonna read the message from the client
                    msg = d1.readUTF();
                    JPanel j2 = format(msg);
                    JPanel p5 = new JPanel();
                    p5.setLayout(new BorderLayout());
                    p5.add(j2 , BorderLayout.LINE_START);
                    ver.add(p5);
                    f1.validate();   //as whenever we make a panel we have to refresh our window in order to show it and validate method does the same
                }
            }
        }
        catch (Exception e)
        {
        }
    }
}
