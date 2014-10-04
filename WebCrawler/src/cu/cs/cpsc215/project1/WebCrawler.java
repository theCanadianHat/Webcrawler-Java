package cu.cs.cpsc215.project1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.Jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


 
public class WebCrawler {


	public static void main(String[] args) {
		
		if(args.length != 3){
			System.out.println("ERROR: You need three argument from command line.");
			System.out.println("URL root, depth, directory for results");
			System.exit(-1);
		}
		
		
		
		File file = new File(args[2]);
		if(file.mkdir()){
			System.out.println("Directory "+args[2]+" created.");
		}else{
			System.out.println("Unable to create directory.");
		}
		
		int depth = Integer.parseInt(args[1]);
		DownloadRepository dataBase = DownloadRepository.getInstance();
		dataBase.init(args[2]);
		
		WebPage root = new WebPage(args[0]);
		root.crawl(depth,args[0]);
		
		System.out.println("Crawl Complete.");
				
	
		}
	}

