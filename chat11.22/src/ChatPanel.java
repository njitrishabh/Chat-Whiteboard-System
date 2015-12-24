import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatPanel extends JPanel
{
    // Otherclass objects
        JTextField tf;
	JTextArea ta;
	JLabel um;
        Client obj;
	String name;
	
	public ChatPanel()
	{
	}
        
	public ChatPanel(final Client container)
	{
            // Variable Declarations
            
			this.obj = container;
			setLayout(new BorderLayout());
			
			
			ta = new JTextArea();
                        ta.setEditable(false);
                        JScrollPane tasp = new JScrollPane(ta);
                        
			um = new JLabel("Your conversation will appear below:");
			um.setForeground(new java.awt.Color(64,64,64));
			ta.setToolTipText("Type your message in the given field below and press enter to send");

                        tf = new JTextField();
                        tf.setToolTipText("Type your message and hit enter to send");
			tf.addActionListener(new ActionListener()
					   {
				public void actionPerformed(ActionEvent ae)
						{
							String text = tf.getText();
							tf.setText("");
							StringName sn = new StringName();
							sn.setName(name);
							final StringMessage sm = new StringMessage();
							sm.setMessage(text);
							container.sendMessage(sm);
                                                        tf.setToolTipText("");
						}
				
					   });
                        
			add(tf, BorderLayout.SOUTH);
			add(um, BorderLayout.NORTH);
			add(tasp, BorderLayout.CENTER);
	
	}

// Functions and Dimensions

	public void appendMessage(String m)
		{
		ta.append(m+"\n");
		}
	public Dimension getPreferredSize()
		{
		return new Dimension(450,150);
		}
	public Dimension getMinimumSize()
		{
		return new Dimension(450,150);
		}
}