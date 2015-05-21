import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;


public class oregonWinesCrawler extends WebCrawler{
	
String filename = "crawlResults/oregonWinesResults.txt";

	
	/**
	 * Should we visit the url from referringPage?
	 * We should if it is a merchant 
	 */
	@Override
	public boolean shouldVisit(Page page, WebURL url)
	{
		//we generate all of the wineries as seeds so we don't care about links from these pages
		return false;
	}
	
	/**
	 * What we do when we actually visit the page.
	 * Append a line in our running tile for the winery represented by this page
	 */
	@Override
	public void visit(Page page)
	{
		System.out.println(page.getWebURL().getURL());
			
		
		//make a string we will write to file
		String line = "";
		
		//get data from page
		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
		String html = htmlParseData.getHtml();
		
		
		//convert to DOM
		TagNode tagNode = new HtmlCleaner().clean(
		        html);
		org.w3c.dom.Document doc = null;
		
		
		try {
			doc = new DomSerializer(
			        new CleanerProperties()).createDOM(tagNode);
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		String temp;
		
		//get winery name
		try {
			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/h1", 
			                       doc, XPathConstants.STRING);
			
			temp = temp.trim();
			
			//if no name, then no page, then we return
			if (temp == null || temp.equals(""))
				return;
			
			line+= temp;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		line += "\t";
		
		
		//get winery address
		try {
			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/p[1]", 
			                       doc, XPathConstants.STRING);
			
			temp = temp.trim();
			
			line+= temp;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		line += "\t";
		
		
		//get winery website
		try {
			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[3]/table[1]/tbody/tr/td/p[2]/a/@href", 
			                       doc, XPathConstants.STRING);
			
			temp = temp.trim();
			
			temp = "http://www.oregonwines.com/" + temp;
			

			line += getDirectUrl(temp);
			

			
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		line += "\t";
		
		
		//get winery phone number
		try {
			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/p[2]/text()", 
			                       doc, XPathConstants.STRING);
			
			temp = temp.trim();
			
			line+= temp;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		line += "\t";
		
		//get winery accommodations
		try {
			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/ul[1]/li", 
			                       doc, XPathConstants.STRING);
			
			temp = temp.trim();
			temp = temp.replaceAll("\n", "; ");
			
			line+= temp;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		line += "\t";
		
		//get winery information
		try {
			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/p[4]", 
			                       doc, XPathConstants.STRING);
			
			temp = temp.trim();
			temp = temp.replaceAll("\n", "; ");
			
			line+= temp;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		line += "\t";
		
		//get wine varietals
		try {
			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/ul[2]", 
			                       doc, XPathConstants.STRING);
			
			temp = temp.trim();
			temp = temp.replaceAll("\n", "; ");
			
			line+= temp;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		line += "\t";
		
		//get areas
		try {
			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/ul[3]", 
			                       doc, XPathConstants.STRING);
			
			temp = temp.trim();
			temp = temp.replaceAll("\n", "; ");
			
			line+= temp;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

	
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
		    out.println(line);
		    out.close();
		}
		catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
	}
	
	protected String getDirectUrl(String link) {
	    String resultUrl = link;
	    HttpURLConnection connection = null;
	    try {
	        connection = (HttpURLConnection) new URL(link).openConnection();
	        connection.setInstanceFollowRedirects(false);
	        connection.connect();
	        int responseCode = connection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
	            String locationUrl = connection.getHeaderField("Location");

	            if (locationUrl != null && locationUrl.trim().length() > 0) {
	                IOUtils.close(connection);
	                resultUrl = getDirectUrl(locationUrl);
	            }
	        }
	    } catch (Exception e) {
	    } finally {
	        IOUtils.close(connection);
	    }
	    return resultUrl;
	}

}
