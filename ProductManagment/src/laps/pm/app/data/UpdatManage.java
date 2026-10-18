//package laps.pm.app.data;
//
//import java.text.MessageFormat;
//import java.time.LocalDate;
//import java.math.BigDecimal;
//import java.time.format.FormatStyle;
//import java.util.Locale;
//import java.util.ResourceBundle;
//import java.text.NumberFormat;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Set;
//
///**
// * @author elkay
// */
//public class ProductManager {
//
//    private Product product;
//    private Map<Product, List<Review>> products = new HashMap<>();
//    private ResourceFormatter formatter;
//
//    public ProductManager(Locale locale) {
//        this(locale.toLanguageTag());
//    }
//
//    public ProductManager(String languageTag) {
//        changeLocale(languageTag);
//    }
//
//    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
//        product = new Food(id, name, price, rating, bestBefore);
//        products.putIfAbsent(product, new ArrayList<>());
//        return product;
//    }
//
//    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
//        product = new Drink(id, name, price, rating);
//        products.putIfAbsent(product, new ArrayList<>());
//        return product;
//    }
//
//    public Product reviewProduct(Product product, Rating rating, String comment) {
//        List<Review> reviews = products.get(product);
//        if (reviews == null) {
//            throw new IllegalArgumentException("Product not found.");
//        }
//        reviews.add(new Review(rating, comment));
//
//        int sumReviews = 0;
//        for (Review review : reviews) {
//            sumReviews += review.getRating().ordinal();
//        }
//        product = product.applyRating(Rateable.convert(Math.round((float) sumReviews / reviews.size())));
//        products.put(product, reviews);
//        return product;
//    }
//
//    public void printProductReview(Product product) {
//        if (product == null) {
//            System.out.println("Product not found.");
//            return;
//        }
//        StringBuilder text = new StringBuilder();
//        text.append(formatter.formatProduct(product)).append('\n');
//        List<Review> reviews = products.get(product);
//        if (reviews == null || reviews.isEmpty()) {
//            text.append(formatter.getText("no.reviews")).append('\n');
//        } else {
//            Collections.sort(reviews);
//            for (Review review : reviews) {
//                text.append(formatter.formatReview(review)).append('\n');
//            }
//        }
//        System.out.println(text);
//    }
//
//    public Product findProduct(int id) {
//        for (Product product : products.keySet()) {
//            if (product.getId() == id) {
//                return product;
//            }
//        }
//        return null;
//    }
//
//    public Product reviewProduct(int id, Rating rating, String comment) {
//        Product product = findProduct(id);
//        if (product == null) {
//            throw new IllegalArgumentException("Product with ID " + id + " not found.");
//        }
//        return reviewProduct(product, rating, comment);
//    }
//
//    public void printProductReview(int id) {
//        Product product = findProduct(id);
//        printProductReview(product);
//    }
//
//    private static class ResourceFormatter {
//
//        private Locale locale;
//        private ResourceBundle resources;
//        private DateTimeFormatter dateFormatter;
//        private NumberFormat moneyFormat;
//
//        public ResourceFormatter(Locale locale) {
//            this.locale = locale;
//            resources = ResourceBundle.getBundle("laps.pm.app.data.resources", locale);
//            dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
//            moneyFormat = NumberFormat.getCurrencyInstance(locale);
//        }
//
//        private String formatProduct(Product product) {
//            return MessageFormat.format(resources.getString("products"),
//                    product.getName(),
//                    moneyFormat.format(product.getPrice()),
//                    product.getRating().getStars(),
//                    dateFormatter.format(product.getBestBefore()));
//        }
//
//        private String formatReview(Review review) {
//            return MessageFormat.format(resources.getString("reviews"),
//                    review.getRating().getStars(),
//                    review.getComment());
//        }
//
//        private String getText(String key) {
//            return resources.getString(key);
//        }
//
//        public static Map<String, ResourceFormatter> formatters =
//                Map.of("en-GB", new ResourceFormatter(Locale.UK),
//                        "en-US", new ResourceFormatter(Locale.US),
//                        "es-US", new ResourceFormatter(new Locale("es", "US")),
//                        "fr-FR", new ResourceFormatter(Locale.FRANCE),
//                        "zh-CN", new ResourceFormatter(Locale.CHINA));
//
//    }
//
//    private void changeLocale(String languageTag) {
//        formatter = ResourceFormatter.formatters.getOrDefault(languageTag, ResourceFormatter.formatters.get("en-GB"));
//    }
//
//    public static Set<String> getSupportedLocales() {
//        return ResourceFormatter.formatters.keySet();
//    }
//}
