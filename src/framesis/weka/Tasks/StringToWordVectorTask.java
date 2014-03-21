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
import framesis.weka.WekaParams;

public class StringToWordVectorTask implements TextMiningTask<Instances>{

	@Override
	public String execute(Map<String, String> params) {
		// TODO Auto-generated method stub
		Instances processedData = executeTask(params);
		String filename = params.get(FILE) + "_StringToWordVectorTask.arff";
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
			DataSource input = new DataSource(params.get(PREPARATEDFILE));
			Instances instances = input.getDataSet();
			
			if(instances.classIndex() == -1)
			{
				if(params.get(WekaParams.CLASSINDEX) != null)
					instances.setClassIndex(Integer.parseInt(params.get(WekaParams.CLASSINDEX)));
				else if(params.get(WekaParams.CLASSATTRIBUTE) != null)
					instances.setClass(instances.attribute(params.get(WekaParams.CLASSATTRIBUTE)));
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
			
			if( !entry.getKey().equals(FILE) && !entry.getKey().equals(PREPARATEDFILE)  && !entry.getKey().equals(WekaParams.EVALUATIONRESULTS)
					&& !entry.getKey().equals(WekaParams.CLASSINDEX) && !entry.getKey().equals(WekaParams.CLASSATTRIBUTE) )
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
