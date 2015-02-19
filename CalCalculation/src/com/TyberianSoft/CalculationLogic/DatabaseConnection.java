package com.TyberianSoft.CalculationLogic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseConnection {
	
public DatabaseConnection() {
	// TODO Auto-generated constructor stub
}
public ArrayList<Scores> getWorldScores(){
	ArrayList<Scores> scoreList=new ArrayList<Scores>();
	String line=null;
	InputStream io;
	String result=null;
	String URL="http://mrmuhendis.com/calculationGame/selectListOfUsers.php";
	try {
	HttpClient httpClient=new DefaultHttpClient();
	HttpPost httppost=new HttpPost(URL);
	HttpResponse response = httpClient.execute(httppost);
	HttpEntity entity=response.getEntity();
	io=entity.getContent();
	} catch (Exception e) {
		
		e.printStackTrace();
	   return null;
}
	try{
	BufferedReader reader=new BufferedReader(new InputStreamReader(io, "iso-8859-1"));
	StringBuilder sb=new StringBuilder();
	while((line=reader.readLine())!=null){
		sb.append(line+"\n");
	}
	io.close();
	result=sb.toString();
	}catch(Exception ex){
		
	}
	try {
		JSONArray json_array=new JSONArray(result);
		for(int i=0;i<json_array.length();i++){
			JSONObject jsonob=json_array.getJSONObject(i);
			Scores score=new Scores(jsonob.getInt("score"), jsonob.getString("name"));
			scoreList.add(score);
		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	
	return scoreList;
}
public int insertScore(Scores score){
	
	InputStream is=null;
	String line;
	String result=null;
	int code;
	String URL="http://mrmuhendis.com/calculationGame/insertNewPlayer.php";
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("score", score.getScore()+""));
	nameValuePairs.add(new BasicNameValuePair("name",score.getName()));
	try
	{
	HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(URL);
        
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost); 
        HttpEntity entity = response.getEntity();
        is = entity.getContent();
        
}
    catch(Exception e)
{
    	e.printStackTrace();
    	
    	return -1;//Connection problem ...	
}
	 try
     {
         BufferedReader reader = new BufferedReader
			(new InputStreamReader(is,"iso-8859-1"),8);
         StringBuilder sb = new StringBuilder();
         while ((line = reader.readLine()) != null)
	    {
             sb.append(line + "\n");
         }
         is.close();
         result = sb.toString();
	    
	}
     catch(Exception e)
	{
    	 e.printStackTrace();
        return -1;//Connection problem
	}     
    
	try
	{
         JSONObject json_data = new JSONObject(result);
         code=(json_data.getInt("code"));
         return code;
		
	}
	catch(Exception e)
	{
		e.printStackTrace();
    return 0;     //Json Exception...
	}

	
}
}
