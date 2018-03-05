package rainbow.Common;

import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class XmlParser {
	
	final static String UPNP_CLASS = "upnp:class";
	final static String UPNP_TITLE = "dc:title";
	final static String UPNP_ARTIST = "upnp:artist";
	final static String UPNP_URL = "";
	final static String UPNP_COVER = "upnp:albumArtURI";

	public static class MediaMetaData{
		
		public MediaMetaData() {
			// TODO Auto-generated constructor stub
		}
		
		public String Class = "";
		public String Title = "";
		public String Artist = "";
		public String CoverUrl = "";
		public String Url = "";
		
	}
	
	public static String getElementText(String tagName,String xml) throws XmlPullParserException, IOException{

		  	XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	    	XmlPullParser parser = factory.newPullParser();
	    	boolean isFound = false;
	    		parser.setInput(new StringReader(xml));
	    		 int eventType = parser.getEventType();
	    		 while (eventType != XmlPullParser.END_DOCUMENT) {
	    			 switch (eventType) {
		    			 case XmlPullParser.START_DOCUMENT:
		    				 break;
		    			  
		    			 case XmlPullParser.START_TAG:
			    			 if(tagName.equals( parser.getName()))			    				
			    					 isFound = true;
		    			 	break;
		    			  case XmlPullParser.END_TAG:
		    				  break;
		    			  case XmlPullParser.END_DOCUMENT:
		    				  break;
		    				  
		    			  case XmlPullParser.TEXT:
		    				  if(isFound)
		    					  return parser.getText(); 
		    				  break;

	    			   }
	    		
	    			eventType = parser.next();
	    		 }	
	    		 return "";
		}
	
	
	
	public static MediaMetaData getMetaData(String xml){
		XmlPullParserFactory factory;
		MediaMetaData mediaMetaData  =new MediaMetaData();
		
		try {
			factory = XmlPullParserFactory.newInstance();
		   	XmlPullParser xmlParser = factory.newPullParser();
		   	xmlParser.setInput(new StringReader(xml));
	    	int eventType = xmlParser.getEventType();
	    	while(eventType!=XmlPullParser.END_DOCUMENT){ 

	            switch(eventType){ 
	            case XmlPullParser.START_TAG:
	            	String tag = xmlParser.getName(); 
	            	if(tag.equals(UPNP_CLASS)){
	            		
	            		mediaMetaData.Class = xmlParser.nextText();
	            		Log.i("XmlParser", mediaMetaData.Class);
	            	}else if(tag.equals(UPNP_ARTIST)){
	            		mediaMetaData.Artist = xmlParser.nextText();
	            		Log.i("XmlParser", mediaMetaData.Artist);

	            	}else if(tag.equals(UPNP_COVER)){
	            		mediaMetaData.CoverUrl = xmlParser.nextText();
	            		Log.i("XmlParser", mediaMetaData.CoverUrl);

	            	}else if(tag.equals(UPNP_TITLE)){
	            		mediaMetaData.Title = xmlParser.nextText();
	            		Log.i("XmlParser", mediaMetaData.Title);

	            	}
	                break;
	                
	           case XmlPullParser.END_TAG:
	        	   
	        	   break; 
	           default:
	        	   break;
	            }
	            eventType=xmlParser.next();
	    	}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("XmlParser", "Parser finished");
    	return mediaMetaData;	
	}
	
}
