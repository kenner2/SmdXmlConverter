package kenner.ko.n3base;

import java.nio.ByteBuffer;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CellSub {
	private int polyCount;
	private Vector3d[] vertIndices;
	private int x = -1;
	private int z = -1;
	
	public void load(ByteBuffer buff){
		polyCount = buff.getInt();
		if(polyCount > 0){
			vertIndices = new Vector3d[polyCount];
			for(int i = 0; i < vertIndices.length; i++){
				Vector3d v = new Vector3d(buff.getFloat(),buff.getFloat(),buff.getFloat());
				vertIndices[i] = v;
			}
		}
	}
	
	/**
	 * Loads from an xml element
	 * @param subCell
	 */
	public void load(Node subCell){
		NodeList nl = subCell.getChildNodes();
		for(int i = 1; i < nl.getLength(); i+=2){
			Node n = nl.item(i);
			switch(n.getNodeName()){
				case "x":
					this.x = Integer.parseInt(n.getFirstChild().getNodeValue());
					break;
				case "z":
					this.z = Integer.parseInt(n.getFirstChild().getNodeValue());
					break;
				case "PolygonCount":
					this.polyCount = Integer.parseInt(n.getFirstChild().getNodeValue());
					break;
				case "Polygons":
					NodeList vectors = n.getChildNodes();
					this.vertIndices = new Vector3d[vectors.getLength() / 2];
					int c = 0;
					for(int j = 1; j < vectors.getLength(); j=j+2){
						float x = 0, z = 0, y = 0;
						Node h = vectors.item(j);
						if(h.getChildNodes().getLength() > 0){
							for(int k = 1; k < h.getChildNodes().getLength(); k=k+2){
								switch(h.getChildNodes().item(k).getNodeName()){
									case "x":
										x = Float.parseFloat(h.getChildNodes().item(k).getFirstChild().getNodeValue());
										break;
									case "y":
										y = Float.parseFloat(h.getChildNodes().item(k).getFirstChild().getNodeValue());
										break;
									case "z":
										z = Float.parseFloat(h.getChildNodes().item(k).getFirstChild().getNodeValue());
										break;
								}
							}
							vertIndices[c++] = new Vector3d(x, y, z);
						}
					}
					break;
			}
		}
	}
	
	public String toXML(){
		StringBuilder xml = new StringBuilder();
		xml.append("<SubCell>");
		if(x != -1 && z != -1){
			xml.append("<x>"+x+"</x><z>"+z+"</z>");
		}
		xml.append("<PolygonCount>"+polyCount+"</PolygonCount>");
		if(polyCount > 0){
			xml.append("<Polygons>");
			for(Vector3d v : vertIndices){
				xml.append(v.toXML());
			}
			xml.append("</Polygons>");
		}
		xml.append("</SubCell>");
		return xml.toString();
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

	public int getPolyCount() {
		return polyCount;
	}

	public void setPolyCount(int polyCount) {
		this.polyCount = polyCount;
	}

	public Vector3d[] getVertIndices() {
		return vertIndices;
	}

	public void setVertIndices(Vector3d[] vertIndices) {
		this.vertIndices = vertIndices;
	}
}
