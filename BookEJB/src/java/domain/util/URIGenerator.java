package domain.util;



import java.net.URI;
import java.util.UUID;


public class URIGenerator {

	public static URI generateUri(Object resource) {
		String uri = Constants.NAMESPACE + resource.getClass().getSimpleName() + "/" + UUID.randomUUID();
		return URI.create(uri);
	}
}
