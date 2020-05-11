package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private BordersDAO dao;
	private Graph<Country, DefaultEdge> graph;
	private Map<Integer, Country> stati;

	public Model() {
		dao = new BordersDAO();
		this.stati = new HashMap<>();

		dao.loadAllCountries(stati);
	}

	public void generateGraph(Integer anno) {
		this.graph = new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class);
		List<Border> confini = dao.getCountryPairs(anno, stati);

		for (Border b : confini) {
			if (!graph.containsVertex(b.getState1()))
				graph.addVertex(b.getState1());
			if (!graph.containsVertex(b.getState2()))
				graph.addVertex(b.getState2());
			graph.addEdge(b.getState1(), b.getState2());
		}
	}

	public Set<Country> getVertici() {
		return graph.vertexSet();
	}

	public Integer getGrado(Country stato) {
		return Graphs.successorListOf(graph, stato).size();
	}

	public Integer getComponenteConnessa() {
		ConnectivityInspector<Country, DefaultEdge> ispettore = new ConnectivityInspector<Country, DefaultEdge>(graph);
		return ispettore.connectedSets().size();
	}

	public List<Country> getVicini(Country stato) {
		BreadthFirstIterator<Country, DefaultEdge> iterator = new BreadthFirstIterator<Country, DefaultEdge>(graph,
				stato);
		List<Country> vicini = new ArrayList<>();

		while (iterator.hasNext()) {
			vicini.add(iterator.next());
		}

		vicini.remove(stato);

		return vicini;
	}

}
