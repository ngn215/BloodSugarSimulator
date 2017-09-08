package Concrete;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserLogRecord {
	
	private String type;
	private String name;
	private LocalDateTime timeStamp;
	
	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	private UserLogRecord(String type, String name, LocalDateTime timeStamp)
	{
		this.type = type;
		this.name = name;
		this.timeStamp = timeStamp;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		System.out.println(type + ", " + name + ", " + timeStamp.format(formatter));
	}

	public static UserLogRecord getInstance(String type, String name, LocalDateTime timeStamp)
	{
		//validation
		if (!type.equals("Food") && !type.equals("Exercise"))
			throw new IllegalArgumentException("Only food and exercise types are supported.");
		
		return new UserLogRecord(type, name, timeStamp);
	}
	
}
