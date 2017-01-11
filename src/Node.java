//Node class representing an intersection
public class Node {

	private int name;
	private boolean mark;
	public Node(int name){
		this.name = name;
	}
	public void setMark(boolean mark){
		this.mark = mark;
	}
	public int getName(){
		return this.name;
	}
	public boolean getMark(){
		return this.mark;
	}
}
