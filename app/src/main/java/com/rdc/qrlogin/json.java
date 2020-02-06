
package com.rdc.qrlogin;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class json {
	String result = null;
   	InputStream is = null;

	 public String getJSONFromUrl(String url, List<NameValuePair> params ){
		 try {

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				Log.d("tttttt" , ""+httpPost);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
				Log.d("system out", "connection success");
				
			} catch (Exception e) {
				// TODO: handle exception
				
				Log.e("log_tag", "Error in http connection "+e.toString());
			}
			try {
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine())!=null) {
					sb.append(line + "\n");
					//Toast.makeText(getApplicationContext(), "sb:"+sb, Toast.LENGTH_LONG).show();
					result = sb.toString();
				}
				is.close();
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag", "json not converted"+e.toString());
			}
		return result;
		 
	 }

    public String getJSONFromUrl(String url, List<NameValuePair> params , String token ){
        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params) );
            httpPost.setHeader("x-access-token" , token);

            Log.d("tttttt" , ""+httpPost);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.d("lol", String.valueOf(httpResponse));
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            Log.d("is", String.valueOf(is));
            Log.d("system out", "connection success");

        } catch (Exception e) {
            // TODO: handle exception

            Log.e("log_tag", "Error in http connection "+e.toString());
        }
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine())!=null) {
                sb.append(line + "\n");
                //Toast.makeText(getApplicationContext(), "sb:"+sb, Toast.LENGTH_LONG).show();
                result = sb.toString();
            }
            is.close();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "json not converted"+e.toString());
        }
        return result;

    }

	 public String getJSONFromUrl(String url){
		 
		 try {
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
				Log.d("system out", "connection success");
				
			} catch (Exception e) {
				// TODO: handle exception
				
				Log.e("log_tag", "Error in http connection "+e.toString());
			}
			
			try {
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				
				String line = null;
				while ((line = reader.readLine())!=null) {
					sb.append(line + "\n");
					//Toast.makeText(getApplicationContext(), "sb:"+sb, Toast.LENGTH_LONG).show();
					result = sb.toString();
					
					
				}
				is.close();
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag", "json not converted"+e.toString());
			}
			
		
		
		return result;
		 
	 }







}


