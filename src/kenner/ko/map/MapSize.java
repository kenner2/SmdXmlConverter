package kenner.ko.map;

public class MapSize {
	private int width;
	private int height;
	private long x;
	private long y;
	
	public MapSize(){
		width = 0;
		height = 0;
	}
	
	public MapSize(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	public void setSize(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public long getX() {
		return x;
	}

	public void setX(long x) {
		this.x = x;
	}

	public long getY() {
		return y;
	}

	public void setY(long y) {
		this.y = y;
	}
}
