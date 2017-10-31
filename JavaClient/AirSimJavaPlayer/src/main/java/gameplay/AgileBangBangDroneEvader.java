package gameplay;

import java.awt.geom.Point2D;
import java.net.UnknownHostException;


public class AgileBangBangDroneEvader extends AgileDronePlayer implements Evader {
		private double captureL; // capture radius
		private DronePlayer hunter;
		
		AgileBangBangDroneEvader(String ip, int port, double v, double l, DronePlayer p) throws UnknownHostException {
			super(ip, port, v);
			
			captureL = l;
			setHunter(p);
		}
		
		AgileBangBangDroneEvader(String ip, int port, double v, double l) throws UnknownHostException {
			super(ip, port, v);
			
			captureL = l;
		}
		
		public void setHunter(DronePlayer p) {
			hunter = p;
		}
		
		public boolean isCaught() {
			return (position.distance(hunter.getPos()) <= captureL);
		}

		public void evade() {
			Vector3r pPos = hunter.getPos();
			
			// differences in x, y coords in global frame
			double xDiff = (position.getX() - pPos.getX());
			double yDiff = (position.getY() - pPos.getY());
			
			if (hunter instanceof ChauffeurDronePlayer) {
				ChauffeurDronePlayer h = (ChauffeurDronePlayer) hunter;
				if (Math.sqrt(xDiff*xDiff + yDiff*yDiff) <= (h.getMinR())) {
					double relTheta;
					Point2D turnCentre = CommonOps.inTurningCircle(this, h);
					//System.out.println(turnCentre);
					if (turnCentre != null) {
						xDiff = position.getX() - turnCentre.getX();
						yDiff = position.getY() - turnCentre.getY();
						relTheta = Math.atan2(yDiff, xDiff) + Math.PI;
					} else {
						relTheta = Math.atan2(yDiff, xDiff) + Math.PI/4;
					}

					steer(Math.atan2(Math.sin(relTheta), Math.cos(relTheta)));
					super.move();
//					double pTheta = hunter.getTheta();
//					steer(pTheta/0.7);
					
					return;
				}
			}
				
			steer(Math.atan2(yDiff, xDiff));
			super.move();		
		}
	}

