package Factory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

import Concrete.Food;

public class FoodFactory {

	private final static HashMap<String, Food> foodsMap = new HashMap<String, Food>();
	private final static String FOODSLISTINPUTFILE = "InputFiles/FoodDB - Sheet1.txt";
	private static boolean factoryInitialized = false;
	
	private FoodFactory()
	{
		//do nothing
	}
	
	public static boolean initializeFactory()
	{
		//makes sure we only initialize factory once
		if (!factoryInitialized)
		{
			factoryInitialized = true;
			populateFoodsMap();
			return true;
		}
		
		return false;
	}
	
	private static void populateFoodsMap()
	{
		getFoodsListFromInputFile();
	}
	
	private static void getFoodsListFromInputFile()
	{
		Path path = null;
		Scanner scanner = null;
		
		try
		{
			path = Paths.get(FOODSLISTINPUTFILE);
			scanner = new Scanner(path);
			
			scanner.nextLine(); //skipping headers
			
			while (scanner.hasNextLine())
		    {
				String lineFromFile = scanner.nextLine();
				String[] tokensFromLine = lineFromFile.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				
				Food food = createFoodInstance(tokensFromLine[0], tokensFromLine[1], tokensFromLine[2]);
				
				foodsMap.put(tokensFromLine[1], food);
		    }      
		}
		catch(IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException("Exception while getting foods list from file.");
		}
		finally
		{
			if (scanner != null)
				scanner.close();
		}
	}
	
	private static Food createFoodInstance(String id, String name, String glycemicIndex)
	{						
		return Food.getInstance(id, name, glycemicIndex);
	}
	
	public static Food getFoodInstance(String foodName)
	{
		return foodsMap.get(foodName);
	}
}
