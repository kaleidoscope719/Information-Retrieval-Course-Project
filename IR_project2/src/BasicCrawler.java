/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package edu.uci.ics.crawler4j.examples.basic;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.io.*;

import org.apache.http.Header;

/**
 * @author Yasser Ganjisaffar [lastname at gmail dot com]
 */
public class BasicCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

  /**
   * You should implement this function to specify whether the given url
   * should be crawled or not (based on your crawling logic).
   */
  @Override
  public boolean shouldVisit(WebURL url) {
    String href = url.getURL().toLowerCase();
    Pattern FILTERS2 = Pattern.compile(".*\\.ics.uci.edu.*");
//    Pattern FILTERS2 = Pattern.compile(".*www.ics.uci.edu.*");
    Pattern FILTERS3 = Pattern.compile(".*calendar.ics.uci.edu.*");
    Pattern FILTERS4 = Pattern.compile(".*archive.ics.uci.edu.*");
    Pattern FILTERS5 = Pattern.compile(".*drzaius.ics.uci.edu.*");
    Pattern FILTERS6 = Pattern.compile(".*fano.ics.uci.edu.*");
    Pattern FILTERS7 = Pattern.compile(".*djp3-pc2.ics.uci.edu.*");
    Pattern FILTERS8 = Pattern.compile(".*wics.ics.uci.edu/events.*");
    Pattern FILTERS9 = Pattern.compile(".*wics.ics.uci.edu/news.*");
    		
//    return !FILTERS.matcher(href).matches() && FILTERS2.matcher(href).matches() && !FILTERS3.matcher(href).matches() &&  
//    !FILTERS4.matcher(href).matches() && !FILTERS5.matcher(href).matches() && !FILTERS7.matcher(href).matches() && !FILTERS6.matcher(href).matches() && !FILTERS8.matcher(href).matches()&& !FILTERS9.matcher(href).matches() && !href.startsWith("http://www.ics.uci.edu/~eppstein/");
    return !FILTERS.matcher(href).matches() && FILTERS2.matcher(href).matches() && !FILTERS3.matcher(href).matches() && !FILTERS4.matcher(href).matches() && !FILTERS5.matcher(href).matches() && !FILTERS6.matcher(href).matches() && !FILTERS7.matcher(href).matches() && !FILTERS8.matcher(href).matches() &&!FILTERS9.matcher(href).matches();
  }

  /**
   * This function is called when a page is fetched and ready to be processed
   * by your program.
   */
  @Override
  public void visit(Page page){
    int docid = page.getWebURL().getDocid();
    String url = page.getWebURL().getURL();
    String domain = page.getWebURL().getDomain();
    String path = page.getWebURL().getPath();
    String subDomain = page.getWebURL().getSubDomain();
    String parentUrl = page.getWebURL().getParentUrl();
    String anchor = page.getWebURL().getAnchor();
    
    try{
    	saveUrl(url,subDomain,docid);
    	saveSubdomain(subDomain,docid);
    }catch(IOException e){
    	e.printStackTrace();
    }

    System.out.println("DocId: {}"+ docid);
    System.out.println("URL: "+ url);
    System.out.println("Domain: '{}'"+ domain);
    System.out.println("Sub-domain: '{}'"+ subDomain);
    System.out.println("Path: '{}'"+ path);
    System.out.println("Parent page: {}"+ parentUrl);
    System.out.println("Anchor text: {}"+ anchor);

    if (page.getParseData() instanceof HtmlParseData) {
      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
      String text = htmlParseData.getText();
      String html = htmlParseData.getHtml();
      String title = htmlParseData.getTitle();
      ArrayList<WebURL> links = (ArrayList<WebURL>) htmlParseData.getOutgoingUrls();

      try{
    	  saveTitle(title,docid);
    	  saveText(text,docid);
    	  saveLinks(links, docid);
    	  saveLength(text.length(),docid);
      }catch(IOException e){
    	  e.printStackTrace();
      }
//      System.out.println("Text length: {}"+ text.length());
//      System.out.println("Html length: {}"+ html.length());
//      System.out.println("Number of outgoing links: {}"+ links.size());
    }

    Header[] responseHeaders = page.getFetchResponseHeaders();
    if (responseHeaders != null) {
    	System.out.println("Response headers:");
//      logger.debug("Response headers:");
      for (Header header : responseHeaders) {
    	  System.out.println("\t{}: {}"+ header.getName()+ header.getValue());
      }
    }

    System.out.println("=============");
//    logger.debug("=============");
  }
  public static void saveUrl (String url,String subDomain, int docid) throws IOException{
		FileWriter urlWriter = new FileWriter("/Users/xuruoyun/Documents/dataIR4/url.txt",true);
	    urlWriter.write("{}docid: "+docid + "  "+"{}subDomain:"+subDomain+".uci.edu   {}url:" + url + "\n");
	    urlWriter.close();

	}
  public static void saveSubdomain (String subDomain, int docid) throws IOException{
		FileWriter urlWriter = new FileWriter("/Users/xuruoyun/Documents/dataIR4/subDomain.txt",true);
	    urlWriter.write(docid + "  " + subDomain + ".uci.edu"+"\n");
	    urlWriter.close();
	}
	public static void saveText (String text, int docid) throws IOException{
		File file = new File("/Users/xuruoyun/Documents/dataIR4/text/text"+docid+".txt");
		FileWriter textWriter = new FileWriter(file);
	    textWriter.write(text);
	    textWriter.close();
	}
	public static void saveLinks(List<WebURL> webUrls, int docid) throws IOException {
		File file = new File("/Users/xuruoyun/Documents/dataIR4/outgoinglinks/" + docid + ".txt");
		FileWriter linksWriter = new FileWriter(file, true);
		BufferedWriter linksBfWriter = new BufferedWriter(linksWriter);
		for (WebURL tempUrl : webUrls) {
			linksBfWriter.write(tempUrl.getURL() + "\n");
		}
		linksBfWriter.close();
		linksWriter.close();
	}
	public static void saveTitle (String title, int docid) throws IOException{
		FileWriter urlWriter = new FileWriter("/Users/xuruoyun/Documents/dataIR4/title.txt",true);
	    urlWriter.write(docid + "  " + title + "\n");
	    urlWriter.close();
	}
	
	public static void saveLength (int length,int docid) throws IOException{
		FileWriter lengthWriter = new FileWriter("/Users/xuruoyun/Documents/dataIR4/length.txt",true);
	    lengthWriter.write(length +"for docid" + docid + "\r\n");
	    lengthWriter.close();
	}
}