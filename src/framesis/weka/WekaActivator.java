package framesis.weka;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import weka.filters.unsupervised.attribute.StringToWordVector;
import framesis.api.TaskRegistry;
import framesis.weka.Tasks.NaiveBayesTask;
import framesis.weka.Tasks.StringToWordVectorTask;

public class WekaActivator implements BundleActivator{

	@Override
	public void start(BundleContext arg0) throws Exception {
		TaskRegistry.register(StringToWordVectorTask.class);
		TaskRegistry.register(NaiveBayesTask.class);
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		TaskRegistry.deregister(StringToWordVector.class);
		TaskRegistry.deregister(NaiveBayesTask.class);
	}

}
