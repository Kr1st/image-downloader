package si.fiat.imagedownloader.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarContent;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarHeader;
import de.sstoehr.harreader.model.HarRequest;
import de.sstoehr.harreader.model.HarResponse;
import si.fri.imagedownloader.pojos.ImageData;



public class Main {

	private static List<ImageData> processHar(String harFilePath) throws HarReaderException {

		List<ImageData> retVal = new ArrayList<ImageData>();

		HarReader harReader = new HarReader();
		Har har = harReader.readFromFile(new File(harFilePath));

		for (HarEntry harEntry : har.getLog().getEntries()) {
			HarResponse harResponse = harEntry.getResponse();

			for (HarHeader harHeader : harResponse.getHeaders()) {
				String headerName = harHeader.getName();
				String headerValue = harHeader.getValue();

				if (headerName.equals("Content-Type") && headerValue.startsWith("image")) {

					HarRequest harRequest = harEntry.getRequest();

					String url = harRequest.getUrl();
					HarContent harContent = harResponse.getContent();

					ImageData imageData = new ImageData(url, harContent);

					retVal.add(imageData);

					break;
				}

			}

		}

		return retVal;

	}

	private static boolean createDir(String dirPath) {

		File theDir = new File(dirPath);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + dirPath);
			boolean result = false;

			try {
				theDir.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
			if (result) {
				System.out.println("DIR created");
			}

			return result;
		}
		return false;

	}
	

	private static String createDirs(String imageUrl) throws MalformedURLException{
		URL url = new URL(imageUrl);

		String prefix = "images/";

		// create root directory
		String host = url.getHost();
		prefix += host;
		createDir(prefix);

		String path = url.getPath();
		String[] splittedPath = path.split("/");

		// create sub directories
		

		
		for (int i = 0; i < splittedPath.length - 1; i++) {
			prefix += splittedPath[i] + "/";
			createDir(prefix);
		}
		
		String imagePath =  prefix + splittedPath[splittedPath.length - 1];
		
		return imagePath;
		
	}
	
	private static void decodeAndSaveImage(String imagePath, String encoded) throws IOException{
		
		byte[] decoded = Base64.getDecoder().decode(encoded);
		
		FileOutputStream fos = new FileOutputStream(imagePath);
		try {
		    fos.write(decoded);
		}
		finally {
		    fos.close();
		}
		
	}
	
	
	private static void decodeAndSaveImages(List<ImageData> imageDataList) throws IOException {

		for (ImageData imageData : imageDataList) {
			System.out.println(imageData.getUrl());

			String imagePath =  createDirs(imageData.getUrl());
			decodeAndSaveImage(imagePath, imageData.getContent().getText());
			
			
		}

	}

	public static void main(String[] args) throws HarReaderException, IOException {

		List<ImageData> imageDataList = processHar("./hars/www.fiat.it.har");
		decodeAndSaveImages(imageDataList);

	}

}
