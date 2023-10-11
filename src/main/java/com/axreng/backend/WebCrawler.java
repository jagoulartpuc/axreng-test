package com.axreng.backend;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class WebCrawler {

    public static void bfsCrawl(String wordToSearch) {
        String startUrl = Constants.BASE_URL;
        Queue<String> queue = new LinkedList<>();
        Set<String> visitedUrls = new HashSet<>();
        System.out.println("Search starting with base URL " + startUrl + " and keyword " + wordToSearch);
        queue.add(startUrl);
        int wordsFoundedInPages = 0;
        while (!queue.isEmpty()) {
            String currentUrl = queue.poll();

            if (!visitedUrls.contains(currentUrl) && PageSearchUtil.checkValidUrl(currentUrl)) {
                try {
                    String htmlPage = fetchHtmlPage(currentUrl);

                    if (PageSearchUtil.containsWordInPage(wordToSearch, htmlPage)) {
                        System.out.println("Result found: " + currentUrl);
                        wordsFoundedInPages++;
                    }

                    List<String> anchorUrls = PageSearchUtil.findAnchorsInPage(htmlPage);

                    for (String anchorUrl : anchorUrls) {
                        String absoluteUrl = makeAbsoluteUrl(startUrl, anchorUrl);
                        queue.add(absoluteUrl);
                    }

                    visitedUrls.add(currentUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Search finished with " + wordsFoundedInPages + " results found");
    }

    private static String fetchHtmlPage(String url) throws IOException {
        try {
            Document document = Jsoup.connect(url).get();
            return document.toString();
        } catch (HttpStatusException ignored) {}
        return "";
    }

    private static String makeAbsoluteUrl(String baseUrl, String relativeUrl) {
        if (relativeUrl.startsWith("http://") || relativeUrl.startsWith("https://")) {
            return relativeUrl;
        }
        return baseUrl + relativeUrl;
    }

    public static void main(String[] args) {
        String wordToSearch = "four";
        bfsCrawl(wordToSearch);
    }
}

