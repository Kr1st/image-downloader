package si.fri.imagedownloader.pojos;

import java.io.Serializable;

import de.sstoehr.harreader.model.HarContent;

public class ImageData implements Serializable{


	private static final long serialVersionUID = 1L;

	private String url;
	
	private HarContent content;

	
	
	public ImageData(String url, HarContent content) {
		
		this.url = url;
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HarContent getContent() {
		return content;
	}

	public void setContent(HarContent content) {
		this.content = content;
	} 
	
	
	
	
}
