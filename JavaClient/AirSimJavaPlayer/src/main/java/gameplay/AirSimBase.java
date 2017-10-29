package gameplay;

import java.net.UnknownHostException;
import java.util.List;

import org.msgpack.rpc.Client;
import org.msgpack.rpc.loop.EventLoop;
import org.msgpack.type.MapValue;
import org.msgpack.type.Value;


public class AirSimBase{

}

class AirSimClientBase extends AirSimStructures {
		protected Client client;
		
	    public AirSimClientBase(String ip, int port) throws UnknownHostException {
	    	EventLoop loop = EventLoop.start();
	        if (ip == "") {
	            ip = "127.0.0.1";
	        }
	        
	    	this.client = new Client(ip, port, loop);        
	    }
	    
	    public String ping() {
	        Value response = this.client.callApply("ping", new Object[]{});
	        return response.asRawValue().getString();
	    }
	    
	    public void confirmConnection() throws InterruptedException {
	        System.out.print("Waiting for connection: ");
	        GeoPoint home = this.getHomeGeoPoint();
	        while (home.isZero()) {
	            Thread.sleep(1000);
	            home = this.getHomeGeoPoint();
	            System.out.print(".");
	        }
	        
	        System.out.print("connected. ");
	        System.out.println(home.toString());
	    }
	    
	    public GeoPoint getHomeGeoPoint() {
	    	Value result = this.client.callApply("getHomeGeoPoint", new Object[]{});
	    	
	    	return new GeoPoint(result.asMapValue());
	    }
	    
	    // Basic flight control
	    public void enableApiControl(boolean is_enabled) {
	        this.client.callApply("enableApiControl", new Object[] {is_enabled});
	    }
	    
	    public boolean isApiControlEnabled() {
	    	Value response = this.client.callApply("isApiControlEnabled", new Object[] {});
	    	return response.asBooleanValue().getBoolean();
	    }
	}

// -----------------------------------  Multirotor APIs ---------------------------------------------
class MultirotorClient extends AirSimClientBase {
    public MultirotorClient(String ip, int port) throws UnknownHostException {
        super(ip, port);
    }
        
    public void armDisarm(boolean arm) {
    	this.client.callApply("armDisarm", new Object[] {arm});
    }
    
    public boolean timeoutCommand(String command, float max_wait_seconds) {
    	Value response = this.client.callApply(command, new Object[] {max_wait_seconds});
    	return response.asBooleanValue().getBoolean();
    }
    
    public boolean booleanCommand(String command) {
    	Value response = this.client.callApply(command, new Object[] {});
    	return response.asBooleanValue().getBoolean();
    }
    
    public int intCommand(String command) {
    	Value response = this.client.callApply(command, new Object[] {});
    	return response.asIntegerValue().getInt();
    }
    
    public MapValue mapCommand(String command) {
    	Value response = this.client.callApply(command, new Object[] {});
    	return response.asMapValue();
    }
    
    public void voidCommand(String command) {
    	this.client.callApply(command, new Object[] {});
    }
        
    public boolean takeoff(float max_wait_seconds) {
    	return timeoutCommand("takeoff", max_wait_seconds);
    }
        
    public boolean land(float max_wait_seconds) {
    	return timeoutCommand("land", max_wait_seconds);
    }
    
    public void goHome() {
        voidCommand("goHome");
    }
    
    public void hover() {
        voidCommand("hover");
    }
        

 // -----------------------------------  Query Methods ---------------------------------------------
    public Vector3r getPosition() {
        return new Vector3r(mapCommand("getPosition"));
    }
    
    public Vector3r getVelocity() {
    	return new Vector3r(mapCommand("getVelocity"));
    }
    
    public Quaternionr getOrientation() {
        return new Quaternionr(mapCommand("getOrientation"));
    }
    
    public int getLandedState() {
        return intCommand("getLandedState");
    }
    
    public GeoPoint getGpsLocation() {
        return new GeoPoint(mapCommand("getGpsLocation"));
    }
    
    public Triplet getRollPitchYaw() {
        return AirSimStructures.toEulerianAngle(this.getOrientation());
    }
    
//	    public CollisionInfo getCollisionInfo() {
//	        return CollisionInfo.from_msgpack(this.client.call('getCollisionInfo'))
//	    }
//    
//	     getRCData(self) {
//	    #    return this.client.call('getRCData')
//	    }
//	     
//	     timestampNow(self) {
//	        return this.client.call('timestampNow')
//	    }
//	     
//	     getServerDebugInfo(self) {
//	         return this.client.call('getServerDebugInfo')
    
    public boolean isSimulationMode() {
        return booleanCommand("isSimulationMode");
    }


 // -----------------------------------  APIs for control ---------------------------------------------
    public Value moveByAngle(float pitch, float roll, float z,
    		float yaw, float duration) {
    	Object[] args = new Object[] {pitch, roll, z, yaw, duration};
    	
        return this.client.callApply("moveByAngle", args);
    }
    
    public Value moveByVelocity(Vector3r vel, float duration,
    		int drivetrain, YawMode yaw_mode) {
    	Object[] args = new Object[] {vel.getX(), vel.getY(), vel.getZ(),
    			duration, drivetrain, yaw_mode.toMap()};
    	
        return this.client.callApply("moveByVelocity", args);
    }
    
    public Value moveByVelocity(Vector3r vel, float duration) {
    	Object[] args = new Object[] {vel.getX(), vel.getY(), vel.getZ(), duration,
    			DrivetrainType.ForwardOnly, new YawMode()};
        return this.client.callApply("moveByVelocity", args);
    }
    
    public Value moveByVelocityZ(Vector3r vel, Vector3r pos, float duration,
    		int drivetrain, YawMode yaw_mode) {    	
    	Object[] args = new Object[] {vel.getX(), vel.getY(), pos.getZ(), duration,
    			drivetrain, yaw_mode.toMap()};
        return this.client.callApply("moveByVelocityZ", args);
    }    
    
    public Value moveByVelocityZ(Vector3r vel, Vector3r pos, float duration) {
    	int drivetrain = DrivetrainType.ForwardOnly;
    	YawMode yaw_mode = new YawMode();
 
        return moveByVelocityZ(vel, pos, duration, drivetrain, yaw_mode);
    }
    
    public Value moveOnPath(List<Vector3r> path, float velocity, float max_wait_seconds, int drivetrain, YawMode yaw_mode,
			float lookahead, float adaptive_lookahead) {
    	Object[] args = new Object[] {path, velocity, max_wait_seconds,
        		yaw_mode.toMap(), lookahead, adaptive_lookahead};
	        return this.client.callApply("moveOnPath", args);
	}
    
	public Value moveOnPath(List<Vector3r> path, float velocity) {
    	float max_wait_seconds = 60;
    	int drivetrain = DrivetrainType.ForwardOnly;
    	YawMode yaw_mode = new YawMode();
    	float lookahead = -1;
    	float adaptive_lookahead = 1;
	    return moveOnPath(path, velocity, max_wait_seconds, drivetrain, yaw_mode, lookahead, adaptive_lookahead);
	}
    
    public Value moveToZ(float z, float velocity, float max_wait_seconds,
    		YawMode yaw_mode, float lookahead, float adaptive_lookahead) {
        Object[] args = new Object[] {z, velocity, max_wait_seconds,
        		yaw_mode.toMap(), lookahead, adaptive_lookahead};
        
    	return this.client.callApply("moveToZ", args);
    }
    
    public Value moveToZ(float z, float velocity, float max_wait_seconds) {
    	YawMode yaw_mode = new YawMode();
    	float lookahead = -1;
    	float adaptive_lookahead = 1;
        return moveToZ(z, velocity, max_wait_seconds, yaw_mode,
        		lookahead, adaptive_lookahead);
    }
    
    public Value moveToZ(float z, float velocity) {
    	float max_wait_seconds = 60;
    	YawMode yaw_mode = new YawMode();
    	float lookahead = -1;
    	float adaptive_lookahead = 1;
        return moveToZ(z, velocity, max_wait_seconds, yaw_mode,
        		lookahead, adaptive_lookahead);
    }
    
    public Value moveToPosition(Vector3r pos, float velocity,
    		float max_wait_seconds, int drivetrain, YawMode yaw_mode,
    		float lookahead, float adaptive_lookahead) {
    	Object[] args = new Object[] {pos.getX(), pos.getY(), pos.getZ(), velocity, max_wait_seconds,
        		drivetrain, yaw_mode.toMap(), lookahead, adaptive_lookahead};
    	
    	return this.client.callApply("moveToPosition", args);

    }
    
    public Value moveToPosition(Vector3r pos, float velocity) {
        float max_wait_seconds = 20;
        int drivetrain = DrivetrainType.MaxDegreeOfFreedom;
    	YawMode yaw_mode = new YawMode();    	
    	float lookahead = -1;
    	float adaptive_lookahead = 1;
    	
    	return moveToPosition(pos, velocity, max_wait_seconds, drivetrain,
    			yaw_mode, lookahead, adaptive_lookahead);
    }
    
//	    def moveByManual(self, vx_max, vy_max, z_min, duration, drivetrain = DrivetrainType.ForwardOnly, yaw_mode = YawMode()) {
//	        return this.client.call('moveByManual', vx_max, vy_max, z_min, duration, drivetrain, yaw_mode)
//
//	    }
    
    public Value rotateToYaw(float yaw, float max_wait_seconds, float margin) {
    	Object[] args = new Object[] {yaw, max_wait_seconds, margin}; 

        return this.client.callApply("rotateToYaw", args);
    }
    
    public Value rotateToYaw(float yaw) {
    	float max_wait_seconds = 60;
    	float margin = 5;
    	return rotateToYaw(yaw, max_wait_seconds, margin);
    }
    
    public Value rotateByYawRate(float yaw_rate, float duration) {
    	Object[] args = new Object[] {yaw_rate,duration}; 
    
        return this.client.callApply("rotateByYawRate", args);
    }
}

