package framesis.weka.Tasks;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import framesis.api.TextMiningTask;
import framesis.weka.WekaParams;

public class NaiveBayesTask implements TextMiningTask<Classifier>{

	private Instances train;
	private Instances test;
	private Instances original;
	@Override
	public String execute(Map<String, String> params) {

		String filename = params.get(FILE) + "_naiveBayes.txt";
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
			
			evaluateClassifier(params, naiveBayes);
			
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

	private void evaluateClassifier(Map<String, String> params,
			Classifier naiveBayes) throws Exception {
		Evaluation eval = new Evaluation(train);
		eval.evaluateModel(naiveBayes, test);
		String results = eval.toSummaryString() + eval.toClassDetailsString();
		params.put(WekaParams.EVALUATIONRESULTS, results);
	}

	private void setTrainTestInstances(Map<String, String> params)
			throws Exception {
		DataSource input = new DataSource(params.get(PREPARATEDFILE));
		Instances data = input.getDataSet();
		original = input.getDataSet();
		
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
			if(params.get(WekaParams.CLASSINDEX) != null)
				instances.setClassIndex(Integer.parseInt(params.get(WekaParams.CLASSINDEX)));
			else if(params.get(WekaParams.CLASSATTRIBUTE) != null)
				instances.setClass(instances.attribute(params.get(WekaParams.CLASSATTRIBUTE)));
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
			if( !entry.getKey().equals(FILE) && !entry.getKey().equals(PREPARATEDFILE) && !entry.getKey().equals(WekaParams.EVALUATIONRESULTS)
					&& !entry.getKey().equals(WekaParams.CLASSATTRIBUTE) && !entry.getKey().equals(WekaParams.CLASSINDEX) )
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
