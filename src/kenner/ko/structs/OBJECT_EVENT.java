package kenner.ko.structs;

import java.text.DecimalFormat;

public class OBJECT_EVENT {
	private byte 	life;
	private int 	belong;
	private short 	index;
	private short 	type;
	private short 	controlNpcId;
	private short 	status;
	private float 	posX;
	private float 	posY;
	private float 	posZ;
	
	public byte getLife() {
		return life;
	}
	public void setLife(byte life) {
		this.life = life;
	}
	public int getBelong() {
		return belong;
	}
	public void setBelong(int belong) {
		this.belong = belong;
	}
	public short getIndex() {
		return index;
	}
	public void setIndex(short index) {
		this.index = index;
	}
	public short getType() {
		return type;
	}
	public void setType(short type) {
		this.type = type;
	}
	public short getControlNpcId() {
		return controlNpcId;
	}
	public void setControlNpcId(short controlNpcId) {
		this.controlNpcId = controlNpcId;
	}
	public short getStatus() {
		return status;
	}
	public void setStatus(short status) {
		this.status = status;
	}
	public float getPosX() {
		return posX;
	}
	public void setPosX(float posX) {
		this.posX = posX;
	}
	public float getPosY() {
		return posY;
	}
	public void setPosY(float posY) {
		this.posY = posY;
	}
	public float getPosZ() {
		return posZ;
	}
	public void setPosZ(float posZ) {
		this.posZ = posZ;
	}
	
	public String toXML(){
		StringBuilder xml = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0");
		df.setMaximumFractionDigits(340);
		xml.append("<ObjectEvent>");
		xml.append("<Belong>"+belong+"</Belong>");
		xml.append("<Index>"+index+"</Index>");
		xml.append("<Type>"+type+"</Type>");
		xml.append("<ControlNpcId>"+controlNpcId+"</ControlNpcId>");
		xml.append("<Status>"+status+"</Status>");
		xml.append("<PosX>"+df.format(posX)+"</PosX>");
		xml.append("<PosY>"+df.format(posY)+"</PosY>");
		xml.append("<PosZ>"+df.format(posZ)+"</PosZ>");
		xml.append("</ObjectEvent>");
		return xml.toString();
	}
}
