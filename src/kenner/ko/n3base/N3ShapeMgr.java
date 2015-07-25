package kenner.ko.n3base;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import kenner.ko.util.Logger;

public class N3ShapeMgr {
	public static final int CELL_MAIN_DIVIDE = 4;
	public static final int CELL_SUB_SIZE = 4;
	public static final int CELL_MAIN_SIZE = CELL_MAIN_DIVIDE * CELL_SUB_SIZE;
	public static final int MAX_CELL_MAIN = 4096 / CELL_MAIN_SIZE;
	public static final int MAX_CELL_SUB = MAX_CELL_MAIN * CELL_MAIN_DIVIDE;
	
	private float width;
	private float length;
	private int collisionFaceCount;
	private CellMain[][] cells = new CellMain[MAX_CELL_MAIN][MAX_CELL_MAIN];
	private ArrayList<Vector3d> collisions = new ArrayList<Vector3d>();
	
	/**
	 * Loads collision and cell data ( which is just more collision data from the looks of it )
	 * @param buff
	 * @return
	 */
	public void loadCollisionData(ByteBuffer buff) {
		width = buff.getFloat();
		length = buff.getFloat();
		create(width, length);
		
		
		//load collision data
		collisionFaceCount = buff.getInt();
		
		Logger.debug("Collision face count: " + collisionFaceCount);
		
		Logger.debug("Reading collision faces.");
		
		if(collisionFaceCount > 0){
			for(int i = 0; i < collisionFaceCount*3; i++){
				Vector3d v = new Vector3d(buff.getFloat(), buff.getFloat(), buff.getFloat());
				collisions.add(v);
			}
		}
		
		Logger.debug("Collision face data end: " + buff.position());
		
		Logger.debug("Loading cell data.");
		
		//load cell data
		int exist = 0;
		int z = 0;
		for(float fz = 0; fz < length; fz += CELL_MAIN_SIZE, z++){
			int x = 0;
			for(float fx = 0; fx < width; fx += CELL_MAIN_SIZE, x++){
				cells[x][z] = new CellMain();
				exist = buff.getInt();
				//if cell doesn't exist, skip over and continue.
				if(exist == 0){
					continue;
				}
				
				cells[x][z].load(buff);
				cells[x][z].setExist(exist);
			}
		}
		
		Logger.debug("Cell data end: " + buff.position());
		
	}

	public boolean create(float width, float length) {
		if(width <= 0 || width > MAX_CELL_MAIN * CELL_MAIN_SIZE || 
			length <= 0 || length > MAX_CELL_MAIN * CELL_MAIN_SIZE){
			return false;
		}
		
		this.width = width;
		this.length = length;
		
		return true;
	}
	
	/**
	 * Loads collision data from an XML NodeList
	 * @param vectors
	 * @return
	 * @throws NumberFormatException 
	 */
	public void loadCollisionData(NodeList vectors) throws NumberFormatException {
		create(width, length);
		
		collisionFaceCount = vectors.getLength() / 2; //due to every other child pretty much being a blank string for some reason.  
		for(int i = 1; i < vectors.getLength(); i=i+2){
			float x = 0, z = 0, y = 0;
			Node n = vectors.item(i);
			
			if(n.getChildNodes().getLength() > 0){
				for(int j = 1; j < n.getChildNodes().getLength(); j=j+2){
					switch(n.getChildNodes().item(j).getNodeName()){
						case "x":
							x = Float.parseFloat(n.getChildNodes().item(j).getFirstChild().getNodeValue());
							break;
						case "y":
							y = Float.parseFloat(n.getChildNodes().item(j).getFirstChild().getNodeValue());
							break;
						case "z":
							z = Float.parseFloat(n.getChildNodes().item(j).getFirstChild().getNodeValue());
							break;
					}
				}
				collisions.add(new Vector3d(x, y, z));
			}
		}
	}
	
	/**
	 * Loads cell data from an xml NodeList
	 * @param cellList
	 */
	public void loadCellData(NodeList cellList){
		//fill the cells array with objects to avoid the chance of a null
		int z = 0;
		for(float fz = 0; fz < length; fz += CELL_MAIN_SIZE, z++){
			int x = 0;
			for(float fx = 0; fx < width; fx += CELL_MAIN_SIZE, x++){
				cells[x][z] = new CellMain();
			}
		}
		
		//load cell data
		for(int j = 1; j < cellList.getLength(); j=j+2){
			Node cell = cellList.item(j);
			CellMain c = new CellMain();
			c.load(cell);
			cells[c.getX()][c.getZ()] = c;
		}
	}

	public float getWidth() {
		return width;
	}

	/**
	 * yeah, yeah.  mgame logic.
	 * @return
	 */
	public float getHeight() {
		return width;
	}

	public boolean checkCollision(Vector3d vec1, Vector3d dir, float speed) {
		return false;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public int getCollisionFaceCount() {
		return collisionFaceCount;
	}

	public void setCollisionFaceCount(int collisionFaceCount) {
		this.collisionFaceCount = collisionFaceCount;
	}

	public CellMain[][] getCells() {
		return cells;
	}

	public void setCells(CellMain[][] cells) {
		this.cells = cells;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public ArrayList<Vector3d> getCollisions() {
		return collisions;
	}

	public void setCollisions(ArrayList<Vector3d> collisions) {
		this.collisions = collisions;
	}
}
