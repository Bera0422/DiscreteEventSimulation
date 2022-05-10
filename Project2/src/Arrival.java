//Arrival parent class which hold the player, time of arrival and service time infos.
public class Arrival {

	private Player player;
	private double arrivalTime;
	private double serviceTime;
	
	public Arrival(Player player, double arrivalTime, double serviceTime) {
		this.arrivalTime = arrivalTime;
		this.serviceTime = serviceTime;
		this.player = player;
	}
	
	public double getArrivalTime() {
		return arrivalTime;
	}
	
	public double getServiceTime() {
		return serviceTime;
	}
	
	public Player getPlayer() {
		return player;
	}
}
