package comp557.a4;


import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Class for a plane at y=0.
 * 
 * This surface can have two materials.  If both are defined, a 1x1 tile checker 
 * board pattern should be generated on the plane using the two materials.
 */
public class Quadrics extends Intersectable {
    

	

    
    /**
     * Default constructor
     */
    public Quadrics() {
    	super();
    }

    public float aa=6;
    public float bb=6;
    
    @Override
    public void intersect( Ray ray, IntersectResult result ) { 
    	


    	Vector3d d=new Vector3d(ray.viewDirection);
    	Vector3d e=new Vector3d(ray.eyePoint);
    	//At^2+Bt+C=0
    	double A,B,C,t;
    	A=bb*d.x*d.x+aa*d.y*d.y;
    	B=bb*2*e.x*d.x+aa*2*e.y*d.y-2*aa*bb*d.z;
    	C=bb*e.x*e.x+aa*e.y*e.y-2*aa*bb*e.z;
    	double sqrt=B*B-4*A*C;
    	if(sqrt>=0 && A!=0){
    		
       	double root=Math.sqrt(sqrt);
        	double t1=(-B+root)/A/2;
        	double t2=(-B-root)/A/2;
        	t=Math.min(t1, t2);
        	
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
           	//System.out.println(result.p.z);
//            	
//            	Vector3d normal=new Vector3d(result.p);
//            	normal.sub(center);
//            	normal.normalize();
           	result.n=new Vector3d();
           	result.n.x=2/aa*result.p.x;
           	result.n.y=2/bb*result.p.y;
           	result.n.z=-2;
           	result.n.normalize();
           	
        	}
//            	
//
//        	}}
    	
    }}}
    	
    	

    

