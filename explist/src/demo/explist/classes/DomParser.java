package demo.explist.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.os.Environment;

public class DomParser {

	//XML TAG
	static final String CAT = "Category";
	static final String GROUP = "Group";
	static final String ID = "Id";
	static final String NAME ="Name";
	static final String IMAGE = "Image";	
	static final String CHILD = "Child";
		
	//Context
	Context mContext;
	
	public DomParser (Context ct){
		this.mContext = ct;
	}
	
	public List<MainGroup> parse(){
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		List<MainGroup> results = new ArrayList<MainGroup>();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(this.getInput());
			org.w3c.dom.Element root = dom.getDocumentElement();
			NodeList groups = root.getElementsByTagName(GROUP);
			for (int i=0;i<groups.getLength();i++){
				MainGroup current = new MainGroup();
				Node group = groups.item(i);
				NodeList properties = group.getChildNodes();
				for (int j=0;j<properties.getLength();j++){
					Node property = properties.item(j);
					String name = property.getNodeName();
					if (name.equalsIgnoreCase(ID)){
						current.setId(Integer.parseInt(property.getFirstChild().getNodeValue()));
					}
					else if (name.equalsIgnoreCase(NAME)){
						current.setName(mContext.getResources().getIdentifier("demo.explist:"+property.getFirstChild().getNodeValue(), null, null));
					}
					else if (name.equalsIgnoreCase(IMAGE)){
						current.setImgID(mContext.getResources().getIdentifier("demo.explist:"+property.getFirstChild().getNodeValue(), null, null));
					}
					else if (name.equalsIgnoreCase(CHILD)){
						NodeList childpros = property.getChildNodes();
						Child currentChild = new Child();
						for (int k=0;k<childpros.getLength();k++){
							Node childpro = childpros.item(k);
							String childname = childpro.getNodeName();
							
							if (childname.equalsIgnoreCase(ID)){
								currentChild.setId(Integer.parseInt(childpro.getFirstChild().getNodeValue()));
							}
							else if (childname.equalsIgnoreCase(NAME)){
								currentChild.setName(mContext.getResources().getIdentifier("demo.explist:"+childpro.getFirstChild().getNodeValue(), null, null));
							}
							else if (childname.equalsIgnoreCase(IMAGE)){
								currentChild.setImgID(mContext.getResources().getIdentifier("demo.explist:"+childpro.getFirstChild().getNodeValue(), null, null));
							}
						}
						current.addChild(currentChild);
					}
				}
				results.add(current);
			}
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}
		
		return results;
	}
	
	protected InputStream getInput(){
		FileInputStream fl = null;
		File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(sdcard, "categories.xml");
		try {
			fl = new FileInputStream(file);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fl;
		
	}
}
