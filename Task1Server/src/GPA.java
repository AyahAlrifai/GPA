import java.util.Scanner;
import java.sql.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ClientHandler extends Thread
{
    private final DataInputStream reader;
    private final DataOutputStream writer;
    private final Socket socket;
    Connection con;
    public ClientHandler(Socket socket,DataInputStream reader,DataOutputStream writer) throws ClassNotFoundException, SQLException
    {
        this.socket=socket;
        this.reader=reader;
        this.writer=writer;
        Class.forName("com.mysql.jdbc.Driver");
   	 	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/task1","root","");
    }
    @Override
    public void run()
    {
    	try {          
	        Scanner sc=new Scanner(System.in);
	        float CumulativeGPA,hourSum=0,SemesterGPA=0;
	        int CumulativeHours,choice;
		    do {    
	        	PreparedStatement stmt = con.prepareStatement("select * from student where id=?");
	        	writer.writeUTF("\nEnter your ID:");
		        int id=reader.readInt();
		        stmt.setInt(1, id);
		        ResultSet rs = stmt.executeQuery();
		        if(rs.next()) {
		            CumulativeHours=rs.getInt("passHours");
		            CumulativeGPA=rs.getFloat("gpa");
		        	writer.writeUTF("\nStudent info");
		        	writer.writeUTF("\nStudent ID:"+rs.getInt("id")+"\tStudent Name:"+rs.getString("name")+"\nCumulative GPA:"+rs.getFloat("gpa")+"\tCumulative Hours:"+rs.getInt("passHours")+"\n\nMenu\n1:Insert new mark\n2:Calculate GPA\n3:Exit\n.............?!");
			        choice=reader.readInt();
			        if(choice==3){
	                    writer.writeUTF("Bye");
	                    this.socket.close();
	                    break;
	                }
			        do {
			        	writer.writeUTF("\nenter number of hours:");
				        int h=reader.readInt();
				        writer.writeUTF("Enter mark:");
				        float m=reader.readFloat();
				        hourSum+=h;
				        SemesterGPA+=h*m;
				        writer.writeUTF("\n\nMenu\n1:Insert new mark\n2:Calculate GPA\n3:Exit\n.............?!");
    			        choice=reader.readInt();
    			        if(choice==3){
    	                    writer.writeUTF("\nBye");
    	                    this.socket.close();
    	                    break;
    	                }
				    } while(choice==1);
                    
			        writer.writeUTF("\nGPA Rank:"+SemesterGPA/hourSum+"\tSemester Status:");
			        if(SemesterGPA/hourSum>=3.75) {
				        writer.writeUTF("HONOURED");
			        }
			        else {
				        writer.writeUTF("IN GOOD STANDING");
			        }
			        writer.writeUTF("\tCumulative GPA:"+(CumulativeGPA*CumulativeHours+SemesterGPA)/(hourSum+CumulativeHours));
			        writer.writeUTF("\nBye");
                    this.socket.close();
		        }
		        else {
			        writer.writeUTF("\nInvalid ID ,try again....");
		        }
		    } while(true);
		}
    	catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
public class GPA extends Thread {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException   {
		ServerSocket server = new ServerSocket(3000);
        while (true){
            Socket socket = null;
            try{
                socket = server.accept();
                DataInputStream reader = new DataInputStream(socket.getInputStream());
                DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                Thread thread = new ClientHandler(socket, reader, writer);
                thread.start();
            }
            catch (Exception e){
                if (socket != null)
                    socket.close();
                server.close();
                e.printStackTrace();
            }
        }  
    }  	
}
