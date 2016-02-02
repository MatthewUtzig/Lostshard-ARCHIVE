package com.lostshard.Lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.Lostshard.Objects.Recorders.Record;

public class RecordManager {
	static RecordManager manager = new RecordManager();

	public static RecordManager getManager() {
		return manager;
	}

	private List<Record> records = new ArrayList<Record>();

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
