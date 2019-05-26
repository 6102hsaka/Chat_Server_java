import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.*;


class Server 
{
	// number of active clients
	static int count=0;

	//add client
	static void add()
	{
		count++;
	}

	//remove client
	static void remove()
	{
		count--;
		if(count==0)
		{
			try
			{
				// wait for 5 sec to check whether a new client is added or not	
				Thread.sleep(5000);
				if(count==0)
					{
						System.out.println("No client online");
						System.exit(0);
					}
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception{

		// at max 7 client can communicate simultaneously
		ExecutorService service = Executors.newFixedThreadPool(7);
		int index = 1;
	
		try(ServerSocket ss = new ServerSocket(1234))
		{
			while(true)
			{

				Socket s = ss.accept();
				add();
				System.out.println("New Client added ");
				System.out.println("Active client count : "+count);
				System.out.println();
				ClientHandler ch = new ClientHandler(s,"client "+index);
				service.execute(ch);
				index++;
			}
		}
		
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
					Server.remove();
					break;
					
				}
				else if(receive.trim().equals("2"))
				{
					receive = dis.readUTF();
					System.out.println("calculated..");
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

