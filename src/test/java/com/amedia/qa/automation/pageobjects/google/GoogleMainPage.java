package com.amedia.qa.automation.pageobjects.google;

import org.openqa.selenium.By;

/**
 * Created by voravuthboonchai on 3/29/2016 AD.
 */
public class GoogleMainPage {

    public static By searchBox = By.id("lst-ib");
    public static By searchButton = By.name("btnG");

    //ABC page
    public static By abcLogo = By.xpath("//img[@class='sticky-logo']");

    //ABC Contact Us page
    public static By name = By.name("contact_name");
    public static By email = By.name("contact_email");
    public static By subject = By.name("contact_subject");
    public static By message = By.name("contact_comment");
    public static By emailLink = By.xpath("//div[contains(@class, 'one-third')]//p//a[text()]");
    public static By phone = By.xpath("//div[contains(@class, 'one-third')]//div[2]");
    public static By howToReach = By.xpath("//div[@class='be-custom-column-inner']/p[2]/strong");
    public static By bts = By.xpath("//div[@class='be-custom-column-inner']/p[3]");

    //Amedia page
    public static By ceoName = By.xpath("//div[@class='column']/div[1]/h5");

}

