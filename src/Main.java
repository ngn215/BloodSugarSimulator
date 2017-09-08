import Concrete.BPLogProcessor;
import Factory.ExerciseFactory;
import Factory.FoodFactory;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try
		{
			FoodFactory.initializeFactory();
			ExerciseFactory.initializeFactory();
			
			BPLogProcessor processor = new BPLogProcessor("InputFiles/UserLog.txt");
			processor.processUserLog();
		}
		catch(Exception e)
		{
			System.out.println("Errors occured.");
			e.printStackTrace();
		}
		
	}

}
