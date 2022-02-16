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
		public String teamName;
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
	public ArrayList<Node> shortestPath(int Cost, int sourceSeed, int targetSeed) {
		// 0-distance 1-time 2-competitive
		// connects from target to source
		ArrayList<Node> Visited = new ArrayList<Node>();
		ArrayList<Node> UnVisited = new ArrayList<Node>(34);
		int[] shortestDistance = new int[35];
		int[] previousVertex = new int[35];
		
		for(int i = 1; i<35;i++) {
			Node next = nodes.get(i);
			UnVisited.add(next);
			shortestDistance[i] = Integer.MAX_VALUE;
			previousVertex[i] = Integer.MAX_VALUE;
		}
		
		shortestDistance[sourceSeed] = 0;
		
		
		while(!UnVisited.isEmpty()) {
			Node current_vertex = UnVisited.get(0);
			for(int i = 0; i<UnVisited.size();i++) {
				if(shortestDistance[UnVisited.get(i).seed]<shortestDistance[current_vertex.seed]) {
					current_vertex = UnVisited.get(i);
				}
			}
			
			for(int i=0; i<current_vertex.edges.size(); i++) {
				int cost = Integer.MAX_VALUE;
				Node other_vertex = current_vertex.edges.get(i).node2;
				if(Cost==0) {
				cost = current_vertex.edges.get(i).distanceCost;}
				if(shortestDistance[other_vertex.seed]>(shortestDistance[current_vertex.seed] + cost)) {
					shortestDistance[other_vertex.seed] = (shortestDistance[current_vertex.seed] + cost);
					previousVertex[other_vertex.seed] = current_vertex.seed;
				}
				UnVisited.remove(current_vertex);
				Visited.add(current_vertex);
				
			}			
			
		}
		
		
		ArrayList<Node> returns = new ArrayList<Node>();
		int current = targetSeed;
		while(current!=sourceSeed) {
			returns.add(nodes.get(previousVertex[current]));
			current = previousVertex[current];
		}
		//returns.add(nodes.get(sourceSeed));
		return returns;
		
	}


}
