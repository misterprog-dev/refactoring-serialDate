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
* -------------------
* MonthConstants.java
* -------------------
* (C) Copyright 2002, 2003, par Object Refinery Limited.
*
* Auteur : David Gilbert (pour Object Refinery Limited);
* Contributeur(s) : -;
*
* $Id: MonthConstants.java,v 1.4 2005/11/16 15:58:40 taqua Exp $
*
* Modifications
* -------------
* 29-Mai-2002 : Version 1 (code extrait de la classe SerialDate) (DG);398 Coder proprement
*
*/
package com.dsoumaila.serialdate;

/**
 * Constantes utiles pour les mois. Notez qu’elles ne sont PAS équivalentes aux
 * constantes définies par java.util.Calendar (où JANUARY=0 et DECEMBER=11).
 * <p>
 * Utilisées par les classes SerialDate et RegularTimePeriod.
 *
 * @author David Gilbert
 **/
public interface MonthConstants {
    /**
     * Constante pour January (janvier).
     */
    public static final int JANUARY = 1;

    /**
     * Constante pour February (février).
     */
    public static final int FEBRUARY = 2;

    /**
     * Constante pour March (mars).
     */
    public static final int MARCH = 3;

    /**
     * Constante pour April (avril).
     */
    public static final int APRIL = 4;

    /**
     * Constante pour May (mai).
     */
    public static final int MAY = 5;

    /**
     * Constante pour June (juin).
     */
    public static final int JUNE = 6;

    /**
     * Constante pour July (juillet).
     */
    public static final int JULY = 7;

    /**
     * Constante pour August (août).
     */
    public static final int AUGUST = 8;

    /**
     * Constante pour September (septembre).
     */
    public static final int SEPTEMBER = 9;

    /**
     * Constante pour October (octobre).
     */
    public static final int OCTOBER = 10;

    /**
     * Constante pour November (novembre).
     */
    public static final int NOVEMBER = 11;

    /**
     * Constante pour December (décembre).
     */
    public static final int DECEMBER = 12;
}
