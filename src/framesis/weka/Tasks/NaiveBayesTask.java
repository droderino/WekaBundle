package framesis.weka.Tasks;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import framesis.api.TextMiningTask;

public class NaiveBayesTask implements TextMiningTask<Classifier>{

	private Instances train;
	private Instances test;
	@Override
	public String execute(Map<String, String> params) {

		String filename = params.get("file") + "_naiveBayes.txt";
		this.executeTask(params);
		try {
			Instances output = mergeInstances(train, test);
			String result = output.toString();
			
			FileWriter fw = new FileWriter(filename);
			fw.write(result);
			fw.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filename;
	}

	@Override
	public Classifier executeTask(Map<String, String> params) {

		Classifier naiveBayes = null;
		try {
			this.setTrainTestInstances(params);
			naiveBayes = new NaiveBayes();
			String[] options = this.configureOptions(params);
			naiveBayes.setOptions(options);
			
			naiveBayes.buildClassifier(train);
			
			for(int i=0; i<test.numInstances(); i++)
			{
				test.instance(i).setClassValue(naiveBayes.classifyInstance(test.instance(i)));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return naiveBayes;
	}

	private void setTrainTestInstances(Map<String, String> params)
			throws Exception {
		DataSource input = new DataSource(params.get("preparatedFile"));
		Instances data = input.getDataSet();
		
		int trainSize = (int)Math.round(data.numInstances() * 0.8);
		int testSize = data.numInstances() - trainSize;
		train = new Instances(data, 0, trainSize);
		test = new Instances(data, trainSize, testSize);
		
		setClassAttribute(params, train);
		setClassAttribute(params, test);
	}

	private void setClassAttribute(Map<String, String> params, Instances instances) {
		if(instances.classIndex() == -1)
		{
			if(params.get("classIndex") != null)
				instances.setClassIndex(Integer.parseInt(params.get("classIndex")));
			else if(params.get("classAttribute") != null)
				instances.setClass(instances.attribute(params.get("classAttribute")));
		}
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "NaiveBayesTask";
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
					&& !entry.getKey().equals("file") && !entry.getKey().equals("classAttribute") )
			{
				options.add(entry.getKey());
				if(entry.getValue() != null)
					options.add(entry.getValue());
			}
		}
		
		String[] ret = new String[options.size()];
		return options.toArray(ret);
	}
	
	private Instances mergeInstances(Instances first, Instances second)
	{
		Instances result = first;
		for(int i=0; i<second.numInstances(); i++)
			result.add(second.instance(i));
		return result;
	}
}
