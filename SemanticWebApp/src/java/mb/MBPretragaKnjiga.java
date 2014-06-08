/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mb;

import domain.Book;
import domain.Person;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
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
    String currentTerm;
    boolean prikazRezultata=false;
    int brojStrana;
    boolean novaPretraga=false;
    
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
                if(prikazRezultata){
                    sbl.getDataFromNet();
                    books = sbl.pretraziKnjige(term, first, first + pageSize);
                    
                    System.out.println("doslo u load");
                    if(!term.equals(currentTerm)){
                        novaPretraga=true;
                        brojStrana=sbl.countBooks(term);
                    }
                    currentTerm=term;
                    setRowCount(brojStrana);
                    //if(novaPretraga){
//                        DataTable dataTable = (DataTable)  FacesContext.getCurrentInstance().getViewRoot().findComponent("tableBooksId");
//                        dataTable.reset();
//                        dataTable.resetValue();
                    //}
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

    public LazyDataModel<Book> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Book> lazyModel) {
        this.lazyModel = lazyModel;
    }
    
    

     
     
    public MBPretragaKnjiga() {
        System.out.println("inicijalizacija konstruktora beana");
    }
    
    public String pretraziKnjige(){
        System.out.println("ulaz u metodu");
        
        prikazRezultata=true;
        // dodati na stranici kod dugmeta onclick="tableBooks.getPaginator().setPage(0);"
        DataTable dataTable = (DataTable)  FacesContext.getCurrentInstance().getViewRoot().findComponent("tableBooksId");
        dataTable.reset();// jump to first page
        //dataTable.resetValue();
     
        return null;
    }
    
    public boolean daLiPostojePodaci(){
        //return books != null;
        return prikazRezultata;
    }
    
    public String vratiAutoreZaKnjigu(Book b){
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
}
