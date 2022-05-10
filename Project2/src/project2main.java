import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Scanner;

public class project2main {

	//ArrayLists for storing players and arrivals(events)
	private static ArrayList<Player> players;
	private static ArrayList<Arrival> arrivals;
	
	//PriorityQueues for storing the players who are either in training queue or massage queue or therapy queue.
	private static PriorityQueue<Player> trainingQueue;
	private static PriorityQueue<Player> massageQueue;
	private static PriorityQueue<Player> therapyQueue;
	
	//ArrayLists for storing trainers, masseurs and pyhsiotherapists.
	private static ArrayList<Trainer> trainers;
	private static ArrayList<Masseur> masseurs;
	private static ArrayList<Physiotherapist> therapists;
	
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		//Reading the input file and adding datas into the related lists.
		readInput(args[0]);
		
		//A comparator defined to sort arrivals mainly according to their arrival time.
		Comparator<Arrival> com =  new Comparator<Arrival>() {

			@Override
			public int compare(Arrival o1, Arrival o2) {
				
				if(o1.getArrivalTime() == o2.getArrivalTime()) {
					return o1.getPlayer().getID() - o2.getPlayer().getID();
				}
				
				if(o1.getArrivalTime() - o2.getArrivalTime() < 0) {
					return -1;
				}
				return 1;
			}
		};

		//The player who is related to an arrival
		Player p = null;

		//Number of valid training and massage  attempts.
		int TA = 0;
		int MA = 0;
		
		//Number of invalid and cancelled attempts.
		int invals = 0;
		int ccls = 0;
		
		//Number of the maximum people in training, physiotherapist and massage queues. 
		int maxTQ = 0;
		int maxPQ = 0;
		int maxMQ = 0;

		//Average waiting time of the players in training, physiotherapist and massage queues.
		double avTQ = 0;
		double avPQ = 0;
		double avMQ = 0;
		
		//Average taken service time of the players in trainers, physiotherapists and masseurs.
		double avTS = 0;
		double avPS = 0;
		double avMS = 0;
		
		//ID and the waiting time of the player who spent the most time in the physiotherapist queue.
		int maxPID = 0;
		double maxPT = 0;
		
		//ID and the waiting time of the player who spent the least time in the massage queue.
		int minMID = -1;
		double minMT = 0;
		
		//Arrival time of an arrival.
		double time = 0;
		
		Collections.sort(arrivals,com);
		
		//A PriorityQueue for storing the leave operations.
		PriorityQueue<Leave> leaves = new PriorityQueue<>();
		//A PriorityQueue for storing the pyhsiotherapy arrivals.
		PriorityQueue<Physiotherapy> pts = new PriorityQueue<>();
		
		 
		double aTime = 0;
		double lTime = 0;
		double pTime = 0;
		
		double min = 0;
		
		Arrival a = null;
		int i = 0;
		
		int arrivalsSize = arrivals.size();
		int leavesSize = 0 , ptsSize = 0;
		
		//For sake of the efficiency input file arrivals, leave operations and physiotherapy arrivals are stored in different data collections.
		//As long as those lists are not empty a loop will get the related objects and compare them with respect to their arrival times and the earliest will be chosen.
		
		while(arrivalsSize > 0 || leavesSize >  0 || ptsSize > 0) {
			
			
			if(arrivalsSize > 0) {
				aTime = arrivals.get(i).getArrivalTime();
			}
			else {
				aTime = Double.MAX_VALUE;
			}
			if(leavesSize > 0) {
				lTime = leaves.peek().getArrivalTime();
			}
			else {
				lTime = Double.MAX_VALUE;
			}
			
			if(ptsSize > 0) {
				pTime = pts.peek().getArrivalTime();
			}
			else {
				pTime = Double.MAX_VALUE;
			}
			
			min = Math.min(Math.min(aTime, lTime) , pTime);
			
			
			
			if(min == lTime) {
				a = leaves.peek();
			}
			else if(min == aTime ) {
				a = arrivals.get(i);
				
			}
			else {
				a = pts.peek();
			}
					
		
			
			p = a.getPlayer();
			time = a.getArrivalTime();
			//Service time of the related arrival.
			double duration = a.getServiceTime();
			
			//If the player has taken 3 massages and tries to attempt for another massage, this attempt will be considered invalid.
			
			if(a.getClass().getTypeName().equals("Massage") && p.getTakenMassages() == 3) {
				invals++;
				i++;
				arrivalsSize--;
				continue;
			}
			
			//If the player is currently busy and tries to attempt for a service, this attempt will be considered cancelled.
			if(!a.getClass().getTypeName().equals("Leave") && p.isInService()) {
				ccls++;
				if(a.getClass().getName().equals("Physiotherapy"))
					{pts.remove();
					ptsSize--;}
				else
					{i++;
					arrivalsSize--;}
				continue;
			}
			
			
			
			p.setInService(true);
			
			//If there is a non-busy coach the player is assigned to him. Otherwise the player is added to the related queue.
			switch(a.getClass().getTypeName()) {
				case "Training":
					
					p.setServiceTime(duration);
					p.setArrivalTime(time);
					
					Collections.sort(trainers);
					Trainer t = trainers.get(0);
					if(!t.isBusy()) {
						t.setBusy(true);
						avTS += a.getServiceTime();
						
						p.setTrainingTime(duration);
						TA++;
						leaves.add(new Leave(p,time + duration,t));
						leavesSize++;
					}
					else {
						
						trainingQueue.add(p);
						if(trainingQueue.size() > maxTQ) {
							maxTQ = trainingQueue.size();
						}
					}
					break;
				case "Massage":
				
					Collections.sort(masseurs);
					Masseur m = masseurs.get(0);
					p.setServiceTime(duration);
					p.setArrivalTime(time);
					p.increaseTakenMassages();
					if(!m.isBusy()) {
						m.setBusy(true);
						
						if(p.getTakenMassages() == 3) {
							if(minMID == -1) {
								minMID = p.getID();
								minMT = p.getMQTime();
							}
							else {
							if(p.getMQTime() < minMT) {
								minMT = p.getMQTime();
								minMID = p.getID();							
								}
							else if(p.getMQTime() == minMT && p.getID() < minMID) {
								minMID = p.getID();
							}
							}
						}
						avMS += duration;
						leaves.add(new Leave(p,time + duration,m)); 
						leavesSize++;
						MA++;
					}
					else {
						

						massageQueue.add(p);
						if(massageQueue.size() > maxMQ) {
							maxMQ = massageQueue.size();
						}
					}
					break;
				case "Physiotherapy":
				
					
					Collections.sort(therapists);
					Physiotherapist pt = therapists.get(0);
					
					p.setArrivalTime(time);
					
					if(!pt.isBusy()) {
						pt.setBusy(true);
						p.setServiceTime(pt.getServiceTime());
						avPS += pt.getServiceTime();
						leaves.add(new Leave(p,time + pt.getServiceTime(),pt));
						leavesSize++;
						}
					else {
						

						therapyQueue.add(p);
						if(therapyQueue.size() > maxPQ) {
							maxPQ = therapyQueue.size();
						}
					}
					break;
				//If it's a leave operation the related coach is being set as non-busy and the next player in queue is assigned to that coach.	
				case "Leave":

					Leave l = (Leave) a;
					Staff s = l.getLeavingStaff();
					s.setBusy(false);
					p.setInService(false);

					
					switch(s.getClass().getName()) {
						
					case "Trainer":	
						
					pts.add(new Physiotherapy(p,time, 0));
					ptsSize++;
					if(trainingQueue.size() > 0) {
						Player pl = trainingQueue.poll();
						avTQ += time - pl.getArrivalTime();
				
						s.setBusy(true);
						avTS += pl.getServiceTime();
						pl.setTrainingTime(pl.getServiceTime());
						TA++;
						leaves.add(new Leave(pl,time + pl.getServiceTime(),s));
						leavesSize++;
					
					}

					break;
					
					case "Masseur":
						if(massageQueue.size() > 0) {
							Player pl = massageQueue.poll();

						
							avMQ += time - pl.getArrivalTime();
							pl.setMQTime(pl.getMQTime() + time-pl.getArrivalTime());
							
							s.setBusy(true);
							if(pl.getTakenMassages() == 3) {
								if(minMID == -1) {
									minMID = pl.getID();
									minMT = pl.getMQTime();
								}
							else {
								if(pl.getMQTime() < minMT) {
									minMT = pl.getMQTime();
									minMID = pl.getID();							
									}
								else if(pl.getMQTime() == minMT && pl.getID() < minMID) {
									minMID = pl.getID();
								}
								}
							}
							avMS += pl.getServiceTime();
							leaves.add(new Leave(pl,time + pl.getServiceTime(),s)); 
							leavesSize++;
							MA++;
							
							}
						break;
					case "Physiotherapist":
						if(therapyQueue.size() > 0) {
							Player pl = therapyQueue.poll();
							pl.setPQTime(time - pl.getArrivalTime() + pl.getPQTime());
	
							if(pl.getPQTime() > maxPT) {
								maxPT = pl.getPQTime();
								maxPID = pl.getID();
								
							}
							else if(pl.getPQTime() == maxPT && pl.getID() < maxPID) {
								maxPID = pl.getID();
							}
							
							
							avPQ += time - pl.getArrivalTime();
							
							s.setBusy(true);
							
							Physiotherapist ptt = (Physiotherapist) s;
							
							pl.setServiceTime(ptt.getServiceTime());
							avPS += ptt.getServiceTime();
							leaves.add(new Leave(pl,time + ptt.getServiceTime(),ptt));
							leavesSize++;
						
							
							
							}
						break;
					}
							
					
					break;
					
	}
	
		
	
	
		//Updating arrival (event) lists	
		if(a.getClass().getName().equals("Leave"))
			{leaves.remove();
			leavesSize--;}
		else if(a.getClass().getName().equals("Physiotherapy"))
			{pts.remove();
			ptsSize--;}
		else
		{	
			i++;
			arrivalsSize--;
		}
	
		}
		
		//Last computations for outputs.
		avTQ /= TA;
		avPQ /= TA;
		avMQ /= MA;
		
		avTS /= TA;
		avPS /= TA;
		avMS /= MA;
		
		//Printing the relevant data to the target file.
		PrintStream out = new PrintStream(new File(args[1]));
		out.append(String.valueOf(maxTQ) + "\n");
		out.append(String.valueOf(maxPQ) + "\n");
		out.append(String.valueOf(maxMQ) + "\n");
		out.append(String.format(Locale.US,"%.3f",avTQ) + "\n");
		out.append(String.format(Locale.US,"%.3f",avPQ) + "\n");		
		out.append(String.format(Locale.US,"%.3f",avMQ) + "\n");
		out.append(String.format(Locale.US,"%.3f",avTS) + "\n");
		out.append(String.format(Locale.US,"%.3f",avPS) + "\n");
		out.append(String.format(Locale.US,"%.3f",avMS) + "\n");
		out.append(String.format(Locale.US,"%.3f", avPQ + avTQ + avTS + avPS) + "\n");
		out.append(maxPID +" "+ String.format(Locale.US,"%.3f", maxPT) + "\n");
		
		if(minMID == -1) {
			out.append("-1 -1\n");
		}
		else if(players.get(minMID).getTakenMassages() == 3)
			out.append(minMID + " " + String.format(Locale.US, "%.3f",minMT) + "\n");
		else
			out.append("-1 -1\n");

			
		out.append(invals + "\n");
		out.append(ccls + "\n");
		out.append(String.format(Locale.US, "%.3f",time)+"\n");
		
		
		out.close();
	
		
	}

	private static void readInput(String path) throws FileNotFoundException {

		Scanner scanner = new Scanner(new File(path));
		
		players = new ArrayList<>();
		
		//Defining the training queue which prioritizes the player who has the least arrival time and ID.
		trainingQueue = new PriorityQueue<>(new Comparator<Player>() {

			@Override
			public int compare(Player o1, Player o2) {
				// TODO Auto-generated method stub
				if(o1.getArrivalTime() == o2.getArrivalTime()) {
					return o1.getID() - o2.getID();
				}
				if(o1.getArrivalTime() < o2.getArrivalTime()) {
					return -1;
				}
				else if(o1.getArrivalTime() > o2.getArrivalTime()) {
					return 1;
				}
				return 0;
			}
		});
		
		//Defining the massage queue which prioritizes the player who has the highest skill level and the least arrival time and ID.
		massageQueue = new PriorityQueue<>(new Comparator<Player>() {

			@Override
			public int compare(Player o1, Player o2) {
				// TODO Auto-generated method stub
				
				if(o1.getSkillLevel() == o2.getSkillLevel()) {
					if(o1.getArrivalTime() == o2.getArrivalTime()) {
						return o1.getID() - o2.getID();
					}
					if(o1.getArrivalTime() < o2.getArrivalTime()) {
						return -1;
					}
					else if(o1.getArrivalTime() > o2.getArrivalTime()) {
						return 1;
					}
					return 0;
				}
				
				
				return o2.getSkillLevel() - o1.getSkillLevel();
			}
		});
		
		//Defining the therapy queue which prioritizes the player who has the biggest training time and the least arrival time and ID.
		therapyQueue = new PriorityQueue<>(new Comparator<Player>() {

			@Override
			public int compare(Player o1, Player o2) {
				// TODO Auto-generated method stub
				if(o1.getTrainingTime() == o2.getTrainingTime()) {
					if(o1.getArrivalTime() == o2.getArrivalTime()) {
						return o1.getID() - o2.getID();
					}
					else if(o1.getArrivalTime() < o2.getArrivalTime()) {
						return -1;
					}
					
						return 1;
					
					
				}
				
				
				else if(o1.getTrainingTime() < o2.getTrainingTime()) {
					return 1;
				}
				
				
					return -1;
				
				
			
				
			}
		});
		
		
		arrivals = new ArrayList<>();
		
		
		trainers = new ArrayList<>();
		masseurs = new ArrayList<>();
		therapists = new ArrayList<>();
		int count = Integer.valueOf(scanner.nextLine());
		String[] params = null;
		//Reading players informations.
		while(count > 0) {
			count--;			
			params = scanner.nextLine().split(" ");	
			
			players.add(new Player(Integer.valueOf(params[0]),Integer.valueOf(params[1]),0));		
			}
		
		
		count = Integer.valueOf(scanner.nextLine());
		Player p;
		//Reading arrival data and adding them to relevant lists.
		while(count > 0) {
			count--;
			params = scanner.nextLine().split(" ");		
			
			switch(params[0]) {
			
			case "t":
				p = players.get(Integer.valueOf(params[1]));
				p.setArrivalTime(Double.valueOf(params[2]));
				p.setTrainingTime(Double.valueOf(params[3]));
				p.setServiceTime(p.getTrainingTime());
				arrivals.add(new Training(p, Double.valueOf(params[2]), Double.valueOf(params[3])));

				break;
			case "m":
				p = players.get(Integer.valueOf(params[1]));
				p.setArrivalTime(Double.valueOf(params[2]));
				p.setTrainingTime(0);
				p.setServiceTime(Double.valueOf(params[3]));
				arrivals.add(new Massage(p, Double.valueOf(params[2]),Double.valueOf(params[3])));

				break;
			
			}
		}
		
		params = scanner.nextLine().split(" ");
		count = 0;
		//Reading pyhsiotherapists data.
		while(count < Integer.valueOf(params[0])) {
			count++;
			therapists.add(new Physiotherapist(count, Double.valueOf(params[count])));
			
		}
		
		params = scanner.nextLine().split(" ");
		count = 0;
		//Reading trainers data.
		while(count < Integer.valueOf(params[0])) {
			count++;
			trainers.add(new Trainer(count));
		}
		count = 0;
		//Reading masseurs data.
		while(count < Integer.valueOf(params[1])) {
			count++;
			masseurs.add(new Masseur(count));
		}
		
		scanner.close();
		
				
	}
	
}
