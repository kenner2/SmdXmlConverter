package kenner.ko.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import kenner.ko.util.Logger;
import kenner.ko.util.MapFileNameFilter;
import kenner.ko.util.XmlFileNameFilter;

/**
 * This program has two main actions:
 * 1)  Convert SMD in the smd input directory to XML in the xml output directory.
 * 2)  Convert XML in the xml input directory to SMD in the smd output directory.
 * 
 * @author kenner
 *
 */
public class SmdXmlConverter {
	
	private static Properties config;
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		//load the configuration file
		config = new Properties();
		config.load(new FileInputStream("converter.properties"));
		
		//set logger level
		String outputLevel = config.getProperty("output.level");
		if(outputLevel.equalsIgnoreCase("debug")){
			Logger.isDebug = true;
		} else {
			Logger.isDebug = false;
		}
		
		//grab the directories.
		String smdSourcesPath = config.getProperty("smdInputDirectory");
		String smdCompiledPath = config.getProperty("smdOutputDirectory");
		String xmlFromSmdPath = config.getProperty("xmlOutputDirectory");
		String xmlToCompilePath = config.getProperty("xmlInputDirectory");
		File smdSourcesDir = new File(smdSourcesPath);
		File xmlToCompileDir = new File(xmlToCompilePath);
		
		//get the smds to convert to xml
		File[] smdSourcesFiles = smdSourcesDir.listFiles(new MapFileNameFilter());
		ArrayList<ServerMap> smdSources = new ArrayList<ServerMap>();
		ServerMap serverMap = null;
		for(File map : smdSourcesFiles){
			serverMap = new ServerMap();
			serverMap.loadMap(map);
			smdSources.add(serverMap);
		}
		
		//Write the maps' XML to file in the xml-from-smd folder
		printXmlFiles(smdSources, xmlFromSmdPath);
		
		//we're done with smdSources, clear that memory up.
		smdSources = null;
		
		Logger.info("Loading maps from xml...");
		//load the xml to be compiled
		File[] xmlSourcesFiles = xmlToCompileDir.listFiles(new XmlFileNameFilter());
		ArrayList<ServerMap> xmlSources = new ArrayList<ServerMap>();
		
		for(File xml : xmlSourcesFiles){
			serverMap = new ServerMap();
			try {
				serverMap.loadFromXML(xml);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
			xmlSources.add(serverMap);
		}
		
		//Compile the xml sources into smd files.
		printSmdFiles(xmlSources, smdCompiledPath);
		Logger.info("Done.");
	}
	
	/**
	 * Prints the XML representations of ServerMaps to file.
	 * @param maps
	 * @param directoryPath
	 */
	public static void printXmlFiles(ArrayList<ServerMap> maps, String directoryPath){
		//Write the maps' XML to file in the xml-from-smd folder
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			for(int i = 0; i < maps.size(); i++){
				ServerMap m = maps.get(i);
				Logger.info("Beginning to write " + m.getName() + ".xml to " + directoryPath);
				String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + m.toXML();
				
				if(m.getName().equalsIgnoreCase("Delos")){
					//Logger.debug(xml.substring(11356537-200, 11356537));
				}
				Document document = docBuilder.parse(new InputSource(new StringReader(xml)));
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(new File(directoryPath+File.separator+m.getName()+".xml"));
				transformer.transform(source, result);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints ServerMap.toSmd() to file.
	 * @param maps
	 * @param directoryPath
	 */
	public static void printSmdFiles(ArrayList<ServerMap> maps, String directoryPath){
		int maxBufferSize = Integer.parseInt(config.getProperty("outputBuffer.maxSize"));
		for(ServerMap m : maps){
			FileOutputStream stream = null;
			try {
				stream = new FileOutputStream(directoryPath+File.separator+m.getName()+".smd");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				continue;
			}
			Logger.info("Writing " + directoryPath+File.separator+m.getName()+".smd");
			try {
			    stream.write(m.toSmd(maxBufferSize));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			    try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
