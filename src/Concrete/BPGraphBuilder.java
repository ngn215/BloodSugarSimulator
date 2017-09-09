package Concrete;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Factory.BPRangeRecordFactory;
import Interface.BPEffector;

public class BPGraphBuilder {
	
	private final HashMap<LocalDateTime, Float> bpGraphMap;
	private final HashMap<LocalDateTime, Integer> glycationGraphMap;
	private final BPRangeRecordFactory bpRangeRecordFactory;
	
	public BPGraphBuilder(BPRangeRecordFactory bpRangeRecordFactory)
	{
		this.bpGraphMap = new HashMap<LocalDateTime, Float>();
		this.glycationGraphMap = new HashMap<LocalDateTime, Integer>();
		this.bpRangeRecordFactory = bpRangeRecordFactory;
	}
	
	public HashMap<LocalDateTime, Float> buildGlycationGraph()
	{
		int glycation = 0;
		
		Iterator<Entry<LocalDateTime, Float>> it = bpGraphMap.entrySet().iterator();
	    while (it.hasNext()) 
	    {
	        Map.Entry<LocalDateTime, Float> pair = (Map.Entry<LocalDateTime, Float>)it.next();
	        
	        LocalDateTime time = pair.getKey();
	        glycation += pair.getValue() > 150 ? 1 : 0;
	        
	        glycationGraphMap.put(time, glycation);
	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
			System.out.println(time.format(formatter) + ", " + glycation);
	    }
		
		return bpGraphMap;
	}
	
	public HashMap<LocalDateTime, Float> buildBPGraph()
	{
		List<BPRangeRecord> listOfRecords = bpRangeRecordFactory.getBprangerecordlist();
		
		BPRangeRecord record = listOfRecords.get(0);
		LocalDateTime prevEndTime = record.getStart();
		float prevBP = 80;
		
		for (int i=0; i < listOfRecords.size(); i++)
		{
			record = listOfRecords.get(i);
			LocalDateTime recordStart = record.getStart();
			LocalDateTime recordEnd = record.getEnd();
			
			//if its been more than 2 hours since next record then bp will try to approach 80 with steady rate of 1 per min
			if (prevEndTime.plusHours(2).isBefore(recordStart))
			{
				prevBP = addSteadyRateBPToMap(prevEndTime.plusMinutes(1), recordStart.minusMinutes(1), prevBP);
			}
						
			float effectiveImpact = calculateEffectiveImpact(record);
			float currentBP = addBPToMap(recordStart, recordEnd, effectiveImpact, prevBP);
			
			prevEndTime = recordEnd;
			prevBP = currentBP;
			
		}
		
		return bpGraphMap;
	}
	
	private float calculateEffectiveImpact(BPRangeRecord record)
	{
		float effectiveIndex = 0;
		for (BPEffector bpEffector : record.getEffectorList())
		{
			effectiveIndex += bpEffector.getEffectIndex() / 120f; //per min effect
		}
		
		BigDecimal bd = new BigDecimal(Float.toString(effectiveIndex));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		//System.out.println("2 decimal places " + bd.floatValue());
		
        //rounding to 2 decimal places
		return bd.floatValue();
	}
	
	private float addSteadyRateBPToMap(LocalDateTime recordStart, LocalDateTime recordEnd, float startBp)
	{
		LocalDateTime temp = recordStart;
		int addBP = 0;
		
		if (startBp > 80.0)
			addBP = -1;
		else if (startBp < 80.0)
			addBP = 1;
		
		while(!temp.isAfter(recordEnd) && (int) startBp != 80)
		{
			//keep adding / subtracting till BP hits 80
			startBp += addBP;
			
			//rounding to 2 decimals
			BigDecimal bd = new BigDecimal(Float.toString(startBp));
	        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
	        startBp = bd.floatValue();
			
			bpGraphMap.put(temp, startBp);
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
			System.out.println(temp.format(formatter) + ", " + startBp);
			
			temp = temp.plusMinutes(1);
		}
		
		//now bp is 80. add rest of values		
		if (!temp.isAfter(recordEnd))
			startBp = addBPToMap(temp, recordEnd, 0, 80);
		
		return startBp;
	}
	
	private float addBPToMap(LocalDateTime recordStart, LocalDateTime recordEnd, float effectiveImpact, float startBP)
	{
		LocalDateTime temp = recordStart;
		while(!temp.isAfter(recordEnd))
		{
			startBP += effectiveImpact;
			
			//rounding to 2 decimals
			BigDecimal bd = new BigDecimal(Float.toString(startBP));
	        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
	        startBP = bd.floatValue();
	        
			bpGraphMap.put(temp, startBP);
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
			System.out.println(temp.format(formatter) + ", " + startBP);
			
			temp = temp.plusMinutes(1);
		}
		
		return startBP;
	}
}
