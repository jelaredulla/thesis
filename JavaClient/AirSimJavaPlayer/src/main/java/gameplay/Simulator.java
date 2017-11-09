package gameplay;

import java.awt.geom.Point2D;
import java.net.UnknownHostException;

import gameplay.AirSimStructures.*;
import manualInput.XboxController;
import visualiser.*;

public class Simulator {
	public final static float planeHeight = -6f;
	public final static float resetHeight = -7f;
	public final static float setupVelocity = 3f;
	public final static float setupWaitTime = 3f;
	
	public static Vector3r eInitPos = new Vector3r(0, 0, 0);
	private double captureL;
	private DronePlayer pursuer;
	private DronePlayer evader;
	
    public static void main(String[] args)
    {
    	try {
    		Simulator sim = new Simulator(5);
    		sim.run();
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    		e.printStackTrace();
    	}
    	
    	System.exit(0);
	}
    
    public Simulator(int type) throws UnknownHostException {
		double gamma = 0.85;
		double beta = 0.5;
		double baseV = 3;
		double baseR = 4;
		captureL = beta * baseR;
		
		String ipAddress = "127.0.0.1"; // localhost
		int ePort = 41451;
		int pPort = 41452;
		
		// player maximum velocities
		double eMaxV = gamma * baseV;
		double pMaxV = baseV;
		
		
		switch(type) {
		case 0:
			// Pedestrian Tag
			evader = new AgileEvader(ipAddress, ePort, eMaxV, captureL);
			pursuer = new AgilePursuer(ipAddress, pPort, pMaxV, captureL, evader);
			break;
		case 1:
			// Homicidal Chauffeur
			evader = new AgileEvader(ipAddress, ePort, eMaxV, captureL);
			pursuer = new ChauffeurPursuer(ipAddress, pPort, pMaxV, baseR, captureL, evader);
			break;
		case 2:
			// Suicidal Pedestrian
			evader = new ChauffeurEvader(ipAddress, ePort, eMaxV, baseR, captureL);
			pursuer = new AgilePursuer(ipAddress, pPort, pMaxV, captureL, evader);
			break;
		case 3:
			// Game of Two Cars, naively combined
			evader = new ChauffeurEvader(ipAddress, ePort, eMaxV, baseR, captureL);
			pursuer = new ChauffeurPursuer(ipAddress, pPort, pMaxV, baseR, captureL, evader);
			break;
		case 4:
			// Game of Two Cars
			evader = new ChauffeurGOTCEvader(ipAddress, ePort, eMaxV, baseR, captureL);
			pursuer = new ChauffeurGOTCPursuer(ipAddress, pPort, pMaxV, baseR, captureL, evader);
			break;
		case 5:
			// Homicidal Chauffeur, xBox controlled
			evader = new AgileDronePlayer(ipAddress, ePort, eMaxV);
			pursuer = new ChauffeurPursuer(ipAddress, pPort, pMaxV, baseR, captureL, evader);
			break;
		}
		
		evader.setHunter(pursuer);
    }
    
    public void run() throws InterruptedException {
    	int rNum = 5;
    	double r_init = rNum*captureL;
    	int count = 0;
    	double theta_init = count * Math.PI/4; 
    	eInitPos = new Vector3r((float) (r_init*Math.cos(theta_init)), (float) (r_init*Math.sin(theta_init)), planeHeight);
    	evader.setTheta(0);
    	pursuer.setTheta(0);
    	String folder = "";
    	String filename = folder+String.format("innovationDemo_round2_", rNum);
    	
    	
    	GlobalField vis = new GlobalField(1600, 20, captureL);
    	RelativeField relativeVis = new RelativeField(800, 20, captureL);
		relativeVis.setPursuerState(new Point2D.Float(0, 0),  0);
		XboxController xbox = new XboxController();
		
		setupAPIControl();
		double t;
	
		setupPositions(eInitPos);
		
		//int count = 0;
		double start;
		while (true) {
			
			System.out.println("Press any key to begin the chase:");
			vis.waitKey();
			
			evader.updatePositionData();
			pursuer.updatePositionData();
			System.out.println("Evader's initial position: " + evader.getPos());
			System.out.println("Pursuer's initial position: " + pursuer.getPos());
			
	
			
			
			start = System.currentTimeMillis();
			t = 0;			
			while (!pursuer.targetCaught() && !evader.isCaught() && (t < 180)) {
				if (!(evader instanceof AgileEvader) && !(evader instanceof ChauffeurEvader) && (xbox.gamepadSet())) {
					evader.steer(xbox.pollLeftJoyStick());
					evader.move();
				} else {
					evader.evade();
				}
				
				pursuer.pursue();
				
				vis.setPursuerState(pursuer.get2DPos(), pursuer.getTheta());
				vis.setEvaderState(evader.get2DPos(), evader.getTheta());
				relativeVis.setEvaderState(pursuer.getCurrentRelativePos(), evader.getTheta() - pursuer.getTheta());
				
				vis.addPursuerSegment(pursuer.getLastMovement());
				vis.addEvaderSegment(evader.getLastMovement());
				relativeVis.setEvaderPath(pursuer.getRelativeTrajectory());
	
				vis.resetBoundaryForMax();
				vis.repaint();
				relativeVis.repaint();
	
				Thread.sleep(100);
				t += 0.1;
			}
			
			double totalTime = (System.currentTimeMillis() - start)/1000;
			System.out.println("Time is: "+totalTime+" seconds");
			
			evader.hover();
			pursuer.hover();			
			
			vis.setPursuerPath(pursuer.getPath());
			vis.setEvaderPath(evader.getPath());
			
			vis.resetBoundaryForMax();
			vis.repaint();
			
			relativeVis.resetBoundaryForMax();
			relativeVis.repaint();
			
			System.out.println("Press any key to save the image");
			vis.waitKey();
			
			vis.saveImage(filename+count+".png");
			
			
			vis.clearAll();
			
			evader.clearPath();
			pursuer.clearPath();
			
			count++;
			
			theta_init = count * Math.PI/4; 
	    	eInitPos = new Vector3r((float) (r_init*Math.cos(theta_init)), (float) (r_init*Math.sin(theta_init)), planeHeight);
	    	evader.setTheta(0);
	    	pursuer.setTheta(0);
			reset(eInitPos);
		}
		
		
    }
    
    public void setupAPIControl() throws InterruptedException {
		pursuer.confirmConnection();
		pursuer.enableApiControl(true);			
		
		evader.confirmConnection();
		evader.enableApiControl(true);
    }
    
    public void setupPositions(Vector3r evaderInitPos) throws InterruptedException {
    	setupPosition(pursuer, new Vector3r(0, 0, planeHeight));
    	setupPosition(evader, evaderInitPos);
    }
    
    private void setupPosition(DronePlayer drone, Vector3r initPos) throws InterruptedException {
    	// Arm the drone and take off if not flying already
    	if (drone.getLandedState() != LandedState.Flying) {
    		drone.armDisarm(true);
    		drone.takeoff(setupWaitTime);
    	}
    	
    	// move to the initial position
		drone.moveToPosition(initPos, setupVelocity);
		
		// wait for the drone to travel so the initial position is updated correctly
		Thread.sleep((long) (setupWaitTime * 1000)); 
		drone.updatePositionData();	
    }
    
    private void reset(Vector3r evaderInitPos) {
    	double eDist = evader.get2DPos().distance(0,  0);
    	double pDist = pursuer.get2DPos().distance(0, 0);
    	if (eDist < pDist) {
    		evader.moveToPosition(evaderInitPos, setupVelocity);
    		pursuer.moveToPosition(new Vector3r(0, 0, planeHeight), setupVelocity);
    	} else {
    		pursuer.moveToPosition(new Vector3r(0, 0, planeHeight), setupVelocity);
    		evader.moveToPosition(evaderInitPos, setupVelocity);
    	}
    }
}
