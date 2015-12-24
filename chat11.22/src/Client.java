
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.Border;



public class Client extends JFrame implements Runnable
{
        
	//LoginPanel lp;
   	ChatPanel cp;
	DrawPanel dp;
        ControlPanel conp;
	
  	public static JPanel container = new JPanel();
        
        JList online;
        JScrollPane SP_ONLINE=new JScrollPane();
        
        boolean connected;
        Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
        public Client()
		{
		}
	
	public Client(String s)
		{
		super(s);
                online = new JList();
                
                online.setForeground(new java.awt.Color(64,64,64));
	 
                SP_ONLINE.setHorizontalScrollBarPolicy(
			 ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                SP_ONLINE.setVerticalScrollBarPolicy(
			 ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                SP_ONLINE.setViewportView(online);
	 
                SP_ONLINE.setBounds(350,90,130,180);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                cp = new ChatPanel(this);
		dp = new DrawPanel(this);
		conp = new ControlPanel(this,cp,dp);
		
                container.setLayout(new BorderLayout());
		container.add(cp, BorderLayout.WEST);
		container.add(dp, BorderLayout.EAST);
                container.add(SP_ONLINE,BorderLayout.CENTER);

		getContentPane().add(container, BorderLayout.CENTER);
                getContentPane().add(conp, BorderLayout.NORTH);
		
		//Display the window.
                cp.setVisible(false);
		dp.setVisible(false);
                setSize(1000,500);
		setLocationRelativeTo(null);
		conp.b2.setVisible(false);
                conp.b3.setVisible(false);
                conp.b4.setVisible(false);
                conp.b5.setVisible(false);
                conp.b6.setVisible(false);
                conp.b7.setVisible(false);
               
                dp.setVisible(false);
                SP_ONLINE.setVisible(false);
     		setVisible(true);
                conp.b1.setForeground(Color.red);
                conp.b2.setForeground(Color.red);
                conp.b3.setForeground(Color.red);
                conp.b4.setForeground(Color.red);
                conp.b5.setForeground(Color.red);
                conp.b6.setForeground(Color.red);
                conp.b7.setForeground(Color.red);
               
                SP_ONLINE.setForeground(Color.red);
                cp.ta.setForeground(Color.red);
                dp.setForeground(Color.black);
                }
	

	public void setConnected(boolean c)
		{
		connected = c;
		}
	public boolean isConnected()
		{
		return connected;
		}
	public void connect()
		{
                setConnected(true);
		System.out.println("Should connect now . . . ");
			try
			{
                        s = new Socket("localhost",4450);
			oos = new ObjectOutputStream(s.getOutputStream());
			new Thread(this).start();
			System.out.println("Connected . . . ");
			conp.b3.setVisible(true);
			conp.b1.setVisible(false);
                        }
			catch(IOException e)
			{
			System.out.println(e.getMessage());
			}	
		}
        
	public void close()
		{
		setConnected(false);	//should send last message so others can update user list
								// and remove self from shared arralist of handlers
			try
			{
                          oos.writeObject("remove");
                          oos.flush();
                          oos.close();
                          s.close();
                          JOptionPane.showMessageDialog(null,"You disconnected!");
                          System.out.println("Disconnected . . . ");
                          System.exit(0);
			}
			catch(IOException e)
			{
			System.out.println(e.getMessage());
			}
		}
	public void run()
		{
		try
			{
			ois = new ObjectInputStream(s.getInputStream());
			for(;;)
				{
					Object o = receiveMessage();
					if(o != null)
					{
                                        if(o.toString().contains("@#!"))
                                            {
                                                String TEMP1=o.toString().substring(3);
                                                TEMP1=TEMP1.replace("[","");
                                                TEMP1=TEMP1.replace("]","");
                                                String[] CurrentUsers=TEMP1.split(",");
                                                online.setListData(CurrentUsers);
                                            }
                                        else if(o instanceof String)
                                            {
                                                cp.appendMessage((String)o);
                                            }
                                        else if(o instanceof StringName)
                                            {
                                                StringName sn = (StringName)o;
						String s = (String)sn.getName();
						System.out.println(s);
                                                cp.ta.append(s+" joined the chat"+"\n");
                                            }
					else if(o instanceof ArrayList)
                                            {
                                                ArrayList<Line> message;
                                                message=(ArrayList)o;
                                                dp.linelist = message;
						dp.repaint();
                                            }
					else if(o instanceof UserMessage)
						{
					
						}
					}
					else
						{
						break;
						}
				}
                         
			}
		catch(FileNotFoundException e)
			{
			System.out.println(e.getMessage());
			}
		catch(IOException e)
			{
			System.out.println("IO Exception: " + e.getMessage());
			}
		finally
			{
				try
					{
						ois.close();
					}
				catch(IOException e)
					{
						System.out.println(e.getMessage());
					}
			}
		}
	
       
       
        public void savechat()
        {
            try
            {
                PrintWriter textout = new PrintWriter("Log.txt");
                FileOutputStream out = new FileOutputStream("DrawLog.txt");
                ObjectOutputStream oout = new ObjectOutputStream(out);
                textout.print(cp.ta.getText());
                oout.writeObject(dp.linelist);
                textout.close();
                oout.flush();
            }
            catch(IOException e)
            {
                System.out.println(e);
            }
        }
        public void loadchat()
        {
            
          try  
                {
                    Object myObject;
                BufferedReader br = new BufferedReader(new FileReader("Log.txt"));
             ObjectInputStream ois =
                 new ObjectInputStream(new FileInputStream("DrawLog.txt"));
            
             myObject = (Object)ois.readObject();
             
              ArrayList<Line> message;
                                                message=(ArrayList)myObject;
                                                dp.linelist = message;
						dp.repaint();
          
                
                String line = null;
             
                cp.ta.setText("");
           
                    while ((line = br.readLine()) != null) 
                        {
                            cp.ta.append(line + "\n");
                        }
                  
                }
           catch(IOException e)
                {
                System.out.println(e.getMessage());
                } catch (ClassNotFoundException ex) { 
                   Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
               } 
       
        }
	 final public void sendMessage(Object o)
		{
		
			if(isConnected())
			{
                            try
                                {
                                    if(o instanceof LineMessage) 
                                        {
					System.out.println("LineMessage written to stream");
                                        dp.setVisible(true);
                                        oos.writeObject(o);
                                        oos.flush();
                                        }
                                    else
                                        {
					oos.writeObject(o);
                                        oos.flush();}
                                        }
			catch(IOException e)
                                    {
				System.out.println(e.getMessage());
                                    }
                                }
		}
	public Object receiveMessage()
		{
	
			Object obj = null;
		try
			{
				obj = ois.readObject();
			}
		catch(IOException e)
			{
			System.out.println("End of stream.");
			}
		catch(ClassNotFoundException e)
			{
			System.out.println(e.getMessage());
			}
		return obj;
		}
	
	public void clear()
		{
		dp.linelist = new ArrayList<Line>();
		dp.repaint();
		cp.ta.setText("");
		}
	
    private static void createAndShowGUI() 
		{
       		Client frame = new Client("Chat System with Shared Whiteboard");   
    		}

    public static void main(String[] args) 
		{
       		 //Schedule a job for the event-dispatching thread:
       		 //creating and showing this application's GUI.
        
		javax.swing.SwingUtilities.invokeLater(new Runnable() 
						     {
            
							public void run() 
							{

				
                					createAndShowGUI();
            						}
        					      });
    		}
}
