
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;


public class MyCrawler extends WebCrawler {
    private static final Logger logger = LoggerFactory.getLogger(MyCrawler.class);

    
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|xml|json|rss"
    		 + "|mp3|mp4|zip|gz))$");

    CrawlStat myCrawlStat;

    public MyCrawler() {
        myCrawlStat = new CrawlStat();
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && href.startsWith("https://www.c-span.org/");
        //return !FILTERS.matcher(href).matches() && href.startsWith("http://www.ics.uci.edu/");
    }

    @Override
    public void visit(Page page) {
        logger.info("Visited: {}", page.getWebURL().getURL());
        myCrawlStat.incProcessedPages();

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData parseData = (HtmlParseData) page.getParseData();
            Set<WebURL> links = parseData.getOutgoingUrls();
            myCrawlStat.incTotalLinks(links.size());
            try {
                myCrawlStat.incTotalTextSize(parseData.getText().getBytes("UTF-8").length);
            } catch (UnsupportedEncodingException ignored) {
                // Do nothing
            }
        }
        // We dump this crawler statistics after processing every 50 pages
        if ((myCrawlStat.getTotalProcessedPages() % 50) == 0) {
            dumpMyData();
        }
    }

    /**
     * This function is called by controller to get the local data of this crawler when job is
     * finished
     */
    @Override
    public Object getMyLocalData() {
        return myCrawlStat;
    }

    /**
     * This function is called by controller before finishing the job.
     * You can put whatever stuff you need here.
     */
    @Override
    public void onBeforeExit() {
        dumpMyData();
    }

    public void dumpMyData() {
        int id = getMyId();
     
       // System.out.println("This is test output");

        // You can configure the log to output to file
        logger.info("Crawler {} > Processed Pages: {}", id, myCrawlStat.getTotalProcessedPages());
        logger.info("Crawler {} > Total Links Found: {}", id, myCrawlStat.getTotalLinks());
        logger.info("Crawler {} > Total Text Size: {}", id, myCrawlStat.getTotalTextSize());
    }
}

/*
public class MyCrawler extends WebCrawler {
 private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
 + "|png|mp3|mp3|zip|gz))$");
 /**
 * This method receives two parameters. The first parameter is the page
 * in which we have discovered this new url and the second parameter is
 * the new url. You should implement this function to specify whether
 * the given url should be crawled or not (based on your crawling logic).
 * In this example, we are instructing the crawler to ignore urls that
 * have css, js, git, ... extensions and to only accept urls that start
 * with "http://www.viterbi.usc.edu/". In this case, we didn't need the
 * referringPage parameter to make the decision.
 */
 //@Override
 /*public boolean shouldVisit(Page referringPage, WebURL url) {
	 String href = url.getURL().toLowerCase();
	 return !FILTERS.matcher(href).matches()
	 && href.startsWith("http://www.viterbi.usc.edu/");
	 }*/
 
 /**
  * This function is called when a page is fetched and ready
  * to be processed by your program.
  */
 /* @Override
  public void visit(Page page) {
  String url = page.getWebURL().getURL();
  System.out.println("URL: " + url);
  if (page.getParseData() instanceof HtmlParseData) {
  HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
  String text = htmlParseData.getText();
  String html = htmlParseData.getHtml();
  Set<WebURL> links = htmlParseData.getOutgoingUrls();
  System.out.println("Text length: " + text.length());
  System.out.println("Html length: " + html.length());
  System.out.println("Number of outgoing links: " + links.size());
  }
  }
  
}*/