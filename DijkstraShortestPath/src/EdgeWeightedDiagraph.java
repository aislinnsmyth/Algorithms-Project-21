import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class EdgeWeightedDiagraph {
	
	private final int V;		//no. of vertices in this digraph
	private int E;				//number of edges in this digraph
	private Bag<DirectedEdge>[] adj;
	private int[] indegree;			//indegree of vertex v

	//Graph class where we build the map file using stop, stop.txt and transfers.txt

    public EdgeWeightedDiagraph(int V) {
        
        this.V = V;
        this.E = 0;
        this.indegree = new int[V];
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<DirectedEdge>();
    }
    
    public int V() {
    	return V;
    }
    
    public int E() {
    	return E;
    }
    
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
    
    public void addEdge(DirectedEdge e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        indegree[w]++;
        E++;
    }
    
    public Iterable<DirectedEdge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }
}

  