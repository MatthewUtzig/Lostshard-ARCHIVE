package com.lostshard.lostshard.Manager;

import java.util.LinkedList;
import java.util.List;

import com.lostshard.lostshard.Objects.Recorders.Record;

public class RecordManager {
	static RecordManager manager = new RecordManager();

	public static RecordManager getManager() {
		return manager;
	}

	private List<Record> records = new LinkedList<Record>();

	public RecordManager() {
	}

	/**
	 * @return the records
	 */
	public List<Record> getRecords() {
		return records;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(List<Record> records) {
		this.records = records;
	}
}
