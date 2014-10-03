package dataStructures;

import java.util.Properties;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * An object from a object-group on the map
 * 
 * @author kulpae
 */
public class GroupObject {
  /** The index of this object */
  public int index;
  /** The name of this object - read from the XML */
  public String name;
  /** The type of this object - read from the XML */
  public String type;
  /** The x-coordinate of this object */
  public int x;
  /** The y-coordinate of this object */
  public int y;
  /** The width of this object */
  public int width;
  /** The height of this object */
  public int height;
  /** The image source */
//  private String image;
  
  /** the properties of this group */
  public Properties props;

 public GroupObject(Element element) {
		name = element.getAttribute("name");
		type = element.getAttribute("type");
		x = Integer.parseInt(element.getAttribute("x"));
		y = Integer.parseInt(element.getAttribute("y"));
		width = Integer.parseInt(element.getAttribute("width"));
		height = Integer.parseInt(element.getAttribute("height"));

		// now read the layer properties
		Element propsElement = (Element) element.getElementsByTagName(
				"properties").item(0);
		if (propsElement != null) {
			NodeList properties = propsElement
					.getElementsByTagName("property");
			if (properties != null) {
				props = new Properties();
				for (int p = 0; p < properties.getLength(); p++) {
					Element propElement = (Element) properties.item(p);

					String name = propElement.getAttribute("name");
					String value = propElement.getAttribute("value");
					props.setProperty(name, value);
				}
			}
		}
	}
}