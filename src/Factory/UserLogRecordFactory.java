package Factory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import Concrete.UserLogRecord;

public class UserLogRecordFactory {
		
	private UserLogRecordFactory()
	{
		
	}
	
	public static UserLogRecord createInstance(String type, String name, String timeStamp)
	{						
		LocalDateTime timeStampDateTime = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		
		try
		{
			timeStampDateTime = LocalDateTime.parse(timeStamp, formatter);
		}
		catch(DateTimeParseException e)
		{
			System.out.println("Invalid date format in userlog : " + timeStamp);
			throw new IllegalArgumentException("Invalid date format in userlog : " + timeStamp);
		}
		
		if (type == null || type.isEmpty())
			throw new IllegalArgumentException("Type cannot be null or empty");
		
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Name cannot be null or empty");
		
		return UserLogRecord.getInstance(type, name, timeStampDateTime);
	}
}
