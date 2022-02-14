package Graph;
import java.util.ArrayList;
import java.util.Hashtable;

public class Graph<T> {
	private Hashtable<Integer, Node> nodes;
	
	public Graph(){
		nodes = new Hashtable<Integer, Node>();	
	}
	
	public Hashtable<Integer, Node> getNodes() {
		return nodes;
	}
	
	
	public class Node {
		private int seed;		
		private ArrayList<Edge> edges;
		private String teamName;
		private int x;
		private int y;
		
		
		public Node(int seed, String teamName, int x, int y){
			this.seed = seed;
			this.edges = new ArrayList<Edge>();
			this.teamName = teamName;
			this.x = x;
			this.y = y;
		}
		
		public int getX() {
			return this.x;
		}
		public int getY() {
			return this.y;
		}
		
		
		
		public void addEdge(int team2, int tcost, int dcost) {
			Node otherNode = nodes.get(team2);
			edges.add(new Edge(this, otherNode, tcost, dcost));
		}
		
		public int timeCost(T e) {
			return 0;
		}
		
		public int distanceCost(T e) {
			return 0;
		}
		
		public int competitionCost() {
			return seed;
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

	public boolean addNode(int seed, String teamName, int x, int y) {
		nodes.put(seed, new Node(seed, teamName, x, y));
		return true;
	}
	
	public boolean addEdge(int seed1, int seed2, int tcost, int dcost) {
		if (!nodes.containsKey(seed1) || !nodes.containsKey(seed2)) return false;
		nodes.get(seed1).addEdge(seed2, tcost, dcost);
		nodes.get(seed2).addEdge(seed1, tcost, dcost);
	    return true;
	}


}
