package com.dmall.miner.wicc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dmall.miner.wicc.service.DataBaseService;
import com.dmall.miner.wicc.task.CollectTask;

/**
 * Hello world!
 *
 */
public class BootStrap {
	
	private static int startPage;
	
	private static int endPage;
	
	private static int threadNum;
	
	static {
		startPage = 1;
		endPage = 4000;
		threadNum = 2;
	}
	
    public static void main( String[] args ) {
    	ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"spring-service.xml"});
    	DataBaseService dataBaseService = (DataBaseService)context.getBean("dataBaseService");
    	int threadDealPage = endPage / threadNum;
        for(int i = startPage; i <= threadNum; i++) {
//        	System.out.println("startPage = " + (threadDealPage * (i - 1) + startPage) + ", endPage = " + threadDealPage * i);
        	new Thread(new CollectTask((threadDealPage * (i - 1) + startPage), threadDealPage * i, dataBaseService)).start();
        }
    }
}
