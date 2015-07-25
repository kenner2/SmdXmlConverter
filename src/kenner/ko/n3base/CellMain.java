package kenner.ko.n3base;

import java.nio.ByteBuffer;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CellMain {
	private int exist = 0;
	private int shapeCount;
	private short[] shapeIndices;
	public CellSub[][] subCells = new CellSub[N3ShapeMgr.CELL_MAIN_DIVIDE][N3ShapeMgr.CELL_MAIN_DIVIDE];
	private int x = -1;
	private int z = -1;
	
	public void load(ByteBuffer buff){
		shapeCount = buff.getInt();
		if(shapeCount > 0){
			shapeIndices = new short[shapeCount];
			for(int i = 0; i < shapeCount; i++){
				shapeIndices[i] = buff.getShort();
			}
		}
		for(int i = 0; i < N3ShapeMgr.CELL_MAIN_DIVIDE; i++){
			for(int j = 0; j < N3ShapeMgr.CELL_MAIN_DIVIDE; j++){
				if(subCells[j][i] == null){
					subCells[j][i] = new CellSub();
				}
				subCells[j][i].load(buff);
			}
		}
	}
	
	/**
	 * Loads from XML element.
	 * @param cell
	 */
	public void load(Node cell){
		for(int i = 1; i < cell.getChildNodes().getLength(); i=i+2){
			Node n = cell.getChildNodes().item(i);
			switch(n.getNodeName()){
				case "Exist":
					this.exist = Integer.parseInt(n.getFirstChild().getNodeValue());
					break;
				case "x":
					this.x = Integer.parseInt(n.getFirstChild().getNodeValue());
					break;
				case "z":
					this.z = Integer.parseInt(n.getFirstChild().getNodeValue());
					break;
				case "ShapeCount":
					this.shapeCount = Integer.parseInt(n.getFirstChild().getNodeValue());
					break;
				case "ShapeIndices":
					NodeList indices = n.getChildNodes();
					shapeIndices = new short[indices.getLength()/2];
					int k = 0;
					for(int j = 1; j < indices.getLength(); j=j+2){
						Node index = indices.item(j);
						switch(index.getNodeName()){
							case "Index":
								shapeIndices[k++] = Short.parseShort(index.getFirstChild().getNodeValue());
								break;
						}
					}
					break;
				case "SubCells":
					//make sure there are no null subcells
					for(int l = 0; l < N3ShapeMgr.CELL_MAIN_DIVIDE; l++){
						for(int j = 0; j < N3ShapeMgr.CELL_MAIN_DIVIDE; j++){
							if(subCells[j][l] == null){
								subCells[j][l] = new CellSub();
							}
						}
					}
					//load subcells
					NodeList subCellList = n.getChildNodes();
					for(int j = 1; j < subCellList.getLength(); j=j+2){
						CellSub subCell = new CellSub();
						subCell.load(subCellList.item(j));
						subCells[subCell.getX()][subCell.getZ()] = subCell;
					}
			}
		}
	}
	
	public String toXML(){
		StringBuilder xml = new StringBuilder();
		xml.append("<Cell>");
		xml.append("<Exist>"+exist+"</Exist>");
		
		if(x != -1 && z != -1){
			xml.append("<x>"+x+"</x><z>"+z+"</z>");
		}
		
		if(exist != 0){
			xml.append("<ShapeCount>"+shapeCount+"</ShapeCount>");
			if(shapeCount > 0){
				xml.append("<ShapeIndices>");
				for(short s : shapeIndices){
					xml.append("<Index>"+s+"</Index>");
				}
				xml.append("</ShapeIndices>");
			}
			xml.append("<SubCells>");
			for(int i = 0; i < N3ShapeMgr.CELL_MAIN_DIVIDE; i++){
				for(int j = 0; j < N3ShapeMgr.CELL_MAIN_DIVIDE; j++){
					CellSub subcell = subCells[j][i];
					subcell.setX(j);
					subcell.setZ(i);
					xml.append(subcell.toXML());
				}
			}
			xml.append("</SubCells>");
		}
		xml.append("</Cell>");
		return xml.toString();
	}
	
	/*
	 * GETTERS / SETTERS
	 */

	public int getExist() {
		return exist;
	}

	public void setExist(int exist) {
		this.exist = exist;
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

	public int getShapeCount() {
		return shapeCount;
	}

	public void setShapeCount(int shapeCount) {
		this.shapeCount = shapeCount;
	}

	public short[] getShapeIndices() {
		return shapeIndices;
	}

	public void setShapeIndices(short[] shapeIndices) {
		this.shapeIndices = shapeIndices;
	}

	public CellSub[][] getSubCells() {
		return subCells;
	}

	public void setSubCells(CellSub[][] subCells) {
		this.subCells = subCells;
	}
}
