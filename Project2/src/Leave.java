
//Leave child class of the parent Arrival class.

public class Leave extends Arrival implements Comparable<Leave>{
	//Holds the info of the staff from which the player leaves.
	private Staff leavingStaff;
	public Leave(Player player, double arrivalTime , Staff leavingStaff) {
		super(player, arrivalTime, 0);
		this.leavingStaff = leavingStaff;
		
	}
	
	public Staff getLeavingStaff(){
		return leavingStaff;
	}
	//Prioritizes the leave operation which has the least arrival time and ID.
	@Override
	public int compareTo(Leave o) {
		if(this.getArrivalTime() == o.getArrivalTime()) {
			
		
			
			return this.getPlayer().getID() - o.getPlayer().getID();
			
		}
		
		if(this.getArrivalTime() - o.getArrivalTime() < 0) {
			return -1;
		}
		return 1;	}

	
}
