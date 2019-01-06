package comp557.a4;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Class for a plane at y=0.
 * 
 * This surface can have two materials.  If both are defined, a 1x1 tile checker 
 * board pattern should be generated on the plane using the two materials.
 */
public class Plane extends Intersectable {
    
	/** The second material, if non-null is used to produce a checker board pattern. */
	Material material2;
	
	/** The plane normal is the y direction */
	public static final Vector3d n = new Vector3d( 0, 1, 0 );
    
    /**
     * Default constructor
     */
    public Plane() {
    	super();
    }

        
    @Override
    public void intersect( Ray ray, IntersectResult result ) { 

    	Vector3d position = new Vector3d(ray.eyePoint);
		double t = - position.dot(n) / ray.viewDirection.dot(n);//y=0 of plane

    	if(t>0.0001 &&t<result.t){
        	result.n= new Vector3d( 0, 1, 0 );
        	result.t=t;
        	
        	result.p=new Point3d(ray.viewDirection);
        	result.p.scale(t);
        	result.p.add(ray.eyePoint);
        	
        	result.material=material;
        	int x=(int)Math.floor(result.p.x);
        	int z=(int)Math.floor(result.p.z);
        	if(material2!=null && (x+z)%2!=0)
    			result.material=material2;
    	}
    }
    
}
