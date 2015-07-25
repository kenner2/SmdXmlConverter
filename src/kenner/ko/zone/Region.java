package kenner.ko.zone;

public class Region {
	private byte 	moving;
	
	public byte getMoving() {
		return moving;
	}
	public void setMoving(byte moving) {
		this.moving = moving;
	}
	
	/**
	 * Not really needed.
	 * @return
	 */
	public String toXML(){
		return "<Region><Moving>"+moving+"</Moving></Region>";
	}
}
