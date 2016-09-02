package RoutingPerformance;


/**
 * @author Jiashu Chen z3411585 jche804
 * @author Rudi Purnomo z3410682 rpur114
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class Workload {
	private double time = 0;
	private TreeMap<Double, ArrayList<Request>> requests;
	//used in simulation
	private TreeMap<Double, ArrayList<Request>> expired;
	private int id = 0;
	private ConnectionSeeker router;

	
	public Workload(String fileName, VirtualNetworkGraph graph){
		this.requests = new TreeMap<Double, ArrayList<Request>>();
		this.expired = new TreeMap<Double, ArrayList<Request>>();
		
		if (RoutingPerformance_IO.current_Network_Scheme.equals(RoutingPerformance_IO.Network_Scheme_Circuit)) {
			this.initCircuitWorkload(fileName, graph);
		} else if (RoutingPerformance_IO.current_Network_Scheme.equals(RoutingPerformance_IO.Network_Scheme_Packet)) {
			this.initPacketWorkload(fileName, graph);
		} else {
			throw new RuntimeException("current Network Scheme not supported");
		}
	}
	
	private void initCircuitWorkload(String fileName, VirtualNetworkGraph graph) {
		try {
			BufferedReader workload;
			workload = new BufferedReader(new FileReader(fileName));
			String line = workload.readLine();
			
			while (line != null) {
				String[] temp = line.split(" ");
				double startTime = Double.parseDouble(temp[0]);
				double duration = Double.parseDouble(temp[3]);
				int num_packets = circuit_num_packet(duration);
				Request request = new Request(startTime, temp[1], temp[2], duration, id, num_packets);
				
				if (requests.containsKey(startTime)) {
					requests.get(startTime).add(request);
				} else {
					ArrayList<Request> tempList = new ArrayList<Request>();
					tempList.add(request);
					requests.put(startTime, tempList);
				}
				
				id++;
				line = workload.readLine();
			}//
			workload.close();
			this.router = new ConnectionSeeker(graph);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private int circuit_num_packet(double duration) {
		if(round6dec(duration * RoutingPerformance_IO.packet_rate) > Math.floor(duration * RoutingPerformance_IO.packet_rate)) {
			return (int) Math.ceil(duration * RoutingPerformance_IO.packet_rate);
		} else {
			return (int) Math.floor(duration * RoutingPerformance_IO.packet_rate);
		}
	}
	
	private void initPacketWorkload(String fileName, VirtualNetworkGraph graph) {
		try {
			BufferedReader workload;
			workload = new BufferedReader(new FileReader(fileName));
			String line = workload.readLine();
			while (line != null) {
				String[] temp = line.split(" ");
				double startTime = Double.parseDouble(temp[0]);
				double duration = Double.parseDouble(temp[3]);
				double packetDuration = 1/(double)RoutingPerformance_IO.packet_rate;
				double curr, currStartTime;
				for(curr = 0 ; round6dec(curr+packetDuration) < duration ; curr += packetDuration){
					currStartTime = round6dec(startTime + curr);
					Request request = new Request(currStartTime, temp[1], temp[2], packetDuration, id, 1);
					
					if (requests.containsKey(currStartTime)) {
						requests.get(currStartTime).add(request);
					} else {
						ArrayList<Request> tempList = new ArrayList<Request>();
						tempList.add(request);
						requests.put(currStartTime, tempList);
					}
					
					id++;
				}
				//For the last packet
				double lastPacketStart = round6dec(startTime + curr);
				double lastPacketExp = round6dec(duration - curr);
				Request request = new Request(lastPacketStart, temp[1], temp[2], lastPacketExp, id, 1);
				
				if (requests.containsKey(lastPacketStart)) {
					requests.get(lastPacketStart).add(request);
				} else {
					ArrayList<Request> tempList = new ArrayList<Request>();
					tempList.add(request);
					requests.put(lastPacketStart, tempList);
				}
				
				id++;
				line = workload.readLine();
			}
			workload.close();
			
			this.router = new ConnectionSeeker(graph);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public Map<Double, ArrayList<Request>> getRequests() {
		return requests;
	}
	
	public void printRequests() {
		for (double key : requests.keySet()) {
			for (Request r : this.requests.get(key)) {
				r.print();
			}
		}
	}
	
	
	
	public void simulate() {
		while (!requests.isEmpty() || !expired.isEmpty()) {
			//time calculator
			if (requests.isEmpty()) {
				time = round6dec(expired.firstKey());
			} else if (expired.isEmpty()) {
				time = round6dec(requests.firstKey());
			} else {
				time = round6dec(Math.min(requests.firstKey(), expired.firstKey()));
			}
			time = Workload.round6dec(time);
			//
			ArrayList<Request> requestInitList = requests.get(time);
			ArrayList<Request> requestExpireList = expired.get(time);
			//request time.
			if (requestExpireList != null) {
				for (Request requestExpire : requestExpireList) {
					router.terminateConnection(requestExpire);
				}
				expired.remove(time);
			}
			if (requestInitList != null) {
				for (Request requestInit : requestInitList) {
					if (router.initConnection(requestInit)) {
						double expire_timer = requestInit.getDuration() + time;
						expire_timer = Workload.round6dec(expire_timer);
						if (expired.containsKey(expire_timer)) {
							expired.get(expire_timer).add(requestInit);
						} else {
							ArrayList<Request> tempList = new ArrayList<Request>();
							tempList.add(requestInit);
							expired.put(expire_timer, tempList);
						}		
					}
				}
				requests.remove(time);
			}

		}
		this.time = 0;
		router.printData();
	}
	
	
	private static double round6dec(double value) {
	    return (double) Math.round(value * 1000000)/ 1000000;
	}

	public int getWorkloadSize() {
		int i = 0;
		for (double key : requests.keySet()) {
			i += this.requests.get(key).size();
		}
		return i;
	}
	
//	private static double round(double value, int places) {
//		if (places < 0) throw new IllegalArgumentException();
//		
//		long factor = (long) Math.pow(10, places);
//		value = value * factor;
//		long tmp = Math.round(value);
//		return (double) tmp / factor;
//	}
}
