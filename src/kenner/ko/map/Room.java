package kenner.ko.map;

import java.util.HashMap;
import java.util.Map;
import kenner.ko.game.Globals;

public class Room {

	private int 			zoneNumber;
	private short 			roomNumber;
	private byte 			status;
	private byte 			check;
	private byte 			roomType;
	private int 			initMinX;
	private int 			initMinZ;
	private int 			initMaxX;
	private int 			initMaxZ;
	private int 			endMinX;
	private int 			endMinZ;
	private int 			endMaxX;
	private int 			endMaxZ;
	private RoomEvent[] 	logic	= new RoomEvent[Globals.MAX_CHECK_EVENT];
	private RoomEvent[]		exec	= new RoomEvent[Globals.MAX_CHECK_EVENT];
	private float 			delayTime;
	private Map<Integer, Integer> npcIds = new HashMap<Integer, Integer>(); 
	private byte 			logicNumber;
	
	public Room(){
		zoneNumber = 0;
		roomNumber = 1;
		status = 1;
		initMinX = 0;
		initMinZ = 0;
		initMaxX = 0;
		initMaxZ = 0;
		endMinX = 0;
		endMinZ = 0;
		endMaxX = 0;
		endMaxZ = 0;
		check = 0;
		roomType = 0;
		delayTime = 0;
		logicNumber = 1;
		for(int i = 0; i < Globals.MAX_CHECK_EVENT; i++){
			logic[i] = new RoomEvent();
			exec[i] = new RoomEvent();
		}
	}
	
	public int getZoneNumber() {
		return zoneNumber;
	}
	public void setZoneNumber(int zoneNumber) {
		this.zoneNumber = zoneNumber;
	}
	public short getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(short roomNumber) {
		this.roomNumber = roomNumber;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public byte getCheck() {
		return check;
	}
	public void setCheck(byte check) {
		this.check = check;
	}
	public byte getRoomType() {
		return roomType;
	}
	public void setRoomType(byte roomType) {
		this.roomType = roomType;
	}
	public int getInitMinX() {
		return initMinX;
	}
	public void setInitMinX(int initMinX) {
		this.initMinX = initMinX;
	}
	public int getInitMinZ() {
		return initMinZ;
	}
	public void setInitMinZ(int initMinZ) {
		this.initMinZ = initMinZ;
	}
	public int getInitMaxX() {
		return initMaxX;
	}
	public void setInitMaxX(int initMaxX) {
		this.initMaxX = initMaxX;
	}
	public int getInitMaxZ() {
		return initMaxZ;
	}
	public void setInitMaxZ(int initMaxZ) {
		this.initMaxZ = initMaxZ;
	}
	public int getEndMinX() {
		return endMinX;
	}
	public void setEndMinX(int endMinX) {
		this.endMinX = endMinX;
	}
	public int getEndMinZ() {
		return endMinZ;
	}
	public void setEndMinZ(int endMinZ) {
		this.endMinZ = endMinZ;
	}
	public int getEndMaxX() {
		return endMaxX;
	}
	public void setEndMaxX(int endMaxX) {
		this.endMaxX = endMaxX;
	}
	public int getEndMaxZ() {
		return endMaxZ;
	}
	public void setEndMaxZ(int endMaxZ) {
		this.endMaxZ = endMaxZ;
	}
	public RoomEvent[] getLogic() {
		return logic;
	}
	public void setLogic(RoomEvent[] logic) {
		this.logic = logic;
	}
	public RoomEvent[] getExec() {
		return exec;
	}
	public void setExec(RoomEvent[] exec) {
		this.exec = exec;
	}
	public float getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(float delayTime) {
		this.delayTime = delayTime;
	}
	public Map<Integer, Integer> getNpcIds() {
		return npcIds;
	}
	public void setNpcIds(Map<Integer, Integer> npcIds) {
		this.npcIds = npcIds;
	}
	public byte getLogicNumber() {
		return logicNumber;
	}
	public void setLogicNumber(byte logicNumber) {
		this.logicNumber = logicNumber;
	}
}

/**internal struct for Room **/
class RoomEvent{
	private short 	number;
	private short 	option1;
	private short 	option2;
	
	public RoomEvent(){
		number = 0;
		option1 = 0;
		option2 = 0;
	}
	
	public short getNumber() {
		return number;
	}
	public void setNumber(short number) {
		this.number = number;
	}
	public short getOption1() {
		return option1;
	}
	public void setOption1(short option1) {
		this.option1 = option1;
	}
	public short getOption2() {
		return option2;
	}
	public void setOption2(short option2) {
		this.option2 = option2;
	}
}