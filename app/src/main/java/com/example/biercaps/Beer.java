package com.example.biercaps;

import com.orm.SugarRecord;

import java.util.Calendar;

public class Beer extends SugarRecord<Beer> {
	
	public String name;
	public String country;
	public String notes;
	public float rate;
    public String photoPath;
	public static Calendar date;

	public Beer(){
		
	}
	
	public Beer(String name_beer, String country_beer, String notes_beer, float rate_beer, String photoPath){
	
		Beer.date = Calendar.getInstance();
		
		this.name = name_beer;
		this.country = country_beer;
		this.notes = notes_beer;
		this.rate = rate_beer;
        this.photoPath = photoPath;
	}
	
	public String getName(){
		return name;
	}
	
	public String getCountry(){
		return country;
	}
	
	public Calendar getDate(){
		return date;
	}
	
	public String getNotes(){
		return notes;
	}
	
	public float getRate(){
		return rate;
	}
}
