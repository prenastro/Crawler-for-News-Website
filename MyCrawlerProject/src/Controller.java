import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	
	 private static final Logger logger =
		        LoggerFactory.getLogger(Controller.class);
	 
 public static void main(String[] args) throws Exception {
	 
	 try {
			System.setOut(new PrintStream(new FileOutputStream("output.csv")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
 String crawlStorageFolder = "data/crawl/root";
 int numberOfCrawlers = 7;
 CrawlConfig config = new CrawlConfig();
 config.setCrawlStorageFolder(crawlStorageFolder);
 /*
 * Instantiate the controller for this crawl.
 */

 config.setMaxPagesToFetch(20000);
 config.setMaxDepthOfCrawling(16);
 config.setIncludeBinaryContentInCrawling(false);
 PageFetcher pageFetcher = new PageFetcher(config);
 RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
 RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
 CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
 
 /*
 * For each crawl, you need to add some seed urls. These are the first
 * URLs that are fetched and then the crawler starts following links
 * which are found in these pages
 */
 controller.addSeed("https://www.c-span.org/");
		 
		 /*
		  * Start the crawl. This is a blocking operation, meaning that your code
		  * will reach the line after this only when crawling is finished.
		  */
		  controller.start(MyCrawler.class, numberOfCrawlers);
		  
		    List<Object> crawlersLocalData = controller.getCrawlersLocalData();
	        long totalLinks = 0;
	        long totalTextSize = 0;
	        int totalProcessedPages = 0;
	        for (Object localData : crawlersLocalData) {
	            CrawlStat stat = (CrawlStat) localData;
	            totalLinks += stat.getTotalLinks();
	            totalTextSize += stat.getTotalTextSize();
	            totalProcessedPages += stat.getTotalProcessedPages();
	        }
	       
	        logger.info("Aggregated Statistics:");
	        logger.info("\tProcessed Pages: {}", totalProcessedPages);
	        logger.info("\tTotal Links found: {}", totalLinks);
	        logger.info("\tTotal Text Size: {}", totalTextSize);
		  }
	 }