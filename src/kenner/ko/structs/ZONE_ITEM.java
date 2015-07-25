package kenner.ko.structs;

public class ZONE_ITEM {
	private int 	bundle_index;
	private int[]	itemid		= new int[6]; //box slots
	private short[]	count		= new short[6];
	private float	x;
	private float 	y;
	private float 	z;
	private float 	time;
	
	public int getBundle_index() {
		return bundle_index;
	}
	public void setBundle_index(int bundle_index) {
		this.bundle_index = bundle_index;
	}
	public int[] getItemid() {
		return itemid;
	}
	public void setItemid(int[] itemid) {
		this.itemid = itemid;
	}
	public short[] getCount() {
		return count;
	}
	public void setCount(short[] count) {
		this.count = count;
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
	public float getTime() {
		return time;
	}
	public void setTime(float time) {
		this.time = time;
	}
}
