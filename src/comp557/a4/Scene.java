package comp557.a4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Simple scene loader based on XML file format.
 */
public class Scene {
    
    /** List of surfaces in the scene */
    public List<Intersectable> surfaceList = new ArrayList<Intersectable>();
	
	/** All scene lights */
	public Map<String,Light> lights = new HashMap<String,Light>();

    /** Contains information about how to render the scene */
    public Render render;

    /** The ambient light colour */
    public Color3f ambient = new Color3f();
    
    
    public boolean depthofblur=false; //may change this bool
    public boolean reflections=true; //may change this bool
    /** 
     * Default constructor.
     */
    public Scene() {
    	
    	this.render = new Render();
    }
    
    /**
     * renders the scene
     */
    public void render(boolean showPanel) {
 
        Camera cam = render.camera; 
        int w = cam.imageSize.width;
        int h = cam.imageSize.height;
        
        render.init(w, h, showPanel);
        //bonus--multi threaded
        RenderThread t1 = new RenderThread(this,cam,w,h,1);
        RenderThread t2 = new RenderThread(this,cam,w,h,2);
        RenderThread t3 = new RenderThread(this,cam,w,h,3);
        RenderThread t4 = new RenderThread(this,cam,w,h,4);
        RenderThread t5 = new RenderThread(this,cam,w,h,5);
        RenderThread t6 = new RenderThread(this,cam,w,h,6);
        RenderThread t7 = new RenderThread(this,cam,w,h,7);
        RenderThread t8 = new RenderThread(this,cam,w,h,8);
        RenderThread t9 = new RenderThread(this,cam,w,h,9);
        RenderThread t10 = new RenderThread(this,cam,w,h,10);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();        
        t9.start();
        t10.start();//10-threaded
        
        
        

        
        // save the final render image
        render.save();
        
        // wait for render viewer to close
        render.waitDone();
        
    }
    class RenderThread extends Thread {
		int h;
		int w;
		Scene scene;
		Camera cam;
		int index;
		
		RenderThread(Scene scene, Camera cam, int w, int h, int index){
			this.scene = scene;
			this.cam = cam;
			this.w = w;
			this.h = h;
			this.index = index;
		}
		public void run() {
				              int camnumber;
				              double distance;
			int upperHeight, lowerHeight;
	        upperHeight=h*(index-1)/10;
	        lowerHeight=h*index/10;
	        for ( int i = upperHeight; i < lowerHeight && !render.isDone(); i++ ) {
	            for ( int j = 0; j < w && !render.isDone(); j++ ) {
	            	
	            		

	            	ArrayList<Ray> rays=new ArrayList<Ray>();
	            	if(depthofblur)
	            		{camnumber=9;
	            		distance=0.01;
	            		}
	            	else {camnumber=1;distance=0;}
	            	for (int numbercam=0;numbercam<camnumber;numbercam++ )
	                {
	            		//bonus----depth of blur. You may change the setting bool depthofblur.
	            		Camera currentcam=new Camera();
	            		currentcam.from.x=cam.from.x+(numbercam/3-1)*(cam.to.z-cam.from.z)*distance;
	            		currentcam.from.y=cam.from.y+(numbercam%3-1)*(cam.to.z-cam.from.z)*distance;
	            		currentcam.from.z=cam.from.z;
	            		currentcam.fovy=cam.fovy;
	            		currentcam.imageSize=cam.imageSize;
	            		currentcam.up.set(cam.up);
	            		currentcam.to.set(cam.to);
	            	
	            	
	            	  // TODO: Objective 1: generate a ray (use the generateRay method)
	            	
	            	// TODO: Objective 8: do antialiasing by sampling more than one ray per pixel
	            	
	            	//subsample        	
	            	int c=(int)Math.sqrt(render.samples);
	            	int r=render.samples/c;
	            	int remainder=render.samples-r*c;   
	            	//not square sample--get reminder
	            	
	            	for(int k=0;k<r;k++){

	            		int numCol=c;
	            		if(k==r-1)
	            			numCol+=remainder;
	            		
	            		for(int l=0;l<numCol;l++){
	            			
	            			
	            			//Objective 8---------------subsample         			
	                    	double eachx=(1.0/(numCol+1)); 
	                    	double eachy=(1.0/(r+1));      
	                    	//Objective 8---------------jitter
	                    	double jitterx=eachx/2;
	                    	double jittery=eachy/2;

	                    	if(render.jitter){
	                        	jitterx=Math.random()*eachx;
	                        	jittery=Math.random()*eachy;
	                    	}

	                    	double xOffset=(k)*eachx+jitterx-0.5;
	                    	double yOffset=(l)*eachy+jittery-0.5;

	                    	
	                    	
	                    	
	                    	Ray ray=new Ray();
	                		generateRay(j, i, new double[]{xOffset,yOffset}, currentcam, ray);
	                		rays.add(ray);
	            		}}
	   
	                	
	            	
	            	
	                // TODO: Objective 2: test for intersection with scene surfaces
	            	Color3f sum=new Color3f();
	            	for(Ray ray:rays){        		
	                	IntersectResult result=new IntersectResult();
	                	for(Intersectable surface:surfaceList){
	                		surface.intersect(ray, result);
	                	}
	                	result.n.normalize();	
	                	Color3f color=new Color3f(render.bgcolor);
	            
	                	
	                // TODO: Objective 3: compute the shaded result for the intersection point (perhaps requiring shadow rays)		
	                	if(result.t<Double.POSITIVE_INFINITY)        		
	                		color=computeshadedresult(ray,result, reflections);
	                	
	                	sum.add(color);//add to sum
	            	}
	            	

	            	

	            	sum.scale((float) (1.0/render.samples));//samples times
	            	sum.scale(1.0f/camnumber);  //depth of blur times
	            	sum.clampMax(1);
	            	int red = (int)(255*sum.x);
	                int green = (int)(255*sum.y);
	                int blue = (int)(255*sum.z);
	                int a = 255;
	                int argb = (a<<24 | red<<16 | green<<8 | blue);         
	                render.setPixel(j, i, argb);
	                }}
	        }
		render.save();
		}
    }
    
    
    
    
    private Color3f computeshadedresult(Ray ray,IntersectResult result,boolean reflection) {
    	
    	
    	Color3f Color=new Color3f(ambient.x*result.material.diffuse.x,
				ambient.y*result.material.diffuse.y,ambient.z*result.material.diffuse.z);
    	
    	for(String key:lights.keySet()){
    		
			Light light=lights.get(key);
			if(light.type.equals("point"))
			{
				if(!inShadow(result, light, surfaceList, new IntersectResult(), new Ray())) {
					
					Vector3d l=new Vector3d(light.from);
					l.sub(result.p);
					l.normalize();// l is light
					Vector3d bisector=new Vector3d(ray.eyePoint);
					bisector.sub(result.p);// is v
					bisector.normalize();
					bisector.add(l);
					bisector.normalize(); 
		    	
					// diffuse=k_d*I*max(0, n dot l)
					Color3f diffuse=new Color3f(result.material.diffuse.x*light.color.x,result.material.diffuse.y*light.color.y,result.material.diffuse.z*light.color.z);
					diffuse.scale((float)(light.power*Math.max(0, result.n.dot(l))));
					Color.add(diffuse);
					
				
					// specular=k_s*I*max(0, n dot bisec)^p
					Color3f specular=new Color3f(result.material.specular.x*light.color.x,result.material.specular.y*light.color.y,result.material.specular.z*light.color.z);
					specular.scale((float)(light.power*Math.pow(Math.max(0, result.n.dot(bisector)),result.material.shinyness)));
					Color.add(specular);
				
				
				
				if(reflection){
					
    				Ray ref=new Ray();
    				IntersectResult reResult=new IntersectResult();
    				
    				ref.viewDirection.set(result.n);
    				ref.viewDirection.scale(-2*ray.viewDirection.dot(result.n));
    				ref.viewDirection.add(ray.viewDirection);
    				
    				ref.eyePoint=new Point3d(ref.viewDirection);
    				ref.eyePoint.scale(1e-9);
    				ref.eyePoint.add(result.p);
    				
    				
    				for(Intersectable surface:surfaceList){
                		surface.intersect(ref, reResult);
                	}
    				reResult.n.normalize();
    				if(reResult.t<Double.POSITIVE_INFINITY){
    					Color3f reflectionresult=computeshadedresult(ray, reResult, false);
        				reflectionresult=new Color3f(0.1f*reflectionresult.x, 0.1f*reflectionresult.y,
        						0.1f*reflectionresult.z);//here I use constant as reflection parameters. It saves time.
        				Color.add(reflectionresult);
    				}
    			}
				
				
				
			}}
			else //for area light
			{
			Light[] arealight=new Light[25];
			for(int numberoflight=0;numberoflight<25;numberoflight++)
			{
			arealight[numberoflight]=new Light();
			arealight[numberoflight].from.x=light.from.x+(numberoflight/5-2)*0.5;
			arealight[numberoflight].from.y=light.from.y;
			arealight[numberoflight].from.z=light.from.z+(numberoflight%5-2)*0.5;
			arealight[numberoflight].color=light.color;
			arealight[numberoflight].power=light.power/25;
			arealight[numberoflight].type="point";
			
			
			if(!inShadow(result, arealight[numberoflight], surfaceList, new IntersectResult(), new Ray())) {
				
			Vector3d l=new Vector3d(light.from);
			l.sub(result.p);
			l.normalize();// l is light
			Vector3d bisector=new Vector3d(ray.eyePoint);
			bisector.sub(result.p);// is v
			bisector.normalize();
			bisector.add(l);
			bisector.normalize(); 
    	// diffuse=k_d*I*max(0, n dot l)
			Color3f diffuse=new Color3f(result.material.diffuse.x*light.color.x,result.material.diffuse.y*light.color.y,result.material.diffuse.z*light.color.z);
			diffuse.scale((float)(arealight[numberoflight].power*Math.max(0, result.n.dot(l))));
			Color.add(diffuse);
			
		
		// specular=k_s*I*max(0, n dot bisec)^p
		Color3f specular=new Color3f(result.material.specular.x*light.color.x,result.material.specular.y*light.color.y,result.material.specular.z*light.color.z);
		specular.scale((float)(arealight[numberoflight].power*Math.pow(Math.max(0, result.n.dot(bisector)),result.material.shinyness)));
		Color.add(specular);
		
		if(reflection){
			
			Ray ref=new Ray();
			IntersectResult reResult=new IntersectResult();
			ref.viewDirection.set(result.n);
			ref.viewDirection.scale(-2*ray.viewDirection.dot(result.n));
			ref.viewDirection.add(ray.viewDirection);
			ref.eyePoint=new Point3d(ref.viewDirection);
			ref.eyePoint.scale(1e-9);
			ref.eyePoint.add(result.p);        				
			
			for(Intersectable surface:surfaceList){
        		surface.intersect(ref, reResult);
        	}
			reResult.n.normalize();
			if(reResult.t<Double.POSITIVE_INFINITY){
				Color3f reflectionresult=computeshadedresult(ray, reResult, false);
				reflectionresult=new Color3f(0.007f*reflectionresult.x, 0.007f*reflectionresult.y,
						0.007f*reflectionresult.z);//here I use constant as reflection parameters. It saves time.
				Color.add(reflectionresult);
			}
		}
    	}}}}
    	
    	
		return Color;
		
	}

	/**
     * Generate a ray through pixel (i,j).
     * 
     * @param i The pixel row.
     * @param j The pixel column.
     * @param offset The offset from the center of the pixel, in the range [-0.5,+0.5] for each coordinate. 
     * @param cam The camera.
     * @param ray Contains the generated ray.
     */
	public static void generateRay(final int i, final int j, final double[] offset, final Camera cam, Ray ray) {
		
		// TODO: Objective 1: generate rays given the provided parmeters
		
		ray.eyePoint=new Point3d(cam.from);//eyepoint set
		
		Vector3d tofrom=new Vector3d(cam.from);
		tofrom.sub(cam.to);
		double distance=tofrom.length();
		tofrom.normalize();//tofrom is w
		
		double top,bottom,right,left;
		
		top=distance*Math.tan(Math.toRadians(cam.fovy/2.0));
		bottom=-top;
		
		double ratio=(double)cam.imageSize.width/cam.imageSize.height;
		right=top*ratio;
		left=-right;
		
		
		Vector3d v=new Vector3d();
		Vector3d u=new Vector3d();
		
		u.cross(cam.up, tofrom);// u =up * tofrom
		u.normalize();
		v.cross(u, tofrom);//v =u * tofrom
		
		
		Vector3d direction=new Vector3d();
		
		
		// u=left+(right-left)(i+0.5+offset)/nx
		u.scale(left+(right-left)*(i+0.5+offset[0])/cam.imageSize.width);
		// v=top+(top-bottom)(j+0.5+offset)/ny
		v.scale(bottom+(top-bottom)*(j+0.5+offset[1])/cam.imageSize.height);
		tofrom.scale(distance);

		//s=e+uu+vv-dw
		direction.add(u);
		direction.add(v);
		direction.sub(tofrom);		
		ray.viewDirection=direction;// direction set
	}

	/**
	 * Shoot a shadow ray in the scene and get the result.
	 * 
	 * @param result Intersection result from raytracing. 
	 * @param light The light to check for visibility.
	 * @param root The scene node.
	 * @param shadowResult Contains the result of a shadow ray test.
	 * @param shadowRay Contains the shadow ray used to test for visibility.
	 * 
	 * @return True if a point is in shadow, false otherwise. 
	 */
	public static boolean inShadow(final IntersectResult result, final Light light, final List<Intersectable> surfaces, IntersectResult shadowResult, Ray shadowRay) {
		
		shadowRay.viewDirection=new Vector3d(light.from.x - result.p.x, light.from.y - result.p.y, light.from.z - result.p.z);
		shadowRay.eyePoint=new Point3d(shadowRay.viewDirection);
		shadowRay.eyePoint.scale(1e-9);
		shadowRay.eyePoint.add(result.p);
		for (Intersectable surface: surfaces) {
			surface.intersect(shadowRay, shadowResult);
		}
		if (shadowResult.t < 1&& shadowResult.t>0) {
			return true;
		} else {
			return false;
		// TODO: Objective 5: check for shadows and use it in your lighting computation
		}
	}    
}
