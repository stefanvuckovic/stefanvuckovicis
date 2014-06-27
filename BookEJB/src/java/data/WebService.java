package data;


import com.sun.beans.WeakCache;
import domain.Book;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stefan
 */
public abstract class WebService {
   
    List<Book> books;
    
    
    public WebService(){
        books=new ArrayList<>();
    }
    
    public List<Book> getData(){
        
        try {
               
            while(!end()){
                String tekst=getDataFromApi();               
                parse(tekst);
            }
              
               
        }catch(Exception e){
            e.printStackTrace();
        }
        return books; 
    }
    
    public String getDataFromApi() throws Exception{
        try{
                String sUrl=getUrl();
                
                URL url = new URL(sUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                
                setRequestType(conn);
                
                setDataType(conn);
               
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }
                
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
                String text="";
                String output;
                
                while ((output = br.readLine()) != null) {
                   
                    text+=output;
                }
                
                conn.disconnect();
                return text;
                
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public abstract void parse(String text) throws Exception;

    public abstract String getUrl();
    
    public abstract void setRequestType(HttpURLConnection conn) throws Exception;
    
    public abstract void setDataType(HttpURLConnection conn);

    public abstract boolean end();
        
}
