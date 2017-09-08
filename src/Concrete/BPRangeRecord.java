package Concrete;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import Interface.BPEffector;

public class BPRangeRecord {

	LocalDateTime start;
	LocalDateTime end;
	List<BPEffector> effectorList;
	
	private BPRangeRecord(LocalDateTime start, LocalDateTime end, List<BPEffector> effectorList)
	{
		this.start = start;
		this.end = end;
		this.effectorList = effectorList;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		System.out.println(start.format(formatter) + ", " + end.format(formatter) + ", " + effectorList.toString());
	}
	
	public static BPRangeRecord getInstance(LocalDateTime start, LocalDateTime end, List<BPEffector> effectorList)
	{
		//validation
		
		return new BPRangeRecord(start, end, effectorList);
	}
	
}
