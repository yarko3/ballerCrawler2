import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

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
		line+= getName(xpath, doc);
		line += "\t";
		
		
		//get winery address
		line += getAddress(xpath, doc);
		line += "\t";
		
		
		//get winery website
		line += getUrl(xpath, doc);
		line += "\t";
		
		
		//get winery phone number
		line += getPhone(xpath, doc);
		line += "\t";
		
		String header = null;
		int ulCount = 1;
		int innerIndex = 2;
		
		//get the rest of the information
		forLoop:
		for (int i = 2; i < 6; i++)
		{
			
			try {
				//header of this information
				header = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/h2["+i+"]", doc, XPathConstants.STRING);
				
				//get index of this information
				int index = -1;
				switch (header)
				{
				case ("Accommodations"):
					index = 2;
					break;
				case ("Winery Information"):
					//can't do anything about it
					continue forLoop;
				case ("Wine Varietals"):
					index = 4;
					break;
				case ("American Viticultural Areas"):
					index = 5;
					break;
				case ("Sustainability Initiatives"):
					index = 6;
				default:
					//System.out.println("Header of data is not what we expected: " + header);
					break forLoop;
				}
				
				//add tabs for missing info
				while (innerIndex != index)
				{
					innerIndex++;
					line += "\t";
				}
				
				//is this sustainability stuff?
				if (index == 6)
				{
					line += getSustainability(xpath, doc);
				}
				//not the sustainability stuff
				else
				{
					//get data
					temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/ul["+ulCount+"]", 
		                       doc, XPathConstants.STRING);
		
					temp = temp.trim();
					temp = temp.replaceAll("\n", "; ");
					ulCount++;
		
					line+= temp;
				}
				
				
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
//		//get first element of page
//		try {
//			
//			
//			header = header.trim();
//			
//			//check what this info is
//			switch (header)
//			{
//			case ("Accomodations"):
//				break;
//			case ("Winery Information"):
//				line += "\t";
//				break;
//			case ("Wine Varietals"):
//				line += "\t\t";
//				break;
//			case ("American Viticultural Areas"):
//				line += "\t\t\t";
//				break;
//			default:
//					
//			
//			}
//			
//			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/ul[1]/li", 
//			                       doc, XPathConstants.STRING);
//			
//			temp = temp.trim();
//			temp = temp.replaceAll("\n", "; ");
//			
//			line+= temp;
//		} catch (XPathExpressionException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		line += "\t";
//		
//		//get winery information
//		try {
//			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/p[3]", 
//			                       doc, XPathConstants.STRING);
//			
//			temp = temp.trim();
//			temp = temp.replaceAll("\n", "; ");
//			
//			line+= temp;
//		} catch (XPathExpressionException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		line += "\t";
//		
//		//get wine varietals
//		try {
//			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/ul[2]", 
//			                       doc, XPathConstants.STRING);
//			
//			temp = temp.trim();
//			temp = temp.replaceAll("\n", "; ");
//			
//			line+= temp;
//		} catch (XPathExpressionException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		line += "\t";
//		
//		//get areas
//		try {
//			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/ul[3]", 
//			                       doc, XPathConstants.STRING);
//			
//			temp = temp.trim();
//			temp = temp.replaceAll("\n", "; ");
//			
//			line+= temp;
//		} catch (XPathExpressionException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		

	
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
		    out.println(line);
		    out.close();
		}
		catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
	}
	
	/**
	 * get the name of the winery
	 * @param xpath
	 * @param doc
	 * @return winery name
	 */
	private String getName(XPath xpath, org.w3c.dom.Document doc)
	{
		String temp = null;
		try {
			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/h1", 
			                       doc, XPathConstants.STRING);
			
			temp = temp.trim();
				
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return temp;
	}
	
	/**
	 * get winery address
	 * @param xpath
	 * @param doc
	 * @return address
	 */
	private String getAddress(XPath xpath, org.w3c.dom.Document doc)
	{
		String temp = null;
		try {
			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/p[1]", 
                    doc, XPathConstants.STRING);

			temp = temp.trim();
			temp = temp.replaceAll("\n", ", ");
				
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return temp;
	}
	
	/**
	 * get url
	 * @param xpath
	 * @param doc
	 * @return url
	 */
	private String getUrl(XPath xpath, org.w3c.dom.Document doc)
	{
		String temp = null;
		try {
			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[3]/table[1]/tbody/tr/td/p[2]/a/@href", 
                    doc, XPathConstants.STRING);

			temp = temp.trim();
			
			temp = "http://www.oregonwines.com/" + temp;
			
			
			temp = temp.replaceAll("amp;", "");
			
			URLConnection con = new URL( temp ).openConnection();
			
			con.connect();
			InputStream is = con.getInputStream();
			temp = con.getURL().toString();
			
			is.close();
				
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return temp;
	}
	
	/**
	 * get phone number
	 * @param xpath
	 * @param doc
	 * @return phone number/s
	 */
	private String getPhone(XPath xpath, org.w3c.dom.Document doc)
	{
		String temp = null;
		try {
			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/p[2]/text()", 
                    doc, XPathConstants.STRING);

			temp = temp.trim();
				
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		return temp;
	}
	
	/**
	 * get sustainability initiatives
	 * @param xpath
	 * @param doc
	 * @return sustainability initiatives
	 */
	private String getSustainability(XPath xpath, org.w3c.dom.Document doc)
	{
		String temp = null;
		try {
			temp = (String) xpath.evaluate("/html/body/table/tbody/tr[2]/td[2]/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr/td[1]/table/tbody", 
                    doc, XPathConstants.STRING);

			temp = temp.trim();
			temp = temp.replaceAll("\n", "; ");
			
				
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		return temp;
	}
	
	
}
