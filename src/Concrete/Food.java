package Concrete;

import Interface.BPEffector;

public class Food implements BPEffector{
	
	private int id;
	private String name;
	private int glycemicIndex;
	
	private Food(int id, String name, int glycemicIndex)
	{
		this.id = id;
		this.name = name;
		this.glycemicIndex = glycemicIndex;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getGlycemicIndex() {
		return glycemicIndex;
	}

	public static Food getInstance(String id, String name, String glycemicIndex)
	{
		int idInteger = Integer.parseInt(id);
		int glycemicIndexInteger = Integer.parseInt(glycemicIndex);
		
		//validation
		if (glycemicIndexInteger > 150 || glycemicIndexInteger < 0)
			throw new IllegalArgumentException("Glycemic index value should be between 1 and 150.");
		
		//return person instance
		return new Food(idInteger, name, glycemicIndexInteger);
	}

	@Override
	public int getEffectIndex() {
		// TODO Auto-generated method stub
		return glycemicIndex;
	}
	
	public String toString()
	{
		return getName();
	}
}
