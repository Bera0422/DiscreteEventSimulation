
public class Player {
	
	//Holds the ID number, skill level and training time of the player object.
	private final int ID;
	private int skillLevel;
	private double trainingTime;
	
	//Holds the infos of arrival time and taken service time of the player object.
	private double arrivalTime;
	private double serviceTime;
	
	//The number of the massages taken by the player object.
	private int takenMassages = 0;
	
	//The total time that the player spent in the massage queue.
	private double MQTime = 0;
	
	//The total time that the player spent in the physiotherapist queue.
	private double PQTime = 0;
	
	//Holds the info whether the player is in service or not. 
	private boolean isInService = false;
	
	public Player(int ID, int skillLevel,double trainingTime) {
		this.ID = ID;
		this.skillLevel = skillLevel;
		this.setTrainingTime(trainingTime);
	}


	public int getID() {
		return ID;
	}


	public double getTrainingTime() {
		return trainingTime;
	}


	public void setTrainingTime(double trainingTime) {
		this.trainingTime = trainingTime;
	}


	public double getArrivalTime() {
		return arrivalTime;
	}


	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}


	public double getServiceTime() {
		return serviceTime;
	}


	public void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
	}
	
	public int getSkillLevel() {
		return skillLevel;
	}


	public int getTakenMassages() {
		return takenMassages;
	}

	public void increaseTakenMassages() {
		takenMassages+=1;
	}
	
	public boolean isInService() {
		return isInService;
	}


	public void setInService(boolean isInService) {
		this.isInService = isInService;
	}


	public double getMQTime() {
		return MQTime;
	}


	public void setMQTime(double minMQTime) {
		this.MQTime = minMQTime;
	}


	public double getPQTime() {
		return PQTime;
	}


	public void setPQTime(double pQTime) {
		PQTime = pQTime;
	}
	
}
