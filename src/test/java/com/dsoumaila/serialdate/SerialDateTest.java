/* ========================================================================
 * JCommon : bibliothèque libre de classes générales pour Java(tm)
 * ========================================================================
 *
 * (C) Copyright 2000-2005, par Object Refinery Limited et les Contributeurs.
 *
 * Site du projet : http://www.jfree.org/jcommon/index.html
 *
 * Cette bibliothèque est un logiciel libre ; vous pouvez la redistribuer et/ou
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
 *392 Coder proprement
 * --------------------
 * SerialDateTests.java
 * --------------------
 * (C) Copyright 2001-2005, par Object Refinery Limited.
 *
 * Auteur : David Gilbert (pour Object Refinery Limited);
 * Contributeur(s) : -;
 *
 * $Id: SerialDateTests.java,v 1.6 2005/11/16 15:58:40 taqua Exp $
 *
 * Modifications
 * -------------
 * 15-Nov-2001 : Version 1 (DG);
 * 25-Jui-2002 : Suppression des importations inutiles (DG);
 * 24-Oct-2002 : Correction des erreurs signalées par Checkstyle (DG);
 * 13-Mar-2003 : Ajout du test de sérialisation (DG);
 * 05-Jan-2005 : Ajout du test pour le bogue 1096282 (DG);
 *
 */
package com.dsoumaila.serialdate;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.jupiter.api.Test;

import java.io.*;

public class SerialDateTest {
    /**
     * Quelques tests JUnit pour la classe {@link SerialDate}.
     */
    public class SerialDateTests extends TestCase {

    /**
     * Date représentant le 9 novembre.
     */
    private SerialDate nov9Y2001;

    /**
     * Crée un nouveau cas de test.
     *
     * @param name le nom.
     */
    public SerialDateTests(final String name) {
        super(name);
    }

    /**
     * Retourne une suite de tests pour l’exécuteur de tests JUnit.
     *
     * @return la suite de tests.Annexe B org.jfree.date.SerialDate 393
     */
    public static Test suite() {
        return (Test) new TestSuite(SerialDateTests.class);
    }

    /**
     * Configure le problème.
     */
    protected void setUp() {
        this.nov9Y2001 = SerialDate.createInstance(9, MonthConstants.NOVEMBER, 2001);
    }

    /**
     * 9 Nov 2001 plus deux mois doit donner 9 Jan 2002.
     */
    public void testAddMonthsTo9Nov2001() {
        final SerialDate jan9Y2002 = SerialDate.addMonths(2, this.nov9Y2001);
        final SerialDate answer = SerialDate.createInstance(9, 1, 2002);
        assertEquals(answer, jan9Y2002);
    }

    /**
     * Cas de test pour un bogue signalé, à présent corrigé.
     */
    public void testAddMonthsTo5Oct2003() {
        final SerialDate d1 = SerialDate.createInstance(5, MonthConstants.OCTOBER, 2003);
        final SerialDate d2 = SerialDate.addMonths(2, d1);
        assertEquals(d2, SerialDate.createInstance(5, MonthConstants.DECEMBER, 2003));
    }

    /**
     * Cas de test pour un bogue signalé, à présent corrigé.
     */
    public void testAddMonthsTo1Jan2003() {
        final SerialDate d1 = SerialDate.createInstance(1, MonthConstants.JANUARY, 2003);
        final SerialDate d2 = SerialDate.addMonths(0, d1);
        assertEquals(d2, d1);
    }

    /**
     * Le lundi qui précède le vendredi 9 novembre 2001 doit être le 5 novembre.
     */
    public void testMondayPrecedingFriday9Nov2001() {
        SerialDate mondayBefore = SerialDate.getPreviousDayOfWeek(SerialDate.MONDAY, this.nov9Y2001);
        assertEquals(5, mondayBefore.getDayOfMonth());
    }

    /**
     * Le lundi qui suit le vendredi 9 novembre 2001 doit être le 12 novembre.
     */
    public void testMondayFollowingFriday9Nov2001() {
        SerialDate mondayAfter = SerialDate.getFollowingDayOfWeek(
                SerialDate.MONDAY, this.nov9Y2001
        );
        assertEquals(12, mondayAfter.getDayOfMonth());
    }

    /**
     * Le lundi le plus proche du vendredi 9 novembre 2000 doit être le 12 novembre.
     */
    public void testMondayNearestFriday9Nov2001() {
        SerialDate mondayNearest = SerialDate.getNearestDayOfWeek(SerialDate.MONDAY, this.nov9Y2001);
        assertEquals(12, mondayNearest.getDayOfMonth());
    }

    /**
     * Le lundi le plus proche du 22 janvier 1970 tombe le 19.
     */
    public void testMondayNearest22Jan1970() {
        SerialDate jan22Y1970 = SerialDate.createInstance(22, MonthConstants.JANUARY, 1970);
        SerialDate mondayNearest = SerialDate.getNearestDayOfWeek(SerialDate.MONDAY, jan22Y1970);
        assertEquals(19, mondayNearest.getDayOfMonth());
    }

    /**
     * Vérifie que la conversion des jours en chaînes retourne le bon résultat. En réalité,
     * ce résultat dépend des paramètres régionaux et ce test doit être modifié.
     */
    public void testWeekdayCodeToString() {
        final String test = SerialDate.weekdayCodeToString(SerialDate.SATURDAY);
        assertEquals("Saturday", test);
    }

    /**
     * Teste la conversion d’une chaîne en un jour de la semaine. Ce test échouera si les
     * paramètres régionaux n’utilisent pas les noms anglais... trouver un meilleur test !
     */
    public void testStringToWeekday() {
        int weekday = SerialDate.stringToWeekdayCode("Wednesday");
        assertEquals(SerialDate.WEDNESDAY, weekday);

        weekday = SerialDate.stringToWeekdayCode(" Wednesday ");
        assertEquals(SerialDate.WEDNESDAY, weekday);

        weekday = SerialDate.stringToWeekdayCode("Wed");
        assertEquals(SerialDate.WEDNESDAY, weekday);
    }

    /**
     * Teste la conversion d’une chaîne en un mois. Ce test échouera si les paramètres
     * régionaux n’utilisent pas les noms anglais... trouver un meilleur test !
     */
    public void testStringToMonthCode() {
        int m = SerialDate.stringToMonthCode("January");
        assertEquals(MonthConstants.JANUARY, m);

        m = SerialDate.stringToMonthCode(" January ");
        assertEquals(MonthConstants.JANUARY, m);

        m = SerialDate.stringToMonthCode("Jan");
        assertEquals(MonthConstants.JANUARY, m);
    }

    /**
     * Teste la conversion d’un code de mois en une chaîne.
     */
    public void testMonthCodeToStringCode() {
        final String test = SerialDate.monthCodeToString(MonthConstants.DECEMBER);
        assertEquals("December", test);
    }

    /**
     * 1900 n’est pas une année bissextile.
     */
    public void testIsNotLeapYear1900() {
        assertTrue(!SerialDate.isLeapYear(1900));
    }

    /**
     * 2000 est une année bissextile.
     */
    public void testIsLeapYear2000() {
        assertTrue(SerialDate.isLeapYear(2000));
    }

    /**
     * Le nombre d’années bissextiles entre 1900 et 1899, comprise, est 0.
     */
    public void testLeapYearCount1899() {
        assertEquals(SerialDate.leapYearCount(1899), 0);
    }

    /**
     * Le nombre d’années bissextiles entre 1900 et 1903, comprise, est 0.
     */
    public void testLeapYearCount1903() {
        assertEquals(SerialDate.leapYearCount(1903), 0);
    }

    /**
     * Le nombre d’années bissextiles entre 1900 et 1904, comprise, est 1.
     */
    public void testLeapYearCount1904() {
        assertEquals(SerialDate.leapYearCount(1904), 1);
    }

    /**
     * Le nombre d’années bissextiles entre 1900 et 1999, comprise, est 24.
     */
    public void testLeapYearCount1999() {
        assertEquals(SerialDate.leapYearCount(1999), 24);
    }

    /**
     * Le nombre d’années bissextiles entre 1900 et 2000, comprise, est 25.
     */
    public void testLeapYearCount2000() {
        assertEquals(SerialDate.leapYearCount(2000), 25);
    }

    /**
     * Sérialise une instance, la restaure et vérifie l’égalité.
     */
    public void testSerialization() {

        SerialDate d1 = SerialDate.createInstance(15, 4, 2000);
        SerialDate d2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(d1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            d2 = (SerialDate) in.readObject();
            in.close();

        } catch (Exception e) {
            System.out.println(e.toString());

        }
        assertEquals(d1, d2);
    }

    /**
     * Teste le bogue 1096282 (à présent corrigé).
     */
    public void test1096282() {
        SerialDate d = SerialDate.createInstance(29, 2, 2004);
        d = SerialDate.addYears(1, d);
        SerialDate expected = SerialDate.createInstance(28, 2, 2005);
        assertTrue(d.isOn(expected));
    }

    /**
     * Divers tests de la méthode addMonths().
     */
    public void testAddMonths() {
        SerialDate d1 = SerialDate.createInstance(31, 5, 2004);

        SerialDate d2 = SerialDate.addMonths(1, d1);
        assertEquals(30, d2.getDayOfMonth());
        assertEquals(6, d2.getMonth());
        assertEquals(2004, d2.getYYYY());

        SerialDate d3 = SerialDate.addMonths(2, d1);
        assertEquals(31, d3.getDayOfMonth());
        assertEquals(7, d3.getMonth());
        assertEquals(2004, d3.getYYYY());

        SerialDate d4 = SerialDate.addMonths(1, SerialDate.addMonths(1, d1));
        assertEquals(30, d4.getDayOfMonth());
        assertEquals(7, d4.getMonth());
        assertEquals(2004, d4.getYYYY());
    }
    }
}
