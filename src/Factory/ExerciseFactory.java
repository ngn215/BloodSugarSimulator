package Factory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

import Concrete.Exercise;

public class ExerciseFactory {

	private final static HashMap<String, Exercise> exercisesMap = new HashMap<String, Exercise>();
	private final static String EXERCISESLISTINPUTFILE = "InputFiles/Exercise - Sheet1.txt";
	private static boolean factoryInitialized = false;
	
	private ExerciseFactory()
	{
		//do nothing
	}
	
	public static boolean initializeFactory()
	{
		//makes sure we only initialize factory once
		if (!factoryInitialized)
		{
			factoryInitialized = true;
			populateExercisesMap();
			return true;
		}
		
		return false;
	}
	
	private static void populateExercisesMap()
	{
		getExercisesListFromInputFile();
	}
	
	private static void getExercisesListFromInputFile()
	{
		Path path = null;
		Scanner scanner = null;
		
		try
		{
			path = Paths.get(EXERCISESLISTINPUTFILE);
			scanner = new Scanner(path);
			
			scanner.nextLine(); //skipping headers
			
			while (scanner.hasNextLine())
		    {
				String lineFromFile = scanner.nextLine();
				String[] tokensFromLine = lineFromFile.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				
				if (tokensFromLine.length < 3)
					throw new RuntimeException("Invalid exercise list file.");
				
				Exercise exercise = createExerciseInstance(tokensFromLine[0], tokensFromLine[1], tokensFromLine[2]);
				
				exercisesMap.put(tokensFromLine[1], exercise);
		    }      
		}
		catch(IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException("Exception while getting exercises list from file.");
		}
		finally
		{
			if (scanner != null)
				scanner.close();
		}
	}
	
	private static Exercise createExerciseInstance(String id, String name, String exerciseIndex)
	{					
		return Exercise.getInstance(id, name, exerciseIndex);
	}
	
	public static Exercise getExerciseInstance(String exerciseName)
	{
		return exercisesMap.get(exerciseName);
	}
}
