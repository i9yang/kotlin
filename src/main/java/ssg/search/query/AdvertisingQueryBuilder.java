package ssg.search.query;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssg.search.base.Collection;
import ssg.search.collection.advertising.AdvertisingCollection;
import ssg.search.constant.Targets;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AdvertisingQueryBuilder implements QueryBuilder{
	private Logger logger = LoggerFactory.getLogger(AdvertisingQueryBuilder.class);
	private Parameter parameter;
	private boolean debug;
	
	private String advertisingDomain = "";
	private String advertisingUrl = "";
	private String resultContent = "";
	private HashMap<String, String> resultContentMap = new HashMap<String, String>();
	private static final int timeout = 3;
	RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout * 1000).setConnectTimeout(timeout * 1000).setConnectionRequestTimeout(timeout * 1000).build();

	public void set(Parameter parameter) {
		this.parameter = parameter;

		String zone = System.getProperty("spring.profiles.active");

		if(StringUtils.isEmpty(zone) || StringUtils.equals(zone, "local")){ 	//DEV
			advertisingDomain = "http://dev-addp.ssg.com"; //addpapi/dp/cpcDispCall.ssg
		}else if(StringUtils.equals(zone, "qa") || StringUtils.equals(zone, "qa2")){ 	//QA
			advertisingDomain = "http://qa-addp.ssg.com";
		}else if(StringUtils.equals(zone, "stg")){ //STG
			advertisingDomain = "http://stg-addp.ssg.com";
		}else if(StringUtils.equals(zone, "prod")){
			advertisingDomain = "http://addp.ssg.com";
		}
	}
	public void execute() {
		try {
			Targets targets = CollectionUtils.getTargets(parameter);
			for (Iterator<Collection> iter = targets.getCollectionSet(parameter).iterator(); iter.hasNext(); ) {
				Collection collection = iter.next();
				if(collection instanceof AdvertisingCollection){
					List<NameValuePair> params = ((AdvertisingCollection) collection).getQuery(parameter);
					String urlPath = ((AdvertisingCollection) collection).getUrlPath(parameter);
					advertisingUrl = this.advertisingDomain + urlPath;

					URI uri = new URI(advertisingUrl); 
				    uri = new URIBuilder(uri).addParameters(params).build();
				    
				    HttpClient httpClient = HttpClientBuilder.create().build();
				    
				    HttpGet httpGet = new HttpGet(uri);
				    httpGet.setConfig(config);

				    HttpResponse response = httpClient.execute(httpGet); // post 요청은 HttpPost()를 사용하면 된다. 
				    HttpEntity entity = response.getEntity();
				    
				    resultContent = EntityUtils.toString(entity); //URLEncoder.encode(utf8(value), "UTF-8")
				    resultContentMap.put(collection.getCollectionAliasName(parameter), resultContent);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		// TODO Auto-generated method stub
	}

	public Result result(Result result) {
		try {
			Targets targets = CollectionUtils.getTargets(parameter);
			for (Iterator<Collection> iter = targets.getCollectionSet(parameter).iterator(); iter.hasNext(); ) {
				Collection collection = iter.next();
				if(collection instanceof AdvertisingCollection){
					((AdvertisingCollection) collection).getResult(resultContentMap.get(collection.getCollectionAliasName(parameter)), collection.getCollectionName(parameter), parameter, result);
				}
			}
		} catch (Exception e) {
			logger.error("AdvertisingQueryBuilder result fail {}",e.getMessage());
		}
		
		return result;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public void debug() {
		// TODO Auto-generated method stub
		
	}
}
