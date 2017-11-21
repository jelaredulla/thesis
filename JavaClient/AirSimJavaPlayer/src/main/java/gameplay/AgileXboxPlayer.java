package gameplay;

import java.net.UnknownHostException;

import manualInput.XboxController;

public class AgileXboxPlayer extends AgileDronePlayer {
	XboxController xbox;
	AgileXboxPlayer(String ip, int port, double v) throws Exception {
		super(ip, port, v);
		
		xbox = new XboxController();
		if (!xbox.gamepadSet()) {
			throw new Exception("Could not find XBox controller.");
		}
	}
		
	@Override
	public void evade() {
		steer(xbox.pollLeftJoyStick());
		move();
	}
	
	@Override
	public void pursue() {
		steer(xbox.pollLeftJoyStick());
		move();
	}
}
