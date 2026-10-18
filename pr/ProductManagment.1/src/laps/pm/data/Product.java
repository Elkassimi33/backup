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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * {@code product} class represent properties and behavior of
 * product objects in the product management system.
 * <br>
 * each product has an id,nam and price
 * <br>
 * each product can have a discount,calculated base on a
 * {@ink DISCOUNT_RATE discount rate}
 * @version 4.0
 * @author elkay
 **/
public sealed class Product implements Rateable<Product> permits Drink,Food {
    private Rating rating;
    private int id;
    private String name;
    private BigDecimal price;
    /**
     * a constant that define a
     * {@link java.math.BigDecimal BigDecimal} value of discount rate
     * <br>
     * discount rate is 10%
     */
    public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);

    public Product(int id, String name, BigDecimal price, Rating rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }
    public Product(int id, String name, BigDecimal price) {
        this(id,name,price,Rating.NO_RATED);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Rating getRating() {
        return rating;
    }

    public  Product applayRating(Rating newRating){
        return new Product(getId(),getName(),getPrice(),newRating);
    }

    /**
     * assumed that the best before date is today
     * @return the current date
     */
    public LocalDate getBestBefor() {
        return LocalDate.now();
    }
    /**
     * calcule of the discount based on the product brice and
     * {link DISCOUNT_RATE discount rate}
     * @return a {@link java.math.BigDecimal BigDecimal}
     * value of the discount
     */
    public BigDecimal getDiscount() {
        return price.multiply(DISCOUNT_RATE).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String toString() {
        return   id +", " +rating.getStar() +
                " , '" + name + '\'' +
                " ," + price ;
    }

    @Override
    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
        if(!(o instanceof Product)) {

            Product product = (Product) o;
            return id == product.id ;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
