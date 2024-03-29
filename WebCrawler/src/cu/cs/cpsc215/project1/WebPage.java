package cu.cs.cpsc215.project1;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Vector;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebPage
implements WebElement{
	private static Vector m_urls = new Vector();
	
	private String m_url;
	private Vector<WebPage> m_pages;
	private Vector<WebImage> m_images;
	private Vector<WebFile> m_files;
	
	public WebPage(String url){
		m_url=url;
		m_pages = new Vector();
		m_images = new Vector();
		m_files = new Vector();
	}

	public void crawl(int depth, String url){
		if(depth < 1){
			return;
		}
		else{
			if(!this.urlsContain(url)){
				try{
					
					DownloadRepository dataBase = DownloadRepository.getInstance();
					//lines 40-44 use the Jsoup.jar found at http://jsoup.org/
					Document doc = Jsoup.connect(url).timeout(10000).get();	
					Elements imagesHref = doc.select("[href$=.bmp],[href$=.tiff],[href$=.png],[href$=.jpg],[href$=.gif],[href$=.jpeg");
					Elements imagesSrc = doc.select("[src$=.jpg],[src$=.png],[src$=.bmp],[src$=.tiff],[src$=.gif],[src$=.jpeg]");
					Elements pages = doc.select("[href$=.htm],[href$=.html],[href$=.jsp],[href$=.asp],[href$=.php],[href$=.aspx]");
					Elements files = doc.select("[href$=.doc],[href$=.docx],[href$=.txt],[href$=.pdf],[href$=.odt],[href$=.xls],[href$=.ppt],[href$=.csv]");
					
					for(int i=0; i < pages.select("a").size();i++){
						String temp = pages.select("a").get(i).absUrl("href");
						if( !this.pagesContain(temp) ){
							m_pages.add(new WebPage(temp));
						}
					}
					
					for(int i=0; i < imagesHref.select("a").size();i++){
						String temp = imagesHref.select("a").get(i).absUrl("href");
						if( !this.imagesContain(temp) ){
							m_images.add(new WebImage(temp));
							dataBase.saveElement(temp);
						}	
					}
					for(int i=0; i < imagesSrc.select("img").size();i++){
						String temp = imagesSrc.select("img").get(i).absUrl("src");
						if( !this.imagesContain(temp) ){
							m_images.add(new WebImage(temp));
							dataBase.saveElement(temp);
						}
					}
					
					for(int i=0; i < files.select("a").size();i++){
						String temp = files.select("a").get(i).absUrl("href");
						if( !this.filesContain(temp) ){
							m_files.add(new WebFile(temp));
							dataBase.saveElement(temp);
						}
					}
					
					m_urls.add(url);
					
					for(int i =0; i<m_pages.size();i++){
						m_pages.get(i).crawl(depth-1,m_pages.get(i).toString());
					}
					
					return;
				}catch(HttpStatusException HTTPX){
					System.out.println("Skipping "+url+"\n");
					return;
				}catch(IOException IOX){
					System.out.println("ERROR: Bad Connection/Connection Timed out");
					return;
				}catch(IllegalArgumentException IAE){
					System.out.println("Invalid URL "+url);
					return;
				}
			}
		}
	}
	
	public boolean pagesContain(String other){
		for(int i=0; i<m_pages.size(); i++){
			if(m_pages.get(i).toString().compareToIgnoreCase(other) == 0){
				return true;
			}
		}
		return false;
	}
	
	public boolean imagesContain(String other){
		for(int i=0; i<m_images.size(); i++){
			if(m_images.get(i).toString().compareToIgnoreCase(other) == 0){
				return true;
			}
		}
		return false;
	}
	
	public boolean filesContain(String other){
		for(int i=0; i<m_files.size(); i++){
			if(m_files.get(i).toString().compareToIgnoreCase(other) == 0){
				return true;
			}
		}
		return false;
	}
	
	public boolean urlsContain(String other){
		for(int i=0; i<m_urls.size(); i++){
			if(m_urls.get(i).toString().compareToIgnoreCase(other) == 0){
				return true;
			}
		}
		return false;
	}
	
	public String toString(){
		return(m_url);
	}
	
	public Vector getWebImages(){
		return(m_images);
	}
	
	public Vector getWebPages(){
		return(m_pages);
	}
	
	public Vector getWebFiles(){
		return(m_files);
	}

}
