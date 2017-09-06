package Concrete;

public class Exercise {

	private int id;
	private String name;
	private int exerciseIndex;
	
	private Exercise(int id, String name, int exerciseIndex)
	{
		this.id = id;
		this.name = name;
		this.exerciseIndex = exerciseIndex;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getExerciseIndex() {
		return exerciseIndex;
	}

	public static Exercise getInstance(String id, String name, String exerciseIndex)
	{
		int idInteger = Integer.parseInt(id);
		int exerciseIndexInteger = Integer.parseInt(exerciseIndex);
		
		//validation
		if (exerciseIndexInteger > 150 || exerciseIndexInteger < 0)
			throw new IllegalArgumentException("Exercise index value should be between 1 and 150.");
		
		//return person instance
		return new Exercise(idInteger, name, exerciseIndexInteger);
	}
}
