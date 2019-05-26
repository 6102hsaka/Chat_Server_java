import java.io.*;
import java.util.*;
import java.net.*;


class Client
{
	public static void main(String[] args) throws Exception{
	        Socket s = new Socket("localhost",1234);
	        Scanner sc = new Scanner(System.in);
	        DataInputStream dis = new DataInputStream(s.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                while(true)
                {
        	       System.out.println("1.chat 2.Calculation 3.Exit");
        	       String send = sc.nextLine();
        	       dos.writeUTF(send);
        	       if(send.equals("3"))
        	       {
        		      break;
        	       }
        	       else if(send.trim().equals("1"))
        	       {
        		
        		      send = sc.nextLine();
        		      dos.writeUTF(send);	
        	       }
        	       else if(send.trim().equals("2"))
        	       {
				send = sc.nextLine();
        		      dos.writeUTF(send);	
        	       }
        	       String receive = dis.readUTF();
        	       System.out.println("Server : "+receive);
                }


                dis.close();
                dos.close();
                s.close();
	}
}
