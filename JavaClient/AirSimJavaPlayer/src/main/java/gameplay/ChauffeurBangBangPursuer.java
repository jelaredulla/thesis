package gameplay;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

public class ChauffeurBangBangPursuer extends ChauffeurDronePlayer implements Pursuer {
	private double captureL; // capture radius
	private DronePlayer target;
	
	private Point2D relativePos;
	private List<Point2D> relativeTrajectory;
	
	ChauffeurBangBangPursuer(String ip, int port, double v, double r, double l, DronePlayer e) throws UnknownHostException {
		super(ip, port, v, r);
		
		captureL = l;
		relativeTrajectory = new ArrayList<Point2D>();
		relativePos = new Point2D.Double(Simulator.eInitPos.getX(), Simulator.eInitPos.getY());
		setTarget(e);
	}
	
	public void setTarget(DronePlayer e) {
		target = e;
	}
	
	public boolean targetCaught() {
		return (position.distance(target.getPos()) <= captureL);
	}
	
	@Override
	public void updatePositionData() {
		super.updatePositionData();
		
		relativeTrajectory.add(relativePos);
	}
	
	public List<Point2D> getRelativeTrajectory() {
		System.out.println(relativeTrajectory.size());
		return relativeTrajectory;
	}
	
	public void pursue() {
		relativePos = getRelativePos(target);
				
		double x = relativePos.getX();
		double y = relativePos.getY();
		
		// control variable, which is effectively turning radius
		double phi;
		if (y == 0) {
			if (x < 0) {
				phi = 1;
			} else {
				phi = 0;
			}
		} else {
			phi = Math.signum(y);
		}
		

		steer(phi);
		super.move();
	}
}
