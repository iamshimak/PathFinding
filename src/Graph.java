/*************************************************************************
 *  Compilation within a console or terminal:  
 *  Windows version: javac -cp .;stdlib.jar Graph.java
 *  OS / Linux version: javac -cp .:stdlib.jar Graph.java
 *  
 *  Execution within a console or terminal:  
 *  Windows version: java -cp .;stdlib.jar Graph tinyG.txt (or mediumG.txt)
 *  OS / Linux version: javac -cp .:stdlib.jar Graph tinyG.txt (or mediumG.txt)
 * 
 *  Dependencies: Bag.java Stack.java
 *  
 *  
 *  NOTE for execution within an IDE (i.e., Eclipse or NetBeans): 
 *  
 *  You need to set the RUN configurations such that the input data files are read in. In Eclipse, 
 *  select 
 *  
 *  "Run configurations..."
 *  tab (x)=Arguments 
 *  enter name of the file into Program arguments
 *  select as Working Directory the one where the file resides. 
 *  
 *  In NetBeans, select 
 *  
 *  Set Project Configuration
 *  Customise...
 *  Run
 *  enter name of the file into Program arguments
 *  select as Working Directory the one where the file resides.
 *  
 *  NOTE (2) for execution within an IDE: Do not forget to add the stdlib.jar to the Libraries.
 *  
 *  
 *************************************************************************/


/**
 *  The <tt>Graph</tt> class represents an undirected graph of vertices
 *  named 0 through <em>V</em>.
 *  
 *  It supports the following two primary operations: add an edge to the graph,
 *  iterate over all of the vertices adjacent to a vertex. It also provides
 *  methods for returning the number of vertices <em>V</em> and the number
 *  of edges <em>E</em>. Parallel edges and self-loops are permitted.
 *  
 */

public class Graph {
    private final int V;
    private int E;
    private Bag<Integer>[] adj;
    
    /**
     * Initialises an empty graph with <tt>V</tt> vertices and 0 edges.
     * param V the number of vertices
     * @throws IllegalArgumentException if <tt>V</tt> < 0
     */
    
    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Integer>();
        }
    }

    /**  
     * Initialises a graph from an input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices, with each entry separated by whitespace.
     * @param in the input stream
     * @throws IndexOutOfBoundsException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    
    public Graph(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            addEdge(v, w);
        }
    }

    /**
     * Initialises a new graph that is a deep copy of <tt>G</tt>.
     * @param G the graph to copy
     */
    
    public Graph(Graph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            
            Stack<Integer> reverse = new Stack<Integer>();
            for (int w : G.adj[v]) {
                reverse.push(w);
            }
            for (int w : reverse) {
                adj[v].add(w);
            }
        }
    }

    /**
     * Returns the number of vertices in the graph.
     * @return the number of vertices in the graph
     */
    
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in the graph.
     * @return the number of edges in the graph
     */
    
    public int E() {
        return E;
    }

    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the undirected edge v-w to the graph.
     * @param v one vertex in the edge
     * @param w the other vertex in the edge
     * @throws IndexOutOfBoundsException unless both 0 <= v < V and 0 <= w < V
     */
    
    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        E++;
        adj[v].add(w);
        //adj[w].add(v);
    }

    public Bag getV(int v) {
        validateVertex(v);
        return adj[v];
    }


    /**
     * Returns the vertices adjacent to vertex <tt>v</tt>.
     * @return the vertices adjacent to vertex <tt>v</tt> as an Iterable
     * @param v the vertex
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    
    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the degree of vertex <tt>v</tt>.
     * @return the degree of vertex <tt>v</tt>
     * @param v the vertex
     * @throws IndexOutOfBoundsException unless 0 <= v < V
     */
    
    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }


    /**
     * Returns a string representation of the graph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *    followed by the <em>V</em> adjacency lists
     */
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        s.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (int w : adj[v]) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}
