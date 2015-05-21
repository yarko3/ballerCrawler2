import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;


public class wineSearcherCrawler extends WebCrawler{
	
	
	String filename = "crawlResults/wineSearcherResults.txt";

	
	/**
	 * Should we visit the url from referringPage?
	 * We should if it is a merchant 
	 */
	@Override
	public boolean shouldVisit(Page page, WebURL url)
	{
		//we want to traverse the page if it is a merchant or it leads to more merchant pages in pager-container
		System.out.println(url.toString());
		if (url.toString().contains("http://www.wine-searcher.com/merchant/"))
			return true;
		return (url.toString().contains("http://www.wine-searcher.com/merchants/"));
			

//		//is this a link in the pager-container
//		//get data from page
//		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//		String html = htmlParseData.getHtml();
//		
//		
//		//convert to DOM
//		TagNode tagNode = new HtmlCleaner().clean(
//		        html);
//		org.w3c.dom.Document doc = null;
//		
//		
//		try {
//			doc = new DomSerializer(
//			        new CleanerProperties()).createDOM(tagNode);
//		} catch (ParserConfigurationException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		XPath xpath = XPathFactory.newInstance().newXPath();
//		
//		//*[@id="merchantsearch"]/tbody/tr[27]/td/div
//		
//		//check if pager-container has this url
//		try {
//			String pagerContainer = (String) xpath.evaluate("//*[@id='merchantsearch']/tbody/tr[27]/td/div/a/@href", 
//			                       doc);
//			
//			return pagerContainer.contains(url.getURL());
//			
//			
//		} catch (XPathExpressionException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		return false;
	}
	
	/**
	 * What we do when we actually visit the page.
	 * Append a line in our running tile for the merchant represented by this page
	 */
	@Override
	public void visit(Page page)
	{
		System.out.println(page.getWebURL().getURL());
		
		
		//are we at a winery's specific page? If so, get the data from the page
		if (page.getWebURL().getURL().contains("http://www.wine-searcher.com/merchants/"))
			return;
		
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
		
		
		try {
			String wineryName = (String) xpath.evaluate("//h1[contains(@itemprop, 'name')]/text()", 
			                       doc, XPathConstants.STRING);
			wineryName = wineryName.replaceAll("\n", "");
			
			line+= wineryName;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		line += "\t";
		
		
		try {
			String wineryCountry = (String) xpath.evaluate("//*[@id='mer-table']/tbody/tr[1]/td[2]", 
			                       doc, XPathConstants.STRING);
			wineryCountry = wineryCountry.replaceAll("\n", "");
			line += wineryCountry;
			
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		line+= "\t";
		
		try {
			String notes = (String) xpath.evaluate("//*[@id='mer-table']/tbody/tr[2]/td[2]/span", 
			                       doc, XPathConstants.STRING);
			notes = notes.trim();
			notes = notes.replaceAll("\n", ";");
			line += notes;
			
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		line += "\t";
		
		try {
			String websiteURL = (String) xpath.evaluate("//span[contains(@itemprop, 'url')]/text()", 
			                       doc, XPathConstants.STRING);
			
			line += websiteURL;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		line += "\t";
		
		try {
			String address = (String) xpath.evaluate("//td[contains(@itemprop, 'streetAddress')]/text()", 
			        doc, XPathConstants.STRING);
			address = address.trim();
			line += address;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		line += '\t';
		
		try {
			String internet = (String) xpath.evaluate("//*[@id='mer-table']/tbody/tr[5]/td[2]/div[1]/text()", 
			        doc, XPathConstants.STRING);
			internet = internet.trim();
			internet = internet.replaceAll("\n", ";");
			
			line += internet;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		line += '\t';
		
		try {
			String updateFrequency = (String) xpath.evaluate("//*[@id='mer-table']/tbody/tr[6]/td[2]", 
			        doc, XPathConstants.STRING);
			updateFrequency = updateFrequency.trim();
			updateFrequency = updateFrequency.replaceAll("\n", ";");
			
			line += updateFrequency;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		line += '\t';
		
		
		
		try {
			String rating = (String) xpath.evaluate("//*[@id='mer-table']/tbody/tr[7]/td[2]/span[2]/small/span/text()", 
			        doc, XPathConstants.STRING);
			
			
			line += rating;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		line += '\t';
		
		
		try {
			String description = (String) xpath.evaluate("//*[@id='mer-table']/tbody/tr[8]/td[2]/ul", 
			        doc, XPathConstants.STRING);
			description = description.trim();
			description = description.replaceAll("\n", ";");
			
			line += description;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		line += '\t';
		
		
		try {
			String terms = (String) xpath.evaluate("//*[@id='mer-table']/tbody/tr[9]/td[2]", 
			        doc, XPathConstants.STRING);
			terms = terms.trim();
			
			terms = terms.replaceAll("\n", ";");
			
			line += terms;
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		line += '\t';
		
		
		try {
			String contact = (String) xpath.evaluate("//*[@id='mer-table']/tbody/tr[10]/td[2]", 
			        doc, XPathConstants.STRING);
			contact = contact.trim();
			contact = contact.replaceAll("\n", ";");
			
			line += contact;
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

}
