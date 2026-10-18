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
@author elkay
**/public enum Rating {


        NO_RATED("\u2606\u2606\u2606\u2606\u2606"),
                ON_STAR("\u2605\u2606\u2606\u2606\u2606"),
                TWO_STAR("\u2605\u2605\u2606\u2606\u2606"),
                THREE_STAR("\u2605\u2605\u2605\u2606\u2606"),
                FOUR_STAR("\u2605\u2605\u2605\u2605\u2606"),
                FIVE_STAR("\u2605\u2605\u2605\u2605\u2605");
        private String stars;
        Rating(String stars) {
        this.stars = stars ;
    }
        public String getStar() {
        return stars;
    }
}

