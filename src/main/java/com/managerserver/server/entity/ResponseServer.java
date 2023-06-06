package com.managerserver.server.entity;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Yarol Abraham
 */
public class ResponseServer {

     String responseCode = "";
     String responseMessage = "";
     String responseQuery = "";
     List<List<Map<String, String>>> data;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseQuery() {
        return responseQuery;
    }

    public void setResponseQuery(String responseQuery) {
        this.responseQuery = responseQuery;
    }

	public List<List<Map<String, String>>> getData() {
		return data;
	}

	public void setData(List<List<Map<String, String>>> listMaster) {
		this.data = listMaster;
	}
    
}
