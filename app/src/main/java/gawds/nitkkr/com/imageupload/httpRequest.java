package gawds.nitkkr.com.imageupload;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by palak.garg on 01-10-2016.
 */

public class httpRequest {

    public String SendGetRequest(String requestURL)
    {
        StringBuilder stringBuilder=new StringBuilder();
        try {
            URL url=new URL(requestURL);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line="";
            while((line=reader.readLine())!=null)
            {
                stringBuilder.append(line+"\n");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    public String sendPostRequest(String requestUrl, HashMap<String, String> postDataParams)
    {
        StringBuilder result=new StringBuilder();
        try {
            URL url=new URL(requestUrl);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream os=connection.getOutputStream();
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            int RequestCode= connection.getResponseCode();
            if(RequestCode==HttpURLConnection.HTTP_OK)
            {
                InputStream is=connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(reader);
                String x="";
                while((x=br.readLine())!=null)
                {
                    result.append(x+"\n");
                }

            }
        } catch (MalformedURLException e) {
            System.out.println("sendPostRequest Url Malformed");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private String getPostDataString(HashMap<String, String> postDataParams) {
        StringBuilder sb=new StringBuilder();
        boolean x1=false;
        for (Map.Entry<String, String> x: postDataParams.entrySet()) {

            if(!x1) {
                x1 = true;
            }
            else{sb.append("&");}
            sb.append(x.getKey());
            sb.append("=");
            sb.append(x.getValue());

        }
        return sb.toString();
    }


}
