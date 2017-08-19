package flibs.util;

/**
 * Representa un dato que puede ser absoluto(pixeles) o relativo(portentage)
 */
public class StyleData {
	public static final byte UNIT_PIXELS=0, UNIT_PERCENTAGE=1;
	
	public byte unit;
	public int value;
	
	public StyleData(byte unit, int value) {
		this.unit = unit;
		this.value = value;
	}
	
	
	public StyleData(int value) {
		this(UNIT_PIXELS, value);
	}
	
	public StyleData() {
		this(UNIT_PIXELS, 0);
	}
	
	@Override
	public String toString() {
		String unitString = (unit==UNIT_PIXELS)? "px" :"%";
		return value + unitString;
	}
	
}
