package com.dmall.miner.wicc.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dmall.miner.wicc.model.WiccTx;
import com.dmall.miner.wicc.service.DataBaseService;

@Component
@Scope("prototype") 
public class CollectTask implements Runnable {
	
	private DataBaseService dataBaseService;
	
	private int startPage;
	
	private int endPage;
	
	private SimpleDateFormat simpleDateFormat;
	
	public CollectTask(int startPage, int endPage, DataBaseService dataBaseService) {
		this.startPage = startPage;
		this.endPage = endPage;
		this.dataBaseService = dataBaseService;
//		simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
	}

	@Override
	public void run() {
		for(int i = startPage; i < endPage; i++) {
			try {
				List<WiccTx> wiccTxs = new ArrayList<WiccTx>();
				Document doc = Jsoup.connect("https://etherscan.io/token/generic-tokentxns2?contractAddress=0x4f878c0852722b0976a955d68b376e4cd4ae99e5&mode=&p=" + i).get();
				Elements table = doc.getElementsByTag("tr");
				for(Element rows : table) {
					int j = 0;
					WiccTx wiccTx = null;
					for(Element row : rows.getElementsByTag("td")) {
						if(wiccTx == null) {
							wiccTx = new WiccTx();							
						}
						switch(j++) {
							case 0: 
								wiccTx.setTxHash(row.getElementsByTag("a").html());
								break;
							case 1:
								wiccTx.setDateTime(row.getElementsByTag("span").attr("title"));
								break;
							case 2:
								wiccTx.setFrom(row.getElementsByTag("a").html());
								break;			
							case 4:
								wiccTx.setTo(row.getElementsByTag("a").html());
								break;			
							case 5:
								wiccTx.setQuantity(row.html());
								break;	
							default:
								
						}
					}
					if(wiccTx != null) {
						wiccTxs.add(wiccTx);						
					}
				}
				dataBaseService.batchInsertWiccTx(wiccTxs);
				System.out.println("get page = " + i + " successful");
			} catch(Exception e) {
				System.out.println("get page = " + i + " failed");
				e.printStackTrace();
			}
		}
	}

}
