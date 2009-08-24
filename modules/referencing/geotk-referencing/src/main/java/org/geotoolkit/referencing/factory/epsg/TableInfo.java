/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2005-2009, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2009, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotoolkit.referencing.factory.epsg;


/**
 * Information about a specific table. This class also provides some utility methods
 * for the creation of SQL queries. The MS-Access dialect of SQL is assumed (it will
 * be translated into ANSI SQL later by {@link DirectEpsgFactory#adaptSQL} if needed).
 *
 * @author Martin Desruisseaux (IRD)
 * @version 3.00
 *
 * @since 2.2
 * @module
 */
final class TableInfo {
    /**
     * The class of object to be created.
     */
    final Class<?> type;

    /**
     * The table name for SQL queries. May contains a {@code "JOIN"} clause.
     */
    final String table;

    /**
     * Column name for the code (usually with the {@code "_CODE"} suffix).
     */
    final String codeColumn;

    /**
     * Column name for the name (usually with the {@code "_NAME"} suffix), or {@code null}.
     */
    final String nameColumn;

    /**
     * Column type for the type (usually with the {@code "_TYPE"} suffix), or {@code null}.
     */
    final String typeColumn;

    /**
     * Sub-interfaces of {@link #type} to handle, or {@code null} if none.
     */
    final Class<?>[] subTypes;

    /**
     * Names of {@link #subTypes} in the database, or {@code null} if none.
     */
    final String[] typeNames;

    /**
     * Stores information about a specific table.
     */
    TableInfo(final Class<?> type, final String table,
              final String codeColumn, final String nameColumn)
    {
        this(type, table, codeColumn, nameColumn, null, null, null);
    }

    /**
     * Stores information about a specific table.
     */
    TableInfo(final Class<?> type,
              final String table, final String codeColumn, final String nameColumn,
              final String typeColumn, final Class<?>[] subTypes, final String[] typeNames)
    {
        this.type       = type;
        this.table      = table;
        this.codeColumn = codeColumn;
        this.nameColumn = nameColumn;
        this.typeColumn = typeColumn;
        this.subTypes   = subTypes;
        this.typeNames  = typeNames;
    }

    /**
     * Checks {@link Class#isAssignableFrom} both ways. It may seems strange but the
     * intend is to catch the following use cases:
     * <p>
     * <ul>
     *   <li><p>{@code table.type.isAssignableFrom(kind)}<br>
     *       is for the case where a table is for {@code CoordinateReferenceSystem} while the user
     *       type is some subtype like {@code GeographicCRS}. The {@code GeographicCRS} need to be
     *       queried into the {@code CoordinateReferenceSystem} table. An additional filter will be
     *       applied inside the {@link AuthorityCodes} class implementation.</p></li>
     *
     *   <li><p>{@code kind.isAssignableFrom(table.type)}<br>
     *       is for the case where the user type is {@code IdentifiedObject} or {@code Object},
     *       in which case we basically want to iterate through every tables.</p></li>
     * </ul>
     */
    final boolean isTypeOf(final Class<?> kind) {
        return type.isAssignableFrom(kind) || kind.isAssignableFrom(type);
    }
}
