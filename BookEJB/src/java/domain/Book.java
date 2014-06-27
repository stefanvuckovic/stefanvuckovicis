package domain;




import domain.util.Constants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stefan
 */
@Namespace(Constants.SCHEMA)
@RdfType("Book")
public class Book extends Thing{
    @RdfProperty(Constants.SCHEMA+"isbn")
    private String isbn;
    @RdfProperty(Constants.SCHEMA+"name")
    private String title;
    @RdfProperty(Constants.SCHEMA+"numberOfPages")
    private int numberOfPages;
    @RdfProperty(Constants.SCHEMA+"author")
    private List<Person> authors;
    @RdfProperty(Constants.SCHEMA+"publisher")
    private Organization publisher;
    @RdfProperty(Constants.SCHEMA+"datePublished")
    private Date datePublished;
    @RdfProperty(Constants.SCHEMA+"description")
    private String description;
    
    public Book(){
        authors=new ArrayList<>();
    }
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public List<Person> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Person> authors) {
        this.authors = authors;
    }

    public Organization getPublisher() {
        return publisher;
    }

    public void setPublisher(Organization publisher) {
        this.publisher = publisher;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
   
    
    
    
    
}
