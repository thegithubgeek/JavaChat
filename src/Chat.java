import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Timer;

public class Chat {
	JFrame frame;
	JLabel chat;
	JScrollBar scroll;
	JTextField message;
	Socket chatSoc;
	public static int W = 500;
	public static int H = 500;
	Chat(){
		try{
			chatSoc = new Socket("127.0.0.1", 5000);
		}catch(Exception e){}
		frame = new JFrame("Chat'in' 4'ev'r");
		
		chat = new JLabel("Welcome");
		chat.setHorizontalAlignment(SwingConstants.CENTER);
		scroll = new JScrollBar();
		scroll.add(chat);
		
		message = new JTextField();
		
		frame.setPreferredSize(new Dimension(W, H + 50));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(chat, BorderLayout.CENTER);
		frame.add(message, BorderLayout.SOUTH);
		frame.pack();
		
		message.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					Runnable r = new Send();
					Thread t = new Thread(r);
					t.start();
				}
			}
		});
		Timer t = new Timer();
		t.schedule(new CheckChat(), 0, 100);
	}
	
	class CheckChat extends TimerTask{

		@Override
		public void run() {
			Runnable r = new GetChat();
			Thread t = new Thread(r);
			t.start();
		}
		
	}
	
	class GetChat implements Runnable{
		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(chatSoc.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				chat.setText(chat.getText() + br.readLine());
				br.close();
			} catch (Exception e) {}
		}
	}
	
	class Send implements Runnable{
		public void run() {
			try {
				PrintWriter pw = new PrintWriter(chatSoc.getOutputStream());
				pw.println(message.getText());
				pw.flush();
				message.setText("");
				pw.close();
			} catch (Exception e){}
		}
	}
}