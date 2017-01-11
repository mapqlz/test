import java.util.*;

public class Graph implements GraphADT{

	private Node[] graph;
	private Edge[][] matrix;
	
	public Graph(int n){
		
		graph = new Node[n];
		//create graph array
		for(int i=0;i<n;i++){
			graph[i] = new Node(i);
		}
		matrix = new Edge[n][n];//adjacency matrix
		for (int i =0;i<matrix.length;i++){ //initialize matrix to null
			for (int j =0;j<matrix[i].length;j++){
				matrix[i][j] = null;
			}
		}
		
		
	}
	@Override
	public void insertEdge(Node nodeu, Node nodev, int edgeType) throws GraphException {
		int u = nodeu.getName(), v= nodev.getName();
		
		if (u < 0 || u >= graph.length || v <0 || v >= graph.length){// check if nodes exist
			throw new GraphException("Nodes do not exist.");
		}
		else if (edgeType >= 2 || edgeType <= -2){ // check if its proper edgeType
			throw new GraphException("Invalid edge Type.");
		}
		else if (matrix[u][v] != null){ // does edge already exist?
			throw new GraphException("There is already an edge there.");
		}
		matrix[u][v] = new Edge(nodeu,nodev,edgeType); // it is undirected graph so have to add edge both ways
		matrix[v][u] = new Edge(nodev,nodeu,edgeType);
		
	}

	@Override
	public Node getNode(int name) throws GraphException {
		
		if (name <0 || name >= graph.length){ // node exists?
			throw new GraphException("Not in graph");
		}
		else{
			return graph[name]; //array sorted by name 
		}
	}

	@Override
	public Iterator<Edge> incidentEdges(Node u) throws GraphException {
		int initial = u.getName();
		if (initial <0 || initial >= graph.length){ // node exists?
			throw new GraphException("Node not in graph");
		}
		ArrayList<Edge> incident = new ArrayList<Edge>(); // create arraylist to store incident egdes
		for (int i=0;i <matrix[initial].length;i++){ // loop through proper row in matrix to find incident edges
			if (matrix[initial][i] != null){ // null means no edge
				incident.add(matrix[initial][i]);
			}
		}
		if (incident.isEmpty()){ // no edges are incident
			return null;
		}
		return incident.iterator();
	}

	@Override
	public Edge getEdge(Node u, Node v) throws GraphException {
		int initial = u.getName(), end = v.getName();
		if (initial < 0 || initial >= graph.length || end < 0 || end >= graph.length){ //nodes exist?
			throw new GraphException("Nodes do not exist");
		}
		if (matrix[initial][end]==null){ // no edge there
			throw new GraphException("No edge between those nodes.");
		}
		else {
			return matrix[initial][end];
		}
	}

	@Override
	public boolean areAdjacent(Node u, Node v) throws GraphException {
		int initial = u.getName(),end = v.getName();
		if (initial < 0 || initial >= graph.length || end < 0 || end >= graph.length){ //nodes exist?
			throw new GraphException("Nodes do not exist");
		}
		if (matrix[initial][end] == null){ // edge there?
			return false;
		}
		else{
			return true;
		}
	}

}
