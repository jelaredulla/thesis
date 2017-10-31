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
    		Simulator sim = new Simulator(0);
    		sim.run();
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    		e.printStackTrace();
    	}
    	
    	System.exit(0);
	}
    
    public Simulator(int type) throws UnknownHostException {
		double gamma = 0.9;
		double beta = 0.2;
		double baseV = 3;
		double baseR = 10;
		captureL = beta * baseR;
		
		String ipAddress = "127.0.0.1"; // localhost
		int ePort = 41452;
		int pPort = 41451;
		
		// player maximum velocities
		double eMaxV = gamma * baseV;
		double pMaxV = baseV;
		
		
		switch(type) {
		case 0:
			evader = new AgileDronePlayer(ipAddress, ePort, eMaxV);
			pursuer = new ChauffeurBangBangPursuer(ipAddress, pPort, pMaxV, baseR, captureL, evader);
			break;
		case 1:
			evader = new AgileBangBangDroneEvader(ipAddress, ePort, eMaxV, captureL);
			pursuer = new ChauffeurBangBangPursuer(ipAddress, pPort, pMaxV, baseR, captureL, evader);
			evader.setHunter(pursuer);
			break;
		case 2:
			evader = new ChauffeurGOTCEvader(ipAddress, ePort, eMaxV, baseR, captureL);
			pursuer = new ChauffeurGOTCPursuer(ipAddress, pPort, pMaxV, baseR, captureL, evader);
			evader.setHunter(pursuer);
			break;
		}
    }
    
    public void run() throws InterruptedException {
    	double r_init = 2*captureL;
    	int n = 1;
    	double theta_init = n * Math.PI/4; 
    	eInitPos = new Vector3r((float) (r_init*Math.cos(theta_init)), (float) (r_init*Math.sin(theta_init)), planeHeight);
    	
    	String filename = String.format("globalView_r%.4f_n%d.png", r_init, n);
    	
    	
    	GlobalField vis = new GlobalField(1500, 50, captureL);
    	RelativeField relativeVis = new RelativeField(1500, 50, captureL);
		relativeVis.setPursuerState(new Point2D.Float(0, 0),  0);
		XboxController xbox = new XboxController();
		
		setupAPIControl();
		double t;
	
		setupPositions(eInitPos);
		
		System.out.println("Press any key to begin the chase:");
		relativeVis.waitKey();
		t = 0;			
		while (((t < 10) || (!pursuer.targetCaught() && !evader.isCaught())) && (t < 180)) {
			if (xbox.gamepadSet()) {
				evader.steer(xbox.pollLeftJoyStick());
				evader.move();
			} else {
				evader.evade();
			}
			
			pursuer.pursue();
			
			ChauffeurBangBangPursuer pBang= (ChauffeurBangBangPursuer) pursuer;
			
			vis.setPursuerState(pursuer.get2DPos(), pursuer.getTheta());
			vis.setEvaderState(evader.get2DPos(), evader.getTheta());
			relativeVis.setEvaderState(pBang.getCurrentRelativePos(), evader.getTheta() - pursuer.getTheta());
			
			vis.addPursuerSegment(pursuer.getLastMovement());
			vis.addEvaderSegment(evader.getLastMovement());
			relativeVis.setEvaderPath(pBang.getRelativeTrajectory());

			vis.repaint();
			relativeVis.repaint();

			Thread.sleep(100);
			t += 0.1;
		}
		
		System.out.println("Time is: "+t+" seconds");
		
		evader.hover();
		pursuer.hover();			
		
		vis.setPursuerPath(pursuer.getPath());
		vis.setEvaderPath(evader.getPath());
		
		vis.resetBoundaryForMax();
		vis.repaint();
		
		relativeVis.resetBoundaryForMax();
		relativeVis.repaint();
		
//		if (pursuer instanceof ChauffeurBangBangPursuer) {
//			ChauffeurBangBangPursuer p = (ChauffeurBangBangPursuer) pursuer;
//			vis.drawRelativePos(p.getRelativeTrajectory());
//		}
		
		System.out.println("Press any key to save the image");
		vis.waitKey();
		
		vis.saveImage(filename);
    }
    
    public void setupAPIControl() throws InterruptedException {
		pursuer.confirmConnection();
		pursuer.enableApiControl(true);			
		
		evader.confirmConnection();
		evader.enableApiControl(true);
    }
    
    public void setupPositions(Vector3r evaderInitPos) throws InterruptedException {
    	setupPosition(evader, evaderInitPos);
		System.out.println("Evader's intial position: " + evader.getPos());	
    	
		setupPosition(pursuer, new Vector3r(0, 0, planeHeight));
		System.out.println("Pursuer's intial position: " + pursuer.getPos());
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
}
