package kenner.ko.map;

public class MapInfo {
	private short 	event;
	private int x = -1;
	private int z = -1;
	
	public MapInfo(){
		event = 0;
	}

	public short getEvent() {
		return event;
	}

	public void setEvent(short event) {
		this.event = event;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
	public String toXML(){
		StringBuilder xml = new StringBuilder();
		xml.append("<MapInfo>");
		if(x != -1 && z != -1){
			xml.append("<x>"+x+"</x><z>"+z+"</z>");
		}
		xml.append("<Event>"+event+"</Event>");
		xml.append("</MapInfo>");
		return xml.toString();
	}
}
