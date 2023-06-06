package com.managerserver.server.dao;

import java.sql.Connection;

import com.managerserver.server.entity.RequestServer;
import com.managerserver.server.entity.ResponseServer;

public interface ServerDatabaseDao {

	public Connection getNewConnection(RequestServer requestServer, ResponseServer responseServer);
	public ResponseServer responseQuery(RequestServer requestServer, ResponseServer responseServer);
	
}
