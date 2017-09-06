import Factory.ExerciseFactory;
import Factory.FoodFactory;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try
		{
			FoodFactory.initializeFactory();
			ExerciseFactory.initializeFactory();
		}
		catch(Exception e)
		{
			System.out.println("Errors occured.");
		}
		
	}

}
