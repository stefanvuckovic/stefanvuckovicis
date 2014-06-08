package domain;





import java.net.URI;

import thewebsemantic.Id;


public class Thing {

	@Id
	private URI uri;
	
	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}
}
