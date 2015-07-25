package kenner.ko.structs;

public class ZONE_INFO {
	private byte 	serverNo;
	private short	zoneNo;
	private String 	name;
	private int 	initX;
	private int 	initZ;
	private int 	initY;
	private byte 	type;
	private byte 	roomEvent;
	private String 	bz;
	
	public byte getServerNo() {
		return serverNo;
	}
	public void setServerNo(byte serverNo) {
		this.serverNo = serverNo;
	}
	public short getZoneNo() {
		return zoneNo;
	}
	public void setZoneNo(short zoneNo) {
		this.zoneNo = zoneNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getInitX() {
		return initX;
	}
	public void setInitX(int initX) {
		this.initX = initX;
	}
	public int getInitZ() {
		return initZ;
	}
	public void setInitZ(int initZ) {
		this.initZ = initZ;
	}
	public int getInitY() {
		return initY;
	}
	public void setInitY(int initY) {
		this.initY = initY;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public byte getRoomEvent() {
		return roomEvent;
	}
	public void setRoomEvent(byte roomEvent) {
		this.roomEvent = roomEvent;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	@Override
	public String toString() {
		return "ZONE_INFO [serverNo=" + serverNo + ", zoneNo=" + zoneNo
				+ ", name=" + name + ", initX=" + initX + ", initZ=" + initZ
				+ ", initY=" + initY + ", type=" + type + ", roomEvent="
				+ roomEvent + ", bz=" + bz + "]";
	}
}
