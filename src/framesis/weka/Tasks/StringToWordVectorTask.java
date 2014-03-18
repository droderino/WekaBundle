package framesis.weka.Tasks;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import framesis.api.TextMiningTask;

public class StringToWordVectorTask implements TextMiningTask<Instances>{

	@Override
	public String execute(Map<String, String> params) {
		// TODO Auto-generated method stub
		Instances processedData = executeTask(params);
		String filename = params.get("file") + "_StringToWordVectorTask.arff";
		try {
			FileWriter processedFile = new FileWriter(filename);
			processedFile.write(processedData.toString());
			processedFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filename;
	}
	
	@Override
	public Instances executeTask(Map<String, String> params) {
		Instances processedData = null;
		
		try {
			DataSource input = new DataSource(params.get("preparatedFile"));
			Instances instances = input.getDataSet();
			
			if(instances.classIndex() == -1)
			{
				if(params.get("classIndex") != null)
					instances.setClassIndex(Integer.parseInt(params.get("classIndex")));
				else if(params.get("classAttribute") != null)
					instances.setClass(instances.attribute(params.get("classAttribute")));
			}
			
			StringToWordVector filter = new StringToWordVector();
			
			String[] options = configureOptions(params);
			
			filter.setOptions(options);
			filter.setInputFormat(instances);
			
			processedData = Filter.useFilter(instances, filter);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processedData;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "StringToWordVectorTask";
	}

	private String[] configureOptions(Map<String, String> params)
	{
		ArrayList<String> options = new ArrayList<String>();
		
		Set<Entry<String, String>> entries = params.entrySet();
		Iterator<Entry<String, String>> iter = entries.iterator();
		
		while(iter.hasNext())
		{
			Entry<String, String> entry = iter.next();
			
			if( !entry.getKey().equals("preparatedFile") && !entry.getKey().equals("classIndex") 
					&& !entry.getKey().equals("file") && !entry.getKey().equals("classAttriute") )
			{
				options.add(entry.getKey());
				if(entry.getValue() != null)
					options.add(entry.getValue());
			}
		}
		
		String[] ret = new String[options.size()];
		return options.toArray(ret);
	}
}
