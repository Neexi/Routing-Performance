package RoutingPerformance;

/**
 * @author Jiashu Chen z3411585 jche804
 * @author Rudi Purnomo z3410682 rpur114
 */


public class Request {

	private String src;
	private String des;
	private double duration;
	private double start;
	private int num_packets;
	private int id;
	
	public Request(double time, String source, String destination, double timeDuration, int id, int num_packet)
	{
		this.src = source;
		this.des = destination;
		this.duration = timeDuration;
		this.start = time;
		this.id = id;
		this.num_packets = num_packet;
	}

	public String getSrc() {
		return src;
	}
	public String getDes() {
		return des;
	}
	public double getDuration() {
		return duration;
	}
	public double getStart() {
		return start;
	}
	public int getId() {
		return id;
	}
	public int getNum_packets() {
		return num_packets;
	}
	
	public void print()
	{
		System.out.println("Request:"+ src + " -> " + des + "; starting at: "+ start + "; duration: " + duration + "; number of packets: "+ num_packets);
	}

}
