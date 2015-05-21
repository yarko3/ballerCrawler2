import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


public class wineSearcherController {
	
	public static void main(String[] args) throws Exception
	{
		//storage folder for crawl data
		String crawlStorageFolder = "data/crawl/root";
		//number of threads to be used
		int numberOfCrawlers = 1;
		
		//config
		CrawlConfig config = new CrawlConfig();

		config.setCrawlStorageFolder(crawlStorageFolder);
		
		//instantiate stuff
		PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        
        //add starting seeds
        controller.addSeed("http://www.wine-searcher.com/merchant/8000");
        
        //start crawler
        controller.start(wineSearcherCrawler.class, numberOfCrawlers);
        
        
	}

}
