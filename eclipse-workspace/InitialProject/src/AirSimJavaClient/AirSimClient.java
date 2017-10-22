package AirSimJavaClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.nd4j.shade.jackson.*;
import org.nd4j.shade.jackson.databind.ObjectMapper;
import org.msgpack.rpc.*;

public class AirSimClient extends MultirotorClient {

	AirSimClient(String ip, int port) {
		super(ip, port);
		// TODO Auto-generated constructor stub
	}

}

class AirSimImageType {    
    static int Scene = 0;
    static int DepthPlanner = 1;
    static int DepthPerspective = 2;
    static int DepthVis = 3;
    static int DisparityNormalized = 4;
    static int Segmentation = 5;
    static int SurfaceNormals = 6;
}

class Drivetraintype {
    static int MaxDegreeOfFreedom = 0;
    static int ForwardOnly = 1;    
}

class LandedState {
    static int Landed = 0;
    static int Flying = 1;
}

class MsgpackMixin {
   	ObjectMapper mapper = new ObjectMapper();
   	
    public Map to_msgpack(Object obj) {
    	return mapper.convertValue(obj, Map.class);
    }

    public Object from_msgpack(Object type, Map encoded) {
    	return mapper.convertValue(encoded, type.getClass());
    }
}

class Vector3r extends MsgpackMixin {
    float x_val = 0;
    float y_val = 0;
    float z_val = 0;

    public Vector3r(float x_val, float y_val, float z_val) {
        this.x_val = x_val;
        this.y_val = y_val;
        this.z_val = z_val;
    }
    
    public Vector3r() {
        this.x_val = 0;
        this.y_val = 0;
        this.z_val = 0;
    }
}
    
class Quaternionr extends MsgpackMixin {
    float w_val = 0;
    float x_val = 0;
    float y_val = 0;
    float z_val = 0;

    public Quaternionr (float x_val, float y_val, float z_val, float w_val) {
        this.x_val = x_val;
        this.y_val = y_val;
        this.z_val = z_val;
        this.w_val = w_val;
    }
    
    public Quaternionr () {
        this.x_val = 0;
        this.y_val = 0;
        this.z_val = 0;
        this.w_val = 1;
    }
}

class Pose extends MsgpackMixin {
    Vector3r position = new Vector3r();
    Quaternionr orientation = new Quaternionr();

    public Pose(Vector3r position_val, Quaternionr orientation_val) {
        this.position = position_val;
        this.orientation = orientation_val;
    }
}

class CollisionInfo extends MsgpackMixin {
    boolean has_collided = false;
    Vector3r normal = new Vector3r();
    Vector3r impact_point = new Vector3r();
    Vector3r position = new Vector3r();
    float penetration_depth = 0;
    float time_stamp = 0;
}

class GeoPoint extends MsgpackMixin {
    float latitude = 0;
    float longitude = 0;
    float altitude = 0;
}

class YawMode extends MsgpackMixin {
    boolean is_rate = true;
    float yaw_or_rate = 0;
    
    public YawMode(boolean is_rate, float yaw_or_rate) {
        this.is_rate = is_rate;
        this.yaw_or_rate = yaw_or_rate;
    }
    
    public YawMode() {
    	this.is_rate = false;
    	this.yaw_or_rate = 0;
    }
    
    public YawMode(float yaw_or_rate) {
    	this.is_rate = false;
    	this.yaw_or_rate = yaw_or_rate;
    }
}

class ImageRequest extends MsgpackMixin {
    int camera_id = 0;
    int image_type = AirSimImageType.Scene;
    boolean pixels_as_float = false;
    boolean compress = false;

    public ImageRequest(int camera_id, int image_type, boolean pixels_as_float, boolean compress) {
        this.camera_id = camera_id;
        this.image_type = image_type;
        this.pixels_as_float = pixels_as_float;
        this.compress = compress;
    }
    
    public ImageRequest(int camera_id, int image_type) {
        this.camera_id = camera_id;
        this.image_type = image_type;
        this.pixels_as_float = false;
        this.compress = true;
    }
    
    public ImageRequest(int camera_id, int image_type, boolean pixels_as_float) {
        this.camera_id = camera_id;
        this.image_type = image_type;
        this.pixels_as_float = pixels_as_float;
        this.compress = true;
    }


}

class ImageResponse extends MsgpackMixin {
    int image_data_uint8 = 0;
    float image_data_float = 0;
    Vector3r camera_position = new Vector3r();
    Quaternionr camera_orientation = new Quaternionr();
    int time_stamp = 0;
    String message = "";
    float pixels_as_float = 0;
    boolean compress = true;
    int width = 0;
    int height = 0;
    int image_type = AirSimImageType.Scene;
}

class AirSimClientBase {
	protected Client client;
	
    public AirSimClientBase(String ip, int port) {
        if (ip == "") {
            ip = "127.0.0.1";
        }
        
    	this.client = new Client(ip, port);        
    }
    
    public void ping() {
        this.client.callApply("ping", new Object[]{});
    }
    
    public void confirmConnection() {
        System.out.print("Waiting for connection: ");
        GeoPoint home = this.getHomeGeoPoint();
        while ((home.latitude == 0 && home.longitude == 0 && home.altitude == 0) ||
                Double.isNaN(home.latitude) || Double.isNaN(home.longitude) || Double.isNaN(home.altitude)) {
            Thread.sleep(1000);
            home = this.getHomeGeoPoint();
            System.out.print("X");
        }
        
        System.out.println("Connected");
    }
    
    public GeoPoint getHomeGeoPoint() {
        return GeoPoint.from_msgpack(this.client.callApply("getHomeGeoPoint", null));
    }
    
    // Basic flight control
    public void enableApiControl(boolean is_enabled) {
        return this.client.call("enableApiControl", is_enabled);
    }
    
    public boolean isApiControlEnabled() {
        return this.client.call("isApiControlEnabled");
    }

    // static method
    public byte[]  stringToUint8Array(String bstr) {
        return bstr.getBytes();
    }
    
    // helper method for converting getOrientation to roll/pitch/yaw
    // https{#en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles
    // static method
    public Triplet toEulerianAngle(Quaternionr q) {
        float z = q.z_val;
        float y = q.y_val;
        float x = q.x_val;
        float w = q.w_val;
        float ysqr = y*y;

        // roll (x-axis rotation)
        float t0 = 2.0f * (w*x + y*z);
        float t1 = 1.0f - 2.0f*(x*x + ysqr);
        float roll = (float) Math.atan2(t0, t1);

        // pitch (y-axis rotation)
        float t2 = 2.0f * (w*y - z*x);
        
        if (t2 > 1.0f) {
            t2 = 1.0f;
        }
        
        if (t2 < -1.0f) {
            t2 = -1.0f;
        }
        
        float pitch = (float) Math.asin(t2);

        // yaw (z-axis rotation)
        float t3 = 2.0f * (w*z + x*y);
        float t4 = 1.0f - 2.0f * (ysqr + z*z);
        float yaw = (float) Math.atan2(t3, t4);

        return new Triplet(pitch, roll, yaw);
    }

    // static method
    public Quaternionr toQuaternion(Triplet pry) {
    	float pitch = pry.get(0);
    	float roll = pry.get(1);
    	float yaw = pry.get(2);

        float t0 = (float) Math.cos(yaw * 0.5);
        float t1 = (float) Math.sin(yaw * 0.5);
        float t2 = (float) Math.cos(roll * 0.5);
        float t3 = (float) Math.sin(roll * 0.5);
        float t4 = (float) Math.cos(pitch * 0.5);
        float t5 = (float) Math.sin(pitch * 0.5);
        
        float x_val = t0 * t3 * t4 - t1 * t2 * t5;
        float y_val = t0 * t2 * t5 + t1 * t3 * t4;
        float z_val = t1 * t2 * t4 - t0 * t3 * t5;
        float w_val = t0 * t2 * t4 + t1 * t3 * t5;
        
        return new Quaternionr(x_val, y_val, z_val, w_val);

    }
}

// -----------------------------------  Multirotor APIs ---------------------------------------------
class MultirotorClient extends AirSimClientBase {
    public MultirotorClient(String ip, int port) {
        super(ip, port);
    }
        
    public void armDisarm(boolean arm) {
    	this.client.callApply("armDisarm", arm);
    }
    
    public boolean takeoff(int max_wait_seconds) {
        return this.client.callApply("takeoff", max_wait_seconds);
    }
        
//    } def land(self, max_wait_seconds = 60) {
//        return this.client.call('land', max_wait_seconds)
//        
//    } def goHome(self) {
//        return this.client.call('goHome')
//
//    } def hover(self) {
//        return this.client.call('hover')
//
//        
//    # query vehicle state
//    } def getPosition(self) {
//        return Vector3r.from_msgpack(this.client.call('getPosition'))
//    } def getVelocity(self) {
//        return Vector3r.from_msgpack(this.client.call('getVelocity'))
//    } def getOrientation(self) {
//        return Quaternionr.from_msgpack(this.client.call('getOrientation'))
//    } def getLandedState(self) {
//        return this.client.call('getLandedState')
//    } def getGpsLocation(self) {
//        return GeoPoint.from_msgpack(this.client.call('getGpsLocation'))
//    } def getRollPitchYaw(self) {
//        return this.toEulerianAngle(this.getOrientation())
//    } def getCollisionInfo(self) {
//        return CollisionInfo.from_msgpack(this.client.call('getCollisionInfo'))
//    #} def getRCData(self) {
//    #    return this.client.call('getRCData')
//    } def timestampNow(self) {
//        return this.client.call('timestampNow')
//    } def isApiControlEnabled(self) {
//        return this.client.call('isApiControlEnabled')
//    } def isSimulationMode(self) {
//        return this.client.call('isSimulationMode')
//    } def getServerDebugInfo(self) {
//        return this.client.call('getServerDebugInfo')
//
//
//    # APIs for control
//    } def moveByAngle(self, pitch, roll, z, yaw, duration) {
//        return this.client.call('moveByAngle', pitch, roll, z, yaw, duration)
//
//    } def moveByVelocity(self, vx, vy, vz, duration, drivetrain = DrivetrainType.MaxDegreeOfFreedom, yaw_mode = YawMode()) {
//        return this.client.call('moveByVelocity', vx, vy, vz, duration, drivetrain, yaw_mode)
//
//    } def moveByVelocityZ(self, vx, vy, z, duration, drivetrain = DrivetrainType.MaxDegreeOfFreedom, yaw_mode = YawMode()) {
//        return this.client.call('moveByVelocityZ', vx, vy, z, duration, drivetrain, yaw_mode)
//
//    } def moveOnPath(self, path, velocity, max_wait_seconds = 60, drivetrain = DrivetrainType.MaxDegreeOfFreedom, yaw_mode = YawMode(), lookahead = -1, adaptive_lookahead = 1) {
//        return this.client.call('moveOnPath', path, velocity, max_wait_seconds, drivetrain, yaw_mode, lookahead, adaptive_lookahead)
//
//    } def moveToZ(self, z, velocity, max_wait_seconds = 60, yaw_mode = YawMode(), lookahead = -1, adaptive_lookahead = 1) {
//        return this.client.call('moveToZ', z, velocity, max_wait_seconds, yaw_mode, lookahead, adaptive_lookahead)
//
//    } def moveToPosition(self, x, y, z, velocity, max_wait_seconds = 60, drivetrain = DrivetrainType.MaxDegreeOfFreedom, yaw_mode = YawMode(), lookahead = -1, adaptive_lookahead = 1) {
//        return this.client.call('moveToPosition', x, y, z, velocity, max_wait_seconds, drivetrain, yaw_mode, lookahead, adaptive_lookahead)
//
//    } def moveByManual(self, vx_max, vy_max, z_min, duration, drivetrain = DrivetrainType.MaxDegreeOfFreedom, yaw_mode = YawMode()) {
//        return this.client.call('moveByManual', vx_max, vy_max, z_min, duration, drivetrain, yaw_mode)
//
//    } def rotateToYaw(self, yaw, max_wait_seconds = 60, margin = 5) {
//        return this.client.call('rotateToYaw', yaw, max_wait_seconds, margin)
//
//    } def rotateByYawRate(self, yaw_rate, duration) {
//        return this.client.call('rotateByYawRate', yaw_rate, duration)
}

class Triplet {
	ArrayList<Float> data;
	
	public Triplet(float a, float b, float c) {
		data = new ArrayList<Float>();
		
		data.add(a);
		data.add(b);
		data.add(c);
	}
	
	public float get(int index) {
		return data.get(index);
	}
}