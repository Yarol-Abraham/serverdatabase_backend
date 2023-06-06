package com.managerserver.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.managerserver.server.entity.RequestServer;
import com.managerserver.server.entity.ResponseServer;
import com.mysql.cj.jdbc.result.ResultSetMetaData;


@Service
public class ServerDatabaseDaoImpl implements ServerDatabaseDao {
	
	// DINAMYC VARIABLES 
	public Connection           currentConnection    = null;
    public PreparedStatement    stPreparedQuery     = null;
    public ResultSet            rsRecords           = null;
    public ResultSet            rsRecords1          = null;
    public ResultSet            rsRecordsTmp        = null;
    public Statement            stQuery             = null;
    
    // PARAMETRES DATABASE AUX
    private String HOSTNAME = "127.0.0.1";
    private String PORT_HOSTNAME = "3307"; // 3306
    private String AUX_HOSTNAME = ":";
    private String AUX_DBNAME = "?useSSL=false";
    
    /**
     * @param requestServer
     * @param responseServer
     * @return currentConnection
    */
    
    @Override
    public Connection getNewConnection(RequestServer requestServer, ResponseServer responseServer)
    {
        try
        {
            String jdbcUrl = String.format("jdbc:mysql://%s/%s", 
            		!requestServer.getHostname().equals("") ? requestServer.getHostname() : HOSTNAME+AUX_HOSTNAME+PORT_HOSTNAME, 
            		requestServer.getDbName() + AUX_DBNAME
            );
            
             Class.forName("com.mysql.cj.jdbc.Driver");
             currentConnection = DriverManager.getConnection(
                    (String) jdbcUrl,
                    (String) requestServer.getUsername(),
                    (String) requestServer.getPassword());
            
            // success connection in database
            responseServer.setResponseCode("00");
            responseServer.setResponseMessage("Conexión establecida con exito");
        }
        catch(ClassNotFoundException | SQLException ex)
        {
            System.out.println("ERROR EN LA CONEXION DE LA BASE DE DATOS: " + ex.getMessage());
            currentConnection = null;
            responseServer.setResponseCode("-1");
            responseServer.setResponseMessage(ex.getMessage());
        }
        
        return  currentConnection;
        
    }
    
    @Override
    public ResponseServer responseQuery(RequestServer requestServer, ResponseServer responseServer)
    {
    	try
    	{
    		this.currentConnection = this.getNewConnection(requestServer, responseServer);
    		if(this.currentConnection == null)
    			return responseServer;
    		
    		 //------- DYNAMIC DATA -------
    		List<List<Map<String, String>>> data = new ArrayList<List<Map<String, String>>>();
    		String[] divideQuerys = { requestServer.getQuery() };
    		
    		if(requestServer.getQuery().contains(";"))
    			divideQuerys = requestServer.getQuery().split(";");
    		
    		// --------- EXECUTE DATA ----------	
        	for(int j = 0; j < divideQuerys.length; j++)
        	{
        		if (divideQuerys[j].trim().split(" ")[0].toUpperCase().contains("USE"))
        		{
        			String new_database = divideQuerys[j].trim().split(" ")[1].toString();
        			requestServer.setDbName(new_database);
        			this.currentConnection = this.getNewConnection(requestServer, responseServer);
        		}
        		else if(
        		   divideQuerys[j].trim().split(" ")[0].toUpperCase().contains("UPDATE") || 
        		   divideQuerys[j].trim().split(" ")[0].toUpperCase().contains("DELETE") || 
        		   divideQuerys[j].trim().split(" ")[0].toUpperCase().contains("INSERT") ||
        		   divideQuerys[j].trim().split(" ")[0].toUpperCase().contains("DROP") ||
        		   divideQuerys[j].trim().split(" ")[0].toUpperCase().contains("CREATE") ||
        		   divideQuerys[j].trim().split(" ")[0].toUpperCase().contains("ALTER") ||
        		   divideQuerys[j].trim().split(" ")[0].toUpperCase().contains("GRANT") ||
        		   divideQuerys[j].trim().split(" ")[0].toUpperCase().contains("FLUSH") ||
        		   divideQuerys[j].trim().split(" ")[0].toUpperCase().contains("REVOKE")
        		   )
        		{
        			this.stPreparedQuery = this.currentConnection.prepareStatement(divideQuerys[j]);
        			this.stPreparedQuery.executeUpdate();
        		}
        		else
        		{
        			this.stPreparedQuery = this.currentConnection.prepareStatement(divideQuerys[j]);
        			this.rsRecords = this.stPreparedQuery.executeQuery();
                	List<Map<String, String>> listMaster  = new ArrayList<Map<String, String>>();
                	ResultSetMetaData metaData = (ResultSetMetaData) this.rsRecords.getMetaData();
                	int columnCount = metaData.getColumnCount();
                	
                	while (this.rsRecords.next()) 
                    {
                		Map<String, String> captureData = new HashMap<>();
                		for(int i = 1; i <= columnCount; i++)
                		{
                			captureData.put(metaData.getColumnLabel(i), this.rsRecords.getString(metaData.getColumnLabel(i)));
                		}
                		
                		listMaster.add(captureData);
                    }
                	
                	data.add(listMaster);
        		}
        	}
        	
        	responseServer.setData(data);
        	responseServer.setResponseCode("00");
        	responseServer.setResponseMessage("success execute query");
    	}
    	catch (Exception e) {
			// TODO: handle exception
    		System.out.println("ERROR AL INTENTAR: " + e.getMessage());
    		responseServer.setResponseCode("-1");
    		responseServer.setResponseMessage(e.getMessage());
		}
    	
    	return  responseServer;
    }
	
}
