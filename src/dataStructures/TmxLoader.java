package dataStructures;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import states.PlayState;

public class TmxLoader {
	
	private Document doc;
	
	public TmxLoader (String fileName) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			if (PlayState.stage >= 0)
				doc = db.parse(this.getClass().getClassLoader().getResourceAsStream(fileName));
			else
			{
				File file = new File (fileName);
				doc = db.parse(file);
			}
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getTileWidth ()
	{
		return Integer.parseInt(doc.getElementsByTagName("tileset").item(0).getAttributes().getNamedItem("tilewidth").getTextContent());
	}
	
	public int getTileHeight ()
	{
		return Integer.parseInt(doc.getElementsByTagName("tileset").item(0).getAttributes().getNamedItem("tileheight").getTextContent());
	}
	
	public int getMapWidth ()
	{
		return Integer.parseInt(doc.getElementsByTagName("map").item(0).getAttributes().getNamedItem("width").getTextContent());
	}
	
	public int getMapHeight ()
	{
		return Integer.parseInt(doc.getElementsByTagName("map").item(0).getAttributes().getNamedItem("height").getTextContent());
	}
	
	public int getLayerWidth (String layer)
	{
		NodeList nodeLst = doc.getElementsByTagName("layer");
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Node node = nodeLst.item(i);
			if (node.getAttributes().getNamedItem("name").getTextContent().equals(layer))
			{
				return Integer.parseInt(node.getAttributes().getNamedItem("width").getTextContent());
			}
		}
		return -1;
	}
	
	public int getLayerHeight (String layer)
	{
		NodeList nodeLst = doc.getElementsByTagName("layer");
		for (int i = 0; i < nodeLst.getLength(); i++) 
		{
			Node node = nodeLst.item(i);
			if (node.getAttributes().getNamedItem("name").getTextContent().equals(layer))
			{
				return Integer.parseInt(node.getAttributes().getNamedItem("height").getTextContent());
			}
		}
		return -1;
	}
	
	public String getCollisionMap (String layer)
	{
		String output = new String ();
		NodeList nodeLst = doc.getElementsByTagName("layer");
		Element tileData;
		NodeList tiles;
		
		for (int i = 0; i < nodeLst.getLength(); i++)
		{
			Node node = nodeLst.item(i);
			if (node.getAttributes().getNamedItem("name").getTextContent().equals(layer))
			{
				int width = getLayerWidth (layer);
				int height = getLayerHeight (layer);
				
				if (width == -1 || height == -1) return "";

				tileData = (Element) ((Element)node).getElementsByTagName("data").item(0);
				tiles = tileData.getElementsByTagName("tile");
				
				for (int j = 0; j < height; j++)
				{
					for (int k = 0; k < width; k++)
					{
						output += tiles.item(j * width + k).getAttributes().getNamedItem("gid").getTextContent();
						if (k + 1 != width) output += ",";
					}
					if (j + 1 != height)
						output += "\n";
				}	
				return output;
			}
		}
		
		return null;
	}
	
	public Vector <GroupObject> getObject (String objectGroup, String objectType)
	{
		Vector <GroupObject> arReturn = new Vector <GroupObject> ();
		NodeList nodeLst = doc.getElementsByTagName("objectgroup");
		
		NodeList objects;
		GroupObject object;
		
		for (int i = 0; i < nodeLst.getLength(); i++)
		{
			Node node = nodeLst.item(i);
			if (node.getAttributes().getNamedItem("name").getTextContent().equals(objectGroup))
			{
				objects = ((Element) node).getElementsByTagName("object");
				for (int j = 0; j < objects.getLength(); j++)
				{
					object = new GroupObject ((Element) objects.item(j));
					if (object.type.equals(objectType)) arReturn.add(object);
				}
			}
		}
		return arReturn;
	}
	
	public Vector <GroupObject> getObject (String objectGroup)
	{
		Vector <GroupObject> arReturn = new Vector <GroupObject> ();
		NodeList nodeLst = doc.getElementsByTagName("objectgroup");
		
		NodeList objects;
		GroupObject object;
		
		for (int i = 0; i < nodeLst.getLength(); i++)
		{
			Node node = nodeLst.item(i);
			if (node.getAttributes().getNamedItem("name").getTextContent().equals(objectGroup))
			{
				objects = ((Element) node).getElementsByTagName("object");
				for (int j = 0; j < objects.getLength(); j++)
				{
					object = new GroupObject ((Element) objects.item(j));
					arReturn.add(object);
				}
			}
		}
		return arReturn;
	}
}