package kenner.ko.structs;

import java.text.DecimalFormat;

public class REGENE_EVENT {
	private int 	regenePoint;
	private float 	regenePosX;
	private float 	regenePosY;
	private float 	regenePosZ;
	private float 	regionAreaZ;
	private float 	regionAreaX;
	
	public int getRegenePoint() {
		return regenePoint;
	}
	public void setRegenePoint(int regenePoint) {
		this.regenePoint = regenePoint;
	}
	public float getRegenePosX() {
		return regenePosX;
	}
	public void setRegenePosX(float regenePosX) {
		this.regenePosX = regenePosX;
	}
	public float getRegenePosY() {
		return regenePosY;
	}
	public void setRegenePosY(float regenePosY) {
		this.regenePosY = regenePosY;
	}
	public float getRegenePosZ() {
		return regenePosZ;
	}
	public void setRegenePosZ(float regenePosZ) {
		this.regenePosZ = regenePosZ;
	}
	public float getRegionAreaZ() {
		return regionAreaZ;
	}
	public void setRegionAreaZ(float regionAreaZ) {
		this.regionAreaZ = regionAreaZ;
	}
	public float getRegionAreaX() {
		return regionAreaX;
	}
	public void setRegionAreaX(float regionAreaX) {
		this.regionAreaX = regionAreaX;
	}
	
	public String toXML(){
		StringBuilder xml = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0");
		df.setMaximumFractionDigits(340);
		xml.append("<RegeneEvent>");
		xml.append("<Point>"+regenePoint+"</Point>");
		xml.append("<PosX>"+df.format(regenePosX)+"</PosX>");
		xml.append("<PosY>"+df.format(regenePosY)+"</PosY>");
		xml.append("<PosZ>"+df.format(regenePosZ)+"</PosZ>");
		xml.append("<AreaZ>"+df.format(regionAreaZ)+"</AreaZ>");
		xml.append("<AreaX>"+df.format(regionAreaX)+"</AreaX>");
		xml.append("</RegeneEvent>");
		return xml.toString();
	}
}
