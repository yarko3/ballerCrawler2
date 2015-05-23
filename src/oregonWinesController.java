import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


public class oregonWinesController {
	
	public static void main(String[] args) throws Exception
	{
		//storage folder for crawl data
		String crawlStorageFolder = "data/crawl/root";
		//number of threads to be used
		int numberOfCrawlers = 1;
		
		//config
		CrawlConfig config = new CrawlConfig();

		config.setCrawlStorageFolder(crawlStorageFolder);
		
		//only looking at page whose url we spawned
		config.setMaxDepthOfCrawling(0);
		
		//instantiate stuff
		PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        
        
        //add starting seeds
        String baseURL = "http://www.oregonwines.com/winerydetail.php?WineryID=";
        
        for (int i = 2; i < 100; i++)
        {
        	controller.addSeed((baseURL + i));
        }
        
        //start crawler
        controller.start(oregonWinesCrawler.class, numberOfCrawlers);
        
        
	}

}
