package org.a3;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class RebootTplink {


    private static String routerURL = "";               // URL of the TPLink router admin page
    private static String password = "";                // password to access the router admin page
    private static String checkForInet = "";            // Should you check for Internet connection? 1 for yes
    private static String hostToPing = "";              // which host to ping to check for Internet connection



    private static void readConfigFile() {
        // read the content of config.properties file and assign the values to variables.
        File configFile = new File("config.properties");
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            routerURL = props.getProperty("routerURL");
            password = props.getProperty("password");
            hostToPing = props.getProperty(("hostToPing"));
            checkForInet = props.getProperty("checkForInet");

            System.out.println("routerURL is: " + routerURL);
//            System.out.println("pass is: " + password);
            System.out.println("host to ping is: " + hostToPing);
            System.out.println("Check for internet?: " + checkForInet);     // 1 means yes, everything else no
            reader.close();
        } catch (FileNotFoundException ex) {
            // file does not exist
            System.out.println("The file config.propeties doesn't exist! Please add it next to JAR file.");
            System.exit(0);
        } catch (IOException ex) {
            // I/O error
        }
    }

    private static boolean checkInternet(String hostToPing) throws IOException {

        try {
            InetAddress address = InetAddress.getByName(hostToPing); // get the IP of the host
            address.isReachable(10000);
            System.out.println(address + " is reachable!");
            return true;

        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }
        return false;

    }



    private static void restartRouter(String routerURL, String password) throws InterruptedException {
        ChromeDriver driver = new ChromeDriver();
        // open the URL
        driver.get(routerURL);

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


    public static void main(String[] args) throws InterruptedException, IOException {

        // Enabling file logging functionality
        Logger logger = Logger.getLogger(RebootTplink.class.getName()); // g
        // Create an instance of FileHandler that write log to a file called
        // reboots.log. Each new message will be appended at the at of the log file.
        FileHandler fileHandler = new FileHandler("reboots.log", true);
        SimpleFormatter formatterTxt = new SimpleFormatter();   // using SimpleFormatter
        fileHandler.setFormatter(formatterTxt);                 // to log less text in file and not to be XML
        logger.addHandler(fileHandler);


        // read all values from config file
        readConfigFile();

        // verifying if config file says to check for Internet connection
        if (checkForInet.equals("1")) {
            if (checkInternet(hostToPing)) {
                // There is a Internet connection
                // Log a message to file
                if (logger.isLoggable(Level.INFO)) {
                    logger.info("No Router Reboot needed - there is Internet connection");
                }
                System.exit(0); // exit the program
            } else {
                // Log a message to file
                if (logger.isLoggable(Level.WARNING)) {
                    logger.warning("Router Reboot - Internet connection check fail!");
                }
                // No Internet -> reatart the router
                restartRouter(routerURL, password);
            }

        } else {
            // Check for Internet is skipped -> restart the router
            restartRouter(routerURL, password);
            // Log a message to file
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Router Reboot - Didn't check for Internet connection!");
            }
        }






    }




}
