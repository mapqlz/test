
public class Edge {

	private Node u,v;
	private int type;
	private String label;
	public Edge(Node u,Node v,int type){
		this.u = u;
		this.v = v;
		this.type = type;
		label = "";
	}
	//getter and setter methods
	public Node firstEndpoint(){
		return u;
	}
	public Node secondEndpoint(){
		return v;
	}
	public int getType(){
		return this.type;
	}
	public String getLabel(){
		return this.label;
	}
	public void setLabel(String label){
		this.label = label;
	}
}
