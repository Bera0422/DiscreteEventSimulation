
//Physiotherapist child class of the Staff parent class.
//Holds the service time info of the physiotherapist.
public class Physiotherapist extends Staff{
	
	private double serviceTime;
	
	public Physiotherapist(int ID, double serviceTime) {
	super(ID);	
	this.serviceTime = serviceTime;
	}
	
	public double getServiceTime() {
		return serviceTime;
	}

	
}
