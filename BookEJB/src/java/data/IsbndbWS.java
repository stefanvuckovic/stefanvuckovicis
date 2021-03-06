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
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class IsbndbWS extends WebService {

    private boolean end = false;
    private String baseUrl = "http://isbndb.com/api/books.xml?access_key=M94ZHX5G&results=texts&index1=subject_id&value1=computers&page_number=";
    private int currentPageNumber = 1;

    @Override
    public void parse(String text) throws Exception {
        try {

            //List<Book> lista=new ArrayList<>();
            Document document = DocumentHelper.parseText(text);

            List list = document.selectNodes("//BookData");

            if (list == null || list.size() == 0) {
                System.out.println("Doslo do kraja");
                end = true;
            } else {
                System.out.println("ISBNDB, Trenutni broj strane " + currentPageNumber);
                for (int i = 0; i < list.size(); i++) {
                    Book b = new Book();
                    b.setUri(URIGenerator.generateUri(b));
                    Node node = (Node) list.get(i);
                    b.setTitle(node.selectSingleNode("Title").getText());
                    b.setIsbn(node.valueOf("@isbn13"));

                    String authors = node.selectSingleNode("AuthorsText").getText();
                    List<String> listOfAuthors = getAuthors(authors);
                    for (String s : listOfAuthors) {
                        System.out.println("autor isparsiran " + s);
                        if (!s.isEmpty() && !" ".equals(s)) {
                            Person p = new Person();
                            p.setUri(URIGenerator.generateUri(p));
                            //p.setName(node.selectSingleNode("AuthorsText").getText());
                            p.setName(s);
                            b.getAuthors().add(p);
                        }
                    }
                    Organization o = new Organization();
                    o.setUri(URIGenerator.generateUri(o));
                    o.setName(node.selectSingleNode("PublisherText").getText());
                    b.setPublisher(o);
                    System.out.println("Broj autora je " + b.getAuthors().size());
                    books.add(b);

                }

                currentPageNumber++;
                
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public String getUrl() {
        return baseUrl + currentPageNumber;
        //return "http://isbndb.com/api/books.xml?access_key=M94ZHX5G&results=texts&index1=subject_id&value1=computers&page_number=7";
    }

    @Override
    public void setRequestType(HttpURLConnection conn) throws Exception {
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

    private List<String> getAuthors(String authors) {
//        String regex1="\\bby\\b";
//        String regex2="\\bedited\\b";
//        String regex3="\\bet\\b";
//        String regex4="[\\[\\]]";
//        String regex5="\\bal\\b";
//        String regex6="\\bwritten\\b";
//        
//        
//        //String regex8="\\b[a-z]+\\b";
//        
//        //authors=authors.replaceAll(regex8, "");
//        
//        authors=authors.replaceAll(regex1, "");
//        authors=authors.replaceAll(regex2, "");
//        authors=authors.replaceAll(regex3, "");
//        authors=authors.replaceAll(regex4, "");
//        authors=authors.replaceAll(regex5, "");
//        authors=authors.replaceAll(regex6, "");
//          
//        System.out.println("autor pre splitovanja "+authors);
//
//        String [] array=authors.split("and|[;,]");
//        
//        String term1="...";
//        String term2=".";
//        
//        for(int i=0;i<array.length;i++){
//            String s=array[i];
//            int index=s.lastIndexOf("...");
//            System.out.println("nasao ... na poziciji "+index);
//            if(index!=-1){
//                s=s.substring(0,index);
//            }
//            
//            if(s.endsWith(".")){
//                System.out.println("Nasao .");
//                s=s.substring(0,s.length()-1);
//            }
//            
//            array[i]=s;
//            
//        }
//        return array;

//        String pattern1 = "[A-Z]\\. [A-Z]\\. \\b[A-Z][a-z]+\\b";      /
//        String pattern2= "[A-Z]\\. \\b[A-Z][a-z]+\\b";                /
//        String pattern3="\\b[A-Z][a-z]+\\b [A-Z]\\. \\b[A-Z][a-z]+\\b";  /
//        String pattern4="\\b[A-Z][a-z]+\\b \\b[A-Z][a-z]+\\b";          /
//        String pattern5="\\b[A-Z][a-z]+\\b \\b[A-Z][a-z]+\\b \\b[A-Z][a-z]+\\b"; /
//        String pattern6="[A-Z]\\. \\b[A-Z][a-z]+\\b \\b[A-Z][a-z]+\\b";     /
//        String pattern7 = "[A-Z]\\. [A-Z]\\. [A-Z]\\. \\b[A-Z][a-z]+\\b";   /
//        String pattern8 = "[A-Z][a-z]+ [A-Z]\\. [A-Z][a-z]+ [A-Z][a-z]+"; /
//        String pattern9 = "[A-Z][a-z]+ [A-Z]\\. [A-Z]\\. [A-Z][a-z]+";    /
        //String pattern4 = "([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)? ([A-Z]\\. )+([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)?";
        //String pattern5 = "([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)? ([A-Z]\\. )+([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)? ([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)?";
        String pattern1 = "(([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)? )?([A-Z]\\. )+([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)? ([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)? ([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)?";
        String pattern2 = "(([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)? )?([A-Z]\\. )+([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)? ([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)?";
        String pattern3 = "(([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)? )?([A-Z]\\. )+([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)?";
        String pattern4 = "(([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)? )+([A-Z]')?[A-Z][a-z]+((-([A-Z]')?[A-Z][a-z]+)+)?";

        List<String> patterns = new ArrayList<>();
        patterns.add(pattern1);
        patterns.add(pattern2);
        patterns.add(pattern3);
        patterns.add(pattern4);
       
        System.out.println("autori pre brisanja "+authors);
        List<String> listOfAuthors = new ArrayList<>();

        for (String s : patterns) {
            Pattern pattern = Pattern.compile(s);
            Matcher matcher = pattern.matcher(authors);

            int count = 0;
            while (matcher.find()) {
                count++;
                System.out.println("found: " + count + " : "
                        + matcher.start() + " - " + matcher.end());

                String author = authors.substring(matcher.start(), matcher.end());
                listOfAuthors.add(author);
                System.out.println(author);
            }
            authors=matcher.replaceAll("");
            System.out.println("Autori nakon brisanja "+authors);
        }
        
        return listOfAuthors;

    }

}
