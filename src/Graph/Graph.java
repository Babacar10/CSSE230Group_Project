package Graph;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Graph<T> {
	private Hashtable<Integer, Node> nodes;
	
	public Graph() {
		nodes = new Hashtable<Integer, Node>();	
	}
	
	public Hashtable<Integer, Node> getNodes() {
		return nodes;
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
	
	
	public class Node {
		private int seed;		
		private ArrayList<Edge> edges;
		private String teamName;
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
			for (Edge e: edges) neighbors.add(e.endNode);
			return neighbors;
		}
		
		public int timeCost(T e) {
			return 0;
		}
		
		public int distanceCost(Node n) {
			for (Edge e: edges) {
				//Potential Comparison Problem
				if (e.endNode == n) {
					return e.distanceCost;
				}
			}
			return -1;
		}
		
		public int competitionCost() {
			return seed;
		}
	}
	
	private class Edge {
		private Node startNode;
		private Node endNode;
		private int timeCost;
		private int distanceCost;
	
		
		public Edge(Node n1, Node n2, int c, int d){
			startNode = n1;
			endNode = n2;
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
	
	public class APath implements Comparable<APath> {		
		private Node start;
		private Node end;
		private LinkedList<Node> path;
		private int gScore;
		
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
