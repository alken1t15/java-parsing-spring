package alken1t.javaparsingspring;

import alken1t.javaparsingspring.entity.Product;
import alken1t.javaparsingspring.entity.Shop;
import alken1t.javaparsingspring.repository.ProductRepository;
import alken1t.javaparsingspring.repository.ShopRepository;
import org.apache.catalina.Store;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class MyController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepository shopRepository;

    @GetMapping(path = "/store")
    public String store(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products",products);
        return "store_page";
    }


        @PostMapping(path = "/product")
    public void updateProduct() {
        //    parsingFromShopDNS("DNS","Ноутбуки","https://www.dns-shop.kz/catalog/17a892f816404e77/noutbuki/");
            parsingFromShopMechta("Мечта","Ноутбуки","https://www.mechta.kz/section/noutbuki/?setcity=s1","noutbu");
//        List<Shop> shops = shopRepository.findAllBy();
//        for(Shop shop : shops){
//            parsingShowWhiteFly(shop.getStoreName(),shop.getCategory(),shop.getLinkPage());
//        }
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//        Runnable task = () -> {
//            System.out.println("Парсинг страницы...");
//            parsingShowWhiteFly("fdfd");
//        };
//
//        // Задержка перед началом выполнения задачи
//        long initialDelay = 0;
//        // Интервал выполнения задачи (12 часов)
//        long period = 12;
//
//        // Назначить задачу на выполнение с указанным интервалом
//        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.HOURS);
    }

    private void parsingShowWhiteFly(String store, String category, String linkPage) {
        System.setProperty("webdriver.chrome.driver", "E:\\DriverSpring\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        try {
            Document document  = Jsoup.connect(linkPage).header("Content-Type", "text/html; charset=utf-8").get();
            Elements page = document.getElementsByClass("bx-pagination-container row");
            Integer countPage = Integer.valueOf(page.text().substring(page.text().length()-2).trim());
            for (int j = 0; j < countPage; j++) {
                if (j >= 2) {
                    document = Jsoup.connect(linkPage+"?PAGEN_1=" + j).header("Content-Type", "text/html; charset=utf-8").get();
                }
                if (document != null) {
                    // Извлечение всех элементов с определенным классом
                    Elements prices = document.getElementsByClass("bx_price");
                    Elements names = document.getElementsByClass("bx_catalog_item_title_text");
                    Elements links = document.getElementsByClass("bx_catalog_item_title").select("a[href]");
                    // Вывод найденных элементов с определенным классом
                    for (int i = 0; i < prices.size(); i++) {
                        String price = prices.get(i).text();
                        String name = names.get(i).text();
                        String link = "https://shop.kz" + links.get(i).attr("href");
                        String[] priceUpdate = price.substring(price.indexOf("Цена в интернет-магазине") + 24, price.indexOf(price.charAt(price.length() - 1), price.indexOf("Цена в интернет-магазине") + 24)).trim().split(" ");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String str : priceUpdate) {
                            stringBuilder.append(str);
                        }
                        Integer priceNorma = Integer.parseInt(String.valueOf(stringBuilder));
                        Product productNew = productRepository.findByLinkProduct(link);
                        driver.get(link);
                        String img = driver.findElement(By.className("lazyloaded")).getAttribute("src");
                        if (productNew != null && productNew.getPrice() != priceNorma) {
                            Product product = new Product(store,category, name, link, priceNorma, LocalDate.now(),img);
                            productRepository.save(product);
                        }
                        else if(productNew == null){
                            Product product = new Product(store,category, name, link, priceNorma, LocalDate.now(),img);
                            productRepository.save(product);
                        }
                        //     System.out.println(name.text());
                        //     System.out.println(link.attr("href"));
                        //     System.out.println(price.substring(element.text().indexOf("Цена в интернет-магазине") + 24, price.indexOf(price.charAt(price.length() - 1), element.text().indexOf("Цена в интернет-магазине") + 24)));
                    }
                }
            }
            driver.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void parsingFromShopDNS(String store, String category, String linkPage){
        System.setProperty("webdriver.chrome.driver","E:\\DriverSpring\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        driver.get(linkPage);

        List<WebElement> numberPage = driver.findElements(By.className("pagination-widget__page"));
        int countPage = Integer.parseInt(numberPage.get(numberPage.size() - 1).getAttribute("data-page-number"));
        for (int j = 1; j <= countPage; j++) {
            if (j != 1) {
                driver.get(linkPage + "?p=" + j);
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            List<WebElement> prices = driver.findElements(By.className("product-buy__price"));
            List<WebElement> nameProducts = driver.findElements(By.className("catalog-product__name"));
            List<WebElement> linkProducts = driver.findElements(By.className("catalog-product__image-link"));
            List<WebElement> imgs = driver.findElements(By.tagName("source"));
            int a = 0;
            for (int i = 0; i < prices.size(); i++) {
                String priceString = prices.get(i).getText().replace(" ","");
                int price = Integer.parseInt(priceString.substring(0,priceString.length()-1));
                String name = nameProducts.get(i).getText();
                String link = linkProducts.get(i).getAttribute("href");
                String img = imgs.get(a).getAttribute("data-srcset");
                Product product = productRepository.findByLinkProduct(link);
                a+=2;
                if (product == null) {
                    productRepository.save(new Product(store, category, name, link, price, LocalDate.now(),img));
                } else if (product.getPrice() != price) {
                    productRepository.save(new Product(store, category, name, link, price, LocalDate.now(),img));
                }
            }
        }
        driver.close();
    }

    public void parsingFromShopMechta(String store, String category, String linkPage,String categoryEng){
        System.setProperty("webdriver.chrome.driver","E:\\DriverSpring\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        driver.get(linkPage);

        List<WebElement> numberPage = driver.findElements(By.className("block"));
        int countPage = Integer.parseInt(numberPage.get(numberPage.size() - 2).getText());
        for (int j = 1; j <= countPage; j++) {
            if (j != 1) {
                driver.get(linkPage + "&page=" + j);
            }

            List<WebElement> prices =  driver.findElements(By.className("text-bold"));
            List<WebElement> nameProducts = driver.findElements(By.className("q-pt-md"));
            List<WebElement> linkProducts2 = driver.findElements(By.cssSelector("[style*='text-decoration: none;']"));
            List<WebElement> imgs = driver.findElements(By.cssSelector("[style*='margin-top: 20px; max-width: 260px; max-height: 260px;']"));
            List<String> linkProducts = new ArrayList<>();
            for (WebElement link : linkProducts2){
                if(link.getAttribute("href") != null){
                    if(link.getAttribute("href").contains("/product/"+categoryEng)){
                        linkProducts.add(link.getAttribute("href"));
                    }
                }
            }
            for (int i = 0; i < prices.size(); i++) {
                String priceString = prices.get(i).getText().replace(" ","");
                int price = Integer.parseInt(priceString.substring(0,priceString.length()-1));
                String name = nameProducts.get(i).getText();
                String link = linkProducts.get(i);
                String img = imgs.get(i).getAttribute("src");
                Product product = productRepository.findByLinkProduct(link);
                if (product == null) {
                    productRepository.save(new Product(store, category, name, link, price, LocalDate.now(),img));
                } else if (product.getPrice() != price) {
                    productRepository.save(new Product(store, category, name, link, price, LocalDate.now(),img));
                }
            }
        }
        driver.close();
    }
}