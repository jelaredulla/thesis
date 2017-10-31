package gameplay;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class ChauffeurBangBangPursuer extends ChauffeurDronePlayer implements Pursuer {
	private double captureL; // capture radius
	private DronePlayer target;
	
	private Point2D relativePos;
	private List<Point2D> relativeTrajectory;
	
	ChauffeurBangBangPursuer(String ip, int port, double v, double r, double l, DronePlayer e) throws UnknownHostException {
		super(ip, port, v, r);
		
		captureL = l;
		relativeTrajectory = new ArrayList<Point2D>();
		
		setTarget(e);
	}
	
	public void setTarget(DronePlayer e) {
		target = e;
	}
	
	public boolean targetCaught() {
		return (Math.hypot(relativePos.getX(), relativePos.getY()) <= captureL);
	}
	
	@Override
	public void updatePositionData() {
		super.updatePositionData();
		relativeTrajectory = getRelativePath(target);
	}
	
	public List<Point2D> getRelativeTrajectory() {
		return relativeTrajectory;
	}
	
	public Point2D getCurrentRelativePos() {
		return relativePos;
	}
		
	public void pursue() {
		updatePositionData();
		
		if (relativeTrajectory.isEmpty()) {
			return;
		}
		
		relativePos = relativeTrajectory.get(relativeTrajectory.size() - 1);
//		relativePos = getRelativePos(target.get2DPos());
		
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
