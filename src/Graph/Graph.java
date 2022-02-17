package Graph;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

public class Graph<T> {
	private Hashtable<Integer, Node> nodes;
	
	public Graph() {
		nodes = new Hashtable<Integer, Node>();	
	}
	
	public Hashtable<Integer, Node> getNodes() {
		return nodes;
	}
	
	public class Node {
		public int seed;		
		public ArrayList<Edge> edges;
		public String teamName;
		private int x;
		private int y;
		
		public Node(int seed, String teamName, int x, int y) {
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
		
		public double distanceAsCrowFlies(Node n) {
			return Math.sqrt(Math.pow(x + n.x, 2) + Math.pow(y + n.y, 2));
		}
		
		public void addEdge(T e, int tcost, int dcost) {
			Node otherNode = nodes.get(e);
		}

		public void addEdge(int team2, int tcost, int dcost) {
			Node otherNode = nodes.get(team2);
			edges.add(new Edge(this, otherNode, tcost, dcost));
		}
		
		public ArrayList<Node> getNeighbors() {
			ArrayList<Node> neighbors = new ArrayList<Node>(edges.size());
			for (Edge e: edges) neighbors.add(e.node2);
			return neighbors;
		}
		
		public int timeCost(T e) {
			return 0;
		}
		
		public int competitionCost() {
			return seed;
		}

		public double distanceCost(Node n) {
			for (Edge e: edges) {
				if (e.node2 == n) {
					return e.distanceCost;
				}
			}
			return -1;
		}
	}
	
	public class Edge {
		public Node node1;
		public Node node2;
		public int timeCost;
		public int distanceCost;
	
		
		public Edge(Node n1, Node n2, int c, int d){
			node1= n1;
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
		int[] edgeamount = new int[35];
		
		for(int i = 1; i<35;i++) {
			Node next = nodes.get(i);
			UnVisited.add(next);
			shortestDistance[i] = Integer.MAX_VALUE;
			previousVertex[i] = Integer.MAX_VALUE;
			edgeamount[i]=1;
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
				else if (Cost == 1) {
					cost = current_vertex.edges.get(i).timeCost;
				} 
				else {
					cost = current_vertex.edges.get(i).node2.seed;
				}
				//if(Cost != 2) {
				if(shortestDistance[other_vertex.seed]>(shortestDistance[current_vertex.seed] + cost)) {
					shortestDistance[other_vertex.seed] = (shortestDistance[current_vertex.seed] + cost);
					previousVertex[other_vertex.seed] = current_vertex.seed;
				}
				//}
//				else {
//					if(shortestDistance[other_vertex.seed] == Integer.MAX_VALUE) {
//						shortestDistance[other_vertex.seed] =  cost;
//						edgeamount[other_vertex.seed]= edgeamount[current_vertex.seed]+1;
//						previousVertex[other_vertex.seed] = current_vertex.seed;
//					}
//					else if(shortestDistance[other_vertex.seed]<((shortestDistance[current_vertex.seed] + cost)/(edgeamount[current_vertex.seed]+1))) {
//						edgeamount[other_vertex.seed]= edgeamount[current_vertex.seed]+1;
//						shortestDistance[other_vertex.seed] = (shortestDistance[current_vertex.seed] + cost)/(edgeamount[current_vertex.seed]+1);
//						previousVertex[other_vertex.seed] = current_vertex.seed;
//					}
//				}
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
	
	public ArrayList<Node> tripPlanner(int sourceSeed, int timemax){
		ArrayList<Node> returns = new ArrayList<Node>();
		Node Source = nodes.get(sourceSeed);
		Random rand = new Random();
		//rand.nextInt(bound);
		int currentdist =0;
		boolean foundpath = false;
		while(!foundpath) {
			
			Node traverse = Source;
			while(traverse!=Source) {
				for(int i=0; i<traverse.edges.size();i++ ){
					if(traverse.edges.get(i).node2.seed==sourceSeed ){
						if(timemax - (currentdist+traverse.edges.get(i).timeCost) < 100) {
							returns.add(traverse.edges.get(i).node2);
							foundpath=true;
							break;
						}
					}
				}
				if(foundpath==false) {
				int next = rand.nextInt(traverse.edges.size());
				
				returns.add(traverse.edges.get(next).node2);
				currentdist+=(traverse.edges.get(next).timeCost);
				if(traverse.edges.get(next).node2==Source) {
					if (timemax-currentdist < 100) {
						foundpath=true;
						break;
					} else {
						returns.clear();
						currentdist=0;
						break;
					}
				}
				if (currentdist>timemax) {
					returns.clear();
					currentdist=0;
					break;
				} 
				traverse = traverse.edges.get(next).node2;
				
				}
			}
			
		}
		return returns;
	}

	
	public LinkedList<Node> A_Star(Node start, Node goal) {
		PriorityQueue<APath> openSet = new PriorityQueue<APath>();
		LinkedList<APath> closedSet = new LinkedList<APath>();
		
		openSet.add(new APath(start, goal));
		
		while (!openSet.isEmpty()) {
			APath current = openSet.poll();
			
			//Potential Comparison Problem
			if (current.getCurrent() == goal) {
				current.addToPath(goal);
				return current.path;
			}
			
			for (Node neighbor: current.getCurrent().getNeighbors()) {
				APath successor = new APath(neighbor, current);
				boolean add = true;
				
				Iterator<APath> i = openSet.iterator();
				while (i.hasNext()) {
					//Potential Comparison Problem
					APath predecessor = i.next();
					if (predecessor.getCurrent() == successor.getCurrent() && predecessor.compareTo(successor) < 0) {
						add = false;
						break;
					}
				}
				
				for (int j = 0; j < closedSet.size(); j++) {
					//Potential Comparison Problem
					if (closedSet.get(j).getCurrent() == successor.getCurrent() && closedSet.get(j).compareTo(successor) < 0) {
						add = false;
						break;
					}
				}
				
				if (add) {
					openSet.add(successor);
				}
			}
			
			closedSet.add(current);
		}
		
		return null;
	}
	
	

	private class APath implements Comparable<APath> {		
		private Node start;
		private Node end;
		private LinkedList<Node> path;
		private double gScore;
		
		public APath(Node s, Node e) {
			start = s;
			end = e;
			path = new LinkedList<Node>();
			gScore = 0;
			path.addFirst(start);
		}
		
		public double getF() {
			return gScore + start.distanceAsCrowFlies(end);
		}

		public APath(Node n, APath p) {
			start = p.start;
			end = p.end;
			path = p.path;
			gScore = p.gScore + p.path.getFirst().distanceCost(n);
			path.addFirst(n);
		}
		
		public Node getCurrent() {
			return path.peek();
		}
		
		public void addToPath(Node n) {
			path.addFirst(n);
		}
		
		public int compareTo(APath o) {
			if (this.getF() == o.getF()) return 0;
			return this.getF() < o.getF() ? -1 : 1;
		}
	}
}
