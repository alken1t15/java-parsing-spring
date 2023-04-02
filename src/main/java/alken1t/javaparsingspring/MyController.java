package alken1t.javaparsingspring;

import alken1t.javaparsingspring.entity.Product;
import alken1t.javaparsingspring.repository.ProductRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class MyController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path = "/store")
    public String store() {
        updateProduct();
        return "store_page";
    }


    //    @GetMapping(path = "/product")
    public void updateProduct() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            System.out.println("Парсинг страницы...");
            parsingShowWhiteFly("fdfd");
        };

        // Задержка перед началом выполнения задачи
        long initialDelay = 0;
        // Интервал выполнения задачи (12 часов)
        long period = 12;

        // Назначить задачу на выполнение с указанным интервалом
        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.HOURS);
    }

    private void parsingShowWhiteFly(String url) {
        try {
            for (int j = 0; j < 14; j++) {
                Document document = null;
                if (j == 0) {
                    document = Jsoup.connect("https://shop.kz/noutbuki/filter/almaty-is-v_nalichii-or-ojidaem-or-dostavim/apply/").header("Content-Type", "text/html; charset=utf-8").get();
                } else if (j >= 2) {
                    document = Jsoup.connect("https://shop.kz/noutbuki/filter/almaty-is-v_nalichii-or-ojidaem-or-dostavim/apply/?PAGEN_1=" + j).header("Content-Type", "text/html; charset=utf-8").get();
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
                        if (productNew != null && productNew.getPrice() != priceNorma && productNew.getStoreName().equals("Белый ветер") && productNew.getName().equals(name) && productNew.getLinkProduct().equals(link)) {
                            Product product = new Product("Белый ветер", name, link, priceNorma, LocalDate.now());
                            productRepository.save(product);
                        }
                        //     System.out.println(name.text());
                        //     System.out.println(link.attr("href"));
                        //     System.out.println(price.substring(element.text().indexOf("Цена в интернет-магазине") + 24, price.indexOf(price.charAt(price.length() - 1), element.text().indexOf("Цена в интернет-магазине") + 24)));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}