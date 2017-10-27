package gameplay;

import java.net.UnknownHostException;

public class ChauffeurGOTCEvader extends ChauffeurDronePlayer implements Evader {
		private double captureL; // capture radius
		private DronePlayer hunter;
		
		ChauffeurGOTCEvader(String ip, int port, double v, double r, double l, DronePlayer p) throws UnknownHostException {
			super(ip, port, v, r);
			
			captureL = l;
			setHunter(p);
		}
		
		ChauffeurGOTCEvader(String ip, int port, double v, double r, double l) throws UnknownHostException {
			super(ip, port, v, r);
			
			captureL = l;
		}
		
		public void setHunter(DronePlayer p) {
			hunter = p;
		}
		
		public boolean isCaught() {
			return (position.distance(hunter.getPos()) <= captureL);
		}

		public void evade() {
			float pVel = hunter.getMaxV();
			
			// relative angle
			double relativeTheta = theta - hunter.getTheta();
			
			// control variable, which is effectively turning radius
			double phi = Math.signum(maxV*Math.sin(relativeTheta) - pVel);

			steer(phi);
			super.move();		
		}
	}
