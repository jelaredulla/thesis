package gameplay;

import java.io.IOException;
import java.net.UnknownHostException;

import gameplay.AirSimStructures.*;
import manualInput.XboxController;
import visualiser.GlobalField;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException, IOException
    {
		if (args.length < 2) {
			System.out.println("Please specify speed and capture/turn ratios");
			System.exit(1);
		}
		
		double gamma = 2;//Double.parseDouble(args[0]);
		double beta = Double.parseDouble(args[1]);
		
		double baseV = 3;
		double baseR = 2.1;
		
		GlobalField vis = new GlobalField(1000, 50, 1);
		
		XboxController xbox = new XboxController();

		AgileDronePlayer e = new AgileDronePlayer("", 41452, gamma*baseV);
//		// capture = beta*baseR instead of 0.5
		ChauffeurBangBangPursuer p = new ChauffeurBangBangPursuer("", 41451, baseV, baseR, 1, e);
//		
//		ChauffeurGOTCEvader e = new ChauffeurGOTCEvader("", 41452, gamma*baseV, baseR, beta*baseR);
//		// capture = beta*baseR instead of 0.5
//		ChauffeurGOTCPursuer p = new ChauffeurGOTCPursuer("", 41451, baseV, baseR, beta*baseR, e);
//		e.setHunter(p);
	
		
		
		setupAPIControl(e, p);
		double t;
		while (true) {
						
			setupPositions(e, p);
			vis.waitKey();
			t = 0;			
			while ((t < 10) || (!p.targetCaught())) {//&& (t < 100000)) {
				//System.out.println("t="+t+", Evader pos: "+e.getPos()+", Pursuer pos: "+p.getPos());
				//e.steer(Math.sin(t/(16*Math.PI)));
				if (xbox.gamepadSet()) {
					e.steer(xbox.pollLeftJoyStick());
				} else {
					e.steerInBox(30);
				}
				e.move();
				
				
//				e.evade();
				p.pursue();
				//p.linearPredictStalk();
				
				vis.setPursuerState(p.get2DPos(), p.getTheta());
//				//vis.setPursuerState(p2.getPos(), p2.getTheta(), "p2");
				vis.setEvaderState(e.get2DPos(), e.getTheta());
				vis.addPursuerSegment(p.getLastMovement(), "p");
				//vis.addPursuerSegment(p2.getLastMovement(), "p2");
				vis.addEvaderSegment(e.getLastMovement());
//				
				vis.repaint();
				
				Thread.sleep(100);
				t += 0.1;
			}
			
			e.hover();
			p.hover();			
			
			vis.setPursuerPath(p.getPath(), "p");
			//vis.setPursuerPath(p2.getPath(), "p2");
			vis.setEvaderPath(e.getPath());
			
			vis.repaint();
			
			System.out.println("Press any key to play again:");
			vis.waitKey();
			vis.clearAll();
		}

	}
    
    public static void setupAPIControl(DronePlayer e, DronePlayer p) throws InterruptedException {
		p.confirmConnection();
		p.enableApiControl(true);			
		
		e.confirmConnection();
		e.enableApiControl(true);
    }
    
    public static void setupPositions(DronePlayer e, DronePlayer p) throws InterruptedException {
		
    	if (e.getLandedState() == LandedState.Landed) {
    		e.armDisarm(true);
    		e.takeoff(3);
    	}
    	
    	//e.goHome();

		e.moveToZ(-5, 1, 5);
		//e.steer((3*Math.PI)/4);
		e.moveByVelocityZ(new Vector3r(0, -1, 0), new Vector3r(0,  0, -5), 8);
		e.updatePositionData();
		System.out.println(e.getPos());		
		
		if (p.getLandedState() == LandedState.Landed) {
			p.armDisarm(true);
			p.takeoff(3);
		}
		
		//p.goHome();
		
		p.moveToZ(-5, 1, 5);
		p.updatePositionData();
		System.out.println(p.getPos());
    }
}
