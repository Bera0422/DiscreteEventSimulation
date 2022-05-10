
//Staff (coach) parent class which hold the ID info and implements the comparable interface.
public class Staff implements Comparable<Staff>{

	private final int ID;
	
	private boolean isBusy = false;
	
	public Staff(int ID) {
		this.ID = ID;
	}
	
	public boolean isBusy() {
		return isBusy;
		
	}
	
	public void setBusy(boolean b) {
		isBusy = b;
	}

	public int getID() {
		return ID;
	}
	//Prioritizes the staff who is not busy and has the smallest ID.
	@Override
	public int compareTo(Staff o) {
		// TODO Auto-generated method stub
		
		
		if(this.isBusy() && !o.isBusy()) {
			return 1;
		}
		else if(!this.isBusy() && o.isBusy()) {
			return -1;
		}
		
		return this.getID() - o.getID();
	}
}
