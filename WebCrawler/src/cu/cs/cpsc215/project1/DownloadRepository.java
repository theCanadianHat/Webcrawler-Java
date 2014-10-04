package cu.cs.cpsc215.project1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class DownloadRepository {
	
	private static DownloadRepository m_singleton;
	private String m_base;
	
	private DownloadRepository(){}
	
	public static DownloadRepository getInstance(){
		if(m_singleton == null){
			m_singleton = new DownloadRepository();		
		}
		return(m_singleton);
	}
	
	public void init(String base){
		m_base = base;
	}

	public void saveElement(String saveMe){

		try {
			URL url = new URL(saveMe);
			String temp = url.getFile();
			temp = temp.replaceAll("/", "_");
			File file = new File(m_base+"/"+temp);
			BufferedInputStream inBuffer = new BufferedInputStream(url.openStream());
			FileOutputStream outStream = new FileOutputStream(file);
			byte[] aLine = new byte[1024];
			int numOfBytes =0;
			while( (numOfBytes=inBuffer.read(aLine)) != -1){
				outStream.write(aLine,0,numOfBytes);
			}
			
		}catch (MalformedURLException e) {
			System.out.println("ERROR: Bad Connection");
		}catch (FileNotFoundException FNFX){
			System.out.println("Unable to find file.");
		}catch (IOException e) {
			System.out.println("Unable to read/write file.");
		}
		
	}
	
}
