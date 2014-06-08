package queries;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class QueryExecutor {

	public List<String> executeSelectQueryOverSparqlEndpoint(String query,
			String variable, String sparqlEndpoint) {
		
		// Execute the query over the model
		QueryExecution qe = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
		return executeSelectQuery(query, variable, qe);
	}
	
	public Collection<String> executeSelectQueryOverModel(String query,
			String variable, Model model) {
		
		// Execute the query over the model
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		return executeSelectQuery(query, variable, qe);
	}
	
	public List<String> executeSelectQuery(String query, String variable, QueryExecution qe) {
		// Execute the query over the model
		ResultSet resultSet = qe.execSelect();
		
		List<String> results = new LinkedList<String>();
		
		// obtain results from the result set
		while (resultSet.hasNext()) {
			QuerySolution solution = resultSet.nextSolution();
			RDFNode value = solution.get(variable);
			
			if (value.isLiteral())
				results.add(((Literal) value).getLexicalForm());
			else
				results.add(((Resource) value).getURI());
		}
		qe.close();
		
		return results;
	}
	
	public ResultSet executeSelectSparqlQuery(String query, String sparqlEndpoint) {
		// Execute the query over the model
		QueryExecution qe = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
		return qe.execSelect();
	}

	public Model executeDescribeSparqlQuery(String query, Model model) {
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		Model resultModel = qe.execDescribe();
		
		// Important - free up resources used running the query
		qe.close();
		
		return resultModel;
	}

}
