/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mb;

import domain.Book;
import domain.Person;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import sb.SessionBookLocal;

/**
 *
 * @author Stefan
 */
@ManagedBean
@ViewScoped
public class MBPretragaKnjiga implements Serializable{

    private static final long serialVersionUID = -1201944101993687165L;
    
    List<Book> books;
    String term;
    Date dateFrom;
    Date dateTo;
    
    String sortF=null;
    boolean renderResults=false;
    int numberOfPages;
    boolean newSearch=false;
    
    /**
     * Creates a new instance of MBPretragaKnjiga
     */
    
     @EJB
    SessionBookLocal sbl;

     LazyDataModel<Book> lazyModel;
     
    /**
     * Creates a new instance of MBPretragaKnjiga
     */
   
    @PostConstruct
    public void init(){
        System.out.println("Doslo dovde");
        lazyModel=new LazyDataModel<Book>() {
             private static final long    serialVersionUID    = 1L;
          
             @Override
             public List<Book> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
                if(renderResults){
                    
                    sbl.getDataFromNet();
                    System.out.println("KOLONA PO KOJOJ SE SORTIRA "+sortField);
                    System.out.println("SORT ORDER "+sortOrder.toString());
                    books = sbl.searchBooks(term, dateFrom, dateTo, first, first + pageSize, sortField, sortOrder.toString());
                   
                    System.out.println("doslo u load");
                    if(!compare(sortField, sortF) || newSearch){
                        numberOfPages=sbl.countBooks(term, dateFrom, dateTo, sortField);
                    }
                    setRowCount(numberOfPages);
                    
                    sortF=sortField;
                    newSearch=false;
                    return books;
                }
                return null;
             }
        };
    }
    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
    
    

    public LazyDataModel<Book> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Book> lazyModel) {
        this.lazyModel = lazyModel;
    }
    
    

     
     
    public MBPretragaKnjiga() {
        System.out.println("inicijalizacija konstruktora beana");
    }
    
    public String searchBooks(){
        System.out.println("ulaz u metodu");
        
        // dodati na stranici kod dugmeta onclick="tableBooks.getPaginator().setPage(0);"
        DataTable dataTable = (DataTable)  FacesContext.getCurrentInstance().getViewRoot().findComponent("tableBooksId");
        dataTable.reset();// jump to first page
        dataTable.resetValue();
        dataTable.setSortBy(null);
        newSearch=true;
        if(!renderResults){
            renderResults=true;
        }
     
        return null;
    }
    
    public boolean renderResults(){
        //return books != null;
        return renderResults;
    }
    
    public String getAuthorsForBook(Book b){
        String s="";
        boolean prvi=true;
        for(Person p:b.getAuthors()){
            if(!prvi){
                s+=",";
                
            }else{
                prvi=false;
            }
            s+=p.getName();
        }
        return s;
    } 
    
  
    public static boolean compare(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2));
}
}
