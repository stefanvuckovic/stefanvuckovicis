/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sb;

import static com.hp.hpl.jena.enhanced.BuiltinPersonalities.model;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileUtils;
import com.hp.hpl.jena.vocabulary.RDF;
import data.BookshareWS;
import data.GoogleBooksWS;
import data.IsbndbWS;
import domain.Book;
import domain.Person;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import rdfmodel.RDFModel;
import queries.BookQueries;

/**
 *
 * @author Stefan
 */
@Stateless
public class SessionBook implements SessionBookLocal {
    
    BookQueries bq;
    
    public SessionBook(){
        bq=new BookQueries();
    }

    @Override
    public void getDataFromNet() {
        System.out.println("pocetak metode");
        Path path=Paths.get("tdb");
        
//        if(Files.exists(path)){
//            
//            System.out.println("nasao folder usao da izbrise");
//            File file=new File("tdb");
//            String[] entries=file.list();
//            int brojac=1;
//            for(String s:entries){
//                File currentFile=new File(file.getPath(),s);
//                currentFile.delete();
//                System.out.println(brojac);
//                brojac++;
//            }
//            file.delete();
//        }
        
        if(!Files.exists(path)){
            System.out.println("usao");
            
            List<Book> listBS=(List<Book>) new BookshareWS().getData();
            List<Book> listI=(List<Book>) new IsbndbWS().getData();
            List<Book> listGB=(List<Book>) new GoogleBooksWS().getData();
            
            System.out.println("dovlaci podatke");
            
            integrateData(listGB, listI, listBS);
            try{
                RDFModel.getInstance().beginWriteTransaction();
                RDFModel.getInstance().setPrefixes();

                for(Book b:listI){
                    RDFModel.getInstance().save(b);
                }
                for(Book b:listGB){
                    RDFModel.getInstance().save(b);
                }
                for(Book b:listBS){
                    RDFModel.getInstance().save(b);
                }
                
                RDFModel.getInstance().commit();
            }catch(Exception e){
                e.printStackTrace();
                RDFModel.getInstance().abort();
            }finally{
                RDFModel.getInstance().endTransaction();
            }
        }    }
    
    @Override
    public List<Book> searchBooks(String term, Date dateFrom, Date dateTo, int offset, int limit, String sortField, String sortOrder){
        System.out.println("Ulazak u metodu pretraziKnjige");
        List<Book> list=new ArrayList<>();
        //BookQueries bq=new BookQueries();
        Collection<String> books=bq.searchBooks(term, dateFrom, dateTo, offset, limit, sortField, sortOrder );
        Iterator i=books.iterator();
        while(i.hasNext()){
            String s=(String) i.next();
            System.out.println(s);
            Book b=(Book) RDFModel.getInstance().load(s);
            list.add(b);
        }
        
//        for(Book book:lista){
//            System.out.println("ime "+book.getTitle());
//            for(Person p:book.getAuthors()){
//                System.out.println("autor "+p.getName());
//            }
//        
//      
//        }
        return list;
    }
    
    @Override
    public int countBooks(String term, Date dateFrom, Date dateTo, String sortField){
        System.out.println("Usao u metodu countBooks");
        return bq.countBooks(term, dateFrom, dateTo, sortField);
    }
    
    private Book findTheBook(List<Book> list, Book b){
        Book foundBook=null;
        for(Book book:list){
            if (b.getIsbn().equals(book.getIsbn())){
                foundBook=book;
                break;
            }
            
        }
        
        return foundBook;
        
    }

    private void integrateData(List<Book> list1,List<Book> list2, List<Book> list3){
       for(Book b:list1){
                if(b.getIsbn()!=null){
                    //System.out.println("fali podatak");
                    Book book1=findTheBook(list2, b);
                    Book book2=findTheBook(list3, b);
                    if(book1!=null){
                        System.out.println("Nasao tu knjigu kod prvog");
                        System.out.println("ISBN duple knjige "+b.getIsbn());
                        if("".equals(b.getPublisher().getName()) || b.getPublisher().getName()==null){
                            System.out.println("Nedostajuci podatak google books izdavac "+book1.getPublisher().getName());
                            if(book1.getPublisher().getName()!=null){
                                    b.getPublisher().setName(book1.getPublisher().getName());
                            }
                        }
                        if( b.getAuthors().size()==0){
                            System.out.println("Nedostajuci podatak google books autori "+book1.getAuthors().size());
                            if(book1.getAuthors().size()>0){
                                    b.setAuthors(book1.getAuthors());
                            }
                        }
                        
                        list2.remove(book1);
                    }
                    
                    if(book2!=null){
                        System.out.println("Nasao tu knjigu kod drugog");
                        System.out.println("ISBN duple knjige "+b.getIsbn());
                        if("".equals(b.getPublisher().getName()) || b.getPublisher().getName()==null){
                                System.out.println("Nedostajuci podatak google books izdavac"+book2.getPublisher().getName());
                                b.getPublisher().setName(book2.getPublisher().getName());
                        }
                        if( b.getAuthors().isEmpty()){
                            System.out.println("Nedostajuci podatak google books autori "+book2.getAuthors().size());           
                            b.setAuthors(book2.getAuthors());
                        }
                        
                        if("".equals(b.getDescription())||b.getDescription()==null){
                            System.out.println("Nedostajuci podatak google books opis "+book2.getDescription());
                            b.setDescription(book2.getDescription());
                        }
                        list3.remove(book2);
                    }
                    
                    
                }
            }
       
            for(Book b:list2){
                if(b.getIsbn()!=null){
                    //System.out.println("fali podatak");
                   
                    Book book=findTheBook(list3, b);
                    if(book!=null){
                        System.out.println("Nasao knjigu ISBNDB kod BookShare");
                        if("".equals(b.getDescription()) || b.getDescription()==null){
                            System.out.println("Nedostajuci podatak "+book.getDescription());
                            b.setDescription(book.getDescription());
                        }
                        list3.remove(book);
                    }
                    
                       
                    
                    
                }
            }
        
    }
   
}
