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

package laps.pm.app.app;

import laps.pm.app.data.Food;
import laps.pm.app.data.Product;
import laps.pm.app.data.ProductManager;
import laps.pm.app.data.Rating;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

/**
 * {@code Shop } class represents an application that manage products
 * @version 4.0
 * @author elkay
 */

public class Shop {
    public static void main(String[] args){
        ProductManager pm=new ProductManager("en-GB");

        Product p1= pm.createProduct(101,"coffee",BigDecimal.valueOf(1.99),Rating.NO_RATED);

        Product p2= pm.createProduct(102,"Tee",BigDecimal.valueOf(0.99),Rating.NO_RATED);

        p1=pm.reviewProduct(101,Rating.FOUR_STAR,"nice cop of coffee");
        pm.reviewProduct(45,Rating.TWO_STAR,"not good");
        p1=pm.reviewProduct(101,Rating.THREE_STAR," less sugar");
        p1=pm.reviewProduct(101,Rating.FIVE_STAR,"good coffee");
        p1=pm.reviewProduct(101,Rating.NO_RATED,"where is the milk");

        pm.changeLocale("en-US");

        p2=pm.reviewProduct(p2,Rating.FOUR_STAR,"nice cop of Tee");
        p2=pm.reviewProduct(p2,Rating.FIVE_STAR,"a good cup of Tee");
        p2=pm.reviewProduct(p2,Rating.THREE_STAR," less sugar");
        p2=pm.reviewProduct(p2,Rating.FIVE_STAR,"perfect");
        p2=pm.reviewProduct(p2,Rating.TWO_STAR,"not good");

        pm.printProductReport(107);
        pm.printProductReport(p2);
        pm.getDiscounts().forEach((rating,discount)-> System.out.println(rating+"\t"+discount));
    //Product p4= pm.createProduct(101, "coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR,LocalDate.now());
//    System.out.println(p.toString().equals(p1.toString()));
//    System.out.println(p.hashCode());
//    System.out.println(p);

    }
}
