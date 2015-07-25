package kenner.ko.n3base;

import java.text.DecimalFormat;

public class Vector3d implements Cloneable {
	private float x;
	private float y;
	private float z;
	
	public Vector3d(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void normalize(){
		float fn = (float) Math.sqrt(x*x + y*y + z*z);
		if(fn == 0)
			return;
		x /= fn;
		y /= fn;
		z /= fn;
	}
	
	public float magnitude(){
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
	
	public float dot(Vector3d v1){
		return x*v1.getX() + y*v1.getY() + z*v1.getZ();
	}
	
	public void cross(Vector3d v1, Vector3d v2){
		x = v1.getY() * v2.getZ() - v1.getZ() * v2.getY();
		y = v1.getZ() * v2.getX() - v1.getX() * v2.getZ();
		z = v1.getX() * v2.getY() - v1.getY() * v2.getX();
	}
	
	/**
	 * Changes any negative values to positive.
	 */
	public void absolute(){
		if(x < 0)
			x *= -1;
		if(y < 0)
			y *= -1;
		if(z < 0)
			z *= -1;
	}
	
	/**
	 * Sets all vertices to 0.
	 */
	public void zero(){
		x = y = z = 0;
	}
	
	/**
	 * Sets all three vertices. 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void set(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Equivalent of an assignment (=) operator.
	 * @param v1
	 */
	public void assign(Vector3d v1){
		x = v1.getX();
		y = v1.getY();
		z = v1.getZ();
	}
	
	public static Vector3d subtract(Vector3d v1, Vector3d v2){
		return new Vector3d(v1.getX()-v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
	}
	
	@Override
	public Vector3d clone(){
		return new Vector3d(x,y,z);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "Vector3d [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
	public String toXML(){
		DecimalFormat df = new DecimalFormat("0");
		df.setMaximumFractionDigits(340);
		
		return "<Vector3d><x>"+df.format(x)+"</x><y>"+df.format(y)+"</y><z>"+df.format(z)+"</z></Vector3d>";
	}
}
