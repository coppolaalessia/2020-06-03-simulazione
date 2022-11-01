package it.polito.tdp.PremierLeague.model;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao ;
	private Graph<Player,DefaultWeightedEdge> grafo;
	
	
	public Model() {
		this.dao = new PremierLeagueDAO();
	}
	
	public void creaGrafo(int goal) {
		this.grafo= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(goal));
		
		//aggiungo archi
		for(Adiacenza a : this.dao.getAdiacenza(goal)) {
			if(this.grafo.containsVertex(a.getP1()) && this.grafo.containsVertex(a.getP2())) {
				int peso =a.getPeso();
				if(peso>0) {
					Graphs.addEdgeWithVertices(this.grafo,a.getP1(), a.getP2(), peso);
				}
				else if(peso<0) {
					Graphs.addEdgeWithVertices(this.grafo,a.getP2(), a.getP1(), (-1)*peso);
				}
			}
		}
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
}
