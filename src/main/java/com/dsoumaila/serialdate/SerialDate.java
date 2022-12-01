/*
======================================================================== JCommon : bibliothèque libre de classes générales pour Java(tm) ========================================================================
(C) Copyright 2000-2005, par Object Refinery Limited et les Contributeurs.
Site du projet : http://www.jfree.org/jcommon/index.html
Cette bibliothèque est un logiciel libre ; vous pouvez la redistribuer et/ou la modifier en respectant les termes de la GNU Lesser General Public License publiée par la Free Software Foundation, en version 2.1 ou (selon votre choix) en toute version ultérieure.
Cette bibliothèque est distribuée en espérant qu’elle sera utile, mais SANS AUCUNE GARANTIE, sans même la garantie implicite de QUALITÉ MARCHANDE ou d’ADÉQUATION À UN OBJECTIF PRÉCIS. Pour de plus amples détails, consultez la GNU Lesser General Public License.
Vous devez avoir reçu un exemplaire de la GNU Lesser General Public License avec cette bibliothèque. Dans le cas contraire, merci d’écrire à la Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
MA 02110-1301, USA.
[Java est une marque ou une marque déposée de Sun Microsystems, Inc. aux États-Unis et dans d’autres pays.]
---------------
SerialDate.java
---------------
(C) Copyright 2001-2005, par Object Refinery Limited.
Auteur : David Gilbert (pour Object Refinery Limited); Contributeur(s) : -;
$Id: SerialDate.java,v 1.7 2005/11/03 09:25:17 mungady Exp $

Modifications (depuis le 11-Oct-2001) -------------------------------------
11-Oct-2001 : 05-Nov-2001 : 12-Nov-2001 :
05-Déc-2001 : 29-Mai-2002 :
27-Aou-2002 : 03-Oct-2002 : 13-Mar-2003 : 29-Mai-2003 : 04-Sep-2003 : 05-Jan-2005 :
Réorganisation de la classe et son déplacement dans le nouveau paquetage com.jrefinery.date (DG).
Ajout de la méthode getDescription() et suppression de la classe NotableDate (DG).
IBD a besoin d’une méthode setDescription(), à présent que
la classe NotableDate a disparu (DG). Modification de getPreviousDayOfWeek(), getFollowingDayOfWeek() et getNearestDayOfWeek() pour corriger les bogues (DG). Correction du bogue dans la classe SpreadsheetDate (DG). Déplacement des constantes de mois dans une interface séparée (MonthConstants) (DG).
Correction du bogue dans addMonths(), merci à Nalevka Petr (DG). Corrections des erreurs signalées par Checkstyle (DG). Implémentation de Serializable (DG).
Correction du bogue dans la méthode addMonths() (DG).
Implémentation de Comparable. Mise à jour Javadoc de isInRange (DG). Correction du bogue dans la méthode addYears() (1096282) (DG).
*/

package com.dsoumaila.serialdate;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/*
Classe abstraite qui définit nos exigences quant à la manipulation des dates, sans lien avec une implémentation précise.
<P>
Exigence 1 : concorder au moins avec la gestion des dates par Excel ; Exigence 2 : classe immuable ;
<P>
Pourquoi ne pas employer java.util.Date ? Nous le ferons lorsque ce sera sensé. Parfois, java.util.Date est *trop* précise - elle représente un instant, au 1/1000me de seconde (la date dépend elle-même du fuseau horaire). Il arrive
que nous voulions simplement représenter un jour particulier (par exemple, le 21 janvier 2015) sans nous préoccuper de l’heure, du fuseau horaire ou d’autres paramètres. C’est pourquoi nous avons défini SerialDate.
<P>
Vous pouvez invoquer getInstance() pour obtenir une sous-classe concrète de SerialDate, sans vous inquiéter de l’implémentation réelle.
@author David Gilbert
*/

public abstract class SerialDate implements Comparable, Serializable, MonthConstants {
    /**
     * Pour la sérialisation.
     */
    private static final long serialVersionUID = -293716040467423637L;
    /**
     * Symboles du format de date.
     */
    public static final DateFormatSymbols DATE_FORMAT_SYMBOLS = new SimpleDateFormat().getDateFormatSymbols();
    /**
     * Numéro de série pour le 1 janvier 1900.
     */
    public static final int SERIAL_LOWER_BOUND = 2;

    /**
     * Numéro de série pour le 31 décembre 9999.
     */
    public static final int SERIAL_UPPER_BOUND = 2958465;

    /**
     * La première année reconnue par ce format de date.
     */
    public static final int MINIMUM_YEAR_SUPPORTED = 1900;

    /**
     * La dernière année reconnue par ce format de date.
     */
    public static final int MAXIMUM_YEAR_SUPPORTED = 9999;

    /**
     * Constante utile pour Monday (lundi). Équivaut à java.util.Calendar.MONDAY.
     */
    public static final int MONDAY = Calendar.MONDAY;

    /**
     * Constante utile pour Tuesday (mardi). Équivaut à java.util.Calendar.TUESDAY.
     */
    public static final int TUESDAY = Calendar.TUESDAY;

    /**
     * Constante utile pour Wednesday (mercredi). Équivaut à
     * java.util.Calendar.WEDNESDAY.
     */
    public static final int WEDNESDAY = Calendar.WEDNESDAY;

    /**
     * Constante utile pour Thursday (jeudi). Équivaut à java.util.Calendar.THURSDAY.
     */
    public static final int THURSDAY = Calendar.THURSDAY;

    /**
     * Constante utile pour Friday (vendredi). Équivaut à java.util.Calendar.FRIDAY.
     */
    public static final int FRIDAY = Calendar.FRIDAY;

    /**
     * Constante utile pour Saturday (samedi). Équivaut à java.util.Calendar.SATURDAY.
     */
    public static final int SATURDAY = Calendar.SATURDAY;

    /**
     * Constante utile pour Sunday (dimanche). Équivaut à java.util.Calendar.SUNDAY.
     */
    public static final int SUNDAY = Calendar.SUNDAY;

    /**
     * Nombre de jours dans chaque mois, pour les années non bissextiles.
     */
    static final int[] LAST_DAY_OF_MONTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * Nombre de jours dans une année (non bissextile) jusqu’à la fin de chaque mois.
     */
    static final int[] AGGREGATE_DAYS_TO_END_OF_MONTH = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};

    /**
     * Nombre de jours dans une année jusqu’à la fin du mois précédent.
     */
    static final int[] AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH = {0, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};

    /**
     * Nombre de jours dans une année bissextile jusqu’à la fin de chaque mois.
     */
    static final int[] LEAP_YEAR_AGGREGATE_DAYS_TO_END_OF_MONTH = {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366};

    public static final int[] LEAP_YEAR_AGGREGATE_DAYS_TO_END_OF_PRECEDING_MONTH = { 0, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366 };

    /**
     * Constante utile pour désigner la première semaine d’un mois.
     */
    public static final int FIRST_WEEK_IN_MONTH = 1;

    /**
     * Constante utile pour désigner la deuxième semaine d’un mois.
     */
    public static final int SECOND_WEEK_IN_MONTH = 2;

    /**
     * Constante utile pour désigner la troisième semaine d’un mois.
     */
    public static final int THIRD_WEEK_IN_MONTH = 3;

    /**
     * Constante utile pour désigner la quatrième semaine d’un mois.
     */
    public static final int FOURTH_WEEK_IN_MONTH = 4;

    /**
     * Constante utile pour désigner la dernière semaine d’un mois.
     */
    public static final int LAST_WEEK_IN_MONTH = 0;

    /**
     * Constante utile pour un intervalle.
     */
    public static final int INCLUDE_NONE = 0;

    /**
     * Constante utile pour un intervalle.
     */
    public static final int INCLUDE_FIRST = 1;

    /**
     * Constante utile pour un intervalle.
     */
    public static final int INCLUDE_SECOND = 2;

    /**
     * Constante utile pour un intervalle.
     */
    public static final int INCLUDE_BOTH = 3;

    /**
     * Constante utile pour désigner un jour de la semaine relativement à
     * une date fixée.
     */
    public static final int PRECEDING = -1;

    /**
     * Constante utile pour désigner un jour de la semaine relativement à
     * une date fixée.
     */
    public static final int NEAREST = 0;

    /**
     * Constante utile pour désigner un jour de la semaine relativement à
     * une date fixée.
     */
    public static final int FOLLOWING = 1;

    /**
     * Description de la date.
     */
    private String description;

    /**
     * Constructeur par défaut.
     */
    protected SerialDate() {

    }

    /**
     * Retourne <code>true</code> si le code entier indiqué représente un jour
     * de la semaine valide, sinon <code>false</code>.
     *
     * @param code le code dont la validité est testée.
     * @return <code>true</code> si le code entier indiqué représente un jour
     * de la semaine valide, sinon <code>false</code>.
     */
    public static boolean isValidWeekdayCode(final int code) {
        switch (code) {
            case SUNDAY:
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
            case SATURDAY:
                return true;
            default:
                return false;
        }
    }

    /**
     * Convertit la chaîne indiquée en un jour de la semaine.
     *
     * @param s une chaîne représentant le jour de la semaine.
     * @return <code>-1</code> si la chaîne ne peut pas être convertie, sinon le
     * jour de la semaine.
     */
    public static int stringToWeekdayCode(String s) {
        final String[] shortWeekdayNames = DATE_FORMAT_SYMBOLS.getShortWeekdays();
        final String[] weekDayNames = DATE_FORMAT_SYMBOLS.getWeekdays();

        int result = -1;
        s = s.trim();
        for (int i = 0; i < weekDayNames.length; i++) {
            if (s.equals(shortWeekdayNames[i])) {
                result = i;
                break;
            }
            if (s.equals(weekDayNames[i])) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * Retourne une chaîne représentant le jour de la semaine indiqué.
     * <p>
     * Il faut trouver une meilleure solution.
     *
     * @param weekday le jour de la semaine.
     * @return une chaîne représentant le jour de la semaine indiqué.
     */
    public static String weekdayCodeToString(final int weekday) {
        final String[] weekdays = DATE_FORMAT_SYMBOLS.getWeekdays();
        return weekdays[weekday];
    }

    /**
     * Retourne un tableau des noms de mois.
     *
     * @return un tableau des noms de mois.
     */
    public static String[] getMonths() {
        return getMonths(false);
    }

    /**
     * Retourne un tableau des noms de mois.
     *
     * @param shortened indicateur indiquant que des noms de mois abrégés
     *                  doivent être retournés.
     * @return un tableau des noms de mois.
     */
    public static String[] getMonths(final boolean shortened) {
        if (shortened) {
            return DATE_FORMAT_SYMBOLS.getShortMonths();
        } else {
            return DATE_FORMAT_SYMBOLS.getMonths();
        }
    }

    /**
     * Retourne true si le code entier indiqué représente un mois valide.
     *
     * @param code le code dont la validité est testée.
     *             * @return <code>true</code> si le code entier indiqué représente un
     *             mois valide.
     */
    public static boolean isValidMonthCode(final int code) {
        switch (code) {
            case JANUARY:
            case FEBRUARY:
            case MARCH:
            case APRIL:
            case MAY:
            case JUNE:
            case JULY:
            case AUGUST:
            case SEPTEMBER:
            case OCTOBER:
            case NOVEMBER:
            case DECEMBER:
                return true;
            default:
                return false;
        }
    }

    /**
     * Retourne le trimestre du mois indiqué.
     *
     * @param code le code du mois (1-12).
     * @return le trimestre auquel appartient le mois.
     * @throws java.lang.IllegalArgumentException
     */
    public static int monthCodeToQuarter(final int code) {
        switch (code) {
            case JANUARY:
            case FEBRUARY:
            case MARCH:
                return 1;
            case APRIL:
            case MAY:
            case JUNE:
                return 2;
            case JULY:
            case AUGUST:
            case SEPTEMBER:
                return 3;
            case OCTOBER:
            case NOVEMBER:
            case DECEMBER:
                return 4;
            default:
                throw new IllegalArgumentException("SerialDate.monthCodeToQuarter: invalid month code.");
        }
    }

    /**
     * Retourne une chaîne représentant le mois indiqué.
     * <P>* La chaîne retournée correspond à la forme longue du nom du mois
     * pour les paramètres régionaux par défaut.
     *
     * @param month le mois.
     * @return une chaîne représentant le mois indiqué.
     */
    public static String monthCodeToString(final int month) {
        return monthCodeToString(month, false);
    }

    /**
     * Retourne une chaîne représentant le mois indiqué.
     * <p>
     * La chaîne retournée correspond à la forme longue ou courte du nom
     * du mois pour les paramètres régionaux par défaut.
     *
     * @param month     le mois.
     * @param shortened si <code>true</code>, retourne le nom abrégé
     *                  du mois.
     * @return une chaîne représentant le mois indiqué.
     * @throws java.lang.IllegalArgumentException
     */
    public static String monthCodeToString(final int month, final boolean shortened) {
        // Vérifier les arguments...
        if (!isValidMonthCode(month)) {
            throw new IllegalArgumentException("SerialDate.monthCodeToString: month outside valid range.");
        }

        final String[] months;

        if (shortened) {
            months = DATE_FORMAT_SYMBOLS.getShortMonths();

        } else {
            months = DATE_FORMAT_SYMBOLS.getMonths();
        }
        return months[month - 1];
    }

    /**
     * Convertit une chaîne en un code de mois.
     * <p>
     * Cette méthode retourne l’une des constantes JANUARY, FEBRUARY, ...,
     * DECEMBER qui correspond à la chaîne. Si la chaîne n’est pas reconnue
     * cette méthode retourne -1.
     *
     * @param s la chaîne à analyser.
     *          * @return <code>-1</code> si la chaîne ne peut pas être analysée, sinon
     *          le mois de l’année.
     */
    public static int stringToMonthCode(String s) {
        final String[] shortMonthNames = DATE_FORMAT_SYMBOLS.getShortMonths();
        final String[] monthNames = DATE_FORMAT_SYMBOLS.getMonths();

        int result = -1;
        s = s.trim();

        // Commencer par convertir la chaîne en un entier (1-12)...
        try {
            result = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            // Supprimé.
        }

        // Rechercher ensuite parmi les noms de mois...
        if ((result < 1) || (result > 12)) {
            for (int i = 0; i < monthNames.length; i++) {
                if (s.equals(shortMonthNames[i])) {
                    result = i + 1;
                    break;

                }
                if (s.equals(monthNames[i])) {
                    result = i + 1;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Retourne true si le code entier indiqué représente une semaine du mois valide,
     * sinon false.
     *
     * @param code le code dont la validité est testée.
     * @return <code>true</code> si le code entier indiqué représente une semaine
     * du mois valide.
     */
    public static boolean isValidWeekInMonthCode(final int code) {
        switch (code) {
            case FIRST_WEEK_IN_MONTH:
            case SECOND_WEEK_IN_MONTH:
            case THIRD_WEEK_IN_MONTH:
            case FOURTH_WEEK_IN_MONTH:
            case LAST_WEEK_IN_MONTH:
                return true;
            default:
                return false;
        }
    }

    /**
     * Détermine si l’année indiquée est une année bissextile.
     *
     * @param yyyy l’année (dans la plage 1900 à 9999).
     * @return <code>true</code> si l’année indiquée est une année bissextile.
     */
    public static boolean isLeapYear(final int yyyy) {
        if ((yyyy % 4) != 0) {
            return false;
        } else if ((yyyy % 400) == 0) {
            return true;
        } else if ((yyyy % 100) == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Retourne le nombre d’années bissextiles entre 1900 et l’année indiquée
     * COMPRISE.
     * <p>
     * Notez que 1900 n’est pas une année bissextile.
     *
     * @param yyyy l’année (dans la plage 1900 à 9999).
     * @return le nombre d’années bissextiles entre 1900 et l’année indiquée.
     */
    public static int leapYearCount(final int yyyy) {
        final int leap4 = (yyyy - 1896) / 4;
        final int leap100 = (yyyy - 1800) / 100;
        final int leap400 = (yyyy - 1600) / 400;
        return leap4 - leap100 + leap400;
    }

    /**
     * Retourne le numéro du dernier jour du mois, en tenant compte des années
     * bissextiles.
     *
     * @param month le mois.
     * @param yyyy  l’année (dans la plage 1900 à 9999).
     * @return le numéro du dernier jour du mois.
     */
    public static int lastDayOfMonth(final int month, final int yyyy) {
        final int result = LAST_DAY_OF_MONTH[month];
        if (month != FEBRUARY) {
            return result;
        } else if (isLeapYear(yyyy)) {
            return result + 1;
        } else {
            return result;
        }
    }

    /**
     * Crée une nouvelle date en ajoutant le nombre de jours indiqué à
     * la date de base.
     *
     * @param days le nombre de jours à ajouter (peut être négatif).
     * @param base la date de base.
     * @return une nouvelle date.
     */
    public static SerialDate addDays(final int days, final SerialDate base) {
        final int serialDayNumber = base.toSerial() + days;
        return SerialDate.createInstance(serialDayNumber);
    }

    /**
     * Crée une nouvelle date en ajoutant le nombre de mois indiqué à
     * la date de base.
     * <p>
     * Si la date de base est proche de la fin du mois, le jour du résultat
     * peut être ajusté : 31 mai + 1 mois = 30 juin.
     *
     * @param months le nombre de mois à ajouter (peut être négatif).
     * @param base   la date de base.
     * @return une nouvelle date.
     */
    public static SerialDate addMonths(final int months, final SerialDate base) {
        final int yy = (12 * base.getYYYY() + base.getMonth() + months - 1) / 12;
        final int mm = (12 * base.getYYYY() + base.getMonth() + months - 1) % 12 + 1;
        final int dd = Math.min(base.getDayOfMonth(), SerialDate.lastDayOfMonth(mm, yy));
        return SerialDate.createInstance(dd, mm, yy);
    }

    /**
     * Crée une nouvelle date en ajoutant le nombre d’années indiqué à
     * la date de base.
     * 384
     *
     * @param years le nombre d’années à ajouter (peut être négatif).
     * @param base  la date de base.
     * @return une nouvelle date.
     */
    public static SerialDate addYears(final int years, final SerialDate base) {
        final int baseY = base.getYYYY();
        final int baseM = base.getMonth();
        final int baseD = base.getDayOfMonth();

        final int targetY = baseY + years;
        final int targetD = Math.min(baseD, SerialDate.lastDayOfMonth(baseM, targetY));

        return SerialDate.createInstance(targetD, baseM, targetY);
    }

    /**
     * Retourne la dernière date qui tombe le jour de la semaine indiqué
     * AVANT la date de base.
     *
     * @param targetWeekday code du jour de la semaine cible.
     * @param base          la date de base.
     * @return la dernière date qui tombe le jour de la semaine indiqué
     * AVANT la date de base.
     */
    public static SerialDate getPreviousDayOfWeek(final int targetWeekday, final SerialDate base) {
        // Vérifier les arguments...
        if (!SerialDate.isValidWeekdayCode(targetWeekday)) {
            throw new IllegalArgumentException("Invalid day-of-the-week code.");
        }

        // Rechercher la date...
        final int adjust;
        final int baseDOW = base.getDayOfWeek();
        if (baseDOW > targetWeekday) {
            adjust = Math.min(0, targetWeekday - baseDOW);
        } else {
            adjust = -7 + Math.max(0, targetWeekday - baseDOW);
        }

        return SerialDate.addDays(adjust, base);
    }

    /**
     * Retourne la première date qui tombe le jour de la semaine indiqué
     * APRÈS la date de base.Annexe B org.jfree.date.SerialDate 385
     *
     * @param targetWeekday code du jour de la semaine cible.
     * @param base          la date de base.
     * @return la première date qui tombe le jour de la semaine indiqué
     * APRÈS la date de base.
     */
    public static SerialDate getFollowingDayOfWeek(final int targetWeekday, final SerialDate base) {
        // Vérifier les arguments...
        if (!SerialDate.isValidWeekdayCode(targetWeekday)) {
            throw new IllegalArgumentException("Invalid day-of-the-week code.");
        }
        // Rechercher la date...
        final int adjust;
        final int baseDOW = base.getDayOfWeek();
        if (baseDOW > targetWeekday) {
            adjust = 7 + Math.min(0, targetWeekday - baseDOW);
        } else {
            adjust = Math.max(0, targetWeekday - baseDOW);

        }
        return SerialDate.addDays(adjust, base);
    }

    /**
     * Retourne la date qui tombe le jour de la semaine indiqué
     * PROCHE de la date de base.
     *
     * @param targetDOW code du jour de la semaine cible.
     * @param base      la date de base.
     * @return la date qui tombe le jour de la semaine indiqué
     * PROCHE de la date de base.
     */
    public static SerialDate getNearestDayOfWeek(final int targetDOW, final SerialDate base) {
        // Vérifier les arguments...
        if (!SerialDate.isValidWeekdayCode(targetDOW)) {
            throw new IllegalArgumentException("Invalid day-of-the-week code.");
        }

        // Rechercher la date...
        final int baseDOW = base.getDayOfWeek();
        int adjust = -Math.abs(targetDOW - baseDOW);
        if (adjust >= 4) {
            adjust = 7 - adjust;
        }
        if (adjust <= -4) {
            adjust = 7 + adjust;
        }
        return SerialDate.addDays(adjust, base);
    }

    /**
     * Avance la date jusqu’au dernier jour du mois.
     *
     * @param base la date de base.
     * @return une nouvelle date.
     */
    public SerialDate getEndOfCurrentMonth(final SerialDate base) {
        final int last = SerialDate.lastDayOfMonth(base.getMonth(), base.getYYYY());
        return SerialDate.createInstance(last, base.getMonth(), base.getYYYY());
    }

    /**
     * Retourne une chaîne qui correspond au code de la semaine du mois.
     * <p>
     * Il faut trouver une meilleure solution.
     *
     * @param count code entier représentant la semaine du mois.
     * @return une chaîne qui correspond au code de la semaine du mois.
     */
    public static String weekInMonthToString(final int count) {
        switch (count) {
            case SerialDate.FIRST_WEEK_IN_MONTH:
                return "First";
            case SerialDate.SECOND_WEEK_IN_MONTH:
                return "Second";
            case SerialDate.THIRD_WEEK_IN_MONTH:
                return "Third";
            case SerialDate.FOURTH_WEEK_IN_MONTH:
                return "Fourth";
            case SerialDate.LAST_WEEK_IN_MONTH:
                return "Last";
            default:
                return "SerialDate.weekInMonthToString(): invalid code.";
        }
    }

    /**
     * Retourne une chaîne représentant la notion 'relative' indiquée.
     * <p>
     * Il faut trouver une meilleure solution.
     *
     * @param relative constante représantant la notion 'relative'.
     * @return une chaîne représentant la notion 'relative' indiquée.
     */
    public static String relativeToString(final int relative) {
        switch (relative) {
            case SerialDate.PRECEDING:
                return "Preceding";
            case SerialDate.NEAREST:
                return "Nearest";
            case SerialDate.FOLLOWING:
                return "Following";
            default:
                return "ERROR : Relative To String";
        }
    }

    /**
     * Méthode de fabrique qui retourne une instance d’une certaine sous-classe concrète
     * de {@link SerialDate}.
     *
     * @param day   le jour (1-31).
     * @param month le mois (1-12).
     * @param yyyy  l’année (dans la plage 1900 à 9999).
     * @return une instance de {@link SerialDate}.
     */
    public static SerialDate createInstance(final int day, final int month, final int yyyy) {
        return new SpreadsheetDate(day, month, yyyy);
    }

    /**
     * Méthode de fabrique qui retourne une instance d’une certaine sous-classe concrète
     * de {@link SerialDate}.
     *
     * @param serial numéro de série du jour (1 janvier 1900 = 2).
     * @return une instance de SerialDate.
     */
    public static SerialDate createInstance(final int serial) {
        return new SpreadsheetDate(serial);
    }

    /**
     * Méthode de fabrique qui retourne une instance d’une sous-classe de SerialDate.
     *
     * @param date objet date de Java.
     * @return une instance de SerialDate.
     */
    public static SerialDate createInstance(final java.util.Date date) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return new SpreadsheetDate(calendar.get(Calendar.DATE),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));
    }

    /**
     * Retourne le numéro série de la date, où le 1 janvier 1900 = 2 (cela
     * correspond, presque, au système de numérotation employé dans Microsoft
     * Excel pour Windows et Lotus 1-2-3).
     *
     * @return le numéro série de la date.
     */
    public abstract int toSerial();

    /**
     * Retourne un java.util.Date. Puisque java.util.Date est plus précis que
     * SerialDate, nous devons définir une convention pour 'l’heure du jour'.
     *
     * @return this sous forme de <code>java.util.Date</code>.
     */
    public abstract java.util.Date toDate();

    /**
     * Retourne une description de la date.
     *
     * @return une description de la date.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Fixe la description de la date.
     *
     * @param description la nouvelle description de la date.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Convertit la date en une chaîne de caractères.
     *
     * @return une représentation de la date sous forme de chaîne.
     */
    public String toString() {
        return getDayOfMonth() + "-" + SerialDate.monthCodeToString(getMonth()) + "-" + getYYYY();
    }

    /**
     * Retourne l’année (suppose une plage valide de 1900 à 9999).
     *
     * @return l’année.
     */
    public abstract int getYYYY();

    /**
     * Retourne le mois (janvier = 1, février = 2, mars = 3).
     *
     * @return le mois de l’année.
     */
    public abstract int getMonth();

    /**
     * Retourne le jour du mois.
     *
     * @return le jour du mois.
     */
    public abstract int getDayOfMonth();

    /**
     * Retourne le jour de la semaine.
     *
     * @return le jour de la semaine.
     */
    public abstract int getDayOfWeek();

    /**
     * Retourne la différence (en jours) entre cette date et l’autre date
     * indiquée.
     * <p>
     * Le résultat est positif si cette date se trouve après l’autre date,
     * et négatif si elle est avant l’autre date.
     *
     * @param other la date servant à la comparaison.
     * @return la différence entre cette date et l’autre date.
     */
    public abstract int compare(SerialDate other);

    /**
     * Retourne true si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     *
     * @param other la date servant à la comparaison.
     * @return <code>true</code> si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     */
    public abstract boolean isOn(SerialDate other);

    /**
     * Retourne true si ce SerialDate représente une date antérieure au
     * SerialDate indiqué.
     *
     * @param other la date servant à la comparaison.
     * @return <code>true</code> si ce SerialDate représente une date antérieure
     * au SerialDate indiqué.
     */
    public abstract boolean isBefore(SerialDate other);

    /**
     * Retourne true si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     *
     * @param other la date servant à la comparaison.
     * @return <code>true<code> si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     */
    public abstract boolean isOnOrBefore(SerialDate other);

    /**
     * Retourne true si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     * 390
     *
     * @param other la date servant à la comparaison.
     * @return <code>true</code> si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     */
    public abstract boolean isAfter(SerialDate other);

    /**
     * Retourne true si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     *
     * @param other la date servant à la comparaison.
     * @return <code>true</code> si ce SerialDate représente la même date que
     * le SerialDate indiqué.
     */
    public abstract boolean isOnOrAfter(SerialDate other);

    /**
     * Retourne <code>true</code> si ce {@link SerialDate} se trouve dans
     * la plage indiquée (INCLUSIF). L’ordre des dates d1 et d2 n’est pas
     * important.
     *
     * @param d1 une date limite de la plage.
     * @param d2 l’autre date limite de la plage.
     * @return un booléen.
     */
    public abstract boolean isInRange(SerialDate d1, SerialDate d2);

    /**
     * Retourne <code>true</code> si ce {@link SerialDate} se trouve dans
     * la plage indiquée (l’appelant précise si les extrémités sont
     * incluses). L’ordre des dates d1 et d2 n’est pas important.
     *
     * @param d1      une date limite de la plage.
     * @param d2      l’autre date limite de la plage.
     * @param include code qui indique si les dates de début et de fin
     *                sont incluses dans la plage.
     * @return un booléen.
     */
    public abstract boolean isInRange(SerialDate d1, SerialDate d2, int include);

    /**
     * Retourne la dernière date qui tombe le jour de la semaine indiqué
     * AVANT cette date.
     *
     * @param targetDOW code pour le jour de la semaine cible.
     * @return la dernière date qui tombe le jour de la semaine indiqué
     * AVANT cette date.
     */
    public SerialDate getPreviousDayOfWeek(final int targetDOW) {
        return getPreviousDayOfWeek(targetDOW, this);
    }

    /**
     * Retourne la première date qui tombe le jour de la semaine indiqué
     * APRÈS cette date.
     *
     * @param targetDOW code pour le jour de la semaine cible.
     * @return la première date qui tombe le jour de la semaine indiqué
     * APRÈS cette date.
     */
    public SerialDate getFollowingDayOfWeek(final int targetDOW) {
        return getFollowingDayOfWeek(targetDOW, this);
    }

    /**
     * Retourne la date la plus proche qui tombe le jour de la semaine indiqué.
     *
     * @param targetDOW code pour le jour de la semaine cible.
     * @return la date la plus proche qui tombe le jour de la semaine indiqué.
     */
    public SerialDate getNearestDayOfWeek(final int targetDOW) {
        return getNearestDayOfWeek(targetDOW, this);
    }
}








