/**
 * 
 */
package rdfmodel;

import java.util.Date;
import thewebsemantic.Bean2RDF;
import thewebsemantic.RDF2Bean;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;
import com.sun.xml.ws.rx.mc.dev.AdditionalResponses;

import domain.util.Constants;

public class RDFModel {

	private RDF2Bean reader;
	private Bean2RDF writer;
	
	private static final String directory = "tdb";
	private Dataset dataset;

	private static RDFModel instance;
	
	private RDFModel() { 
		dataset = TDBFactory.createDataset(directory);
		reader = new RDF2Bean(getModel());
		writer = new Bean2RDF(getModel());
	}

	public static RDFModel getInstance() {
                
		if (instance == null) {
			instance = new RDFModel();
		}
                //instance.dataset=TDBFactory.createDataset(directory);
                //instance.reader=new RDF2Bean(instance.getModel());
                //instance.writer=new Bean2RDF(instance.getModel());
                
		return instance;
	}

	public Model getModel() {
		return dataset.getDefaultModel();
	}

	public RDF2Bean getReader() {
		return reader;
	}

	public Bean2RDF getWriter() {
		return writer;
	}
	
        
	
	public void closeDataModel() {
                sync();
		dataset.close();
	}
        
        public void save(Object o) {
		writer.save(o);
	}
	
	
	public Object load(String uri) {
		return reader.load(uri);
	}
	
	public void printOut(){
		getModel().write(System.out, "TURTLE");
	}
        
        public void setPrefixes(){
            getModel().setNsPrefix("schema", Constants.SCHEMA);
        }

        public void sync(){
            TDB.sync(dataset);
        }
        
        public void beginReadTransaction(){
            dataset.begin(ReadWrite.READ);
        }
        
        public void beginWriteTransaction(){
            dataset.begin(ReadWrite.WRITE);
        }
        
        public void commit(){
            dataset.commit();
        }
        
        public void abort(){
            dataset.abort();
        }
        
        public void endTransaction(){
            dataset.end();
        }
}
