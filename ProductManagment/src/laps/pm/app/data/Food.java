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

package laps.pm.app.data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author elkay
 **/
public final class Food extends Product {



    private BigDecimal discount;
    private LocalDate bestBefore;

     Food(int id, String name, BigDecimal price, Rating rating , LocalDate bestBefore) {
        super(id, name, price, rating);
        //this.discount = discount;
        this.bestBefore = bestBefore;

    }

    public LocalDate getBestBefor() {
        return bestBefore;
    }

    public void setBestBefore(LocalDate bestBefore) {
        this.bestBefore = bestBefore;
    }

    @Override
    public String toString() {
        String s = super.toString() +
                ", " + discount +
                ", " + bestBefore;
        return s;
    }

    @Override
    public BigDecimal getDiscount() {
        return bestBefore.isEqual(LocalDate.now())? super.getDiscount(): BigDecimal.ZERO;
    }



   // @Override
//    public Product applyRating(Rating newRating) {
//        return new Food(this.getId(), this.getName(), this.getPrice(), newRating);
//    }
}