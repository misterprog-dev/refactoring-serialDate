/* ========================================================================
 JCommon : bibliothèque libre de classes générales pour Java(tm)
 ========================================================================

 (C) Copyright 2000-2005, par Object Refinery Limited et les Contributeurs.

 Site du projet : http://www.jfree.org/jcommon/index.html

 Cette bibliothèque est un logiciel libre ; vous pouvez la redistribuer et/ou
* la modifier en respectant les termes de la GNU Lesser General Public License
* publiée par la Free Software Foundation, en version 2.1 ou (selon votre choix)
* en toute version ultérieure.
*
* Cette bibliothèque est distribuée en espérant qu’elle sera utile, mais SANS
* AUCUNE GARANTIE, sans même la garantie implicite de QUALITÉ MARCHANDE ou
* d’ADÉQUATION À UN OBJECTIF PRÉCIS. Pour de plus amples détails, consultez
* la GNU Lesser General Public License.
*
* Vous devez avoir reçu un exemplaire de la GNU Lesser General Public License
* avec cette bibliothèque. Dans le cas contraire, merci d’écrire à la Free
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA 02110-1301, USA.
*
* [Java est une marque ou une marque déposée de Sun Microsystems, Inc.
* aux États-Unis et dans d’autres pays.]
*
* --------------------
* SpreadsheetDate.java
* --------------------
* (C) Copyright 2001-2005, par Object Refinery Limited.
*
* Auteur : David Gilbert (pour Object Refinery Limited);
* Contributeur(s) : -;
*
* $Id: SpreadsheetDate.java,v 1.8 2005/11/03 09:25:39 mungady Exp $
*
* Modifications
* -------------
* 11-Oct-2001 : Version 1 (DG);
* 05-Nov-2001 : Ajout des méthodes getDescription() et setDescription() (DG);
* 12-Nov-2001 : Nom changé de ExcelDate.java en SpreadsheetDate.java (DG);
* Correction d’un bogue de calcul du jour, mois et année à partir
* du numéro de série (DG);408 Coder proprement
* 24-Jan-2002 : Correction d’un bogue de calcul du numéro de série à partir du jour,
* mois et année. Merci à Trevor Hills pour l’avoir signalé (DG);
* 29-Mai-2002 : Ajout de la méthode equals(Object) (SourceForge ID 558850) (DG);
* 03-Oct-2002 : Correction des erreurs signalées par Checkstyle (DG);
* 13-Mar-2003 : Implémentation de Serializable (DG);
* 04-Sep-2003 : Méthodes isInRange() terminées (DG);
* 05-Sep-2003 : Implémentation de Comparable (DG);
* 21-Oct-2003 : Ajout de la méthode hashCode() (DG);
*
*/

package com.dsoumaila.serialdate;

import java.util.Calendar;
import java.util.Date;

/**
 *  Représente une date à l’aide d’un entier, de manière comparable à l’implémentation
 *  dans Microsoft Excel. La plage des dates reconnues va du 1er janvier 1900 au
 *  31 décembre 9999.
 *  <P>
 *  Sachez qu’il existe un bogue délibéré dans Excel qui reconnaît l’année 1900
 *  comme une année bissextile alors que ce n’est pas le cas. Pour plus d’informations,
 *  consultez l’article Q181370 sur le site web de Microsoft Microsoft :
 *  <P>
 *  http://support.microsoft.com/support/kb/articles/Q181/3/70.asp
 *  <P>
 *  Excel emploie la convention 1er janvier 1900 = 1. Cette classe utilise la
 *  convention 1er janvier 1900 = 2.
 *  Par conséquent, le numéro de jour dans cette date sera différent de celui
 *  donné par Excel pour janvier et février 1900... mais Excel ajoute ensuite un jour
 *  supplémentaire (29 février 1900, qui n’existe pas réellement !) et, à partir de là,
 *  les numéros de jours concordent.
 *
 *  @author David Gilbert
 * */
public class SpreadsheetDate extends SerialDate {
    /**
     * Pour la sérialisation.
     */
    private static final long serialVersionUID = -2039586705374454461L;

    /**
     * Le numéro du jour (1er janvier 1900 = 2, 2 janvier 1900 = 3, ...,
     * 31 décembre 9999 = 2958465).
     */
    private int serial;

    /**
     * Le jour du mois (1 à 28, 29, 30 ou 31 selon le mois).
     */
    private int day;

    /**
     * Le mois de l’année (1 à 12).
     */
    private int month;

    /**
     * L’année (1900 à 9999).
     */
    private int year;
    /**
     * Description facultative de la date.
     */
    private String description;

    /**
     * Crée une nouvelle instance de date.
     *
     * @param day   le jour (dans la plage 1 à 28/29/30/31).
     * @param month le mois (dans la plage 1 à 12).
     * @param year  l’année (dans la plage 1900 à 9999).
     */
    public SpreadsheetDate(final int day, final int month, final int year) {
        if ((year >= 1900) && (year <= 9999)) {
            this.year = year;
        } else {
            throw new IllegalArgumentException("The 'year' argument must be in range 1900 to 9999.");
        }

        if ((month >= MonthConstants.JANUARY) && (month <= MonthConstants.DECEMBER)) {
            this.month = month;
        } else {
            throw new IllegalArgumentException("The 'month' argument must be in the range 1 to 12.");
        }

        if ((day >= 1) && (day <= SerialDate.lastDayOfMonth(month, year))) {
            this.day = day;
        } else {
            throw new IllegalArgumentException("Invalid 'day' argument.");
        }

        // Le numéro de série doit être synchronisé avec le jour-mois-année...
        this.serial = calcSerial(day, month, year);

        this.description = null;
    }

    /**
     * Constructeur standard - crée un nouvel objet date qui représente
     * le jour indiqué (dans la plage 2 à 2958465.
     *
     * @param serial le numéro de série du jour (plage : 2 à 2958465).
     */
    public SpreadsheetDate(final int serial) {
        if ((serial >= SERIAL_LOWER_BOUND) && (serial <= SERIAL_UPPER_BOUND)) {
            this.serial = serial;
        } else {
            throw new IllegalArgumentException("SpreadsheetDate: Serial must be in range 2 to 2958465.");
        }

        // Le jour-mois-année doit être synchronisé avec le numéro de série...
        calcDayMonthYear();
    }

    /**
     * Retourne la description associée à la date. La date n’est pas obligée
     * de posséder une description, mais, pour certaines applications, elle
     * est utile.
     *
     * @return la description associée à la date.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Fixe la description de la date.
     *
     * @param description la description de la date (<code>null</code>
     *                    est autorisé).
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Retourne le numéro série de la date, où le 1 janvier 1900 = 2 (cela
     * correspond, presque, au système de numérotation employé dans Microsoft
     * Excel pour Windows et Lotus 1-2-3).
     *
     * @return le numéro série de la date.
     */
    public int toSerial() {
        return this.serial;
    }

    /**
     * Retourne un <code>java.util.Date</code> équivalent à cette date.
     *
     * @return la date.
     */
    public Date toDate() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(getYYYY(), getMonth() - 1, getDayOfMonth(), 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * Retourne l’année (suppose une plage valide de 1900 à 9999).
     *
     * @return l’année.
     */
    public int getYYYY() {
        return this.year;
    }

    /**
     * Retourne le mois (janvier = 1, février = 2, mars = 3).
     *
     * @return le mois de l’année.
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Retourne le jour du mois.
     *
     * @return le jour du mois.
     */
    public int getDayOfMonth() {
        return this.day;
    }

    /**
     * Retourne un code représentant le jour de la semaine.
     * <p>
     * Les codes sont définis dans la classe {@link SerialDate} sous la forme :
     * <code>SUNDAY</code>, <code>MONDAY</code>, <code>TUESDAY</code>,
     * <code>WEDNESDAY</code>, <code>THURSDAY</code>, <code>FRIDAY</code> et
     * <code>SATURDAY</code>.
     *
     * @return un code représentant le jour de la semaine.
     */
    public int getDayOfWeek() {
        return (this.serial + 6) % 7 + 1;
    }

    /**
     * Teste l’égalité de cette date avec un objet quelconque.
     * <p>
     * Cette méthode retourne true UNIQUEMENT si l’objet est une instance de la classe
     * de base {@link SerialDate} et s’il représente le même jour que ce
     * {@link SpreadsheetDate}.
     *
     * @param object l’objet à comparer (<code>null</code> est autorisé).
     * @return un booléen.
     */
    public boolean equals(final Object object) {
        if (object instanceof SerialDate) {
            final SerialDate s = (SerialDate) object;
            return (s.toSerial() == this.toSerial());
        } else {
            return false;
        }
    }

    /**
     * Retourne un code de hachage pour cette instance.
     *
     * @return un code de hachage.
     */
    public int hashCode() {
        return toSerial();
    }

    /**
     * Retourne la différence (en jours) entre cette date et l’autre date
     * indiquée.
     *
     * @param other la date servant à la comparaison.
     * @return la différence (en jours) entre cette date et l’autre date
     * indiquée.
     */
    public int compare(final SerialDate other) {
        return this.serial - other.toSerial();
    }

    /**
     * Implémente la méthode requise par l’interface Comparable.
     *
     * @param other l’autre objet (en général un autre SerialDate).
     * @return un entier négatif, zéro ou un entier positif selon que cet objet
     * est inférieur à, égal à ou supérieur à l’objet indiqué.
     */
    public int compareTo(final Object other) {
        return compare((SerialDate) other);
    }

    /**
     * Retourne true si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     *
     * @param other la date servant à la comparaison.
     * @return <code>true</code> si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     */
    public boolean isOn(final SerialDate other) {
        return (this.serial == other.toSerial());
    }

    /**
     * Retourne true si ce SerialDate représente une date antérieure
     * au SerialDate indiqué.
     *
     * @param other la date servant à la comparaison.
     * @return <code>true</code> si ce SerialDate représente une date antérieure
     * au SerialDate indiqué.
     */

    public boolean isBefore(final SerialDate other) {
        return (this.serial < other.toSerial());
    }

    /**
     * Retourne true si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     *
     * @param other la date servant à la comparaison.
     * @return <code>true</code> si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     */
    public boolean isOnOrBefore(final SerialDate other) {
        return (this.serial <= other.toSerial());
    }

    /**
     * Retourne true si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     *
     * @param other la date servant à la comparaison.
     * @return <code>true</code> si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     */
    public boolean isAfter(final SerialDate other) {
        return (this.serial > other.toSerial());
    }

    /**
     * Retourne true si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     *
     * @param other la date servant à la comparaison.
     * @return <code>true</code> si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     */
    public boolean isOnOrAfter(final SerialDate other) {
        return (this.serial >= other.toSerial());
    }

    /**
     * Retourne <code>true</code> si ce {@link SerialDate} se trouve dans
     * la plage indiquée (INCLUSIF). L’ordre des dates d1 et d2 n’est pas
     * important.
     *
     * @param d1 une date limite de la plage.
     * @param d2 l’autre date limite de la plage.
     * @return un booléen.
     */
    public boolean isInRange(final SerialDate d1, final SerialDate d2) {
        return isInRange(d1, d2, SerialDate.INCLUDE_BOTH);
    }

    /**
     * Retourne true si ce {@link SerialDate} se trouve dans la plage
     * indiquée (l’appelant précise si les extrémités sont incluses)
     * L’ordre des dates d1 et d2 n’est pas important.
     *
     * @param d1      une date limite de la plage.
     * @param d2      l’autre date limite de la plage.
     * @param include code qui indique si les dates de début et de fin
     *                sont incluses dans la plage.
     * @return <code>true</code> si ce SerialDate se trouve dans la plage
     * indiquée.
     */
    public boolean isInRange(final SerialDate d1, final SerialDate d2, final int include) {
        final int s1 = d1.toSerial();
        final int s2 = d2.toSerial();
        final int start = Math.min(s1, s2);
        final int end = Math.max(s1, s2);

        final int s = toSerial();
        if (include == SerialDate.INCLUDE_BOTH) {
            return (s >= start && s <= end);
        } else if (include == SerialDate.INCLUDE_FIRST) {
            return (s >= start && s < end);
        } else if (include == SerialDate.INCLUDE_SECOND) {
            return (s > start && s <= end);
        } else {
            return (s > start && s < end);
        }
    }

    /**
     * Calcule le numéro de série pour le jour, le mois et l’année.
     * <p>
     * 1er janvier 1900 = 2.
     *
     * @param d le jour.
     * @param m le mois.
     * @param y l’année.
     * @return le numéro de série pour le jour, le mois et l’année.
     */
    private int calcSerial(final int d, final int m, final int y) {
        final int yy = ((y - 1900) * 365) + SerialDate.leapYearCount(y - 1);
        int mm = SerialDate.AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH[m];
        if (m > MonthConstants.FEBRUARY) {
            if (SerialDate.isLeapYear(y)) {
                mm = mm + 1;
            }
        }
        final int dd = d;
        return yy + mm + dd + 1;
    }

    /**
     * Calcule le jour, le mois et l’année pour le numéro de série.
     */
    private void calcDayMonthYear() {
        // Obtenir l’année à partir de la date.
        final int days = this.serial - SERIAL_LOWER_BOUND;
        // Surestimée car nous ignorons les années bissextiles.
        final int overestimatedYYYY = 1900 + (days / 365);
        final int leaps = SerialDate.leapYearCount(overestimatedYYYY);
        final int nonleapdays = days - leaps;
        // Sous-estimée car nous surestimons les années.
        int underestimatedYYYY = 1900 + (nonleapdays / 365);

        if (underestimatedYYYY == overestimatedYYYY) {
            this.year = underestimatedYYYY;
        } else {
            int ss1 = calcSerial(1, 1, underestimatedYYYY);
            while (ss1 <= this.serial) {
                underestimatedYYYY = underestimatedYYYY + 1;
                ss1 = calcSerial(1, 1, underestimatedYYYY);
            }
            this.year = underestimatedYYYY - 1;
        }

        final int ss2 = calcSerial(1, 1, this.year);

        int[] daysToEndOfPrecedingMonth = AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH;

        if (isLeapYear(this.year)) {
            daysToEndOfPrecedingMonth = LEAP_YEAR_AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH;
        }

        // Obtenir le mois à partir de la date.
        int mm = 1;
        int sss = ss2 + daysToEndOfPrecedingMonth[mm] - 1;
        while (sss < this.serial) {
            mm = mm + 1;
            sss = ss2 + daysToEndOfPrecedingMonth[mm] - 1;
        }
        this.month = mm - 1;

        // Il reste d(+1);
        this.day = this.serial - ss2 - daysToEndOfPrecedingMonth[this.month] + 1;
    }
}
