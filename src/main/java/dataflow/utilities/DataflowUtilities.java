package dataflow.utilities;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class DataflowUtilities {

	public static HttpResponse sendPut(String data, String url, String xAccessToken) {
	    int responseCode = -1;
	    HttpClient httpClient = new DefaultHttpClient();
	    HttpResponse response= null;
	    try {
	        HttpPut request = new HttpPut(url);
	        StringEntity params =new StringEntity(data,"UTF-8");
	        params.setContentType("application/json");
	        request.addHeader("content-type", "application/json");
	        request.addHeader("Accept", "application/json, text/plain, */*");
	        request.addHeader("x-access-token", xAccessToken);
	        request.setEntity(params);
	        response = httpClient.execute(request);
	        
			responseCode = response.getStatusLine().getStatusCode();

	    }catch (Exception ex) {
	    	System.out.println("ex Code sendPut: " + ex);
	    	System.out.println("url:" + url);
	    	System.out.println("data:" + data);
	    } finally {
	        httpClient.getConnectionManager().shutdown();
	    }

	    return response;

	}
	
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date1.getTime() - date2.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	public static List<String> addRoles(List<String> roles) {
		roles.add("speadmin");
		roles.add("speoper");
		roles.add("spedesigner");
		roles.add("speexpertdesigner");
		roles.add("spehpdeveloper");
		return roles;
	}
}
