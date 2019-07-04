package org.a3;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;


public class RebootTplink {

/*    private static String url = "http://192.168.1.1";           // URL of the TPLink router admin page
    private static String password = "***REMOVED***";                  // password for authentication
    private static    // using Chrome driver*/
    private static String url = "";           // URL of the TPLink router admin page
    private static String password = "";



    public static void main(String[] args) throws InterruptedException, IOException {

        File configFile = new File("config.properties");
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            url = props.getProperty("url");
            password = props.getProperty("password");

            System.out.println("url is: " + url);
            System.out.println("pass is: " + password);
            reader.close();
        } catch (FileNotFoundException ex) {
            // file does not exist
            System.out.println("The file config.propeties doesn't exist! Please add it next to JAR file.");
            System.exit(0);
        } catch (IOException ex) {
            // I/O error
        }




        InetAddress address = InetAddress.getByName("www.google.com"); // get the IP of the host
        if (address.isReachable(10000)) { // check if the address is Reachable

            System.out.println(address + " is reachable!");
            System.exit(1); // exit the program and don't restart the router

        } else {

            ChromeDriver driver = new ChromeDriver();
            // open the URL
            driver.get(url);

            // Find password textbox
            WebElement element=driver.findElement(By.id("pc-login-password"));
            element.sendKeys(password);
            Thread.sleep(1000);         // wait 1 sec

            // click on login button
            WebElement button=driver.findElement(By.id("pc-login-btn"));
            button.click();
            Thread.sleep(4000);         // wait 4 sec

            // confirm login
            try {
                // Check the presence of alert
                WebElement confirmYes=driver.findElement(By.id("confirm-yes"));
                confirmYes.click();


            } catch (NoSuchElementException ex) {
                // Alert not present
                ex.printStackTrace();
            }
            Thread.sleep(4000);

            // click on reboot button in upper right
            WebElement reboot=driver.findElement(By.id("topReboot"));
            reboot.click();
            Thread.sleep(4000);

            // click on yes button to confirm reboot
            WebElement confirm=driver.findElement(By.cssSelector("button.button-button.green.pure-button.btn-msg." +
                    "btn-msg-ok.btn-confirm"));
            confirm.click();
            Thread.sleep(2000);
            driver.quit();

        }

    }




}
