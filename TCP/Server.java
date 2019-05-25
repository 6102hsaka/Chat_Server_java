import java.io.*;
import java.util.*;
import java.net.*;


class Server 
{
	static int count=0;


	public static void main(String[] args) throws Exception{
		ServerSocket ss = new ServerSocket(1234);
		

		while(true)
		{

			Socket s = ss.accept();
			System.out.println("New Client added");
			ClientHandler ch = new ClientHandler(s,"client "+count);
			ch.start();
			count++;

		}
		ss.close();
	}
}

class ClientHandler extends Thread
{
	Socket s;
	String name;

	public ClientHandler(Socket s,String name)
	{
		this.s = s;
		this.name = name;
	}

	public void run()
	{
		try
		{
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dout =  new DataOutputStream(s.getOutputStream());
			Scanner sc = new Scanner(System.in);
			String receive = "";
			while(true)
			{
				receive = dis.readUTF();
				
				if(receive.trim().equals("3"))
				{
					Server.count--;break;
					
				}
				else if(receive.trim().equals("2"))
				{
					receive = dis.readUTF();
					System.out.println("calculating..");
					String[] ar = receive.split(" ");
					double db1 = Double.parseDouble(ar[0]);
					double db2 = Double.parseDouble(ar[2]);
					if(ar[1].trim().equals("+"))
					{
						dout.writeUTF(Double.toString(db1+db2));
						dout.flush();
					}
					if(ar[1].trim().equals("-"))
					{
						dout.writeUTF(Double.toString(db1-db2));
						dout.flush();
					}
					if(ar[1].trim().equals("*"))
					{
						dout.writeUTF(Double.toString(db1*db2));
						dout.flush();
					}
					if(ar[1].trim().equals("/"))
					{
						dout.writeUTF(Double.toString(db1/db2));
						dout.flush();
					}
					if(ar[1].trim().equals("%"))
					{
						dout.writeUTF(Double.toString(db1%db2));
						dout.flush();
					}

				}
				else
				{
					receive = dis.readUTF();
					System.out.println(name+" : "+receive);
					String send = sc.nextLine();
					dout.writeUTF(send);
					dout.flush();
				}
				
			}
			

			s.close();
			dis.close();
			dout.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}