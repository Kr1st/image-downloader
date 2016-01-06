package si.fiat.main;

import java.io.File;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;

public class Main {

	public static void main(String[] args) throws HarReaderException {
		HarReader harReader = new HarReader();
		Har har = harReader.readFromFile(new File("./hars/www.fiat.it.har"));
		
		System.out.println(har.getLog().getCreator().getName());

	}

}
