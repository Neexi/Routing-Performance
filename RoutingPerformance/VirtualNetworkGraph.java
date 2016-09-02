package RoutingPerformance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Jiashu Chen z3411585 jche804
 * @author Rudi Purnomo z3410682 rpur114
 */

public class VirtualNetworkGraph {
	
	private TreeMap<String, VirtualNetworkNode> nodes;
	private List<VirtualNetworkEdge> edges;

	public VirtualNetworkGraph(String fileName){
		this.nodes = new TreeMap<String, VirtualNetworkNode>();
		this.edges = new ArrayList<VirtualNetworkEdge>();
		
		BufferedReader topology;
		try {
			topology = new BufferedReader(new FileReader(fileName));
			String line = topology.readLine();
			
			while (line != null) {
				this.addEdge(line);
				line = topology.readLine();
			}

			topology.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void addEdge (String line) {
		String[] temp = line.split(" ");
		String nodeNameFrom = temp[0];
		String nodeNameTo = temp[1];
		
		assert(nodeNameFrom != nodeNameTo && nodeNameFrom.length() == 1 && nodeNameTo.length() == 1);
		
		VirtualNetworkNode src = this.nodes.get(nodeNameFrom);
		VirtualNetworkNode des = this.nodes.get(nodeNameTo);
		
		if (src == null) {
			src = new VirtualNetworkNode(nodeNameFrom);
			this.nodes.put(nodeNameFrom, src);
		}
		if (des == null) {
			des = new VirtualNetworkNode(nodeNameTo);
			this.nodes.put(nodeNameTo, des);
		}
		String fromTo = new String(nodeNameFrom+nodeNameTo);
		int delay = Integer.parseInt(temp[2]);
		int totalConnection = Integer.parseInt(temp[3]);
		if (delay < 1 || delay > 199) {
			throw new RuntimeException("delay must between 0 < d < 200 according to spec");
		}
		if (totalConnection < 1 || totalConnection > 99) {
			throw new RuntimeException("totalConnection must between 0 < C < 100 according to spec");
		}
		VirtualNetworkEdge newEdge = new VirtualNetworkEdge(fromTo, delay, totalConnection);
		src.addEdge(nodeNameTo, newEdge);
		des.addEdge(nodeNameFrom, newEdge);
		this.edges.add(newEdge);
	}
	
	public Map<String, VirtualNetworkNode> getNodeHash() {
		return new TreeMap<String, VirtualNetworkNode>(this.nodes);
	}
	
	public List<VirtualNetworkEdge> getEdgeList() {
		return new ArrayList<VirtualNetworkEdge>(this.edges);
	}
	
	public int size() {
		return this.nodes.size();
	}
	
	public List<VirtualNetworkNode> neighbours(VirtualNetworkNode n) {
		List<VirtualNetworkNode> retval = new ArrayList<VirtualNetworkNode>();
		for (String s : n.getNeighbours().split("(?!^)")) {
			if (this.nodes.get(s) != null) {
				retval.add(this.nodes.get(s));
			} else {
				throw new RuntimeException("Runtime Error node trying to retrieve does not exist");
			}
		}
		return retval;
	}
	public VirtualNetworkEdge edgeBetween(VirtualNetworkNode src, VirtualNetworkNode des) {
		assert(this.nodes.containsKey(src.getNodeName()) && this.nodes.containsKey(des.getNodeName()));
		assert(src.edgeTo(des.getNodeName()) == des.edgeTo(src.getNodeName()));
		return src.edgeTo(des.getNodeName());
	}
	public int delayBetween(VirtualNetworkNode src, VirtualNetworkNode des) {
		assert(this.nodes.containsKey(src.getNodeName()) && this.nodes.containsKey(des.getNodeName()));
		assert(src.getDistanceTo(des.getNodeName()) == des.getDistanceTo(src.getNodeName()));
		return src.getDistanceTo(des.getNodeName());
	}
	
	public float loadBetween(VirtualNetworkNode src, VirtualNetworkNode des) {
		assert(this.nodes.containsKey(src.getNodeName()) && this.nodes.containsKey(des.getNodeName()));
		assert(src.getLoadTo(des.getNodeName()) == des.getLoadTo(src.getNodeName()));
		return src.getLoadTo(des.getNodeName());
	}
	
	public void print() {
		for (String key: this.nodes.keySet()) {
			VirtualNetworkNode node = this.nodes.get(key);
			System.out.println("Node " + node.getNodeName() + ": ");
			System.out.println("    Has Neighbours: " + node.getNeighbours());
			List<VirtualNetworkEdge> edges = node.getEdges();
			for (VirtualNetworkEdge e : edges) {
				System.out.print("    ");
				e.print();
			}
			System.out.println();
		}
	}
	
	/**
	 * used for testing
	 * @param args
	 */
	public static void main(String[] args) {
		VirtualNetworkGraph temp = new VirtualNetworkGraph("./topology.txt");
		temp.print();
	}

}
