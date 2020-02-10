import java.util.Scanner;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GPA extends Thread {
    public static void main(String[] args) throws ClassNotFoundException, IOException   {
    	try
		{
			Socket s = new Socket("127.0.0.1",3000);
			DataInputStream reader = new DataInputStream(s.getInputStream());
			DataOutputStream writer = new DataOutputStream(s.getOutputStream());
			Scanner sc=new Scanner(System.in);
			while(true)
			{
				System.out.print(reader.readUTF());
				writer.writeInt(sc.nextInt());
				String mesg=reader.readUTF();
				if(mesg.equals("\nStudent info")) {
					System.out.print(mesg);
					System.out.print(reader.readUTF());
					int choice=sc.nextInt();
					writer.writeInt(choice);
					if(choice==3)
					{
		    			System.out.println(reader.readUTF());
		    			s.close();
		    			break;
					}
					do {
						System.out.print(reader.readUTF());
						writer.writeInt(sc.nextInt());
						System.out.print(reader.readUTF());
						writer.writeFloat(sc.nextFloat());
						System.out.print(reader.readUTF());
						choice=sc.nextInt();
						writer.writeInt(choice);
    			        if(choice==3){
    			        	System.out.println(reader.readUTF());
    		    			s.close();
    		    			break;
    	                }
					}while(choice==1);
					System.out.print(reader.readUTF());
					System.out.print(reader.readUTF());
					System.out.print(reader.readUTF());
					System.out.println(reader.readUTF());
	    			s.close();
				}
				else {
					System.out.println(mesg);
				}
			}
		}
		catch(Exception e)
		{
		}  
    }  	
}
