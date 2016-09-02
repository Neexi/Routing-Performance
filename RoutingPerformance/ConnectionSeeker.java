/**
 * this class uses DijkstraRouter to try to connect
 */
package RoutingPerformance;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jiashu Chen z3411585 jche804
 * @author Rudi Purnomo z3410682 rpur114
 */

public class ConnectionSeeker {
	//
	private VirtualNetworkGraph graph;
	//Meta-data used for the analysis

	private int successfulConnections;
	private int totalConnections;
	private int totalhops;
	private int totaldelay;
	private int successfulPacket;
	private int blockedPackets;
	private int totalPackets;
//	private int blockedConnections;

	private boolean LLP;
	private Map<Integer, List<VirtualNetworkEdge>> LLP_PathHash;

	private DecimalFormat rounder = new DecimalFormat("0.00");

	private DijkstraRouter router;

	public ConnectionSeeker(VirtualNetworkGraph graph) {
//		this.blockedConnections = 0;
		this.successfulConnections = 0;
		this.totalConnections = 0;
		this.totalhops = 0;
		this.totaldelay = 0;
		this.successfulPacket = 0;
		this.blockedPackets = 0;
		this.totalPackets = 0;

		this.graph = graph;
		if (RoutingPerformance_IO.current_Routing_Scheme.equals(RoutingPerformance_IO.Routing_Scheme_LLP)) {
			this.LLP = true;
		}
		LLP_PathHash = new HashMap<Integer, List<VirtualNetworkEdge>>();
		this.router = new DijkstraRouter(graph);
	}

	//functions that tries to connect to the network
	public boolean initConnection(Request request) {
		String source = request.getSrc();
		String dest = request.getDes();
		int id = request.getId();
		totalConnections++;
		totalPackets += request.getNum_packets();

		// if (totalConnections % 2000 == 0) {
		// 	System.out.println("Total of "+ totalConnections +" Request initiated");
		// 	this.printData();
		// 	System.out.println();
		// }

		List<VirtualNetworkNode> path = this.router.connect(source, dest);
		List<VirtualNetworkEdge> edgeAlong = this.getEdgesAlongPath(path);

		if (this.LLP) {
			LLP_PathHash.put(id, edgeAlong);
		}

		if (edgeAlong == null) {
			return false;
		}
		int tempDelay = 0;
		for (VirtualNetworkEdge e : edgeAlong) {
			if (!e.useConnection(id)) {
//				blockedConnections++;
				blockedPackets += request.getNum_packets();
				terminateConnection(request);
				return false;
			} else {
				tempDelay += e.getDelay();
			}
		}
		totaldelay += tempDelay;
		totalhops += edgeAlong.size();
		successfulPacket += request.getNum_packets();
		successfulConnections++;



		return true;
	}

	public void terminateConnection(Request request) {
		String source = request.getSrc();
		String dest = request.getDes();
		int id = request.getId();
		List<VirtualNetworkEdge> edgesAlong;

		if (this.LLP) {
			edgesAlong = this.LLP_PathHash.get(id);
		} else {
			List<VirtualNetworkNode> path = router.connect(source,  dest);
			edgesAlong = this.getEdgesAlongPath(path);
		}

		for (VirtualNetworkEdge link : edgesAlong) {
			link.freeConnection(id);
		}

	}

	private List<VirtualNetworkEdge> getEdgesAlongPath(List<VirtualNetworkNode> path) {
		List<VirtualNetworkEdge> retval = new ArrayList<VirtualNetworkEdge>();
		if (path != null) {
			for (int i = 0; i < path.size()-1; i++) {
				VirtualNetworkNode s = path.get(i);
				VirtualNetworkNode d = path.get(i+1);
				retval.add(this.graph.edgeBetween(s, d));
			}
		}

		return retval;
	}

	public void printData() {

		System.out.println("total number of virtual circuit requests: " + totalConnections);
		System.out.println("total number of packets: " + totalPackets);
		System.out.println("number of successfully routed packets: " + successfulPacket);
		System.out.println("percentage of successfully routed packets: " +
				rounder.format(((float)successfulPacket/(float)totalPackets) * 100));
		System.out.println("number of blocked packets: " + blockedPackets);
		System.out.println("percentage of blocked packets: " +
				rounder.format(((float)blockedPackets/(float)totalPackets) * 100));
		System.out.println("average number of hops per circuit: " +
				rounder.format((double)totalhops/(double)successfulConnections));
		System.out.println("average cumulative propagation delay per circuit: " +
				rounder.format((double)totaldelay/(double)successfulConnections));
	}
}
