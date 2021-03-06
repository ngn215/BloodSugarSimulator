package Concrete;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;

import Factory.BPRangeRecordFactory;
import Factory.ExerciseFactory;
import Factory.FoodFactory;
import Factory.UserLogRecordFactory;
import Interface.BPEffector;

public class BPLogProcessor {

	private final String userlogPath;
	private final List<UserLogRecord> recordsFromLog;
	PriorityQueue<LocalDateTime> pq;
	HashMap<LocalDateTime, List<BPEffector>> map;
	BPRangeRecordFactory bpRangeRecordFactory;
	
	public BPLogProcessor(String userlogPath, BPRangeRecordFactory bpRangeRecordFactory)
	{
		this.recordsFromLog = new ArrayList<UserLogRecord>();
		this.pq = new PriorityQueue<LocalDateTime>();
		this.map = new HashMap<LocalDateTime, List<BPEffector>>();
		
		this.userlogPath = userlogPath;
		this.bpRangeRecordFactory = bpRangeRecordFactory;
	}
	
	public void processUserLog()
	{
		getUserLogsFromFile();
		processLogs();
	}
	
	private void getUserLogsFromFile()
	{
		System.out.println("Getting user logs from file..");
		
		Path path = null;
		Scanner scanner = null;
		//we could use stream here
		
		try
		{
			path = Paths.get(userlogPath);
			scanner = new Scanner(path);
			
			while (scanner.hasNextLine())
		    {
				String lineFromFile = scanner.nextLine();
				if (!lineFromFile.equals(""))
				{
					String[] tokensFromLine = lineFromFile.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
					
					if (tokensFromLine.length < 3)
						throw new RuntimeException("Invalid entry in user log file.");
					
					String type = tokensFromLine[0];
					String name = tokensFromLine[1];
					String timeStamp = tokensFromLine[2];
					
					UserLogRecord record = UserLogRecordFactory.createInstance(type, name, timeStamp);
					recordsFromLog.add(record);
				}
		    }      
		}
		catch(IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException("Exception while processing user log.");
		}
		finally
		{
			if (scanner != null)
				scanner.close();
		}
	}

	private void processLogs()
	{
		System.out.println("Processing user logs..");
		
		for (int i=0; i<recordsFromLog.size(); i++)
		{
			UserLogRecord record = recordsFromLog.get(i);
			String name = record.getName();
			
			LocalDateTime startTime = record.getTimeStamp();
			LocalDateTime endTime = null;
			BPEffector bpEffector = null;
			
			if (record.getType().equals("Food"))
			{
				bpEffector = FoodFactory.getFoodInstance(name);
				endTime = startTime.plusHours(2);
			}
			else 
			{
				bpEffector = ExerciseFactory.getExerciseInstance(name);
				endTime = startTime.plusHours(1);
			}
			
			//if this is not last record
			if (i < recordsFromLog.size()-1)
			{
				///get next record
				UserLogRecord nextRecord = recordsFromLog.get(i+1);
				LocalDateTime startTimeNextRecord = nextRecord.getTimeStamp();
				
				addToPriorityQueueAndMap(endTime, bpEffector);
				
				LocalDateTime currentStartTime = startTime;
				
				//add all items smaller than endtime from pq to range
				while(!pq.isEmpty() && pq.peek().isBefore(endTime) && pq.peek().isBefore(startTimeNextRecord))
				{							
					LocalDateTime pqEndTime = pq.peek();
					bpRangeRecordFactory.createInstance(currentStartTime, pqEndTime, getAllItemsFromMap());
					
					removeTopElementFromPQAndMap();
					currentStartTime = pqEndTime.plusMinutes(1);
				}
				
				//NO OVERLAP
				if (endTime.isBefore(startTimeNextRecord) && currentStartTime.isBefore(endTime))
				{
					bpRangeRecordFactory.createInstance(currentStartTime, endTime, getAllItemsFromMap());
					removeTopElementFromPQAndMap(); //remove end time from PQ
				}
				//OVERLAP
				else if(endTime.isAfter(startTimeNextRecord) && currentStartTime.isBefore(startTimeNextRecord))
				{
					bpRangeRecordFactory.createInstance(currentStartTime, startTimeNextRecord.minusMinutes(1), getAllItemsFromMap());
				}
				//EQUAL
				else
				{
					bpRangeRecordFactory.createInstance(currentStartTime, startTimeNextRecord.minusMinutes(1), getAllItemsFromMap());
				}
			}
			else
			{
				//this is last record
				
				addToPriorityQueueAndMap(endTime, bpEffector);
				
				LocalDateTime currentStartTime = startTime;
				
				while(!pq.isEmpty())
				{
					LocalDateTime pqEndTime = pq.peek();
					bpRangeRecordFactory.createInstance(currentStartTime, pqEndTime, getAllItemsFromMap());
					
					removeTopElementFromPQAndMap();
					currentStartTime = pqEndTime.plusMinutes(1);
				}
				
				//if we have still not hit the end time for last record then add the last range
				if (currentStartTime.isBefore(endTime) || currentStartTime.isEqual(endTime))
				{
					addToPriorityQueueAndMap(endTime, bpEffector);
					bpRangeRecordFactory.createInstance(currentStartTime, endTime, getAllItemsFromMap());
				}
			}
		}
	}
	
	private void addToPriorityQueueAndMap(LocalDateTime endTime, BPEffector bpEffector)
	{		
		//System.out.println("adding : " + endTime);
		List<BPEffector> bpEffectorList = null;
		if (!map.containsKey(endTime))
		{
			pq.add(endTime); //if endtime is not present then we need to add to PQ
			bpEffectorList = new ArrayList<BPEffector>();
		}
		else
		{
			bpEffectorList = map.get(endTime);							
		}
		
		bpEffectorList.add(bpEffector);
		map.put(endTime, bpEffectorList);
	}
	
	private void removeTopElementFromPQAndMap()
	{
		LocalDateTime endTime = pq.poll();
		map.remove(endTime);
	}
	
	private List<BPEffector> getAllItemsFromMap()
	{
		List<BPEffector> bpEffectorList = new ArrayList<BPEffector>();
		
		Iterator<Entry<LocalDateTime, List<BPEffector>>> it = map.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Map.Entry<LocalDateTime, List<BPEffector>> pair = (Map.Entry<LocalDateTime, List<BPEffector>>)it.next();
	        //System.out.println(pair.getKey() + " = " + pair.getValue());
	        List<BPEffector> innerList = (List<BPEffector>) pair.getValue();
	        
	        for(BPEffector effectorItem : innerList)
	        {
	        	bpEffectorList.add(effectorItem);
	        }
	    }
		
		return bpEffectorList;
	}
}
