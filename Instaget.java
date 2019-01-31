import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Instaget {
    private WebDriver driver;

    public Instaget(boolean debug)
    {
        if (debug) {
            this.driver = new FirefoxDriver();
        } else {
            FirefoxBinary bin = new FirefoxBinary();
            bin.addCommandLineOptions("--headless");
            FirefoxOptions opts = new FirefoxOptions();
            opts.setBinary(bin);
            this.driver = new FirefoxDriver(opts);
        }
    }
    public Instaget()
    {
        this(false);
    }

    public List<String> getImageUrls(String url)
    {
        List<WebElement> images = getImages(url);
        ArrayList<String> urls = null;
        if (images != null) {
            urls = new ArrayList<String>(images.size());
            for (WebElement image : images) {
                if (image != null) {
                    urls.add(getImageUrl(image));
                }
            }
        }
        return urls;
    }

    private List<WebElement> getImages(String url)
    {
        try {
            driver.get(url);
        } catch (RuntimeException e) {
            return null;
        }
        WebDriverWait wait = new WebDriverWait(driver, 1);
        List<WebElement> imagesContainer = null;
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("_97aPb")));
            imagesContainer = driver.findElements(By.className("_-1_m6"));
        } catch (org.openqa.selenium.NoSuchElementException e) {
        } catch (org.openqa.selenium.TimeoutException e) {
        }
        LinkedList<WebElement> images = new LinkedList<WebElement>();
        if (imagesContainer != null && imagesContainer.size() > 1) {
            for (int i = 0; i < imagesContainer.size(); i++) {
                WebElement image = null;
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.className("FFVAD")));
                    image = imagesContainer.get(i).findElement(By.className("FFVAD"));
                } catch (org.openqa.selenium.NoSuchElementException e) {
                    return null;
                }
                images.add(image);
                if (i != imagesContainer.size() - 1) {
                    WebElement rightElem = null;
                    try {
                        rightElem = driver.findElement(By.className("_6CZji"));
                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        return null;
                    }
                    rightElem.click();
                }
            }
        } else {
            WebElement image = null;
            try {
                image = driver.findElement(By.className("FFVAD"));
            } catch (org.openqa.selenium.NoSuchElementException e) {
                return null;
            }
            images.add(image);
        }
        if (images.isEmpty()) {
            return null;
        }
        return images;
    }

    private String getImageUrl(WebElement image)
    {
        String srcsetAttr = image.getAttribute("srcset");
        if (srcsetAttr == null) {
            return null;
        }
        String[] srcset = image.getAttribute("srcset").split(",");
        if (srcset == null || srcset.length < 1) {
            return null;
        }
        return srcset[srcset.length - 1].split(" ")[0];
    }

    public void close()
    {
        driver.close();
    }
}
