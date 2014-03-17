package framesis.weka.Tasks;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import framesis.TextMiningTask;

public class NaiveBayesTask implements TextMiningTask<Classifier>{

	private Instances train;
	private Instances test;
	@Override
	public String execute(Map<String, String> params) {
		Classifier cls = this.executeTask(params);
		try {
			Evaluation eval = new Evaluation(train);
			eval.evaluateModel(cls, test);
			String result = eval.toSummaryString();
			
			String filename = params.get("file") + "_naiveBayes.txt";
			FileWriter fw = new FileWriter(filename);
			fw.write(result);
			fw.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
		
		if(train.classIndex() == -1)
			train.setClassIndex(Integer.parseInt(params.get("classIndex")));
		if(test.classIndex() == -1)
			test.setClassIndex(Integer.parseInt(params.get("classIndex")));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String[] configureOptions(Map<String, String> params)
	{
		ArrayList<String> options = new ArrayList<String>();
		
		Set<Entry<String, String>> entries = params.entrySet();
		Iterator<Entry<String, String>> iter = entries.iterator();
		
		while(iter.hasNext())
		{
			Entry<String, String> entry = iter.next();
			if( !entry.getKey().equals("preparatedFile") && !entry.getKey().equals("classIndex") && !entry.getKey().endsWith("file") )
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
