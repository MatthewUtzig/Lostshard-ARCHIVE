package com.lostshard.Logging;

import java.util.LinkedList;
import java.util.List;

public class LogManager {

	private final List<Log> logs;
	
	
	public LogManager() {
		this.logs = new LinkedList<Log>();
	
	}
	
	public boolean log(Log log) {
		logs.add(log);
		return true;
	}
	
}
