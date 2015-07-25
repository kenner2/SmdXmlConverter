package kenner.ko.structs;

import java.text.DecimalFormat;

public class WARP_INFO {
	private short 	warpId;
	private String 	warpName;
	private String 	announce;
	private int		pay;
	private short 	zone;
	private float 	x;
	private float 	y;
	private float	z;
	private float 	r;
	private short 	nation;
	
	public WARP_INFO(){
		warpId = 0;
		zone = 0;
		x = z = y = r = 0f;
	}
	
	public short getWarpId() {
		return warpId;
	}
	public void setWarpId(short warpId) {
		this.warpId = warpId;
	}
	public String getWarpName() {
		return warpName;
	}
	public void setWarpName(String warpName) {
		this.warpName = warpName.replace("\0", "");  //remove nulls.
	}
	public String getAnnounce() {
		return announce;
	}
	public void setAnnounce(String announce) {
		this.announce = announce.replace("\0", "");  //remove nulls.
	}
	public int getPay() {
		return pay;
	}
	public void setPay(int pay) {
		this.pay = pay;
	}
	public short getZone() {
		return zone;
	}
	public void setZone(short zone) {
		this.zone = zone;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public float getR() {
		return r;
	}
	public void setR(float r) {
		this.r = r;
	}
	public short getNation() {
		return nation;
	}
	public void setNation(short nation) {
		this.nation = nation;
	}

	@Override
	public String toString() {
		return "WARP_INFO [warpId=" + warpId + ", warpName=" + warpName
				+ ", announce=" + announce + ", pay=" + pay + ", zone=" + zone
				+ ", x=" + x + ", y=" + y + ", z=" + z + ", r=" + r
				+ ", nation=" + nation + "]";
	}
	
	public String toXML(){
		StringBuilder xml = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0");
		df.setMaximumFractionDigits(340);
		xml.append("<Warp>");
		xml.append("<id>"+warpId+"</id>");
		xml.append("<Name>"+(warpName!=null ?warpName:"Undefined")+"</Name>");
		xml.append("<Announce>"+(announce!=null ?announce:"Undefined")+"</Announce>");
		xml.append("<Cost>"+pay+"</Cost>");
		xml.append("<Zone>"+zone+"</Zone>");
		xml.append("<x>"+df.format(x)+"</x>");
		xml.append("<y>"+df.format(y)+"</y>");
		xml.append("<z>"+df.format(z)+"</z>");
		xml.append("<Radius>"+df.format(r)+"</Radius>");
		xml.append("<Nation>"+nation+"</Nation>");
		xml.append("</Warp>");
		return xml.toString();
	}
}
