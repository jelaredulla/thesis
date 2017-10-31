package gameplay;

import java.awt.geom.Point2D;
import java.net.UnknownHostException;
import java.util.Random;

public class AgileEvader extends AgileDronePlayer implements Evader {
	private double captureL; // capture radius
	private DronePlayer hunter;
	
	AgileEvader(String ip, int port, double v, double l, DronePlayer p) throws UnknownHostException {
		super(ip, port, v);
		
		captureL = l;
		setHunter(p);
	}
	
	AgileEvader(String ip, int port, double v, double l) throws UnknownHostException {
		super(ip, port, v);
		
		captureL = l;
	}
	
	public void setHunter(DronePlayer p) {
		hunter = p;
	}
	
	public boolean isCaught() {
		return (position.distance(hunter.getPos()) <= captureL);
	}
	
	public Point2D getCurrentRelativePos() {
		return getRelativePos(hunter.get2DPos());
	}
	
	public void evadePlain() {
		Point2D relativePos = getCurrentRelativePos();
		
		double x = relativePos.getX();
		double y = relativePos.getY();
		
		steer(theta + Math.atan2(y, x) + Math.PI);
		super.move();
	}

	public void evade() {
		Point2D relativePos = getCurrentRelativePos();
		
		double x = relativePos.getX();
		double y = relativePos.getY();
		
		double relTheta = theta + Math.atan2(y, x) + Math.PI;
		if (hunter instanceof ChauffeurDronePlayer) {
			ChauffeurDronePlayer h = (ChauffeurDronePlayer) hunter;
			if (Math.hypot(x, y) <= (h.getMinR())) {
//				Random signGenerator = new Random();
//				relTheta += ( signGenerator.nextBoolean() ? 1 : -1 )*Math.PI/2;
				relTheta += Math.PI/2;
				steer(relTheta);
				super.move();
				return;
			}
		}
			
		steer(relTheta);
		super.move();		
	}
}

