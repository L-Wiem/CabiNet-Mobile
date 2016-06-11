package com.pfe.cabinet;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.pfe.cabinet.CommonUtilities.SERVER_URL;



public final class ServerUtilities {

 
    public static void register(final Context context,final String regId) {
      
        String serverUrl = SERVER_URL;
    
        if(!regId.isEmpty()){


			Log.w("ServiceResponseMsg", regId);
			saveTokenToServer(regId);
        	
        

			
        }
		
    
    }


	private static void saveTokenToServer(String token){


		Map paramPost = new HashMap();
		paramPost.put("action","add");
		paramPost.put("tokenid", token);
		paramPost.put("txtUsername", Config.LOGGEDIN_SHARED_PREF);
		try {
			String msgResult = getStringResultFromService_POST("http://YOUR_PROJECT_HOST/api/mobile-user-set-device-id", paramPost);
			Log.w("ServiceResponseMsg", msgResult);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public static String getStringResultFromService_POST(String serviceURL, Map<String, String> params) {
		HttpURLConnection cnn = null;
		String line = null;
		URL url;
		try{
			url = new URL(serviceURL);
		} catch (MalformedURLException e){
			throw  new IllegalArgumentException("URL invalid:"+serviceURL);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
		//Construct the post body using the parameter
		while (iterator.hasNext()){
			Map.Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
			if(iterator.hasNext()){
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString(); //format same to arg1=val1&arg2=val2
		Log.w("AccessService", "param:" + body);
		byte[]bytes = body.getBytes();
		try{
			cnn = (HttpURLConnection)url.openConnection();
			cnn.setDoOutput(true);
			cnn.setUseCaches(false);
			cnn.setFixedLengthStreamingMode(bytes.length);
			cnn.setRequestMethod("POST");
			cnn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			//Post the request
			OutputStream outputStream = cnn.getOutputStream();
			outputStream.write(bytes);
			outputStream.close();

			//Handle the response
			int status = cnn.getResponseCode();
			if(status!=200){
				throw  new IOException("Post fail with error code:" + status);
			}
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cnn.getInputStream()));
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = bufferedReader.readLine())!=null){
				stringBuilder.append(line+'\n');
			}
			return stringBuilder.toString();
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

    /**
     * Unregister this account/device pair within the server.
     */
    static void unregister(final Context context, final String regId) {
     
        String serverUrl = SERVER_URL + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        // post(serverUrl,pa);
            GCMRegistrar.setRegisteredOnServer(context, false);
           
    }

 
    
}
