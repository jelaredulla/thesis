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
		
		double gamma = Double.parseDouble(args[0]);
		double beta = Double.parseDouble(args[1]);
		
		double baseV = 2;
		double baseR = 0.1;
		
		//Player p = new Player(new Point2D.Double(0.5, 0), 0, 1, 1);
		
		GlobalField vis = new GlobalField(1000, 15, beta*baseR);
		
		XboxController xbox = new XboxController();
		
//    	double t1 = 0;
//    	double angle1;
//    	while (t1 < 15) {
//			angle1 = xbox.pollLeftJoyStick();
//			System.out.println(angle1);
//			t1 += 0.1;
//			Thread.sleep(100);
//    	}
		
		DronePlayer e = new DronePlayer("", 41452, gamma*baseV);
		// capture = beta*baseR instead of 0.5
		DronePursuer p = new DronePursuer("", 41451, baseV, baseR, 1, e);
		
		setupAPIControl(e, p);
		double t;
		while (true) {
						
			setupPositions(e, p);
			t = 0;			
			while ((t < 10) || (!p.targetCaught())) {//&& (t < 100000)) {
				//System.out.println("t="+t+", Evader pos: "+e.getPos()+", Pursuer pos: "+p.getPos());
				//e.steer(Math.sin(t/(16*Math.PI)));
				if (xbox.gamepadSet()) {
					e.steer(xbox.pollLeftJoyStick());
				} else {
					e.steerInBox();
				}
				
				e.move();

				p.stalk();
				
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
    
    public static void setupAPIControl(DronePlayer e, DronePursuer p) throws InterruptedException {
		p.confirmConnection();
		p.enableApiControl(true);			
		
		e.confirmConnection();
		e.enableApiControl(true);
    }
    
    public static void setupPositions(DronePlayer e, DronePursuer p) throws InterruptedException {
		
    	if (e.getLandedState() == LandedState.Landed) {
    		e.armDisarm(true);
    		e.takeoff(3);
    	}
    	
    	//e.goHome();

		e.moveToZ(-5, 1, 5);
		e.steer((3*Math.PI)/4);
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
    
    
    public static void oldMain() throws InterruptedException, UnknownHostException {
    	GlobalField g = new GlobalField(50, 1, 0.5);
    	XboxController con = new XboxController();
//    	double t1 = 0;
//    	double angle1;
//    	while (t1 < 15) {
//			angle1 = con.pollLeftJoyStick();
//			System.out.println(angle1);
//			t1 += 0.1;
//			Thread.sleep(100);
//    	}
    	
        DronePlayer pursuer = new DronePlayer("", 41451, 1);
		System.out.println( "Hello World!" );
		
		pursuer.confirmConnection();
		pursuer.enableApiControl(true);
		boolean respP = pursuer.isApiControlEnabled();
		System.out.println(respP);
		
		
		
		DronePlayer evader = new DronePlayer("", 41452, 1);
		evader.confirmConnection();
		evader.enableApiControl(true);
		boolean respE = evader.isApiControlEnabled();
		System.out.println(respE);
		
		evader.armDisarm(true);
		evader.takeoff(20);
		
		evader.moveToZ(-7, 1);
		evader.moveByVelocityZ(new Vector3r(0, 1, 0), new Vector3r(0,  0, -7), 15);
				
	
		
		try {
			
			
			pursuer.armDisarm(true);
			pursuer.takeoff(20);
			
			double t = 0;
			
			double angle;
			while (t < 300) {
				angle = con.pollLeftJoyStick();
				//System.out.println(angle);
				Vector3r eVel = new Vector3r((float) Math.sin(angle), (float) Math.cos(angle), 0f);
				//System.out.println("eVel = "+eVel.toString());
				double poseAngle = -angle + Math.PI/2;
				float pose = (float) Math.toDegrees(Math.atan2(Math.sin(poseAngle), Math.cos(poseAngle)));
				System.out.println(pose);
				pursuer.rotateToYaw(pose);
				pursuer.moveByVelocityZ(eVel, new Vector3r(0,  0, -7), 20, DrivetrainType.ForwardOnly, new YawMode(true, 0f));
//				
//				Vector3r ePos = evader.getPosition();
//				System.out.println(ePos);
				
				//evader.moveByVelocityZ(new Vector3r(0, 1, 0), new Vector3r(0,  0, -7), 20);
				
				Thread.sleep(100);
				t += 0.1;
			}
			
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		

    }
}
