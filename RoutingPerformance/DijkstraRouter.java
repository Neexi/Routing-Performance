package RoutingPerformance;
/**
 * Graph search algorithm
 * Connection Establisher
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jiashu Chen z3411585 jche804
 * @author Rudi Purnomo z3410682 rpur114
 */

public class DijkstraRouter {

	//List of all VirtualNetworkNodes/VirtualNetworkEdgess in graph
	private Map<String, VirtualNetworkNode> nodes;
	private VirtualNetworkGraph graph;
	
	private Map<VirtualNetworkNode, VirtualNetworkNode> pred;
	private Map<VirtualNetworkNode, Float> distance;
	
	private Set<VirtualNetworkNode> visited;
	private Set<VirtualNetworkNode> unvisited;
	
	//hash for visited map used to improve efficiency
	private Map<VirtualNetworkNode, Map<VirtualNetworkNode, VirtualNetworkNode>> shortestpath; 
	private Map<VirtualNetworkNode, Map<VirtualNetworkNode, Float>> shortestdistance; 
	
	//boolean Flags
	private boolean SHP;
	private boolean SDP;
	private boolean LLP;
	
	public DijkstraRouter(VirtualNetworkGraph graph) {
		this.nodes = graph.getNodeHash();
		this.graph = graph;
		this.setFlags();
		this.shortestpath = new HashMap<VirtualNetworkNode, Map<VirtualNetworkNode,VirtualNetworkNode>>();
		this.shortestdistance = new HashMap<VirtualNetworkNode, Map<VirtualNetworkNode,Float>>();
	}
	/**
	 * helper function for constructor to determine the scheme
	 */
	private void setFlags() {
		this.SHP = false;
		this.SDP = false;
		this.LLP = false;
		if (RoutingPerformance_IO.current_Routing_Scheme.equals(RoutingPerformance_IO.Routing_Scheme_SHP)) {
			this.SHP = true;
		} else if (RoutingPerformance_IO.current_Routing_Scheme.equals(RoutingPerformance_IO.Routing_Scheme_SDP)) {
			this.SDP = true;
		} else if (RoutingPerformance_IO.current_Routing_Scheme.equals(RoutingPerformance_IO.Routing_Scheme_LLP)) {
			this.LLP = true;
		} else {
			throw new RuntimeException("Invalid Scheme, should have been one of SHP SDP LLP");
		}
	}
	
	//function related to dijkstra search
	public void execute(VirtualNetworkNode src) {
		this.visited = new HashSet<VirtualNetworkNode>();
		this.unvisited = new HashSet<VirtualNetworkNode>();
		this.distance = new HashMap<VirtualNetworkNode, Float>();
		this.pred = new HashMap<VirtualNetworkNode, VirtualNetworkNode>();
		this.distance.put(src, 0f);
		this.unvisited.add(src);
		while (unvisited.size() > 0) {
			VirtualNetworkNode node = getMinimum(unvisited);
			visited.add(node);
			unvisited.remove(node);
			findMinimalDistances(node);
		}
	}
	
	private Float getShortestDistanceHash(VirtualNetworkNode dest) {
		Float d = distance.get(dest);
		if (d == null) {
			return Float.MAX_VALUE;
		} else {
			return d;
		}
	}
	
	private boolean isVisited(VirtualNetworkNode node) {
		return visited.contains(node);
	}
	
	private void findMinimalDistances(VirtualNetworkNode nodeSrc) {
		List<VirtualNetworkNode> adjacentNodes = this.getUnvisitedNeighbours(nodeSrc);
		for (VirtualNetworkNode nodeAdj : adjacentNodes) {
			if (this.LLP) {
				if (getShortestDistanceHash(nodeAdj) > getDistance(nodeSrc, nodeAdj)) {
					if (getDistance(nodeSrc, nodeAdj) > getShortestDistanceHash(nodeSrc)) {
						distance.put(nodeAdj, getDistance(nodeSrc, nodeAdj));
					} else {
						distance.put(nodeAdj, getShortestDistanceHash(nodeSrc));
					}
					this.pred.put(nodeAdj, nodeSrc);
					this.unvisited.add(nodeAdj);
				}
			} else {
				float distanceBetween = this.getDistance(nodeSrc, nodeAdj);
				if (getShortestDistanceHash(nodeAdj) > getShortestDistanceHash(nodeSrc) + distanceBetween) {
					this.distance.put(nodeAdj, new Float(getShortestDistanceHash(nodeSrc) + distanceBetween));
					this.pred.put(nodeAdj, nodeSrc);
					this.unvisited.add(nodeAdj);
				}
			}
		}
	}
	
	private List<VirtualNetworkNode> getUnvisitedNeighbours(VirtualNetworkNode nodeSrc) {
		List<VirtualNetworkNode> retval = new ArrayList<VirtualNetworkNode>();
		for (VirtualNetworkNode n : this.graph.neighbours(nodeSrc)) {
			if (!this.isVisited(n)) {
				retval.add(n);
			}
		}
		return retval;
	}
	private float getDistance(VirtualNetworkNode nodeSrc, VirtualNetworkNode nodeDes) {
		if (this.SHP) {
			return 1;
		} else if (this.SDP) {
			return this.graph.delayBetween(nodeSrc, nodeDes);
		} else if (this.LLP) {
			return this.graph.loadBetween(nodeSrc, nodeDes);
		}
		throw new RuntimeException("Path doesn't exist");
	}
	
	private VirtualNetworkNode getMinimum(Set<VirtualNetworkNode> nodeSet) {
		VirtualNetworkNode minimum = null;
		for (VirtualNetworkNode vertex : nodeSet) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistanceHash(vertex) < getShortestDistanceHash(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}
	
	private List<VirtualNetworkNode> getPath(VirtualNetworkNode des) {
		List<VirtualNetworkNode> path = new ArrayList<VirtualNetworkNode>();
		VirtualNetworkNode step = des;
		if (pred.get(step) == null) {
			return null;
		}
		path.add(step);
		while (pred.get(step) != null) {
			step = pred.get(step);
			path.add(step);
		}
		Collections.reverse(path);
		return path;
	}
	
	public List<VirtualNetworkNode> connect(String source, String destination)
	{
		VirtualNetworkNode src = this.nodes.get(source);
		VirtualNetworkNode dest = this.nodes.get(destination);
		
		if (src == null || dest == null) {
			System.out.println("Request contains nodes does not exist");
			new RuntimeException("Request contains nodes does not exist");
		}
		
		if (SHP || SDP) {
			//create hash for efficiency for SHP and SDP
			//shortest path under SHP and SDP
			//stays the same with respect to time
			if (shortestpath.get(src) == null) {
//				System.out.println("building hash for node: "+source);
				this.execute(src);
				shortestpath.put(src, pred);
				shortestdistance.put(src, distance);
			} else {
				this.pred = shortestpath.get(src);
				this.distance = shortestdistance.get(src);
			}
		} else if (LLP) {
			this.execute(src);
		}
		return getPath(dest);
	}
	
}
