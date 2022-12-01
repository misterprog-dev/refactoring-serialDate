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
* --------------------------
* RelativeDayOfWeekRule.java
* --------------------------
* (C) Copyright 2000-2003, par Object Refinery Limited et les Contributeurs.
*
* Auteur : David Gilbert (pour Object Refinery Limited);
* Contributeur(s) : -;
*
* $Id: RelativeDayOfWeekRule.java,v 1.6 2005/11/16 15:58:40 taqua Exp $
*
* Modifications (depuis le 26-Oct-2001)
* -------------------------------------
* 26-Oct-2: Paquetage modifié en com.jrefinery.date.*;
* 03-Oct-2: Correction des erreurs signalées par Checkstyle (DG);
*
*/

package com.dsoumaila.serialdate;

/**
 * Une règle de date annuelle qui retourne une date pour chaque année basée sur (a)
 * une règle de référence, (b) un jour de la semaine et (c) un paramètre de sélection
 * (SerialDate.PRECEDING, SerialDate.NEAREST, SerialDate.FOLLOWING).
 * <p>
 * Par exemple, le bon vendredi peut être indiqué sous la forme 'the Friday PRECEDING
 * Easter Sunday'.
 *
 * @author David Gilbert
 */
public class RelativeDayOfWeekRule extends AnnualDateRule {
    /**
     * Référence à la règle de date annuelle sur laquelle cette règle se fonde.
     */
    private AnnualDateRule subrule;

    /**
     * Le jour de la semaine (SerialDate.MONDAY, SerialDate.TUESDAY, etc.).
     */
    private int dayOfWeek;

    /**
     * Précise le jour de la semaine (PRECEDING, NEAREST ou FOLLOWING).
     */
    private int relative;

    /**
     * 70 * Constructeur par défaut - crée une règle pour le lundi qui suit le 1er janvier.
     * 71
     */
    public RelativeDayOfWeekRule() {
        this(new DayAndMonthRule(), SerialDate.MONDAY, SerialDate.FOLLOWING);
    }

    /**
     * Constructeur standard - construit une règle basée sur la sous-règle fournie.
     *
     * @param subrule   la règle qui détermine la date de référence.
     * @param dayOfWeek le jour de la semaine relativement à la date de référence.
     * @param relative  indique *quel* jour de la semaine (précédent, plus proche ou
     *                  suivant).
     */
    public RelativeDayOfWeekRule(final AnnualDateRule subrule, final int dayOfWeek, final int relative) {
        this.subrule = subrule;
        this.dayOfWeek = dayOfWeek;
        this.relative = relative;
    }

    /**
     * Retourne la sous-règle (également appelée règle de référence).
     *
     * @return la règle de date annuelle qui détermine la date de référence pour
     * cette règle.
     */
    public AnnualDateRule getSubrule() {
        return this.subrule;
    }

    /**
     * Fixe la sous-règle.
     *
     * @param subrule la règle de date annuelle qui détermine la date de référence pour
     *                cette règle.
     */
    public void setSubrule(final AnnualDateRule subrule) {
        this.subrule = subrule;
    }

    /**
     * Retourne le jour de la semaine pour cette règle.Coder proprement
     *
     * @return le jour de la semaine pour cette règle.
     */
    public int getDayOfWeek() {
        return this.dayOfWeek;
    }

    /**
     * Fixe le jour de la semaine pour cette règle.
     *
     * @param dayOfWeek le jour de la semaine (SerialDate.MONDAY,
     *                  SerialDate.TUESDAY, etc.).
     */
    public void setDayOfWeek(final int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Retourne l’attribut 'relative' qui détermine *quel* jour
     * de la semaine nous intéresse (SerialDate.PRECEDING,
     * SerialDate.NEAREST ou SerialDate.FOLLOWING).
     *
     * @return l’attribut 'relative'.
     */
    public int getRelative() {
        return this.relative;
    }

    /**
     * Fixe l’attribut 'relative' (SerialDate.PRECEDING, SerialDate.NEAREST,
     * SerialDate.FOLLOWING).
     *
     * @param relative détermine *quel* jour de la semaine est sélectionné par
     *                 cette règle.
     */
    public void setRelative(final int relative) {
        this.relative = relative;
    }

    /**
     * Crée un clone de cette règle.
     *
     * @return un clone de cette règle.
     * @throws CloneNotSupportedException this should never happen.
     */
    public Object clone() throws CloneNotSupportedException {
        final RelativeDayOfWeekRule duplicate
                = (RelativeDayOfWeekRule) super.clone();
        duplicate.subrule = (AnnualDateRule) duplicate.getSubrule().clone();
        return duplicate;
    }

    /**
     * Retourne la date générée par cette règle, pour l’année indiquée.
     *
     * @param year l’année (1&lt;= year &lt;= 9999).Annexe B org.jfree.date.SerialDate 419
     * @return la date générée par cette règle pour l’année indiquée (potentiellement
     * <code>null</code>).
     */
    public SerialDate getDate(final int year) {
        // Vérifier l’argument...
        if ((year < SerialDate.MINIMUM_YEAR_SUPPORTED)
                || (year > SerialDate.MAXIMUM_YEAR_SUPPORTED)) {
            throw new IllegalArgumentException(
                    "RelativeDayOfWeekRule.getDate(): year outside valid range.");
        }
        // Calculer la date...
        SerialDate result = null;
        final SerialDate base = this.subrule.getDate(year);
        if (base != null) {
            switch (this.relative) {
                case (SerialDate.PRECEDING):
                    result = SerialDate.getPreviousDayOfWeek(this.dayOfWeek,
                            base);
                    break;
                case (SerialDate.NEAREST):
                    result = SerialDate.getNearestDayOfWeek(this.dayOfWeek,
                            base);
                    break;
                case (SerialDate.FOLLOWING):
                    result = SerialDate.getFollowingDayOfWeek(this.dayOfWeek,
                            base);
                    break;
                default:
                    break;
            }
        }
        return result;
    }
}
