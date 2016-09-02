package RoutingPerformance;

/**
 * Main class accept the command line arguments
 */


/**
 * @author Jiashu Chen z3411585 jche804
 * @author Rudi Purnomo z3410682 rpur114
 */

public class RoutingPerformance_IO {


	public final static String Network_Scheme_Circuit = "CIRCUIT";
	public final static String Network_Scheme_Packet = "PACKET";

	public final static String Routing_Scheme_SHP = "SHP";
    public final static String Routing_Scheme_SDP = "SDP";
    public final static String Routing_Scheme_LLP = "LLP";


    public static String current_Network_Scheme;
    public static String current_Routing_Scheme;
    public static int packet_rate;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// long startTime = System.currentTimeMillis();
	    String topology;
	    Workload workload;
	    VirtualNetworkGraph graph;

		if (args.length < 5) {
			throw new IllegalArgumentException("Incorrect number arguments supported");
		}

		if (args[0].equals(RoutingPerformance_IO.Network_Scheme_Circuit)
				|| args[0].equals(RoutingPerformance_IO.Network_Scheme_Packet)) {
			RoutingPerformance_IO.current_Network_Scheme = new String(args[0]);
		} else {
			throw new IllegalArgumentException("Expect Network Scheme to be CIRCUIT or PACKET");
		}

		if (args[1].equals(RoutingPerformance_IO.Routing_Scheme_SHP)
				|| args[1].equals(RoutingPerformance_IO.Routing_Scheme_SDP)
				|| args[1].equals(RoutingPerformance_IO.Routing_Scheme_LLP)) {
			RoutingPerformance_IO.current_Routing_Scheme = new String(args[1]);
		} else {
			throw new IllegalArgumentException("Expect Routing Scheme to be SHP, SDP or LLP");
		}

		try {
			RoutingPerformance_IO.packet_rate = Integer.parseInt(args[4]);
			if (RoutingPerformance_IO.packet_rate < 1) {
				throw new RuntimeException("packet rate is must be a positive integer");
			}
		} catch (NumberFormatException e) {
			System.err.println("Expected an Integer value for argument packet_rate:\"" + args[4] + "\"");
			System.exit(1);
		} catch (IllegalArgumentException e) {
			System.out.println("Expected specification of packet rate for a network");
			System.exit(1);
		}

		topology = new String(args[2]);
		graph = new VirtualNetworkGraph(topology);
		workload = new Workload(args[3], graph);

//		workload.printRequests();
//		graph.print();
//		System.out.println(RoutingPerformance_IO.current_Network_Scheme + " " + RoutingPerformance_IO.current_Routing_Scheme + " " + RoutingPerformance_IO.packet_rate);
//		System.out.println("Total workload size is " + workload.getWorkloadSize());
		workload.simulate();

		// long endTime   = System.currentTimeMillis();
		// double totalTime = (double) (endTime - startTime)/1000;
		// System.out.println("(Real Time of Execution: "+totalTime+" sec");
	}

}
