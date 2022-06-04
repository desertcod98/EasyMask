package it.volta.ts.easymask.networking;

public class UrlHandler {
    private static final String urlKey = "url";
    private static final String urlCodeKeyword = "code=";
    private static final String url = "https://vuo.elettra.eu/vuo/cgi-bin/easymask.py?";

    public static String getCodeFromUrl(String url){
        return url.substring(url.indexOf(urlCodeKeyword)+urlCodeKeyword.length(),url.length());
    }

    public static String getUrl() {
        return url;
    }

}
