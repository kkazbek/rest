package kdevelop.klogist.rest;
 
import java.lang.reflect.Type;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import kdevelop.klogist.proto.KlogistProtos.*;
//import com.google.common.net.MediaType;

import javax.naming.*;
import javax.sql.*;
import java.sql.*;

@Path("/")
public class RESTfulPing {
	@GET
	@Path("/ping")
	@Produces("application/protobuf")
	public RespPerson getPerson()
	{
		RespPerson.Builder persons = RespPerson.newBuilder();
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

                    while(rst.next())
                    {
                    	Person.Builder person = Person.newBuilder();
                    	person.setId(rst.getInt(1));
                    	person.setName(rst.getString(2));
                    	persons.addPerson(person.build());
                    	System.out.println(person.getName());
                    }
                    persons.setResult(0);
                    conn.close();
                }
            }
        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }
		System.out.println("return");
		return persons.build();
	}
}