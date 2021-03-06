/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2018, Geomatys
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
package org.geotoolkit.coverage.sql;

import java.nio.file.Path;
import java.util.Locale;
import java.util.EnumMap;
import java.util.TimeZone;
import javax.sql.DataSource;
import java.sql.SQLException;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.UnitConverter;

import org.opengis.util.FactoryException;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransformFactory;

import org.apache.sis.measure.UnitFormat;
import org.apache.sis.referencing.CRS;
import org.apache.sis.referencing.CommonCRS;
import org.apache.sis.util.collection.Cache;
import org.apache.sis.referencing.crs.DefaultTemporalCRS;
import org.apache.sis.internal.system.DefaultFactories;
import org.apache.sis.internal.metadata.AxisDirections;

import org.geotoolkit.image.palette.PaletteFactory;


/**
 * Information about the database as a whole, including cached entries.
 *
 * @author Martin Desruisseaux (IRD, Geomatys)
 */
final class Database {
    /**
     * Provider of connections to the database. Should be pooled connections.
     */
    private final DataSource source;

    /**
     * The two-dimensional coordinate reference system of the {@code rasters.GridGeometries.extent} column.
     * This is usually WGS84, but not necessarily.
     *
     * @todo find by inspection of "geometry columns" table.
     */
    final CoordinateReferenceSystem extentCRS;

    /**
     * The temporal CRS.
     */
    final DefaultTemporalCRS temporalCRS;

    /**
     * Combination of {@link #extentCRS} with {@link #temporalCRS}.
     */
    final CoordinateReferenceSystem spatioTemporalCRS;

    /**
     * The timezone to use for reading and writing dates in the database.
     */
    final TimeZone timezone;

    /**
     * The root directory of files.
     */
    final Path root;

    /**
     * The factory to use for creating coordinate reference systems from codes.
     *
     * @todo Currently an EPSG factory, but should be a PostGIS factory.
     */
    final CRSAuthorityFactory authorityFactory;

    /**
     * The factory to use for creating coordinate reference systems from codes.
     */
    final CRSFactory crsFactory;

    /**
     * The math transform factory.
     */
    final MathTransformFactory mtFactory;

    /**
     * The factory for color palettes.
     */
    final PaletteFactory paletteFactory;

    /**
     * The unit format for parsing and formatting unit symbols.
     * Thread safe if the configuration is not changed after {@code Database} construction.
     */
    final UnitFormat unitFormat;

    /**
     * The locale to use for formatting messages, or null for the default.
     * This is used mostly for error messages in exceptions and for warnings
     * during {@linkplain javax.imageio.ImageRead image read} operations.
     */
    final Locale locale;

    /**
     * All caches used by the tables.
     */
    private final EnumMap<CachedTable.Target, Cache<Object,Object>> caches;

    /**
     * Creates a new instance.
     */
    Database(final DataSource ds, final Path directory) throws FactoryException {
        source            = ds;
        extentCRS         = CommonCRS.defaultGeographic();
        temporalCRS       = GridGeometryEntry.TEMPORAL_CRS;
        spatioTemporalCRS = CRS.compound(extentCRS, temporalCRS);
        timezone          = TimeZone.getTimeZone("UTC");
        root              = directory;
        locale            = Locale.getDefault(Locale.Category.DISPLAY);
        authorityFactory  = CRS.getAuthorityFactory("EPSG");
        crsFactory        = DefaultFactories.forBuildin(CRSFactory.class);
        mtFactory         = DefaultFactories.forBuildin(MathTransformFactory.class);
        paletteFactory    = PaletteFactory.getDefault();
        paletteFactory.setWarningLocale(locale);
        unitFormat        = new UnitFormat(locale);
        caches            = new EnumMap<>(CachedTable.Target.class);
    }

    /**
     * Creates a new transaction.
     */
    Transaction transaction() throws SQLException {
        return new Transaction(this, source.getConnection());
    }

    /**
     * Returns the cache for the given table.
     */
    final Cache<Object,Object> cache(final CachedTable.Target table) {
        Cache<Object, Object> c;
        synchronized (caches) {
            c = caches.get(table);
            if (c == null) {
                caches.put(table, c = new Cache<>());
            }
        }
        return c;
    }

    /**
     * Returns a converter from the given unit and axis direction to standard units for cross-product comparison.
     */
    static <Q extends Quantity<Q>> UnitConverter toStandardUnit(final AxisDirection direction, final Unit<Q> unit) {
        UnitConverter c = unit.getConverterTo(unit.getSystemUnit());
        if (AxisDirections.isOpposite(direction)) {
        }
        return c;
    }
}
