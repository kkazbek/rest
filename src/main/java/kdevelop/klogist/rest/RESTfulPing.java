package kdevelop.klogist.rest;

import kdevelop.klogist.proto.KlogislProtos.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
 
import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.naming.*;
import javax.sql.*;
import java.sql.*;

@Path("/")
public class RESTfulPing {
	@GET
	@Path("/ping")
	@Produces(MediaType.TEXT_PLAIN)
	public Response checkDBConnection()
	{
		byte[] output = new byte[100];
		try
        {
            Context ctx = new InitialContext();
            if(ctx == null )
                throw new Exception("Boom - No Context");
    
            // /jdbc/postgres is the name of the resource above 
            DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/postgres");
        
            if (ds != null) 
            {
                Connection conn = ds.getConnection();
        
                if(conn != null) 
                {
                    Statement stmt = conn.createStatement();
                    ResultSet rst = stmt.executeQuery("select id, name from users");
                    
                    RespPerson.Builder persons = RespPerson.newBuilder();
                    while(rst.next())
                    {
                    	Person.Builder person = Person.newBuilder();
                    	person.setId(rst.getInt(1));
                    	person.setName("name = "+rst.getString(2));
                    	persons.addPerson(person.build());
                    }
                    persons.setResult(0);
                    output = persons.build().toByteArray();
                    conn.close();
                }
            }
        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }

		String s2 = new String(output);
		return Response.status(200).entity(s2).build();
	}
}