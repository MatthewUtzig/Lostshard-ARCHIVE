package com.lostshard.Lostshard.Objects.Recorders;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.lostshard.Lostshard.Manager.RecordManager;

@MappedSuperclass
public class Record {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	
	private Date creation;
	
	/**
	 * 
	 */
	public Record() {
		super();
		this.creation = new Date();
		RecordManager.getManager().getRecords().add(this);
	}
	
	/**
	 * @param id
	 */
	public Record(int id) {
		super();
		this.id = id;
	}

	/**
	 * @return the creation
	 */
	public Date getCreation() {
		return creation;
	}

	/**
	 * @param creation the creation to set
	 */
	public void setCreation(Date creation) {
		this.creation = creation;
	}
}
