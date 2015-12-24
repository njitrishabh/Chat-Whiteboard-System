import java.awt.event.*;
import javax.swing.*;

public class ControlPanel extends JPanel
{
    //Variable Declarations
	JButton b1, b2, b3, b4, b5,b6,b7;
	Client obj;
   	ChatPanel cp;
	DrawPanel dp;
	
	public ControlPanel(Client fd, ChatPanel cp1, DrawPanel dp1)
		{
		obj = fd;
		cp=cp1;
                dp = dp1;
		b1 = new JButton("Connect");
		
		b2 = new JButton("Disconnect");
                b3 = new JButton("Login");
		b4 = new JButton("Clear");
		b5 = new JButton("Send Drawing");
                b6 = new JButton("Save-Chat");
		b7 = new JButton("Load-Chat");
              

		b1.addActionListener(new ActionListener()
				   {
						public void actionPerformed(ActionEvent ae)
				   	{
						obj.connect();
					}
				   });
		b2.addActionListener(new ActionListener()
				   {
						public void actionPerformed(ActionEvent ae)
					{
						obj.close();
					}
				   });
		
		b3.addActionListener(new ActionListener()
				   {
	      					public void actionPerformed(ActionEvent ae)
					{
                                            final ChatMessage sn= new ChatMessage();
							
						String urname;
                                             urname= JOptionPane.showInputDialog("Enter username:");
                                             if(urname!=null)
                                             {
                                             sn.setName(urname);
                                             obj.sendMessage(sn);
                                             b3.setVisible(false);
                                             b2.setVisible(true);
                                             b4.setVisible(true);
                                             b5.setVisible(true);
                                             b6.setVisible(true);
                                             b7.setVisible(true);
                                            
                                             cp.setVisible(true);
                                             dp.setVisible(true);
                                             obj.SP_ONLINE.setVisible(true);
                                             obj.online.setVisible(true);
                                             }
					}
				   });

				
		b4.addActionListener(new ActionListener()
				   {
						public void actionPerformed(ActionEvent ae)
					{
						obj.clear();
					}
				   });
		b5.addActionListener(new ActionListener()
				   {
						public void actionPerformed(ActionEvent ae)
					{
						LineMessage lm = new LineMessage();
						lm.setLineMessage(obj.dp.linelist);
						obj.sendMessage(lm);
                                             
					}
				   });
		b6.addActionListener(new ActionListener()
				   {
						public void actionPerformed(ActionEvent ae)
					{
						obj.savechat();
					}
				   });
		b7.addActionListener(new ActionListener()
				   {
						public void actionPerformed(ActionEvent ae)
					{
                                            
						obj.loadchat();
					}
				   });
                
		
		add(b1);
		add(b2);
                add(b3);
		add(b4);
		add(b5);
		add(b6);
		add(b7);
               
		}
}

