package queries;

import com.hp.hpl.jena.rdf.model.Model;
import domain.util.Constants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import rdfmodel.RDFModel;


public class BookQueries {
	
	private QueryExecutor queryExecutor = new QueryExecutor();
	
        
//        public Model searchBooks(String term){
//            String query=
//                    "PREFIX schema: <"+Constants.SCHEMA+"> "+
//                    "DESCRIBE ?book "+
//                    "WHERE { "+
//                        "?book a schema:Book."+
//                    "{"+
//                    "{ ?book schema:isbn \""+term+"\"^^<http://www.w3.org/2001/XMLSchema#string>.}"+
//                   "UNION"+
//                    "{?book schema:name ?name. FILTER regex(?name, \""+term+"\", \"i\")}"
//                    + "UNION"+
//                    "{?book schema:author ?author. ?author schema:name ?authorname. FILTER regex(?authorname, \""+term+"\",\"i\")}}}";
//            System.out.println("UPIT: "+query);
//            return queryExecutor
//				.executeDescribeSparqlQuery(query,
//						           RDFModel.getInstance().getModel());
//             
//        }
        
        public Collection<String> searchBooks(String term, int offset, int limit){
            Collection<String> rezultat=null;
            String query=
                    "PREFIX schema: <"+Constants.SCHEMA+"> "+
                    "SELECT DISTINCT ?book "+
                    "WHERE { "+
                        "?book a schema:Book."+
                    "{"+
                    "{ ?book schema:isbn \""+term+"\"^^<http://www.w3.org/2001/XMLSchema#string>.}"+
                   "UNION"+
                    "{?book schema:name ?name. FILTER regex(?name, \""+term+"\", \"i\")}"
                    + "UNION"+
                    "{?book schema:author ?author. ?author schema:name ?authorname. FILTER regex(?authorname, \""+term+"\",\"i\")}}}"+
                    "LIMIT "+limit+" OFFSET "+offset;
            System.out.println("UPIT: "+query);
            RDFModel.getInstance().beginReadTransaction();
            try{
                rezultat= queryExecutor
				.executeSelectQueryOverModel(query, "book", RDFModel.getInstance().getModel());
            }catch(Exception e){
                e.printStackTrace();
                
            }finally{
                RDFModel.getInstance().endTransaction();
            }
            
            return rezultat;
        }

        public int countBooks(String term){
            List<String> rezultat=null;
            String query=
                    "PREFIX schema: <"+Constants.SCHEMA+"> "+
                    "SELECT (COUNT(DISTINCT ?book) as ?count) "+
                    "WHERE { "+
                        "?book a schema:Book."+
                    "{"+
                    "{ ?book schema:isbn \""+term+"\"^^<http://www.w3.org/2001/XMLSchema#string>.}"+
                   "UNION"+
                    "{?book schema:name ?name. FILTER regex(?name, \""+term+"\", \"i\")}"
                    + "UNION"+
                    "{?book schema:author ?author. ?author schema:name ?authorname. FILTER regex(?authorname, \""+term+"\",\"i\")}}}";
                    
            System.out.println("Broj rezultata: "+query);
            RDFModel.getInstance().beginReadTransaction();
            try{
                rezultat= (List<String>) queryExecutor
				.executeSelectQueryOverModel(query, "?count", RDFModel.getInstance().getModel());
            }catch(Exception e){
                e.printStackTrace();
                
            }finally{
                RDFModel.getInstance().endTransaction();
            }
            
            String s= rezultat.get(0);
            int brojRez=Integer.parseInt(s);
            return brojRez;
        }
        
}
