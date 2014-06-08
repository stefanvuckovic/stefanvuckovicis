package data;


import domain.Book;
import domain.Organization;
import domain.Person;
import domain.util.URIGenerator;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.jaxen.*;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import rdfmodel.RDFModel;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stefan
 */
public class IsbndbWS extends WebService{

    private boolean kraj=false;
    private String baseUrl="http://isbndb.com/api/books.xml?access_key=M94ZHX5G&results=texts&index1=subject_id&value1=computers&page_number=";
    private int trenutniBrojStrane=1;
    
    @Override
    public void parse(String tekst) throws Exception{
        try { 
                    
                    //List<Book> lista=new ArrayList<>();
                    
                    Document document=DocumentHelper.parseText(tekst);
                    
                    List list=document.selectNodes("//BookData");
                    
                    if(list==null || list.size()==0){
                        System.out.println("Doslo do kraja");
                        kraj=true;
                    }else{
                        System.out.println("ISBNDB, Trenutni broj strane "+trenutniBrojStrane);
                        for(int i=0;i<list.size();i++){
                            Book b=new Book();
                            b.setUri(URIGenerator.generateUri(b));
                            Node node=(Node) list.get(i);
                            b.setTitle(node.selectSingleNode("Title").getText());
                            b.setIsbn(node.valueOf("@isbn13"));
                            Person p=new Person();
                            p.setUri(URIGenerator.generateUri(p));
                            p.setName(node.selectSingleNode("AuthorsText").getText());
                            b.getAuthors().add(p);
                            Organization o=new Organization();
                            o.setUri(URIGenerator.generateUri(o));
                            o.setName(node.selectSingleNode("PublisherText").getText());
                            b.setPublisher(o);

                            lista.add(b);

                        }
                        
                        trenutniBrojStrane++;
                        //kraj=true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
    }

    @Override
    public String getUrl() {
        return baseUrl+trenutniBrojStrane;
        //return "http://isbndb.com/api/books.xml?access_key=M94ZHX5G&results=texts&index1=subject_id&value1=computers&page_number=7";
    }

    @Override
    public void setRequestType(HttpURLConnection conn) throws Exception{
        try {
            conn.setRequestMethod("GET");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void setDataType(HttpURLConnection conn) {
        conn.setRequestProperty("Accept", "application/xml");
    }

    @Override
    public boolean daLiJeKraj() {
        return kraj;
    }
    
}
