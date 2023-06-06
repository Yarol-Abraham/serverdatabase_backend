package com.managerserver.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.managerserver.server.dao.ServerDatabaseDao;
import com.managerserver.server.entity.RequestServer;
import com.managerserver.server.entity.ResponseServer;

@RestController
@CrossOrigin("*")
@RequestMapping("serverdba")
public class manager {

	@Autowired
	ServerDatabaseDao serverDataBase;
	ResponseServer responseServer = new ResponseServer();
	
	@PostMapping("/api/connection")
	public ResponseServer getConnection(@RequestBody RequestServer requestServer)
	{
		serverDataBase.getNewConnection(requestServer, responseServer);
		return responseServer;
	}
	
	@PostMapping("/api/execute/query")
	public ResponseServer getExcuteQuery(@RequestBody RequestServer requestServer)
	{
		return serverDataBase.responseQuery(requestServer, responseServer);
	}
	
}
