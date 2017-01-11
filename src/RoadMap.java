import java.io.*;
import java.util.*;
public class RoadMap {

	private Graph graph;
	private int start, end, width, length, cash,toll, gain;
	
	public RoadMap(String inputFile)throws MapException{
		int nodes=0;
		String readLine;
		char letter;
		
		try{
			BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
			file.readLine(); // skip scale factor
			start = Integer.parseInt(file.readLine()); // read the integer values from beginning of file
			end = Integer.parseInt(file.readLine());
			cash = Integer.parseInt(file.readLine());
			width = Integer.parseInt(file.readLine());
			length = Integer.parseInt(file.readLine());
			toll = Integer.parseInt(file.readLine());
			gain = Integer.parseInt(file.readLine());
			graph = new Graph(width*length);
			for (int i=0;i<length;i++){
				readLine = file.readLine(); // gets line
				for(int j=0;j<2*width-1;j++){
					letter = readLine.charAt(j);
					
					if (letter == '+'){ // its a node/intersection
						nodes++;
					}
					else if (letter == 'T'){ // toll road
						graph.insertEdge(graph.getNode(nodes-1), graph.getNode(nodes),1);
					}
					else if (letter == 'C'){ // compensation road
						graph.insertEdge(graph.getNode(nodes-1), graph.getNode(nodes),-1);
					}
					else if (letter == 'F'){ // free road
						graph.insertEdge(graph.getNode(nodes-1), graph.getNode(nodes),0);
					}
					else if (letter == 'X'){ //do nothing as its a house thing
						;
					}
					else{
						System.out.println("Did not recognize symbol.");
					}
				}
				
				readLine = file.readLine();
				if (readLine== null){ // last line should cause this to be triggered
					break;
				}
				
				for (int j=0;j<2*width-1;j++){ // the vertical connections
					letter = readLine.charAt(j);
					
					if (letter == 'T'){ // toll vertical road
						graph.insertEdge(graph.getNode(nodes-width+j/2), graph.getNode(nodes+ (int)j/2), 1);
					}
					else if (letter == 'C'){ // compensation vertical road
						graph.insertEdge(graph.getNode(nodes-width+j/2), graph.getNode(nodes+ (int)j/2), -1);
					}
					else if (letter == 'F'){ // free vertical road
						graph.insertEdge(graph.getNode(nodes-width+j/2), graph.getNode(nodes+ (int)j/2), 0);
					}
					else if (letter == 'X'){ // block of houses
						;
					}
					else{
						System.out.println("Did not recognize symbol.");
					}
				}
			}
			file.close();
		}
		catch (IOException e){ // file not found/cannot open
			throw new MapException("Cannot open file");
		}
		catch (GraphException e){
			System.out.println("Something went wrong?");
		}
	}
	public Graph getGraph(){
		return this.graph;
	}
	public int getStartingNode(){
		return this.start;
	}
	public int getDestinationNode(){
		return this.end;
	}
	public int getInitialMoney(){
		return this.cash;
	}
	public Iterator<Node> findPath(int start, int destination,int initialMoney){
		
		try{
			Stack<Node> stack = new Stack<Node>(); // create stack and push start node
			stack.push(graph.getNode(start));
			graph.getNode(start).setMark(true);
			if (start==destination){ // start is the end
				return stack.iterator();
			}
			return DFS(start,destination,initialMoney,stack).iterator(); // call DFS method
		}
		catch (GraphException e){
			System.out.println("BFS did not work.");
		}
		return null;
	}
	private Stack<Node> DFS(int start, int destination, int initialMoney,Stack<Node> stack)throws GraphException{ // have to implement money still
		Node temp;
		Edge edge, returnEdge;
		int edgeType; // is it toll, free or comp road?
		Iterator<Edge> iter;
		Stack<Node> returnStack; // for the return value from the recursive calls
		while (!stack.isEmpty()){ // while the queue is not empty 
			iter = graph.incidentEdges(stack.peek());// get the adjacent edges
			while(iter.hasNext()){ //for all the incident edges
				edge = iter.next(); //get the edge
				returnEdge = graph.getEdge(edge.secondEndpoint(), edge.firstEndpoint()); // get the same edge but in reverse
				edgeType = edge.getType(); // get the cose
				if (edgeType ==1){
					edgeType = toll;
				}
				else if (edgeType == -1){
					edgeType = gain;
				}
				else {
					edgeType=0;
				}
				temp = edge.secondEndpoint();
				if (edge.getLabel().equals("")){ // is edge labelled?
					if (temp.getMark()!= true && initialMoney>=edgeType){ // is the destination marked and can we afford it?
						initialMoney -= edgeType; //decrease money
						stack.push(temp); //push onto stack
						if (temp.getName()==destination){
							return stack;
						}
						temp.setMark(true); // mark the node and edge
						edge.setLabel("discovery");
						returnEdge.setLabel("discovery"); // the edge going backwards must also be labelled
						returnStack = DFS(temp.getName(),destination,initialMoney,stack);
						if (returnStack !=null){ // if the return value is not null, a path has been found and return it
							return returnStack; 
						}
						temp = stack.pop(); //pop from stack
						temp.setMark(false); // remove mark
						graph.getEdge(stack.peek(), temp).setLabel(""); // remove label from edge
						graph.getEdge(temp, stack.peek()).setLabel("");
						edgeType = graph.getEdge(temp, stack.peek()).getType(); //return money as we did not go that way
						if (edgeType ==1){
							initialMoney +=toll;
						}
						else if (edgeType == -1){
							initialMoney +=gain;
						}
					}
				}
			}
			return null; // no paths found from this node
		}
		
		return stack; // should be null if ever hit
	}
}
