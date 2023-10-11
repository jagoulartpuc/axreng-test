package com.axreng.backend;

import org.jsoup.Jsoup;

import java.util.List;
import java.util.stream.Collectors;

public class PageSearchUtil {

    public static boolean containsWordInPage(String wordToSearch, String htmlPage) {
        return Jsoup.parse(htmlPage).text().contains(wordToSearch);
    }

    public static List<String> findAnchorsInPage(String htmlPage) {
        return Jsoup.parse(htmlPage)
                .select("a")
                .stream()
                .map(link -> link.attr("href"))
                .collect(Collectors.toList());
    }

    public static boolean checkValidUrl(String currentUrl) {
        return currentUrl.startsWith(Constants.BASE_URL) && !(currentUrl.contains("/..")) && !(currentUrl.contains("mailto"));
    }

}