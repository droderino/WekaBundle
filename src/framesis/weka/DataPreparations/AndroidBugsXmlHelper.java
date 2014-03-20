package framesis.weka.DataPreparations;

import java.text.ParseException;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class AndroidBugsXmlHelper extends DefaultHandler {
	
	private Instances data;
	private Instance newInstance;
	
	private boolean bBug = false;
	private boolean bBugId = false;
	private boolean bTitle = false;
	private boolean bStatus = false;
	private boolean bOwner = false;
	private boolean bType = false;
	private boolean bPriority = false;
	private boolean bComponent = false;
	private boolean bClosedOn = false;
	private boolean bStars = false;
	private boolean bReportedBy = false;
	private boolean bOpenedDate = false;
	private boolean bDescription = false;
	
	private String bug = "bug";
	private String bugId = "bugid";
	private String title = "title";
	private String status = "status";
	private String owner = "owner";
	private String type = "type";
	private String priority = "priority";
	private String component = "component";
	private String closedOn = "closedon";
	private String stars = "stars";
	private String reportedBy = "reportedby";
	private String openedDate = "openeddate";
	private String description = "description";
	
	private FastVector statusVal;
	private FastVector typeVal;
	private FastVector priorityVal;
	private FastVector atts;
	
	public AndroidBugsXmlHelper()
	{
		setUpArff();
	}

	private void setUpArff() {
		statusVal = new FastVector();
		statusVal.addElement("new");
		statusVal.addElement("assigned");
		statusVal.addElement("fixed");
		statusVal.addElement("duplicate");
		statusVal.addElement("spam");
		statusVal.addElement("invalid");
		statusVal.addElement("wont fix");
		statusVal.addElement("closed");
		statusVal.addElement("verified");
		statusVal.addElement("reviewed");
		statusVal.addElement("declined");
		statusVal.addElement("needsInfo");
		statusVal.addElement("futureRelease");
		statusVal.addElement("released");
		statusVal.addElement("unreproducible");
		statusVal.addElement("question");
		statusVal.addElement("workingAsIntended");
		statusVal.addElement("unassigned");
		
		typeVal = new FastVector();
		typeVal.addElement("defect");
		typeVal.addElement("enhancement");
		
		priorityVal = new FastVector();
		priorityVal.addElement("critical");
		priorityVal.addElement("high");
		priorityVal.addElement("medium");
		priorityVal.addElement("low");
		priorityVal.addElement("small");
		
		atts = new FastVector();
		atts.addElement(new Attribute(bugId));
		atts.addElement(new Attribute(title, (FastVector)null));
		atts.addElement(new Attribute(status, statusVal));
		atts.addElement(new Attribute(owner, (FastVector)null));
		atts.addElement(new Attribute(type, typeVal));
		atts.addElement(new Attribute(priority, priorityVal));
		atts.addElement(new Attribute(component, (FastVector)null));
		atts.addElement(new Attribute(closedOn, (FastVector)null));
		atts.addElement(new Attribute(stars));
		atts.addElement(new Attribute(reportedBy, (FastVector)null));
		atts.addElement(new Attribute(openedDate, (FastVector)null));
		atts.addElement(new Attribute(description, (FastVector)null));
		
		data = new Instances("Android Bugs", atts, 0);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
	{
		if(qName.equalsIgnoreCase(bug))
		{
			newInstance = new Instance(data.numAttributes());
			bBug = true;
		}
		else if(qName.equalsIgnoreCase(bugId))
			bBugId = true;
		else if(qName.equalsIgnoreCase(title))
			bTitle = true;
		else if(qName.equalsIgnoreCase(status))
			bStatus = true;
		else if(qName.equalsIgnoreCase(owner))
			bOwner = true;
		else if(qName.equalsIgnoreCase(type))
			bType = true;
		else if(qName.equalsIgnoreCase(priority))
			bPriority = true;
		else if(qName.equalsIgnoreCase(component))
			bComponent = true;
		else if(qName.equalsIgnoreCase(closedOn))
			bClosedOn = true;
		else if(qName.equalsIgnoreCase(stars))
			bStars = true;
		else if(qName.equalsIgnoreCase(reportedBy))
			bReportedBy = true;
		else if(qName.equalsIgnoreCase(openedDate))
			bOpenedDate = true;
		else if(qName.equalsIgnoreCase(description))
			bDescription = true;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
	{
		if(qName.equalsIgnoreCase(bug))
		{
			data.add(newInstance);
			bBug = false;
		}
		else if(qName.equalsIgnoreCase(bugId))
			bBugId = false;
		else if(qName.equalsIgnoreCase(title))
			bTitle = false;
		else if(qName.equalsIgnoreCase(status))
			bStatus = false;
		else if(qName.equalsIgnoreCase(owner))
			bOwner = false;
		else if(qName.equalsIgnoreCase(type))
			bType = false;
		else if(qName.equalsIgnoreCase(priority))
			bPriority = false;
		else if(qName.equalsIgnoreCase(component))
			bComponent = false;
		else if(qName.equalsIgnoreCase(closedOn))
			bClosedOn = false;
		else if(qName.equalsIgnoreCase(stars))
			bStars = false;
		else if(qName.equalsIgnoreCase(reportedBy))
			bReportedBy = false;
		else if(qName.equalsIgnoreCase(openedDate))
			bOpenedDate = false;
		else if(qName.equalsIgnoreCase(description))
			bDescription = false;
	}
	
	@Override
	public void characters(char ch[], int start, int length)
	{
		if(bBug)
		{
			String value = new String(ch, start, length);
			if(bBugId)
				newInstance.setValue((Attribute)atts.elementAt(0), Integer.parseInt(value));
			else if(bTitle)
				newInstance.setValue((Attribute)atts.elementAt(1), value);
			else if(bStatus)
			{
				System.out.println("stat " + value);
				value = value.toLowerCase();
				System.out.println(value);
				newInstance.setValue((Attribute)atts.elementAt(2), value);
			}
			else if(bOwner)
				newInstance.setValue((Attribute)atts.elementAt(3), value);
			else if(bType)
				newInstance.setValue((Attribute)atts.elementAt(4), value);
			else if(bPriority)
			{
				System.out.println("prio " + value);
				value = value.toLowerCase();
				System.out.println(value);
				newInstance.setValue((Attribute)atts.elementAt(5), value);
			}
			else if(bComponent)
				newInstance.setValue((Attribute)atts.elementAt(6), value);
			else if(bClosedOn)
				newInstance.setValue((Attribute)atts.elementAt(7), value);
			else if(bStars)
				newInstance.setValue((Attribute)atts.elementAt(8), Integer.parseInt(value));
			else if(bReportedBy)
				newInstance.setValue((Attribute)atts.elementAt(9), value);
			else if(bOpenedDate)
				newInstance.setValue((Attribute)atts.elementAt(10), value);
			else if(bDescription)
				newInstance.setValue((Attribute)atts.elementAt(11), value);
		}
	}
	
	public Instances getData() {
		return data;
	}

	public void setData(Instances data) {
		this.data = data;
	}
}