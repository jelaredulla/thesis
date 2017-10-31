package gameplay;

import java.awt.geom.Point2D;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ChauffeurEvader extends ChauffeurDronePlayer implements Evader {
	private double captureL; // capture radius
	private DronePlayer hunter;
	
	ChauffeurEvader(String ip, int port, double v, double r, double l, DronePlayer e) throws UnknownHostException {
		super(ip, port, v, r);
		
		captureL = l;
		
		setHunter(e);
	}
	
	ChauffeurEvader(String ip, int port, double v, double r, double l) throws UnknownHostException {
		super(ip, port, v, r);
		
		captureL = l;
	}
	
	public void setHunter(DronePlayer e) {
		hunter = e;
	}
	
	public boolean isCaught() {
		return (position.distance(hunter.getPos()) <= captureL);
	}
		
	public Point2D getCurrentRelativePos() {
		return getRelativePos(hunter.get2DPos());
	}
		
	public void evade() {
		Point2D relativePos = getCurrentRelativePos();
		double x = relativePos.getX();
		double y = relativePos.getY();
		
		// control variable, which is effectively turning radius
		double phi;
		if (y == 0) {
			System.out.print("Straight");
			if (x < 0) {
				phi = 0;
			} else {
				phi = 1;
			}
		} else {
			phi = -Math.signum(y);
		}
		
		//System.out.println(relativePos);
		steer(phi);
		super.move();
	}
}
