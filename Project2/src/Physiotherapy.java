
//Physiotherapy child class of the Arrival parent class.
public class Physiotherapy extends Arrival implements Comparable<Physiotherapy>{

	public Physiotherapy(Player player, double arrivalTime, double serviceTime) {
		super(player, arrivalTime, serviceTime);
	}
	//This class implements the Comparable interface.
	//This method prioritizes the physiotherapy arrivals which has the least arrival time and ID.
	@Override
	public int compareTo(Physiotherapy o) {
		// TODO Auto-generated method stub

		
		if(this.getArrivalTime() == o.getArrivalTime()) {
			
		
			
			return this.getPlayer().getID() - o.getPlayer().getID();
			
		}
		
		if(this.getArrivalTime() - o.getArrivalTime() < 0) {
			return -1;
		}
		return 1;
	}
		
	
	}
	


