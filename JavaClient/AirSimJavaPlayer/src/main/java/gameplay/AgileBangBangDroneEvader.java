package gameplay;

import java.net.UnknownHostException;

import gameplay.AirSimStructures.Vector3r;

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
				if (Math.sqrt(xDiff*xDiff + yDiff*yDiff) <= (2*h.getMinR())) {
					double pTheta = hunter.getTheta();
					steer(pTheta/0.7);
					super.move();
					return;
				}
			}
				
			steer(Math.atan2(yDiff, xDiff));
			super.move();		
		}
	}

