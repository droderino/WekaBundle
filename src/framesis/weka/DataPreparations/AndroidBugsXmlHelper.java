package framesis.weka.DataPreparations;


import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class AndroidBugsXmlHelper extends DefaultHandler {
	
	private Instances data;
	private Instance newInstance;
	private String tmpDescription;
	private int curId, i=0;
	
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
	private String prefix = "bug_";
	
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
		statusVal.addElement("needsinfo");
		statusVal.addElement("futurerelease");
		statusVal.addElement("released");
		statusVal.addElement("unreproducible");
		statusVal.addElement("question");
		statusVal.addElement("workingasintended");
		statusVal.addElement("unassigned");
		statusVal.addElement("usererror");
		
		typeVal = new FastVector();
		typeVal.addElement("defect");
		typeVal.addElement("enhancement");
		
		priorityVal = new FastVector();
		priorityVal.addElement("critical");
		priorityVal.addElement("high");
		priorityVal.addElement("medium");
		priorityVal.addElement("low");
		priorityVal.addElement("small");
		priorityVal.addElement("blocker");
		
		atts = new FastVector();
		atts.addElement(new Attribute(prefix + bugId));
		atts.addElement(new Attribute(prefix + title, (FastVector)null));
		atts.addElement(new Attribute(prefix + status, statusVal));
		atts.addElement(new Attribute(prefix + owner, (FastVector)null));
		atts.addElement(new Attribute(prefix + type, typeVal));
		atts.addElement(new Attribute(prefix + priority, priorityVal));
		atts.addElement(new Attribute(prefix + component, (FastVector)null));
		atts.addElement(new Attribute(prefix + closedOn, (FastVector)null));
		atts.addElement(new Attribute(prefix + stars));
		atts.addElement(new Attribute(prefix + reportedBy, (FastVector)null));
		atts.addElement(new Attribute(prefix + openedDate, (FastVector)null));
		atts.addElement(new Attribute(prefix + description, (FastVector)null));
		
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
		{
			tmpDescription = new String();
			bDescription = true;
		}
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
		{
			newInstance.setValue((Attribute)atts.elementAt(11), tmpDescription);
			bDescription = false;
		}
	}
	
	@Override
	public void characters(char ch[], int start, int length)
	{
		if(bBug)
		{
			String value = new String(ch, start, length);
			if(bBugId)
			{
				curId = Integer.parseInt(value);
				newInstance.setValue((Attribute)atts.elementAt(0), Integer.parseInt(value));
			}
			else if(bTitle)
				newInstance.setValue((Attribute)atts.elementAt(1), value);
			else if(bStatus)
				newInstance.setValue((Attribute)atts.elementAt(2), value.toLowerCase());
			else if(bOwner)
				newInstance.setValue((Attribute)atts.elementAt(3), value);
			else if(bType)
				newInstance.setValue((Attribute)atts.elementAt(4), value.toLowerCase());
			else if(bPriority)
				newInstance.setValue((Attribute)atts.elementAt(5), value.toLowerCase());
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
			{
				if(value.startsWith("\n"))
					value = value.replaceAll("\n", " ");
				tmpDescription = tmpDescription + value;
			}
		}
	}
	
	public Instances getData() {
		return data;
	}

	public void setData(Instances data) {
		this.data = data;
	}
}