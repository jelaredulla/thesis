package manualInput;

import net.java.games.input.*;

public class XboxController {
	private Controller gamepad = null;
	
	public XboxController() {
		System.out.print("Looking for gamepad...");
        Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
                
        for(int i = 0; i < ca.length; i++){

            /* Get the name of the controller */
            //System.out.println(ca[i].getName() + ": " + ca[i].getType());
            
            if (ca[i].getType() == Controller.Type.GAMEPAD) {
            	System.out.println("Gamepad found!");
            	gamepad = ca[i];
            	break;
            }
        }
	}
	
	public boolean gamepadSet() {
		return gamepad != null;
	}
	
	public double pollLeftJoyStick() {
		if (gamepadSet()) {
			gamepad.poll();
			
	    	Component[] components = gamepad.getComponents();
	    	 
	    	float y = -components[0].getPollData();
	    	float x = components[1].getPollData();
	    	
	    	double pose = Math.atan2((double) x, (double) y);
	    	return pose;
		} else {
			return 0;
		}
	}
}