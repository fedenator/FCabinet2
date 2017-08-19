package flibs.fson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import flibs.pointer.Pointer;
import flibs.pointer.ReferencedList;
import flibs.util.StringUtilities;
import flibs.util.StyleData;

/**
 * Un elemento que puede tener valores y sub elementos
 * Los valores soportados son boolean, double, int y String
 * Un valor tiene una key(string) y un objeto
 * @author fpalacios
 *
 */
public class FSON {
	public enum Type {
		STRING, INT, DOUBLE, BOOLEAN, STYLE_DATA, UNKNOW;
	}
	
	/*---------------------- Constantes -----------------------------*/
	public static final Character tab='\t', br='\n', spliter=';', entrySpliter='=';
	
	public static final String[] ARRAY_CONTAINER=new String[]{"[","]"};
	
	public static final String EXCEPTION_ARRAY_TYPE_MESSAGE = "Array contains unsupported object types";
	public static final String EXCEPTION_OBJECT_TYPE_MESSAGE = "Object is an unsupported type";

	/*----------------------  Propiedades ---------------------------*/
	private Pointer<FSON> parent = new Pointer<FSON>();
	private String tag;
	private ReferencedList<FSON> subElements = new ReferencedList<FSON>();
	private HashMap<String, Object> values = new HashMap<String, Object>();
	
	/*---------------------- Constructores --------------------------*/
	/**
	 * Constructor completo, si el padre no es null, le dice que te agregue de subelemento
	 */
	public FSON(FSON parent, String tag) {
		this.tag = tag;
		if (parent != null) setParent(parent); //El padre despues te dice que lo agregues de padre
	}
	
	public FSON(String tag) {
		this.tag = tag;
	}
	
	public FSON() {
		this("");
	}
	
	/*-------------------------- Funciones --------------------------*/
	public void loadFromString(String str) {
		//Saco los espacios, tabuladores y enters
		ArrayList<Character> param = removeRedundantCharacters(str);
		
		loadFromStringRecursive(this, param);
	}
	private static FSON loadFromStringRecursive(FSON fson, ArrayList<Character> list) {
		
		int bracketCounter = 0; //Nivel en el que estamos
		boolean inSemiclone = false;//Si el caracter esta entre comillas
		ArrayList<ArrayList<Character>> items = new ArrayList<ArrayList<Character>>();
		
		items.add(new ArrayList<Character>());
		
		//Separa los items de mi nivel con una coma
		for (int i = 0, i2 = 0; i < list.size(); i++) {
			Character character = list.get(i);
			if      (character == '{')  bracketCounter++;
			else if (character == '}')  bracketCounter--;
			else if (character == '\"') inSemiclone = (inSemiclone) ? false : true;
			
			//Si es un item nuevo cambio de item seleccionado
			else if (character == spliter) {
				if (!inSemiclone && bracketCounter == 0) {
					items.add(new ArrayList<Character>());
					i2++;
				}
			}
			
			//Añado el caracter al item seleccionado
			if (bracketCounter > 0 || character != spliter) {
				items.get(i2).add(character);
			}
		}
		
		items.remove(items.size()-1); //Hack de el ultimo ; que hace un item vacio
		//Asigna los valores de los nodos
		for (ArrayList<Character> item : items) {		
			
			//Si es un subElmento
			if(item.contains('{')) { //Habria que revisar que las { no esten entree comillas
				String key = "";
				bracketCounter = 0;
				ArrayList<Character> arg = new ArrayList<Character>();
				boolean bracketFound = false;
				
				for (Iterator<Character> iterator = item.iterator(); iterator.hasNext();) {
					Character character = iterator.next();
					
					if (character == '{') {
						if (bracketCounter == 0) {
							bracketFound = true;
							iterator.remove();
							bracketCounter++;
							continue;
						}
						bracketCounter++;
					} else if (character == '}') {
						if(bracketCounter == 0) {
							iterator.remove();
							bracketCounter--;
							continue;
						}
						bracketCounter--;
					}
					
					if (!bracketFound) key += character;
					else if (bracketCounter > 0) arg.add(character);
				}
				
				
				fson.addSubElement(FSON.loadFromStringRecursive(key, arg));
				
			} else { //Si es un valor
				
				String line="", key="", value="";
				for (Character character : item) line += "" + character;
				key = line.split("" + entrySpliter)[0];
				value = line.split("" + entrySpliter)[1];
				
				fson.saveObject(key, value);
			}
		}
		
		return fson;
	}
	private static FSON loadFromStringRecursive(String myKey, ArrayList<Character> list) {
		return loadFromStringRecursive(new FSON(myKey), list);
	}
	
	@Override
	public String toString() {
		return toStringRecursive(0);
	}
	private String toStringRecursive(int level) {
		String flag = "";
		String indent = "";
		//Calculo el indent
		for(int i = 0; i < level; i++) indent += tab;
		
		
		Iterator<FSON> it = subElements.iterator();
		while (it.hasNext()) {
			FSON fson = (FSON)it.next();
			String aux = indent + fson.getTag() + " {" + br;
			aux += fson.toStringRecursive(level + 1) + br;
			aux += indent + "}" + spliter + br;
			flag += aux;
		}
		
		Iterator<Entry<String, Object>> it2 = values.entrySet().iterator();
		while (it2.hasNext()) {
			Entry<String, Object> entry = it2.next();
			Object key = entry.getKey(), value = entry.getValue();
			String line = indent + key + entrySpliter;
			
			
			//Si es un array
			if (value.getClass().isArray()) {
				Object[] array = (Object[]) value;
				
				line += "[";
				for (Object item : array) line += objToStringFormat(item) + ",";
				line = StringUtilities.removeLastCharacter(line); //Quita la ultima coma que esta de mas
				line += "]";
				
			} else { //Si es un valor solo
				line += objToStringFormat(value);
			}
			
			line += "" + spliter + br;
			flag += line;
			
		}
		
		return flag;
	}
	
	private static Type getType(String value) {
		Type type = null;
		
		if (value.startsWith("\"")) type = Type.STRING;//logicamente no puede haber comillas dentro de un texto
		else if (value.endsWith("%") || value.endsWith("px")) type = Type.STYLE_DATA;
		else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) type = Type.BOOLEAN;
		else if (value.contains(".")) type = Type.DOUBLE;
		else if (!value.isEmpty()) type = Type.INT;
		else type = Type.UNKNOW;
		
		return type;
	}
	private static Type getType(Object value) {
		Type flag = null;
		
		if (value instanceof String) flag = Type.STRING;
		else if (value instanceof Integer) flag = Type.INT;
		else if (value instanceof Double) flag = Type.DOUBLE;
		else if (value instanceof Boolean) flag = Type.BOOLEAN;
		else if (value instanceof StyleData) flag = Type.STYLE_DATA;
		else flag = Type.UNKNOW;
		
		return flag;
	}
	
	/**
	 * Transforma el objecto de String a el formato valido y lo guarda
	 */
	private void saveObject (String key, String obj) {
		if (obj.contains("[")) {
			obj = StringUtilities.removeStrings(obj, ARRAY_CONTAINER);
			String[] arrayObjectsString = obj.split(",");
			Object[] arrayObjects = new Object[arrayObjectsString.length];
			for (int i = 0; i < arrayObjectsString.length; i++)	arrayObjects[i] = getObjectFromString(arrayObjectsString[i]);
			values.put(key, arrayObjects);
		} else {
			values.put(key, getObjectFromString(obj));
		}
	}
	
	private String objToStringFormat(Object obj) {
		String flag = "";
		if (obj instanceof String) {
			flag += "\"" + (String) obj + "\"";
		} else if (obj instanceof Integer || obj instanceof Double || obj instanceof Boolean || obj instanceof StyleData) {
			flag += obj.toString();
		}
		return flag;
	}
	
	private Object getObjectFromString(String obj) {
		Object flag = null;
		
		switch (getType(obj)) {
			case STRING:
				flag = (String)obj.replaceAll("\"", "");
				break;
			case BOOLEAN:
				flag = Boolean.parseBoolean(obj);
				break;
			case DOUBLE:
				flag = Double.parseDouble(obj);
				break;
			case INT:
				flag = Integer.parseInt(obj);
				break;		
			case STYLE_DATA:
				StyleData styleData = (obj.toLowerCase().contains("px"))?
						new StyleData(StyleData.UNIT_PIXELS ,Integer.parseInt(obj.toLowerCase().replace("px", ""))): 
							new StyleData(StyleData.UNIT_PERCENTAGE ,Integer.parseInt(obj.toLowerCase().replace("%", "")));
				flag = styleData;
				break;
			case UNKNOW:
				//estaria bueno guardar un objecto tipo unknow para ayudar el debug de los que usen FSON
				break;
	}
		
		return flag;
	}
	
	private ArrayList<Character> removeRedundantCharacters(String str) {
		ArrayList<Character> flag = new ArrayList<Character>();
		
		boolean inSemiclone = false;
		char[] aux = str.toCharArray();
		for (char character : aux) {
			if (character == '\"')inSemiclone = (inSemiclone) ? false : true;
			
			if ( !((character == ' ' || character == '\n' || character == '\t') && !inSemiclone) )
			flag.add(character);
		}
		
		return flag;
	}
	
	public void clear() {
		this.subElements.clear();
		this.values.clear();
	}
	
	/*--------------------- Manejo de subElementos ------------------*/
	/**
	 * Add un fson y le dice que el es su padre(A lo starwars) 
	 */
	public void addSubElement(FSON fson){
		subElements.add(fson);
		if(fson.parent.foo != this) {
			fson.setParent(this);
		}
	}
	public void removeSubElement(int index) {
		subElements.remove(index);
	}
	
	public boolean hasDirectSubElement(String tag) {
		boolean flag = false;
		
		for (FSON subElement : subElements) {
			if (subElement.tag.equals(tag)) {
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	public boolean hasSubElement(String key) {
		boolean flag = false;
		
		if (!hasDirectSubElement(key)) {
			for (FSON subElement : subElements) {
				if (subElement.hasSubElement(key)) {
					flag = true;
					break;
				}
			}
		} else {
			flag = true;
		}
		
		return flag;
	}
	
	//Clones
	public FSON[] getDirectSubElements() {
		return subElements.toArray(new FSON[subElements.size()]);
	}
	public FSON[] getDirectSubElementsByTag(String tag) {
		ArrayList<FSON> subs = new ArrayList<FSON>();
		for (FSON subElement : subElements) if (subElement.tag.equals(tag)) subs.add(subElement);
		return subs.toArray(new FSON[subs.size()]);
	}
	
	//Refs
	public Pointer<FSON> getSubElemetRef(int index) {
		return subElements.getRef(index);
	}
	
	public ReferencedList<FSON> getDirectSubElementsRef() {
		return subElements;
	}
	public ReferencedList<FSON> getSubElementsRef() {
		ReferencedList<FSON> flag = new ReferencedList<FSON>();
		
		flag.addAll(getDirectSubElementsRef());
		for(FSON item : flag) flag.addAll(item.getDirectSubElementsRef());
		
		return flag;
	}
	
	public ReferencedList<FSON> getDirectSubElementsRefByTag(String tag) {
		ReferencedList<FSON> flag = new ReferencedList<FSON>();
		
		for (FSON subElement : subElements) if (subElement.tag.equals(tag)) flag.add(subElement);
		
		return flag;
	}
	public ReferencedList<FSON> getSubElementsRefByTag(String tag) {
		ReferencedList<FSON> flag = new ReferencedList<FSON>();
		
		flag.addAll(getDirectSubElementsRefByTag(tag));
		
		
		
		return flag;
	}

	/*------------------------- Manejo de valores -------------------*/
	/**
	 * No pongan llaves en el string que se rompe =P
	 * Al que no le guste que haga un commit escapando el string
	 */
	public void addValue(String key, Object value) {
		if (getType(value) == Type.UNKNOW) throw new RuntimeException(EXCEPTION_OBJECT_TYPE_MESSAGE);
		values.put(key, value);
	}
	public void addArray(String key, Object[] value) {
		for (Object obj : value) if (getType(obj) == Type.UNKNOW) throw new RuntimeException(EXCEPTION_ARRAY_TYPE_MESSAGE);
		values.put(key, value);
	}
	
	public Object getValue(String key) {
		return values.get(key);
	}
	public String getStringValue(String key) {
		return "" + values.get(key);
	}
	public int getIntValue(String key) {
		return (int) values.get(key);
	}
	public double getDoubleValue(String key) {
		double flag = 0;
		Object ob = values.get(key);
		
		if (ob instanceof Integer) {
			int aux = (int) ob;
			flag = (double) aux;
		} else {
			flag = (double) ob;
		}
		
		return flag;
	}
	public boolean getBooleanValue(String key) {
		return (boolean) values.get(key);
	}
	public StyleData getStyleDataValue(String key) {
		return (StyleData) values.get(key);
	}
	
	public Object[] getArray(String key) {
		return (Object[]) values.get(key);
	}
	public String[] getStringArray(String key) {
		return (String[]) values.get(key);
	}
	public int[] getIntArray(String key) {
		return (int[]) values.get(key);
	}
	public double[] getDoubleArray(String key) {
		return (double[]) values.get(key);
	}
	public boolean[] getBooleanArray(String key) {
		return (boolean[]) values.get(key);
	}
	public StyleData[] getStyleDataArray(String key) {
		return (StyleData[]) values.get(key);
	}
	
	public Object[] getAllValues() {
		return values.entrySet().toArray();
	}
	public Object[] getValues() {
		return getAllValues();
	}
	
	public int size() {
		return values.size();
	}
	public void removeValue(String key) {
		values.remove(key);
	}
	
	/*------------------------ Manejo de keys --------------------*/
	public List<String> getDirectKeys() {
		ArrayList<String> flag = new ArrayList<String>();
		flag.addAll(values.keySet());
		return flag;
	}
	public List<String> getKeys() {
		ArrayList<String> flag = new ArrayList<String>();
		
		flag.addAll(getDirectKeys());
		for (FSON subElement : subElements) flag.addAll(subElement.getKeys());
		
		return flag;
	}
	
	public boolean hasDirectKey(String key) {
		boolean flag = false;
		
		for (String item : getDirectKeys()) {
			if (item.equals(key)) {
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	public boolean hasKey(String key) {
		boolean flag = false;
		
		if (hasDirectKey(key)) {
			flag = true;
		} else {
			for (FSON subElement : subElements) {
				if (subElement.hasKey(key)) {
					flag = true;
					break;
				}
			}
		}
		
		return flag;
	}
	
	/*---------------------- Getters y Setters -------------------*/

	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getTag() {
		return tag;
	}
	
	public void setParent(FSON parent) {
		this.parent.foo = parent;
		if(!parent.subElements.contains(this)) parent.addSubElement(this);
	}
	public Pointer<FSON> getParent() {
		return parent;
	}
	
}