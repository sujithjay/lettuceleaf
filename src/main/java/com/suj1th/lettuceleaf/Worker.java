package com.suj1th.lettuceleaf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class Worker {

	private static Map<String, Method> methodCache = new HashMap<>();
	private static final Logger LOGGER = Logger.getLogger(Worker.class);
	
	
	public Worker() {
		
	}

	public void execute(Message message) {	
		String workerName = message.getWorkerName();
		Method method = null;
		if(methodCache.containsKey(workerName)){
			method = methodCache.get(workerName);
			
		}else{
			Class worker = null;
			
			try {
				worker = Class.forName(workerName);
			} catch (ClassNotFoundException e) {
				LOGGER.error("Class Not Found with name "+workerName, e);
			}
			
			try {
				method = worker.getMethod("work", Message.class);
			} catch (NoSuchMethodException e) {
				LOGGER.error("'work()' method not defined in Worker class "+workerName, e);
			} catch (SecurityException e) {
				LOGGER.error("Security Exception!!!",e);
			}
		}
		
		try {
			method.invoke(null, message);
		} catch (IllegalAccessException e) {
			LOGGER.error("IllegalAccessException !!!",e);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Argument passed to 'Worker.work()' should be of type Message",e);
		} catch (InvocationTargetException e) {
			LOGGER.error("'Worker.work()' throws an unhandled exception",e);
		}
	}

}