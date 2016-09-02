package RoutingPerformance;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiashu Chen z3411585 jche804
 * @author Rudi Purnomo z3410682 rpur114
 */

public class VirtualNetworkEdge {
	private String fromTo;
	private int delay;
	private int totalCapacity;
	private List<Integer> requestIDList;
	

	public VirtualNetworkEdge(String fromTo, int delay, int totalConnection) {
		assert(fromTo.length() == 2);
		this.fromTo = fromTo;
		this.totalCapacity = totalConnection;
		this.delay = delay;
		this.requestIDList = new ArrayList<Integer>();
	}
	//return true for successful free action
	public boolean freeConnection(int requestID) {
		if (this.requestIDList.contains(requestID)) {
			this.requestIDList.remove(this.requestIDList.indexOf(requestID));
			return true;
		}
		return false;
	}
	//return true for successful deletion
	public boolean useConnection(int requestID) {
		if (this.requestIDList.size() < this.totalCapacity) {
			this.requestIDList.add(requestID);
			return true;
		}
		return false;
	}
	
	public void print() {
		System.out.println(this.fromTo + " Propagation Delay: " + this.delay + ", Total connection: " + this.totalCapacity + ", Connection In Use: " + this.getLoad());
	}
	public int getDelay() {
		return delay;
	}
	public float getLoad() {
		return (float)this.requestIDList.size()/(float)totalCapacity;
	}
	public int getFree() {
		return totalCapacity-this.requestIDList.size();
	}
	public int getTotalCapacity() {
		return totalCapacity;
	}
	public String getFromTo() {
		return new String(this.fromTo);
	}

	public String getTheOtherEnd(String oneEnd) {
		assert(oneEnd.length() == 1);
		String[] temp = this.fromTo.split("(?!^)");
		assert(temp.length == 2);
		return oneEnd.equals(temp[0]) ? temp[1] : temp[0];
	}
}
