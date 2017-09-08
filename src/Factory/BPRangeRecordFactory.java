package Factory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import Concrete.BPRangeRecord;
import Interface.BPEffector;

public class BPRangeRecordFactory {

	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
	private final static List<BPRangeRecord> bpRangeRecordList = new ArrayList<BPRangeRecord>();
	
	private BPRangeRecordFactory()
	{
		//do nothing
	}
	
	public static BPRangeRecord createInstance(String start, String end, List<BPEffector> effectorList)
	{						
		LocalDateTime startDateTime = null;
		LocalDateTime endDateTime = null;
		
		try
		{
			startDateTime = LocalDateTime.parse(start, formatter);
		}
		catch(DateTimeParseException e)
		{
			System.out.println("Invalid date format : " + start);
			throw new IllegalArgumentException("Invalid date format : " + start);
		}
		
		try
		{
			endDateTime = LocalDateTime.parse(end, formatter);
		}
		catch(DateTimeParseException e)
		{
			System.out.println("Invalid date format : " + end);
			throw new IllegalArgumentException("Invalid date format : " + end);
		}
		
		if (effectorList == null || effectorList.isEmpty())
			throw new IllegalArgumentException("effectorList cannot be null or empty");
		
		BPRangeRecord bpRangeRecord = BPRangeRecord.getInstance(startDateTime, endDateTime, effectorList);
		bpRangeRecordList.add(bpRangeRecord);
		
		return bpRangeRecord;
	}
	
	public static BPRangeRecord createInstance(LocalDateTime start, LocalDateTime end, List<BPEffector> effectorList)
	{						
		if (effectorList == null || effectorList.isEmpty())
			throw new IllegalArgumentException("effectorList cannot be null or empty");
		
		BPRangeRecord bpRangeRecord = BPRangeRecord.getInstance(start, end, effectorList);
		bpRangeRecordList.add(bpRangeRecord);
		
		return bpRangeRecord;
	}
}
