package data;



import domain.util.URIGenerator;
import domain.Book;
import domain.Person;
import domain.Organization;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.jaxen.*;
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
public class BookshareWS extends WebService{

    private boolean end=false;
    private int currentPageNumber=1;
    private String baseUrl="https://api.bookshare.org/book/search/category/Computers%20and%20Internet/page/";
    String apiKey="?api_key=mtdgrvurm6vhszb7xxtxc768";
    @Override
    public void parse(String tekst) throws Exception {
        try {
                    //List<Book> lista=new ArrayList<>();
                    Document document=DocumentHelper.parseText(tekst);
                    
                    List list=document.selectNodes("//result");
                    //System.out.println("Velicina liste "+list.size());
                    if(list==null || list.size()==0){
                        System.out.println("Lista je null");
                        end=true;
                    }else{
                        System.out.println("Trenutni broj strana "+currentPageNumber);
                        for(int i=0;i<list.size();i++){
                            Book b=new Book();
                            b.setUri(URIGenerator.generateUri(b));
                            Node node=(Node) list.get(i);
                            b.setTitle(node.selectSingleNode("title").getText());

                            Node isbn=node.selectSingleNode("isbn13");
                            if(isbn!=null){
                                b.setIsbn(isbn.getText());
                            }
                            List autori=node.selectNodes("author");

                            for(int j=0;j<autori.size();j++){
                                Person p=new Person();
                                p.setUri(URIGenerator.generateUri(p));
                                p.setName(((Node) autori.get(j)).getText());
                                b.getAuthors().add(p);
                            }

                            Organization o=new Organization();
                            o.setUri(URIGenerator.generateUri(o));
                            Node publisher=node.selectSingleNode("publisher");
                            if(publisher!=null){
                                o.setName(publisher.getText());
                            }
                            b.setPublisher(o);

                            Node description=node.selectSingleNode("brief-synopsis");
                            if(description!=null){
                                b.setDescription(description.getText());
                            }
                            books.add(b);

                        }
                        currentPageNumber++;
                        //kraj=true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
    }

    @Override
    public String getUrl() {
        return baseUrl+currentPageNumber+apiKey;
        //return "https://api.bookshare.org/book/search/category/Computers%20and%20Internet?api_key=mtdgrvurm6vhszb7xxtxc768";
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
    public boolean end() {
        return end;
    }
    
    
}
