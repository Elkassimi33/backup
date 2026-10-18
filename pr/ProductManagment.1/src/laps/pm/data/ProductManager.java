/*
 * Copyright(c) SoriginalComment .match("Copyright \(c\) (\d+)",1,
 * "-") 2025
 *
 * ‘This program is free software: you can redistribute it and/or
 * modify it under the terms of the GM General Public License as
 * Published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * atch ("Copyright \(c\) (\d4)", 1,
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GW General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * ‘http? //wne.gnu.org/licenses/>.
 *
 */

package laps.pm.data;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.time.format.FormatStyle;
import java.util.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

/**
 * @author elkay
 **/
public class ProductManager {

    private Product product;
    private Review review;
    private Map<Product,List<Review>> products = new HashMap<>();
    private ResourceFormatter formatter;

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());

    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating , LocalDate bestBefore){
        product = new Food(id,name,price,rating,bestBefore);
        products.putIfAbsent(product,new ArrayList<>());
        return product;
    }
    public Product createProduct(int id, String name, BigDecimal price, Rating rating ){
        product = new Drink(id,name,price,rating);
        products.putIfAbsent(product,new ArrayList<>());
        return product;
    }
    public Product reviewProduct(Product product,Rating rating,String comment ){

        List<Review> reviews = products.get(product);
        products.remove(product,reviews);
        reviews.add(new Review(rating,comment));

        int sum_Reviews=0;
        for(Review review : reviews){
            sum_Reviews+=review.getRating().ordinal();
        }
        product = product.applayRating(Rateable.convert(Math.round((float)sum_Reviews/reviews.size())));
        products.put(product,reviews);
        return product;
    }
    public void printProductReport(Product product){
        StringBuilder text = new StringBuilder();
        text.append(formatter.formatProduct(product));
        text.append('\n');
        List<Review> reviews = products.get(product);
/*      Collections.sort(reviews);
        for(Review review : reviews) {

            text.append(formatter.formatReview(review));
            text.append('\n');
        }*/
        reviews.stream().sorted().forEach(review->{ text.append(formatter.formatReview(review));
            text.append('\n');
        });
        if(reviews.isEmpty()){
            text.append(formatter.getText("no.reviews"));
            text.append('\n');
        }
        System.out.println(text);
    }
    public void printProducts(Comparator<Product> sorter){
        List<Product> productList = new ArrayList<>(products.keySet());
        //Collections.sort(productList,sorter);
        StringBuilder txt = new StringBuilder();
/*         for(Product product : productList){
              txt.append(formatter.formatProduct(product));
              txt.append('\n');
           }*/
        productList.stream().sorted(sorter).forEach(product->{
            txt.append(formatter.formatProduct(product));
            txt.append('\n');
            });
        System.out.println(txt);

    }

    public Product findProduct(int id){
        Optional<Product> result = null;
/*
        for(Product product : products.keySet()) {
            if (product.getId() == id) {
                result = product;
                break;
            }
        }
*/
        result= products.keySet().stream().filter(product->product.getId() == id).findFirst();
        return result.orElse(null);

    }

    public Product reviewProduct(int id,Rating rating,String comment ){
        return reviewProduct(findProduct(id),rating,comment);
    }
    public void printProductReview(int id ){
        printProductReport(findProduct(id));
    }

    private static class ResourceFormatter{

        private Locale locale;
        private ResourceBundle resources;
        private DateTimeFormatter dateFormatte;
        private NumberFormat moneyFormat;

        private ResourceFormatter(Locale locale) {
            this.locale = locale;
            resources = ResourceBundle.getBundle("laps.pm.data.resources", locale);
            dateFormatte = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);

        }

        private String formatProduct(Product product){
            String type= (product instanceof Food)?"Food":"Drink";
            return MessageFormat.format(resources.getString("products"),
                    product.getName(),
                    moneyFormat.format(product.getPrice()),
                    product.getRating().getStar(),
                    dateFormatte.format(product.getBestBefor()), type
            );

        }

        private String formatReview(Review review){
            return MessageFormat.format(resources.getString("reviews"),
                    review.getRating().getStar(),
                    review.getComment()
            );

        }

        private String getKey(String key){
            return resources.getString(key);
        }

        private String getText(String key){
            return resources.getString(key);

        }


    }

    public static Map<String,ResourceFormatter> formatters =
            Map.of( "en-GB",new ResourceFormatter(Locale.UK),
                    "en-US",new ResourceFormatter(Locale.US),
                    "es-Us",new ResourceFormatter(new Locale("es","US")),
                    "fr-FR",new ResourceFormatter(Locale.FRANCE),
                    "zh-CN",new ResourceFormatter(Locale.CHINA));

    public  void changeLocale(String languageTag){
        formatter=formatters.getOrDefault(languageTag,formatters.get("en-GB"));
    }

    public static Set<String> getSupportedLocals(){
        return formatters.keySet();
    }
    public ProductManager (String languageTag){
        changeLocale(languageTag);
    }

}
