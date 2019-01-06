package comp557.a4;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A simple box class. A box is defined by it's lower (@see min) and upper (@see max) corner. 
 */
public class Box extends Intersectable {

	public Point3d max;
	public Point3d min;
	
    /**
     * Default constructor. Creates a 2x2x2 box centered at (0,0,0)
     */
    public Box() {
    	super();
    	this.max = new Point3d( 1, 1, 1 );
    	this.min = new Point3d( -1, -1, -1 );
    }	

	@Override
	public void intersect(Ray ray, IntersectResult result) {
		// TODO: Objective 6: intersection of Ray with axis aligned box
				double tmin,tmax;
				double xmin, xmax, ymin, ymax, zmin, zmax;
				//find which face to cross

				if(ray.viewDirection.x>0){
					xmin=(min.x-ray.eyePoint.x)/ray.viewDirection.x;
					xmax=(max.x-ray.eyePoint.x)/ray.viewDirection.x;
				}else{
					xmin=(max.x-ray.eyePoint.x)/ray.viewDirection.x;
					xmax=(min.x-ray.eyePoint.x)/ray.viewDirection.x;
				}
				if(ray.viewDirection.y>0){
					ymin=(min.y-ray.eyePoint.y)/ray.viewDirection.y;
					ymax=(max.y-ray.eyePoint.y)/ray.viewDirection.y;
				}else{
					ymin=(max.y-ray.eyePoint.y)/ray.viewDirection.y;
					ymax=(min.y-ray.eyePoint.y)/ray.viewDirection.y;
				}
				if(ray.viewDirection.z>0){
					zmin=(min.z-ray.eyePoint.z)/ray.viewDirection.z;
					zmax=(max.z-ray.eyePoint.z)/ray.viewDirection.z;
				}else{
					zmin=(max.z-ray.eyePoint.z)/ray.viewDirection.z;
					zmax=(min.z-ray.eyePoint.z)/ray.viewDirection.z;
				}
				
				tmin=Double.max(xmin, ymin);
				tmin=Double.max(tmin, zmin);
				tmax=Double.min(xmax, ymax);
				tmax=Double.min(tmax, zmax);
				
				  
				if(tmin<tmax && tmin>0){
					
					result.material=material;
					result.t=tmin;
					Point3d p=new Point3d(ray.viewDirection);
					p.scale(tmin);
					p.add(ray.eyePoint);
					result.p=p;
					
					
					final double epsilon=1e-9;
					//  find face and set the normal
					if(Math.abs(p.x-min.x)<epsilon){
						result.n=new Vector3d(-1,0,0);
					}else if(Math.abs(p.x-max.x)<epsilon){
						result.n=new Vector3d(1,0,0);
					}else if(Math.abs(p.y-min.y)<epsilon){
						result.n=new Vector3d(0,-1,0);
					}else if(Math.abs(p.y-max.y)<epsilon){
						result.n=new Vector3d(0,1,0);
					}else if(Math.abs(p.z-min.z)<epsilon){
						result.n=new Vector3d(0,0,-1);
					}else if(Math.abs(p.z-max.z)<epsilon){
						result.n=new Vector3d(0,0,1);
					}
				}
	}	

}
