package Server;

import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Server {
	

	public static boolean existe(String[] s , String user) {
		for (int  i=0; i< s.length; i++) {
			if(user.equals(s[i])) {
				return true ;
			}
		}
		
		return false;
	}

	
	
	public static void main(String[] args) {
		Connection conn ;
		String[] s ;
		String ss = "";
		String sql1;
		String user="";
		String id ="";
		String[] tabUser;
		OutputStream os;
		PrintWriter pw;
		ResultSet selectedUser,selectedIdByUserName,selectedMessage;
		
		
		
		// demarrage de serveur et attend le connesion de client 
		ServerSocket socketServeur;
		try {
			
			conn = (Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapp","root", "root") ;
			 Statement stmt = conn.createStatement();
			if(conn!=null) {
				System.out.println("base de donnée est connecté");
			}
			
			socketServeur = new ServerSocket(1235);
		
			System.out.println("Le serveur attend la connexion d'un client ");
		
	
		/*String sql = "create Table users(id INt primary key AUTO_INCREMENT,userName TEXT)";
         stmt.executeUpdate(sql);*/
		
		while(true) {
			Socket socket = socketServeur.accept();
			System.out.println("un client est connecté ");
			
			//_________ select all users from data base___________________ 
				sql1 = "SELECT userName From users";
				selectedUser = stmt.executeQuery(sql1);
				 String users ="userList%";
				 while (selectedUser.next()) {
				        users = users+ selectedUser.getString(1)+ "%";
				       
						
				 }
				System.out.println(users);
			//_________ Send all user to client ___________________________
		        os = socket.getOutputStream();
				pw = new PrintWriter(os, true);
				pw.println(users);
				
			//______ select all message ________
				sql1 = "SELECT message From messages ";
				selectedMessage = stmt.executeQuery(sql1);
				 String messages ="messages%";
				 while (selectedMessage.next()) {
					 messages = messages+ selectedMessage.getString(1)+ "%";
				 }
				System.out.println("message :"+messages);
		   //_______ send all message _________
			
				os = socket.getOutputStream();
				pw = new PrintWriter(os, true);
				pw.append(messages);
				pw.println(messages);
		   //__________ Receive username or message from client ___________
				InputStream is = socket.getInputStream();
				InputStreamReader isr= new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				ss= br.readLine();
				if(!(ss == null)) {
					s = ss.split(" ");
				
				System.out.println(s[1]);
				
				switch(s[0]) {
				case "username":
					sql1 = "SELECT id From users where userName='"+s[1]+"'";
					selectedIdByUserName = stmt.executeQuery(sql1);
					 
					 while (selectedIdByUserName.next()) {
					        id = selectedIdByUserName.getString(1);
					       
							
					 }
					System.out.println(id);
					tabUser = users.split("%");
					// verification user 
					if(!existe(tabUser,s[1]) ) {
						
						sql1 = "INSERT INTO users(userName) VALUES ('"+s[1]+"')";
						stmt.executeUpdate(sql1);
						break;
					}
					else {
						System.out.println("User already exist !");
					break;
					}
					
				case "message" :
					
					 sql1 = "INSERT INTO messages(message,senderId,receiverId) VALUES ('"+s[1]+"','"+id+"','1')";
					 stmt.executeUpdate(sql1);
				}
				
				System.out.println(s[1]);		
				}
				
		
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
