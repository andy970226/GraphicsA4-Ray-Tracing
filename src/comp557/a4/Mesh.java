package comp557.a4;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Mesh extends Intersectable {
	
	/** Static map storing all meshes by name */
	public static Map<String,Mesh> meshMap = new HashMap<String,Mesh>();
	
	/**  Name for this mesh, to allow re-use of a polygon soup across Mesh objects */
	public String name = "";
	
	/**
	 * The polygon soup.
	 */
	public PolygonSoup soup;

	public Mesh() {
		super();
		this.soup = null;
	}			
		
	@Override
	public void intersect(Ray ray, IntersectResult result) {

		// TODO: Objective 9: ray triangle intersection for meshes

	    Vector3d e = new Vector3d(ray.eyePoint);
	    Vector3d d = new Vector3d(ray.viewDirection);
		
	    
	    for (Mesh mesh: meshMap.values()) {
			for (int[] face: mesh.soup.faceList) {
				Point3d V0 = new Point3d(mesh.soup.vertexList.get(face[0]).p);
				Point3d V1 = new Point3d(mesh.soup.vertexList.get(face[1]).p);
				Point3d V2 = new Point3d(mesh.soup.vertexList.get(face[2]).p);
				
				
				Vector3d V0V1 = new Vector3d(V1); V0V1.sub(V0);
				Vector3d V0V2 = new Vector3d(V2); V0V2.sub(V0);
				Vector3d normal = new Vector3d();
				normal.cross(V0V1, V0V2);
				
				double normaldotd = normal.dot(d);
				
				if (Math.abs(normaldotd) < 1e-5) {continue;}
				
				
					Vector3d V0eye = new Vector3d(V0);
					V0eye.sub(e);
					double t = (normal.dot(V0eye)) / normaldotd;
					
					//find the intersection point on plane
					if (t > 0 &&t < result.t) {
					

					Vector3d p = new Vector3d(d); 
					p.scale(t); 
					p.add(e);
					
					
					Vector3d perp1 = new Vector3d();
					Vector3d perp2 = new Vector3d();
					Vector3d perp0 = new Vector3d();
					
					
					Vector3d edge0 = new Vector3d(V1);
					edge0.sub(V0);
					Vector3d boundary0 = new Vector3d(p);
					boundary0.sub(V0);
					perp0.cross(edge0, boundary0);
			
					
					Vector3d edge1 = new Vector3d(V2);
					edge1.sub(V1);
					Vector3d boundary1 = new Vector3d(p);
					boundary1.sub(V1);
					perp1.cross(edge1, boundary1);
								
					
					Vector3d edge2 = new Vector3d(V0);
					edge2.sub(V2);
					Vector3d boundary2 = new Vector3d(p);
					boundary2.sub(V2);
					perp2.cross(edge2, boundary2);
								//check inside each edge, make sure point at the right side
					if(normal.dot(perp0) > 0&&normal.dot(perp1) > 0&&normal.dot(perp2) > 0) {
						result.t = t;
						result.p.set(p);
						normal.normalize();
						result.n.set(normal);
						result.material = material;									
								
					}
			}
					}
	}

}}
