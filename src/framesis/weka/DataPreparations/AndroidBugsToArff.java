package framesis.weka.DataPreparations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import weka.core.Instances;
import framesis.api.DataPreparation;

public class AndroidBugsToArff extends DefaultHandler implements DataPreparation {

	@Override
	public String prepare(Map<String, String> params) {		
		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			AndroidBugsXmlHelper helper = new AndroidBugsXmlHelper();
			saxParser.parse(new File(params.get(FILE)), helper);
			
			Instances output = helper.getData();
			String result = output.toString();
			params.put(PREPARATEDFILE, params.get(FILE) + "_converted.arff");
			FileWriter fw = new FileWriter(params.get(PREPARATEDFILE));
			fw.write(result);
			fw.close();
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return params.get(PREPARATEDFILE);
	}
	
	
}
