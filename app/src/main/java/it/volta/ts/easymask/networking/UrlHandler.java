package it.volta.ts.easymask.networking;

public class UrlHandler {
    private static final String urlKey = "url";
    private static String url;
    private static String baseUrl;
    private static String imgCode;
    private static String urlCodeKeyword = "code";
    private static String splitCharBaseURL= "\\?";
    private static String splitCharFormValues= "&";
    private static String formKey= "=";

    public static String getCodeFromUrl(String url){
        return url.substring(url.indexOf(urlCodeKeyword)+urlCodeKeyword.length(),url.length());
    }

    public static String getUrl() {
        return url;
    }
    public static String getUrlKey() {
        return urlKey;
    }

    public static int setUrl(String url) {
        UrlHandler.url = url;
        try{
            getDataFromURL(url);
            return 0;
        }catch (ArrayIndexOutOfBoundsException e){
            return -1;
        }

    }

    private static void getDataFromURL(String url){
        String urlSplit[] = url.split(splitCharBaseURL);
        baseUrl = urlSplit[0];
        String fromValues[] = urlSplit[1].split(splitCharFormValues);
        for(String value:fromValues) {
            if(value.split(formKey)[0].equals(urlCodeKeyword)) {
                imgCode = value.split(formKey)[1];
            }
        }
    }

    public static String getImgCode() {
        return imgCode;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }
}
