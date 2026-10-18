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

/**
 * @author elkay
 **/
public class Review implements Comparable<Review> {
    private Rating rating;
    private String comment;
    public Review(Rating rating,String comment){
        this.rating = rating;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Review{" +
                "rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }

    public String getComment() {
        return comment;
    }

    public Rating getRating() {
        return rating;
    }

    @Override
    public int compareTo(Review other){
        return other.getRating().ordinal() - this.getRating().ordinal();
    }

}
