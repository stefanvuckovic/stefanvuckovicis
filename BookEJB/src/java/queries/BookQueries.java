package queries;

import com.hp.hpl.jena.rdf.model.Model;
import domain.util.Constants;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import rdfmodel.RDFModel;

public class BookQueries {

    private QueryExecutor queryExecutor = new QueryExecutor();

      
    public Collection<String> searchBooks(String term, Date dateFrom, Date dateTo, int off, int lim, String sortField, String sortOrder) {
        Collection<String> rezultat = null;
        String limit = null;
        String offset = null;
        String sort = null;
        String where = null;
        String date = "";

        String query
                = "PREFIX schema: <" + Constants.SCHEMA + "> "
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
                + "SELECT DISTINCT ?book ";
                  

        where = "WHERE { "
                + "?book a schema:Book;";

        if (sortField != null) {
            sort = "?" + sortField;
            if (sortField.equals("title")) {
                where += " schema:name " + sort + ";";
            } else {
                where += " schema:datePublished " + sort + ";";

            }

            if (sortOrder != null) {
                if (sortOrder.equals("DESCENDING")) {
                    sort = " DESC(" + sort + ")";
                }
            }
            sort = " ORDER BY " + sort;
            System.out.println("SORT " + sort);

        }

        if (dateFrom != null) {
            where += " schema:datePublished ?date; ";
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String from = df.format(dateFrom) + "T00:00:00";
            date = "?date>=\"" + from + "\"^^xsd:dateTime";
                                

        }
        if (dateTo != null) {
            if (date.isEmpty()) {
                System.out.println("prazan string ");
                where += " schema:datePublished ?date; ";

            } else {
                date += " && ";
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String to = df.format(dateTo) + "T23:59:59";
            date += " ?date <= \"" + to + "\"^^xsd:dateTime ";
        }
        if (!date.isEmpty()) {
            date = "FILTER(" + date + ")";
        }

        where += "{"
                + "{?book schema:isbn \"" + term + "\"^^xsd:string.}"
                + "UNION"
                + "{?book schema:name ?name. FILTER regex(?name, \"" + term + "\", \"i\")}"
                + "UNION"
                + "{?book schema:author ?author. ?author schema:name ?authorname. FILTER (regex(?authorname, \"" + term + "\",\"i\") )}}"
                + date + "}";
                          

        if (lim != -1) {
            limit = " LIMIT " + lim;
        }
        if (off != -1) {
            offset = " OFFSET " + off;
        }

        query += where;
        if (sort != null) {
            query += sort;
        }
        if (limit != null) {
            query += limit;
        }
        if (offset != null) {
            query += offset;
        }

        System.out.println("UPIT: " + query);
        RDFModel.getInstance().beginReadTransaction();
        try {
            rezultat = queryExecutor
                    .executeSelectQueryOverModel(query, "book", RDFModel.getInstance().getModel());
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            RDFModel.getInstance().endTransaction();
        }

        return rezultat;
    }

    public int countBooks(String term, Date dateFrom, Date dateTo, String sortField) {
        List<String> result = null;
        String sort = null;
        String where = null;
        String date = "";
          

        String query
                = "PREFIX schema: <" + Constants.SCHEMA + "> "
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                + "SELECT (COUNT(DISTINCT ?book) as ?count) ";

        
         where = "WHERE { "
                + "?book a schema:Book;";

        if (sortField != null) {
            sort = "?" + sortField;
            if (sortField.equals("title")) {
                where += " schema:name " + sort + ";";
            } else {
                where += " schema:datePublished " + sort + ";";

            }

            sort = " ORDER BY " + sort;
            

        }

        if (dateFrom != null) {
            where += " schema:datePublished ?date; ";
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String from = df.format(dateFrom) + "T00:00:00";
            date = "?date>=\"" + from + "\"^^xsd:dateTime";
                                

        }
        if (dateTo != null) {
            if (date.isEmpty()) {
                System.out.println("prazan string ");
                where += " schema:datePublished ?date; ";

            } else {
                date += " && ";
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String to = df.format(dateTo) + "T23:59:59";
            date += " ?date <= \"" + to + "\"^^xsd:dateTime ";
        }
        if (!date.isEmpty()) {
            date = "FILTER(" + date + ")";
        }

        where += "{"
                + "{?book schema:isbn \"" + term + "\"^^xsd:string.}"
                + "UNION"
                + "{?book schema:name ?name. FILTER regex(?name, \"" + term + "\", \"i\")}"
                + "UNION"
                + "{?book schema:author ?author. ?author schema:name ?authorname. FILTER (regex(?authorname, \"" + term + "\",\"i\") )}}"
                + date + "}";
                          
        
        
        query += where;
        if (sort != null) {
            query += sort;
        }

        System.out.println("Broj rezultata: " + query);
        RDFModel.getInstance().beginReadTransaction();
        try {
            result = (List<String>) queryExecutor
                    .executeSelectQueryOverModel(query, "?count", RDFModel.getInstance().getModel());
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            RDFModel.getInstance().endTransaction();
        }

        String s = result.get(0);
        int i = Integer.parseInt(s);
        return i;
    }

}
