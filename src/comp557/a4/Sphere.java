package comp557.a4;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A simple sphere class.
 */
public class Sphere extends Intersectable {
    
	/** Radius of the sphere. */
	public double radius = 1;
    
	/** Location of the sphere center. */
	public Point3d center = new Point3d( 0, 0, 0 );
    
    /**
     * Default constructor
     */
    public Sphere() {
    	super();
    }
    
    /**
     * Creates a sphere with the request radius and center. 
     * 
     * @param radius
     * @param center
     * @param material
     */
    public Sphere( double radius, Point3d center, Material material ) {
    	super();
    	this.radius = radius;
    	this.center = center;
    	this.material = material;
    }
    
    @Override
    public void intersect( Ray ray, IntersectResult result ) {
    
    	Vector3d d=new Vector3d(ray.viewDirection);
    	Vector3d e=new Vector3d(ray.eyePoint);
    	Vector3d p=new Vector3d(e);
    	p.sub(center);
    	
    	
    	double ddotp=d.dot(p);
    	double ddotd=d.dot(d);
    	double pdotp=p.dot(p); 	
    	double sqrt=ddotp*ddotp-ddotd*(pdotp-radius*radius);      //the formula
    	
    	if(sqrt>=0 && ddotd!=0){
    		
        	double root=Math.sqrt(sqrt);
        	double t1=(-ddotp+root)/ddotd;
        	double t2=(-ddotp-root)/ddotd;
        	double t=Math.min(t1, t2);
        	
        	if(t2>0)
        		t=t2;
        	else
        		t=t1;
        	
        		
        	if(t<result.t&&t>0){
            	result.material=material;
        		result.t=t;
            	
            	result.p.set(d);
            	result.p.scale(t);
            	result.p.add(e);
            	
            	Vector3d normal=new Vector3d(result.p);
            	normal.sub(center);
            	normal.normalize();
            	result.n=normal;
            	

        	}
    	}
    
        // TODO: Objective 2: intersection of ray with sphere
	
    }
    
}
