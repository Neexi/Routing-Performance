package RoutingPerformance;

/**
 * @author Jiashu Chen z3411585 jche804
 * @author Rudi Purnomo z3410682 rpur114
 */

import java.util.ArrayList;
import java.util.List;

public class VirtualNetworkNode {
	
	private String nodeName;
	private String neighbours;
	private List<VirtualNetworkEdge> edges;
	
	public VirtualNetworkNode(String nodeName) {
		this.nodeName = nodeName;
		this.neighbours = new String("");
		this.edges = new ArrayList<VirtualNetworkEdge>();
	}
	
	public void addEdge(String to, VirtualNetworkEdge e) {
		this.neighbours = this.neighbours + to;
		this.edges.add(e);
	}
	
	public VirtualNetworkEdge edgeTo(String des) {
		assert(des.length() == 1);
		for (VirtualNetworkEdge e : this.edges) {
			if (des.equals(e.getTheOtherEnd(this.nodeName))) {
				return e;
			}
		}
	    throw new RuntimeException("Never should reach this line in edgeTo");
	}
	
	public int getDistanceTo(String des) {
		assert(des.length() == 1);
		for (VirtualNetworkEdge e : this.edges) {
			if (des.equals(e.getTheOtherEnd(this.nodeName))) {
				return e.getDelay();
			}
		}
	    throw new RuntimeException("Never should reach this line in getDistanceTo");
	}
	
	public float getLoadTo(String des) {
		assert(des.length() == 1);
		for (VirtualNetworkEdge e : this.edges) {
			if (des.equals(e.getTheOtherEnd(this.nodeName))) {
				return e.getLoad();
			}
		}
	    throw new RuntimeException("Never should reach this line in getLoadTo");
	}
	
	public String getNodeName() {
		return this.nodeName;
	}
	
	public String getNeighbours() {
		return neighbours;
	}
	
	public List<VirtualNetworkEdge> getEdges() {
		return edges;
	}
}
