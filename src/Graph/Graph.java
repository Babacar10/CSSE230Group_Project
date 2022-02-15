package Graph;
import java.util.ArrayList;
import java.util.Hashtable;

public class Graph<T> {
	private Hashtable<T, Node> nodes;	
	
	public Graph(){
		nodes = new Hashtable<T, Node>();	
	}
	
	
	private class Node {
		private T id;
		private ArrayList<Edge> edges;
		private int competitiveCost;
		
		public Node(T e, int d){
			id = e;
			edges = new ArrayList<Edge>();
			competitiveCost = d;
		}
		
		public class Team {
			private int x;
			private int y;
			private String teamName;
			
			public Team() {
				
			}
		}
		
		
		public void addEdge(T e, int tcost, int dcost) {
			Node otherNode = nodes.get(e);
			edges.add(new Edge(this, otherNode, tcost, dcost));
		}
		
		public int timeCost(T e) {
			return 0;
		}
		
		public int distanceCost(T e) {
			return 0;
		}
		
		public int competitionCost() {
			return 0;
		}
	}
	
	
	private class Edge {
		private Node node1;
		private Node node2;
		private int timeCost;
		private int distanceCost;
	
		
		public Edge(Node n1, Node n2, int c,int d){
			node1 = n1;
			node2 = n2;
			timeCost = c;
			distanceCost = d;
			
		}
	}

	public boolean addNode(T e, int c) {
		nodes.put(e, new Node(e,c));
		return true;
	}
	
	public boolean addEdge(T e1, T e2, int tcost, int dcost) {
		if (!nodes.containsKey(e1) || !nodes.containsKey(e2)) return false;
		nodes.get(e1).addEdge(e2, tcost, dcost);
	    return true;
	}


}
