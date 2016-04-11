package com.amedia.qa.automation.pageobjects.google;

import org.openqa.selenium.By;

/**
 * Created by voravuthboonchai on 4/11/2016 AD.
 */
public class ObjectsETC {

    //Ostiarius
    public static By googleUsername = By.id("username");
    public static By firstName = By.id("firstName");
    public static By lastName = By.id("lastName");
    public static By email = By.id("email");
    public static By description = By.id("description");
    public static By primaryPublication = By.name("primaryPublication");
    public static By multidesk = By.xpath("//div[contains(@class, 'checkbox')]//input[@type='checkbox']");
    public static By cancelButton = By.xpath("//button[contains(@class, 'btn-cancel')]");

    //Transition
    public static By clientName = By.id("clientName");
    public static By url = By.name("url");
    public static By parameter = By.name("parameter");
    public static By minute = By.name("minute");
    public static By month = By.name("month");
    public static By weekday = By.name("weekday");
    public static By minute_Choose = By.id("minute_chooser_choose");
    public static By hour_Every = By.id("hour_chooser_every");
    public static By day_Every = By.id("day_chooser_every");
    public static By month_Choose = By.id("month_chooser_choose");
    public static By weekday_Choose = By.id("weekday_chooser_choose");
}

