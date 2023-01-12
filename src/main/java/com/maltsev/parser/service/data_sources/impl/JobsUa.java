package com.maltsev.parser.service.data_sources.impl;

import lombok.Getter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

@Getter
public class JobsUa extends AbstractSite {

    private Set<String> vacanciesTitlesSet = new HashSet<>();
    private Set<String> vacanciesDescriptionsSet = new HashSet<>();
    private String siteLink = "https://jobs.ua/vacancy/it_web_specialists/page-";
    //13 pages
    private int pages = 10;
    private String siteName = "https://jobs.ua";

    @Override
    public Set<String> selectVacanciesTitles(String siteLink) throws IOException {
        System.out.println("Сбор вакансий с сайта " + siteName + " ...");
        for (int i = 1; i <= pages; i++) {
            Document doc = Jsoup.connect(siteLink + i).get();
            Elements vacanciesTitles = doc.getElementsByClass("b-vacancy__top__title js-item_title");
            try {
                vacanciesTitles.forEach(title -> {
                    vacanciesTitlesSet.add(title.text());
                });
            } catch (IndexOutOfBoundsException e) {
            }
        }
        return vacanciesTitlesSet;
    }

    @Override
    public Set<String> selectDescriptions(String siteLink) throws IOException, InterruptedException {
        System.out.println("Збір описів ваканасій з сайта " + siteName + " ...");
        int x = 0;
        for(int i = 1; i <= pages; i++) {
            Document linksDoc = Jsoup.connect(siteLink + i).get();
            Elements links = linksDoc.getElementsByClass("b-vacancy__top__title js-item_title");
            links.forEach(link ->{
                try {
                    Document vacancyDetails = Jsoup.connect(link.attr("href")).get();
                    vacanciesDescriptionsSet.add(vacancyDetails.getElementsByClass("b-vacancy-full__block b-text js-phone-replace").text());

                } catch (IOException e) {}
            });
            System.out.println(siteName + " " + (i) + " % loaded...");
        }
        return vacanciesDescriptionsSet;
    }


}

