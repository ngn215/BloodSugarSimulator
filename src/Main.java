import Concrete.BPGraphBuilder;
import Concrete.BPLogProcessor;
import Factory.BPRangeRecordFactory;
import Factory.ExerciseFactory;
import Factory.FoodFactory;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try
		{
			FoodFactory.initializeFactory();
			ExerciseFactory.initializeFactory();
			
			BPRangeRecordFactory bpRangeRecordFactory = new BPRangeRecordFactory();
			
			BPLogProcessor processor = new BPLogProcessor("InputFiles/UserLog.txt", bpRangeRecordFactory);
			processor.processUserLog();
			
			BPGraphBuilder graphBuilder = new BPGraphBuilder(bpRangeRecordFactory);
			graphBuilder.buildBPGraph();
			graphBuilder.buildGlycationGraph();
		}
		catch(Exception e)
		{
			System.out.println("Errors occured.");
			e.printStackTrace();
		}
		
	}

}
