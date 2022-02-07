package Graph;
import java.util.ArrayList;
import java.util.Hashtable;

public class Graph<T> {
	private Hashtable<T, Node> nodes;

	public Graph(){
		nodes = new Hashtable<T, Node>();	
	}
	
	
	private class Node {
		private T element;
		private ArrayList<Edge> neighbors;
		private int competitivecost;
		
		public Node(T e,int d){
			element = e;
			neighbors = new ArrayList<Edge>();
			competitivecost= d;
		}
		
		public void addEdge(T e, int tcost, int dcost) {
			Node otherNode = nodes.get(e);
			neighbors.add(new Edge(otherNode, tcost,dcost));
		}
		
		public void timecost(T e) {
			
		}
		
		public void distancecost(T e) {
			
		}
		
		public void competitioncost() {
			
		}
	}
	
	
	private class Edge {
		private Node otherNode;
		private int timecost;
		private int distancecost;
	
		
		public Edge(Node n, int c,int d){
			otherNode = n;
			timecost = c;
			distancecost = d;
			
		}
	}

	public boolean addNode(T e, int c) {
		nodes.put(e, new Node(e,c));
		return true;
	}
	
	public boolean addEdge(T e1, T e2, int tcost, int dcost) {
		if (!nodes.containsKey(e1) && !nodes.containsKey(e2)) return false;
		nodes.get(e1).addEdge(e2, tcost, dcost);
	    return true;
	}


}
