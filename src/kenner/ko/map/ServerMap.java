package kenner.ko.map;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import kenner.ko.game.Globals;
import kenner.ko.n3base.CellMain;
import kenner.ko.n3base.CellSub;
import kenner.ko.n3base.N3ShapeMgr;
import kenner.ko.n3base.Vector3d;
import kenner.ko.structs.OBJECT_EVENT;
import kenner.ko.structs.REGENE_EVENT;
import kenner.ko.structs.WARP_INFO;
import kenner.ko.structs.ZONE_INFO;
import kenner.ko.util.Logger;
import kenner.ko.zone.Region;

public class ServerMap {

	/**
	 * 1299 Pointers
	 * Variable			Offset(Hex)
	 * ----------------------------
	 * main				40198
	 * shapeManager		2C
	 * size				40060
	 * unitDistance		40064
	 */
	private N3ShapeMgr	shapeManager;
	private MapInfo[][]	mapInfo;
	private Region[][]	regions;
	private MapSize 	size;
	private MapSize		regionSize;
	private short 		zoneNumber;
	private byte 		serverNumber;
	private String 		name;
	private int 		mapSize;
	private float		unitDistance;
	private float[][]	height;
	private byte 		roomType;
	private byte 		roomEvent;
	private byte 		roomStatus;
	private byte 		initRoomCount;
	private Map<Integer, OBJECT_EVENT> 	objectEvents = new HashMap<Integer, OBJECT_EVENT>();
	private Map<Integer, Room> 			roomEvents = new HashMap<Integer, Room>();
	private Map<Integer, REGENE_EVENT>  regeneEvents = new HashMap<Integer, REGENE_EVENT>();
	private Map<Integer, WARP_INFO> 	warps = new HashMap<Integer, WARP_INFO>();
	private short 		karusRoom;
	private short 		elmoradRoom;
	private ZONE_INFO 	zoneInfo;
	
	private DecimalFormat df = new DecimalFormat("0");
	
	/**
	 * Default constructor.  Initializes most variables.
	 * @param main
	 */
	public ServerMap(){
		mapSize = 0;
		unitDistance = 0;
		height = null;
		
		regionSize = new MapSize();
		regionSize.setX(0);
		regionSize.setY(0);
		regionSize.setWidth(0);
		regionSize.setHeight(0);
		
		size = new MapSize();
		size.setX(0);
		size.setY(0);
		size.setWidth(0);
		size.setHeight(0);
		
		regions = null;
		zoneNumber = 0;
		roomType = 0;
		roomEvent = 0;
		roomStatus = 1;
		initRoomCount = 0;
		name = "";
		karusRoom = 0;
		elmoradRoom = 0;
		
		shapeManager = new N3ShapeMgr();
		df.setMaximumFractionDigits(340);
	}
	
	/** 
	 * checks to see if a given location is navigable (collision check)
	 * @param dest_x
	 * @param dest_y
	 * @return
	 */
	public boolean isMovable(int dest_x, int dest_y){
		//invalid location
		if(dest_x < 0 | dest_y < 0)
			return false;
		
		if(mapInfo == null)
			return false;
		
		boolean ret = false;
		
		//event = collision data?
		if(mapInfo[dest_x][dest_y].getEvent() == 0){
			ret = true;
		} else {
			ret = false;
		}
		
		return ret;
	}
	
	/**
	 * Loads map data from an .smd file.
	 * @param file
	 * @return
	 */
	public boolean loadMap(File file){
		byte[] bytes;
		try{
			bytes = Files.readAllBytes(file.toPath());
		} catch(IOException e){
			Logger.error("Failed to read map file: " + file.getName());
			e.printStackTrace();
			return false;
		}
		Logger.info("Reading map file: " + file.getName());
		
		this.name = file.getName().replace(".smd", "");
		
		ByteBuffer buff = ByteBuffer.wrap(bytes);
		buff = buff.order(ByteOrder.LITTLE_ENDIAN);
		
		Logger.debug("Map file size (bytes): " + bytes.length);
		
		loadTerrain(buff);
		
		shapeManager.create((mapSize-1)*unitDistance, (mapSize-1)*unitDistance);
		
		shapeManager.loadCollisionData(buff);
		
		if((mapSize-1)*unitDistance != shapeManager.getWidth()){
			Logger.error("Map boundary error", "ServerMap", "loadMap");
			return false;
		}
		
		int mapWidth = (int) shapeManager.getWidth();
		int x = (int)(mapWidth/Globals.VIEW_DISTANCE) + 1;
		regionSize.setX(x);
		regionSize.setY(x);
		regionSize.setWidth(x);
		regionSize.setHeight(x);
		
		size.setX(mapSize);
		size.setY(mapSize);
		size.setWidth(mapSize);
		size.setHeight(mapSize);
		
		regions = new Region[regionSize.getWidth()][regionSize.getHeight()];
		for(int i = 0; i < regionSize.getWidth(); i++){
			for(int j = 0; j < regionSize.getHeight(); j++){
				regions[i][j] = new Region();
				regions[i][j].setMoving((byte)0);
			}
		}
		
		//do this check after every function - not all of these are nessessarily filled.
		if(buff.position() != bytes.length)
			loadObjectEvent(buff);
		if(buff.position() != bytes.length)
			loadMapTile(buff);
		if(buff.position() != bytes.length)
			loadRegeneEvent(buff);
		if(buff.position() != bytes.length)
			loadWarpList(buff);
			
			
		
		Logger.info("Map file " + file.getName() + " successfully loaded. (" + buff.position() + "/" + bytes.length + " bytes)");
			
		return true;
	}
	
	/**
	 * Loads terrain data 
	 * @param bytes
	 * @return
	 */
	private void loadTerrain(ByteBuffer buff){
		Logger.debug("Reading terrain data.");
		//first 4 bytes of file - size
		mapSize = buff.getInt();
				//Utilities.toUnsignedByte(buff.get());
		//buff.get();buff.get();buff.get();
		//next 4 bytes - unit distance
		unitDistance = buff.getFloat();
		
		height = new float[mapSize][mapSize];
		for(int i = 0; i < mapSize; i++){
			for(int j = 0; j < mapSize; j++){
				//read height (float) - 4 bytes
				height[i][j] = buff.getFloat();
			}
		}
		Logger.debug("Terrain data end: " + buff.position());
	}
	
	/**
	 * Loads terrain data from XML NodeList
	 * @param bytes
	 * @return
	 */
	private void loadTerrain(NodeList heights){
		for(int j = 1; j < heights.getLength(); j+=2){
			int x = 0, z = 0;
			float y = 0;
			Node h = heights.item(j);
			
			if(h.getChildNodes().getLength() > 0){
				for(int k = 1; k < h.getChildNodes().getLength(); k+=2){
					switch(h.getChildNodes().item(k).getNodeName()){
						case "x":
							x = Integer.parseInt(h.getChildNodes().item(k).getFirstChild().getNodeValue());
							break;
						case "y":
							y = Float.parseFloat(h.getChildNodes().item(k).getFirstChild().getNodeValue());
							break;
						case "z":
							z = Integer.parseInt(h.getChildNodes().item(k).getFirstChild().getNodeValue());
							break;
					}
				}
				this.height[x][z] = y;
			}
		}
	}
	
	/**
	 * Wooo math
	 * @param x
	 * @param z
	 * @return
	 */
	public float getHeight(float x, float z){
		int ix, iz;
		float y, h1, h2, h3, dx, dz;
		
		ix = (int)(x/unitDistance);
		iz = (int)(z/unitDistance);
		dx = (x - ix*unitDistance) / unitDistance;
		dz = (z - iz*unitDistance) / unitDistance;
		
		if(!(dx >= 0 && dz >= 0 && dx < 1 && dz < 1)){
			return Globals.FLT_MIN;
		}
		
		if((ix+iz)%2 == 1){
			if((dx+dz) < 1){
				h1 = height[ix][iz+1];
				h2 = height[ix+1][iz];
				h3 = height[ix][iz];
				
				float h12 = h1+(h2-h1)*dx;
				float h32 = h3+(h2-h3)*dx;
				y = h32 + (h12-h32)*(dz/(1-dx));
			} else {
				h1 = height[ix][iz+1];
				h2 = height[ix+1][iz];
				h3 = height[ix+1][iz+1];
				
				if(dx == 0)
					return h1;
				
				float h12 = h1+(h2-h1)*dx;
				float h13 = h1+(h3-h1)*dx;
				y = h13 + (h12-h13)*((1-dz)/dx);
			}
		} else {
			if(dz > dx){
				h1 = height[ix][iz+1];
				h3 = height[ix][iz];
				h2 = height[ix+1][iz+1];
				
				float h12 = h1+(h2-h1)*dx;
				float h32 = h3+(h2-h3)*dx;
				y = h12 + (h32-h12)*((1-dz)/(1-dx));
			} else {
				h1 = height[ix][iz];
				h2 = height[ix+1][iz];
				h3 = height[ix+1][iz+1];
				
				if(dx == 0)
					return h1;
				
				float h12 = h1+(h2-h1)*dx;
				float h13 = h1+(h3-h1)*dx;
				y = h12 + (h13-h12)*(dz/dx);
			}
		}
		
		return y;
	}
	
	/**
	 * Checks for the intersection of two objects.
	 * @param x1
	 * @param z1
	 * @param y1
	 * @param x2
	 * @param z2
	 * @param y2
	 * @return
	 */
	public boolean objectIntersect(float x1, float z1, float y1, float x2, float z2, float y2){
		Vector3d vec1 = new Vector3d(x1, y1, z1);
		Vector3d vec2 = new Vector3d(x2, y2, z2);
		Vector3d dir = Vector3d.subtract(vec2, vec1);
		float speed = dir.magnitude();
		dir.normalize();
		
		return shapeManager.checkCollision(vec1, dir, speed);
	}
	
	/**
	 * Loads MapInfo event data.
	 * @param bytes
	 * @param bytesRead
	 * @return
	 */
	private void loadMapTile(ByteBuffer buff){
		Logger.debug("Loading map tile data.");
		int w = size.getWidth();
		
		//instantiate MapInfo 2d array
		mapInfo = new MapInfo[w][w];
		
		// read event info
		for(int i = 0; i < w; i++){
			for(int j = 0; j < w; j++){
				mapInfo[j][i] = new MapInfo();
				mapInfo[j][i].setEvent(buff.getShort());
			}
		}
		
		Logger.debug("Map Tile data end: " + buff.position());
	}
	
	/**
	 * Loads MapInfo event data from a NodeList
	 * @param bytes
	 * @param bytesRead
	 * @return
	 */
	private void loadMapTile(NodeList mapTileNodeList){
		int w = size.getWidth();
		//instantiate MapInfo 2d array
		mapInfo = new MapInfo[w][w];
		
		// Ensure there are no null objects
		for(int i = 0; i < w; i++){
			for(int j = 0; j < w; j++){
				mapInfo[j][i] = new MapInfo();
			}
		}
		
		//start to read from XML
		for(int i = 1; i < mapTileNodeList.getLength(); i+=2){
			NodeList nl = mapTileNodeList.item(i).getChildNodes();
			MapInfo m = new MapInfo();
			for(int j = 1; j < nl.getLength(); j+=2){
				Node n = nl.item(j);
				switch(n.getNodeName()){
					case "x":
						m.setX(Integer.parseInt(n.getFirstChild().getNodeValue()));
						break;
					case "z":
						m.setZ(Integer.parseInt(n.getFirstChild().getNodeValue()));
						break;
					case "Event":
						m.setEvent(Short.parseShort(n.getFirstChild().getNodeValue()));
						break;
				}
			}
			mapInfo[m.getX()][m.getZ()] = m;
		}
	}
	
	/**
	 * loads object events
	 * @param bytes
	 * @param bytesRead
	 * @return
	 */
	private void loadObjectEvent(ByteBuffer buff){
		Logger.debug("Loading object events.");
		int eventObjectCount = buff.getInt();
		Logger.debug("Event object count: " + eventObjectCount);
		
		for(int i = 0; i < eventObjectCount; i++){
			//read object events
			OBJECT_EVENT e = new OBJECT_EVENT();
			e.setBelong(buff.getInt());
			e.setIndex(buff.getShort());
			e.setType(buff.getShort());
			e.setControlNpcId(buff.getShort());
			e.setStatus(buff.getShort());
			e.setPosX(buff.getFloat());
			e.setPosY(buff.getFloat());
			e.setPosZ(buff.getFloat());
			
			if(e.getType() > 0 && e.getType() < 4){
				//old dev test comment
				//main.addObjectEventNpc(e, zoneNumber);
			}
			
			if(e.getIndex() <= 0)
				continue;
			
			objectEvents.put((int)e.getIndex(), e);
		}
		Logger.debug("Event object data end: " + buff.position());
	}
	
	/**
	 * loads object events from XML element
	 * @param bytes
	 * @param bytesRead
	 * @return
	 */
	private void loadObjectEvent(NodeList objectEventNodeList){
		for(int i = 1; i < objectEventNodeList.getLength(); i+=2){
			//read object events
			Node event = objectEventNodeList.item(i);
			OBJECT_EVENT e = new OBJECT_EVENT();
			
			for(int j = 1; j < event.getChildNodes().getLength(); j+=2){
				Node n = event.getChildNodes().item(j);
				switch(n.getNodeName()){
					case "Index":
						e.setIndex(Short.parseShort(n.getFirstChild().getNodeValue()));
						break;
					case "Belong":
						e.setBelong(Integer.parseInt(n.getFirstChild().getNodeValue()));
						break;
					case "Type":
						e.setType(Short.parseShort(n.getFirstChild().getNodeValue()));
						break;
					case "ControlNpcId":
						e.setControlNpcId(Short.parseShort(n.getFirstChild().getNodeValue()));
						break;
					case "Status":
						e.setStatus(Short.parseShort(n.getFirstChild().getNodeValue()));
						break;
					case "PosX":
						e.setPosX(Float.parseFloat(n.getFirstChild().getNodeValue()));
						break;
					case "PosY":
						e.setPosY(Float.parseFloat(n.getFirstChild().getNodeValue()));
						break;
					case "PosZ":
						e.setPosZ(Float.parseFloat(n.getFirstChild().getNodeValue()));
						break;
				}
			}
			
			if(e.getIndex() <= 0)
				continue;
			
			objectEvents.put((int)e.getIndex(), e);
		}
	}
	
	/**
	 * Loads regene (Region?) event data.
	 * @param buff
	 */
	private void loadRegeneEvent(ByteBuffer buff){
		Logger.debug("Loading regene event data.");
		
		int eventObjectCount = buff.getInt();
		REGENE_EVENT event = null;
		
		Logger.debug("Regene event count: " + eventObjectCount);
		
		//populate and add regene event
		for(int i = 0; i < eventObjectCount; i++){
			event = new REGENE_EVENT();
			
			event.setRegenePoint(i);
			event.setRegenePosX(buff.getFloat());
			event.setRegenePosY(buff.getFloat());
			event.setRegenePosZ(buff.getFloat());
			event.setRegionAreaZ(buff.getFloat());
			event.setRegionAreaX(buff.getFloat());
			
			regeneEvents.put(i, event);
		}
		
		Logger.debug("Regene event data end: " + buff.position());
	}
	
	/**
	 * Loads regene (Region?) event data from a NodeList
	 * @param buff
	 */
	private void loadRegeneEvent(NodeList regeneEventNodeList){
		for(int i = 1; i < regeneEventNodeList.getLength(); i+=2){
			Node n = regeneEventNodeList.item(i);
			REGENE_EVENT e = new REGENE_EVENT();
			for(int j = 1; j < n.getChildNodes().getLength(); j+=2){
				Node re = n.getChildNodes().item(j);
				switch(re.getNodeName()){
					case "Point":
						e.setRegenePoint(Integer.parseInt(re.getFirstChild().getNodeValue()));
						break;
					case "PosX":
						e.setRegenePosX(Float.parseFloat(re.getFirstChild().getNodeValue()));
						break;
					case "PosY":
						e.setRegenePosY(Float.parseFloat(re.getFirstChild().getNodeValue()));
						break;
					case "PosZ":
						e.setRegenePosZ(Float.parseFloat(re.getFirstChild().getNodeValue()));
						break;
					case "AreaZ":
						e.setRegionAreaZ(Float.parseFloat(re.getFirstChild().getNodeValue()));
						break;
					case "AreaX":
						e.setRegionAreaX(Float.parseFloat(re.getFirstChild().getNodeValue()));
						break;
				}
			}
			regeneEvents.put(e.getRegenePoint(), e);
		}
	}
	
	/**
	 * Loads warp list data
	 * @param buff
	 */
	private void loadWarpList(ByteBuffer buff){
		Logger.debug("Loading warp list data.");
		
		int warpCount = buff.getInt();
		
		Logger.debug("Warp list count: " + warpCount);
		WARP_INFO warp = null;
		StringBuilder sb = null;
		for(int i = 0; i < warpCount; i++){
			warp = new WARP_INFO();
			warp.setWarpId(buff.getShort());
			//read warp name
			sb = new StringBuilder("");
			for(int j = 0; j < 32; j++){
				sb.append((char)buff.get());
			}
			warp.setWarpName(sb.toString().trim());
			//read warp description
			sb = new StringBuilder("");
			for(int j = 0; j < 256; j++){
				sb.append((char)buff.get());
			}
			warp.setAnnounce(sb.toString().trim());
			
			//read others
			warp.setPay(buff.getInt());
			
			//pad 2 bytes i think
			buff.position(buff.position()+2);
			
			warp.setZone(buff.getShort());
			
			//pad 2 i think
			buff.position(buff.position()+2);
			
			warp.setX(buff.getFloat());
			warp.setY(buff.getFloat());
			warp.setZ(buff.getFloat());
			warp.setR(buff.getFloat());
			warp.setNation(buff.getShort());
			
			//pad 2 i think
			buff.position(buff.position()+2);
			
			Logger.debug(warp.toString());
			
			warps.put((int)warp.getWarpId(), warp);
		}
		
		Logger.debug("Warp list data end: " + buff.position());
	}
	
	/**
	 * Loads warp list data from a NodeList
	 * @param buff
	 */
	private void loadWarpList(NodeList warpNodeList){
		for(int i = 1; i < warpNodeList.getLength(); i+=2){
			NodeList warp = warpNodeList.item(i).getChildNodes();
			WARP_INFO w = new WARP_INFO();
			for(int j = 1; j < warp.getLength(); j+=2){
				Node n = warp.item(j);
				switch(n.getNodeName()){
					case "id":
						w.setWarpId(Short.parseShort(n.getFirstChild().getNodeValue()));
						break;
					case "Name":
						String wName = n.getFirstChild().getNodeValue();
						if(wName.length() > 32){
							Logger.info("Warp name \""+wName+"\" is too long.  Trimming to 32 characters.");
							wName = wName.substring(0,32);
						}
						w.setWarpName(wName);
						break;
					case "Announce":
						String wAnnounce = n.getFirstChild().getNodeValue();
						if(wAnnounce.length() > 256){
							Logger.info("Warp name \""+wAnnounce+"\" is too long.  Trimming to 256 characters.");
							wAnnounce = wAnnounce.substring(0,256);
						}
						w.setWarpName(wAnnounce);
						break;
					case "Cost":
						w.setPay(Integer.parseInt(n.getFirstChild().getNodeValue()));
						break;
					case "Zone":
						w.setZone(Short.parseShort(n.getFirstChild().getNodeValue()));
						break;
					case "x":
						w.setX(Float.parseFloat(n.getFirstChild().getNodeValue()));
						break;
					case "y":
						w.setY(Float.parseFloat(n.getFirstChild().getNodeValue()));
						break;
					case "z":
						w.setZ(Float.parseFloat(n.getFirstChild().getNodeValue()));
						break;
					case "Radius":
						w.setR(Float.parseFloat(n.getFirstChild().getNodeValue()));
						break;
					case "Nation":
						w.setNation(Short.parseShort(n.getFirstChild().getNodeValue()));
						break;
				}
			}
			warps.put((int)w.getWarpId(), w);
		}
	}
	
	/*
	 * GETTERS / SETTERS
	 */

	public N3ShapeMgr getShapeManager() {
		return shapeManager;
	}

	public void setShapeManager(N3ShapeMgr shapeManager) {
		this.shapeManager = shapeManager;
	}

	public MapInfo[][] getMapInfo() {
		return mapInfo;
	}

	public void setMapInfo(MapInfo[][] mapInfo) {
		this.mapInfo = mapInfo;
	}

	public Region[][] getRegions() {
		return regions;
	}

	public void setRegions(Region[][] regions) {
		this.regions = regions;
	}

	public MapSize getSize() {
		return size;
	}

	public void setSize(MapSize size) {
		this.size = size;
	}

	public MapSize getRegionSize() {
		return regionSize;
	}

	public void setRegionSize(MapSize regionSize) {
		this.regionSize = regionSize;
	}

	public short getZoneNumber() {
		return zoneNumber;
	}

	public void setZoneNumber(short zoneNumber) {
		this.zoneNumber = zoneNumber;
	}

	public byte getServerNumber() {
		return serverNumber;
	}

	public void setServerNumber(byte serverNumber) {
		this.serverNumber = serverNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMapSize() {
		return mapSize;
	}

	public void setMapSize(int mapSize) {
		this.mapSize = mapSize;
	}

	public float getUnitDistance() {
		return unitDistance;
	}

	public void setUnitDistance(float unitDistance) {
		this.unitDistance = unitDistance;
	}

	public float[][] getHeight() {
		return height;
	}

	public void setHeight(float[][] height) {
		this.height = height;
	}

	public byte getRoomType() {
		return roomType;
	}

	public void setRoomType(byte roomType) {
		this.roomType = roomType;
	}

	public byte getRoomEvent() {
		return roomEvent;
	}

	public void setRoomEvent(byte roomEvent) {
		this.roomEvent = roomEvent;
	}

	public byte getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(byte roomStatus) {
		this.roomStatus = roomStatus;
	}

	public byte getInitRoomCount() {
		return initRoomCount;
	}

	public void setInitRoomCount(byte initRoomCount) {
		this.initRoomCount = initRoomCount;
	}

	public Map<Integer, OBJECT_EVENT> getObjectEvents() {
		return objectEvents;
	}

	public void setObjectEvents(Map<Integer, OBJECT_EVENT> objectEvents) {
		this.objectEvents = objectEvents;
	}

	public Map<Integer, Room> getRoomEvents() {
		return roomEvents;
	}

	public void setRoomEvents(Map<Integer, Room> roomEvents) {
		this.roomEvents = roomEvents;
	}

	public Map<Integer, REGENE_EVENT> getRegeneEvents() {
		return regeneEvents;
	}

	public void setRegeneEvents(Map<Integer, REGENE_EVENT> regeneEvents) {
		this.regeneEvents = regeneEvents;
	}

	public Map<Integer, WARP_INFO> getWarps() {
		return warps;
	}

	public void setWarps(Map<Integer, WARP_INFO> warps) {
		this.warps = warps;
	}

	public short getKarusRoom() {
		return karusRoom;
	}

	public void setKarusRoom(short karusRoom) {
		this.karusRoom = karusRoom;
	}

	public short getElmoradRoom() {
		return elmoradRoom;
	}

	public void setElmoradRoom(short elmoradRoom) {
		this.elmoradRoom = elmoradRoom;
	}

	public ZONE_INFO getZoneInfo() {
		return zoneInfo;
	}

	public void setZoneInfo(ZONE_INFO zoneInfo) {
		this.zoneInfo = zoneInfo;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	/**
	 * Returns this object as an XML String
	 * @return
	 */
	public String toXML(){
		StringBuilder xml = new StringBuilder();
		xml.append("<ServerMap>");
		xml.append("<FileName>" + this.name + "</FileName>");
		xml.append("<MapSize>" + this.mapSize + "</MapSize>");
		xml.append("<UnitDistance>" + df.format(this.unitDistance) + "</UnitDistance>");
		xml.append("<Terrain>");
		for(int i = 0; i < mapSize; i++){
			for(int j = 0; j < mapSize; j++){
				xml.append("<Height>");
				xml.append("<x>"+i+"</x>");
				xml.append("<z>"+j+"</z>");
				xml.append("<y>"+df.format(height[i][j])+"</y>");
				xml.append("</Height>");
			}
		}
		xml.append("</Terrain>");
		xml.append("<Width>"+shapeManager.getWidth()+"</Width>");
		xml.append("<Length>"+shapeManager.getLength()+"</Length>");
		//xml.append("CollisionFaceCount>"+shapeManager.getCollisionFaceCount()+"</CollisionFaceCount>");
		if(shapeManager.getCollisionFaceCount() > 0){
			xml.append("<Collisions>");
			for(Vector3d v : shapeManager.getCollisions()){
				xml.append(v.toXML());
			}
			xml.append("</Collisions>");
		}
		
		xml.append("<Cells>");
		for(int z = 0; z < shapeManager.getCells().length; z++){
			for(int x = 0; x < shapeManager.getCells().length; x++){
				CellMain cell = shapeManager.getCells()[x][z];
				if(cell != null){
					cell.setX(x);
					cell.setZ(z);
					xml.append(cell.toXML());
				}
			}
		}
		xml.append("</Cells>");
		
		if(!objectEvents.isEmpty()){
			xml.append("<ObjectEvents>");
			for(OBJECT_EVENT o : objectEvents.values()){
				xml.append(o.toXML());
			}
			xml.append("</ObjectEvents>");
		}
		
		if(mapInfo != null && mapInfo.length > 0){
			int w = size.getWidth();
			xml.append("<MapTiles>");
			for(int i = 0; i < w; i++){
				for(int j = 0; j < w; j++){
					MapInfo m = mapInfo[j][i];
					m.setX(j);
					m.setZ(i);
					xml.append(m.toXML());
				}
			}
			xml.append("</MapTiles>");
		}
		
		if(!regeneEvents.isEmpty()){
			xml.append("<RegeneEvents>");
			for(REGENE_EVENT e : regeneEvents.values()){
				xml.append(e.toXML());
			}
			xml.append("</RegeneEvents>");
		}
		
		if(!warps.isEmpty()){
			xml.append("<WarpList>");
			for(WARP_INFO w : warps.values()){
				xml.append(w.toXML());
			}
			xml.append("</WarpList>");
		}
		
		xml.append("</ServerMap>");
		
		return xml.toString();
	}
	
	/**
	 * Loads map data from an .xml file.
	 * @param file
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 */
	public boolean loadFromXML(File file) throws ParserConfigurationException, SAXException, IOException{
		Logger.info("Reading xml file: " + file.getName());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		doc.getDocumentElement().normalize();
		
		Element root = doc.getDocumentElement();

		//name
		NodeList nl = root.getChildNodes();
		for(int i = 1; i < nl.getLength(); i+=2){
			switch(nl.item(i).getNodeName()){
				case "FileName":
					this.name = nl.item(i).getFirstChild().getNodeValue();
					break;
				case "MapSize":
					this.mapSize = Integer.parseInt(nl.item(i).getFirstChild().getNodeValue());
					break;
				case "UnitDistance":
					this.unitDistance = Float.parseFloat(nl.item(i).getFirstChild().getNodeValue());
					break;
				case "Terrain":
					this.height = new float[mapSize][mapSize];
					NodeList heights = nl.item(i).getChildNodes();
					loadTerrain(heights);
					break;
				case "Width":
					this.shapeManager.setWidth(Float.parseFloat(nl.item(i).getFirstChild().getNodeValue()));
					break;
				case "Length":
					this.shapeManager.setLength(Float.parseFloat(nl.item(i).getFirstChild().getNodeValue()));
					break;
				case "Collisions":
					NodeList vectors = nl.item(i).getChildNodes();
					this.shapeManager.loadCollisionData(vectors);
					//additional stuff
					if((mapSize-1)*unitDistance != shapeManager.getWidth()){
						Logger.error("Map boundary error", "ServerMap", "loadMap");
						return false;
					}
					
					int mapWidth = (int) shapeManager.getWidth();
					int x = (int)(mapWidth/Globals.VIEW_DISTANCE) + 1;
					regionSize.setX(x);
					regionSize.setY(x);
					regionSize.setWidth(x);
					regionSize.setHeight(x);
					
					size.setX(mapSize);
					size.setY(mapSize);
					size.setWidth(mapSize);
					size.setHeight(mapSize);
					break;
				case "Cells":
					NodeList cells = nl.item(i).getChildNodes();
					this.shapeManager.loadCellData(cells);
					break;
				case "ObjectEvents":
					NodeList objectEventList = nl.item(i).getChildNodes();
					loadObjectEvent(objectEventList);
					break;
				case "MapTiles":
					NodeList mapInfoList = nl.item(i).getChildNodes();
					loadMapTile(mapInfoList);
					break;
				case "RegeneEvents":
					NodeList regeneEventList = nl.item(i).getChildNodes();
					loadRegeneEvent(regeneEventList);
					break;
				case "WarpList":
					NodeList warpNodeList = nl.item(i).getChildNodes();
					loadWarpList(warpNodeList);
					break;
			}
		}
		
		
		
		
		regions = new Region[regionSize.getWidth()][regionSize.getHeight()];
		for(int i = 0; i < regionSize.getWidth(); i++){
			for(int j = 0; j < regionSize.getHeight(); j++){
				regions[i][j] = new Region();
				regions[i][j].setMoving((byte)0);
			}
		}
		
		Logger.info("Map file " + file.getName() + " successfully loaded.");
		
		return true;
	}
	
	/**
	 * Returns a byte stream in smd format.
	 * @param maxBufferSize  - from converter.properties
	 * @return
	 */
	public byte[] toSmd(int maxBufferSize){
		int bytesWritten = 0;
						
		byte[] bytes = new byte[maxBufferSize];
		ByteBuffer buff = ByteBuffer.wrap(bytes);
		buff = buff.order(ByteOrder.LITTLE_ENDIAN);
		
		//first 4 bytes of file - size
		buff.putInt(mapSize);
		bytesWritten +=4;
		//next 4 bytes - unit distance
		buff.putFloat(unitDistance);
		bytesWritten +=4;
		
		//read terrain heights (4*mapSize*mapSize bytes)
		for(int i = 0; i < mapSize; i++){
			for(int j = 0; j < mapSize; j++){
				//read height (float) - 4 bytes
				buff.putFloat(height[i][j]);
				bytesWritten +=4;
			}
		}
		Logger.debug("Terrain Data // written bytes: " + bytesWritten);
		//Map width (4)
		buff.putFloat(shapeManager.getWidth());
		bytesWritten +=4;
		//Map Length (4)
		buff.putFloat(shapeManager.getLength());
		bytesWritten +=4;
		//collision face count (4)
		buff.putInt(shapeManager.getCollisions().size() / 3);
		Logger.debug("collisions: " + shapeManager.getCollisions().size() / 3);
		bytesWritten +=4;
		
		if(shapeManager.getCollisionFaceCount() > 0){
			for(int i = 0; i < shapeManager.getCollisions().size(); i++){
				Vector3d v = shapeManager.getCollisions().get(i);
				//x y z of a collision (4*3 bytes)
				buff.putFloat(v.getX());
				bytesWritten +=4;
				buff.putFloat(v.getY());
				bytesWritten +=4;
				buff.putFloat(v.getZ());
				bytesWritten +=4;
			}
		}
		
		Logger.debug("Collision Data // written bytes: " + bytesWritten);
		
		//load cell data
		int z = 0;
		for(float fz = 0; fz < shapeManager.getLength(); fz += N3ShapeMgr.CELL_MAIN_SIZE, z++){
			int x = 0;
			for(float fx = 0; fx < shapeManager.getWidth(); fx += N3ShapeMgr.CELL_MAIN_SIZE, x++){
				CellMain m = shapeManager.getCells()[x][z];
				
				//set exists (4)
				buff.putInt(m.getExist());
				bytesWritten +=4;
				//if cell doesn't exist, skip over and continue.
				if(m.getExist() == 0){
					continue;
				}
				
				//shape count (4)
				buff.putInt(m.getShapeCount());
				bytesWritten +=4;
				//shape indices (2*shapeCount)
				if(m.getShapeCount() > 0){
					for(int i = 0; i < m.getShapeCount(); i++){
						buff.putShort(m.getShapeIndices()[i]);
						bytesWritten +=2;
					}
				}
				
				for(int i = 0; i < N3ShapeMgr.CELL_MAIN_DIVIDE; i++){
					for(int j = 0; j < N3ShapeMgr.CELL_MAIN_DIVIDE; j++){
						CellSub sc = m.getSubCells()[j][i];
						
						//polygon count (4)
						buff.putInt(sc.getPolyCount());
						bytesWritten +=4;
						
						if(sc.getPolyCount() > 0){
							for(int k = 0; k < sc.getVertIndices().length; k++){
								Vector3d v = sc.getVertIndices()[k];
								//print x, y, z (4*3 bytes)
								buff.putFloat(v.getX());
								bytesWritten +=4;
								buff.putFloat(v.getY());
								bytesWritten +=4;
								buff.putFloat(v.getZ());
								bytesWritten +=4;
							}
						}
					}
				}
			}
		}
		
		Logger.debug("Cell Data // written bytes: " + bytesWritten);
		
		//object event count (4)
		buff.putInt(objectEvents.size());
		bytesWritten +=4;
		
		for(OBJECT_EVENT e : objectEvents.values()){
			//belong (4)
			buff.putInt(e.getBelong());
			bytesWritten +=4;
			//index (2)
			buff.putShort(e.getIndex());
			bytesWritten +=2;
			//type (2)
			buff.putShort(e.getType());
			bytesWritten +=2;
			//controlNpcId (2)
			buff.putShort(e.getControlNpcId());
			bytesWritten +=2;
			//status (2)
			buff.putShort(e.getStatus());
			bytesWritten +=2;
			//PosX/Y/Z (4*3)
			buff.putFloat(e.getPosX());
			bytesWritten +=4;
			buff.putFloat(e.getPosY());
			bytesWritten +=4;
			buff.putFloat(e.getPosZ());
			bytesWritten +=4;
		}

		Logger.debug("Object Event Data // written bytes: " + bytesWritten);
		
		//map tile data
		for(int i = 0; i < size.getWidth(); i++){
			for(int j = 0; j < size.getWidth(); j++){
				MapInfo m = mapInfo[j][i];
				//event (2)
				buff.putShort(m.getEvent());
				bytesWritten +=2;
			}
		}
		
		Logger.debug("Map Tile Data // written bytes: " + bytesWritten);
		
		//regene event object count (4)
		buff.putInt(regeneEvents.size());
		bytesWritten +=4;
		
		for(REGENE_EVENT e : regeneEvents.values()){
			//4*5 bytes
			buff.putFloat(e.getRegenePosX());
			bytesWritten +=4;
			buff.putFloat(e.getRegenePosY());
			bytesWritten +=4;
			buff.putFloat(e.getRegenePosZ());
			bytesWritten +=4;
			buff.putFloat(e.getRegionAreaZ());
			bytesWritten +=4;
			buff.putFloat(e.getRegionAreaX());
			bytesWritten +=4;
		}
		
		Logger.debug("Regene Event Data // written bytes: " + bytesWritten);
		
		//warp count
		buff.putInt(warps.size());
		bytesWritten +=4;
		
		for(WARP_INFO w : warps.values()){
			//warp id (2)
			buff.putShort(w.getWarpId());
			bytesWritten +=2;
			
			//seen it happen a few files
			if(w.getWarpName() == null) w.setWarpName("");
			//name (32)
			if(w.getWarpName().length() > 32){
				buff.put(w.getWarpName().substring(0,32).getBytes());
			} else {
				buff.put(w.getWarpName().getBytes());
				
				if(w.getWarpName().length() < 32){
					int diff = 32 - w.getWarpName().length();
					for(int i = 0; i < diff; i++){
						buff.put((byte)0x0);
					}
				}
			}
			bytesWritten +=32;
			
			//seen it happen a few files
			if(w.getAnnounce() == null) w.setAnnounce("");
			//announce (256)
			if(w.getAnnounce().length() > 256){
				buff.put(w.getAnnounce().substring(0,256).getBytes());
			} else {
				buff.put(w.getAnnounce().getBytes());
				
				if(w.getAnnounce().length() < 256){
					int diff = 256 - w.getAnnounce().length();
					for(int i = 0; i < diff; i++){
						buff.put((byte)0x0);
					}
				}
			}
			bytesWritten +=256;
			
			//Pay (4)
			buff.putInt(w.getPay());
			bytesWritten +=4;
			//pad 2 bytes
			buff.put((byte)0x0);
			buff.put((byte)0x0);
			bytesWritten +=2;
			//zone (2)
			buff.putShort(w.getZone());
			bytesWritten +=2;
			//pad 2
			buff.put((byte)0x0);
			buff.put((byte)0x0);
			bytesWritten +=2;
			//x y z r (4*3)
			buff.putFloat(w.getX());
			bytesWritten +=4;
			buff.putFloat(w.getY());
			bytesWritten +=4;
			buff.putFloat(w.getZ());
			bytesWritten +=4;
			buff.putFloat(w.getR());
			bytesWritten +=4;
			//nation (2)
			buff.putShort(w.getNation());
			bytesWritten +=2;
			//pad another 2
			buff.put((byte)0x0);
			buff.put((byte)0x0);
			bytesWritten +=2;
		}
		
		Logger.debug("Warp Data // written bytes: " + bytesWritten);
		
		//ByteBuffer temp = ByteBuffer.wrap(buff.array(), 0, buff.position());
		//temp.order(buff.order());
		byte[] temp = new byte[bytesWritten];
		for(int i = 0; i < bytesWritten; i++){
			temp[i] = buff.array()[i];
		}
		return temp;
	}
}
