package gametheory;

import visualiser.GlobalFieldWindow;
import java.awt.geom.Point2D;

public class GamePlay {
	public static void main(String[] args) throws InterruptedException{
		if (args.length < 2) {
			System.out.println("Please specify speed and capture/turn ratios");
			System.exit(1);
		}
		
		double gamma = Double.parseDouble(args[0]);
		double beta = Double.parseDouble(args[1]);
		
		double baseV = 2;
		double baseR = 0.1;
		
		//Player p = new Player(new Point2D.Double(0.5, 0), 0, 1, 1);
		
		GlobalFieldWindow vis = new GlobalFieldWindow(1200, beta*baseR);
		
		ControllerHandler xbox = new ControllerHandler();
		
		while (true) {
			
			Player e = new Player(new Point2D.Double(0.5, 0.4), -Math.PI/2, gamma*baseV);
			
			PerfectPursuer p = new PerfectPursuer(new Point2D.Double(0.25, 0), 0, baseV, 0.8*baseR, beta*baseR, e);
			PerfectPursuer p2 = new PerfectPursuer(new Point2D.Double(0.75, 0.75), 0, 1.2*baseV, baseR, beta*baseR, e);
			
			while (!p.targetCaught() && !p2.targetCaught()) {//&& (t < 100000)) {
				
				//e.steer(Math.sin(t/(16*Math.PI)));
				if (xbox.gamepadSet()) {
					e.steer(xbox.pollLeftJoyStick());
				} else {
					e.steerInBox();
				}
				
				e.move();
				//System.out.println("Speed is perceived as " + p.estimateVelocity());
				p.stalk();
				p2.stalk();
				
				//p.linearPredictStalk();
				
				vis.setPursuerState(p.getPos(), p.getTheta());
				//vis.setPursuerState(p2.getPos(), p2.getTheta(), "p2");
				vis.setEvaderState(e.getPos(), e.getTheta());
				vis.addPursuerSegment(p.getLastMovement(), "p");
				vis.addPursuerSegment(p2.getLastMovement(), "p2");
				vis.addEvaderSegment(e.getLastMovement());
				
				vis.repaint();
				
				Thread.sleep(10);
			}
			
			
			vis.setPursuerPath(p.getPath(), "p");
			vis.setPursuerPath(p2.getPath(), "p2");
			vis.setEvaderPath(e.getPath());
			
			vis.repaint();
			
			System.out.println("Press any key to play again:");
			vis.waitKey();
			vis.clearAll();
		}

	}
}
