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
package org.geotoolkit.referencing.operation.transform;

import java.util.Arrays;
import java.io.Serializable;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.media.jai.Warp;
import javax.media.jai.WarpOpImage;
import javax.media.jai.WarpPolynomial;
import javax.media.jai.operator.WarpDescriptor;

import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.operation.NoninvertibleTransformException;

import org.geotoolkit.util.XArrays;
import org.geotoolkit.util.Utilities;
import org.geotoolkit.parameter.Parameter;
import org.geotoolkit.parameter.ParameterGroup;
import org.geotoolkit.resources.Vocabulary;

import static org.geotoolkit.referencing.operation.provider.WarpPolynomial.*;


/**
 * Wraps an arbitrary {@link Warp} object as a {@linkplain MathTransform2D two-dimensional transform}.
 * Calls to {@linkplain #transform(float[],int,float[],int,int) transform} methods are forwarded to
 * the {@link Warp#warpPoint(int,int,float[]) warpPoint} method, or something equivalent. This
 * implies that source coordinates may be rounded to nearest integers before the transformation
 * is applied.
 * <p>
 * This transform is typically used with {@linkplain org.geotoolkit.coverage.processing.operation.Resample
 * grid coverage "Resample" operation} for reprojecting an image. Source and destination coordinates
 * are usually pixel coordinates in source and target image, which is why this transform may use
 * integer arithmetic.
 * <p>
 * This math transform can be created alone (by invoking its public constructors directly), or it can
 * be created by a factory like {@link org.geotoolkit.referencing.operation.builder.LocalizationGrid}.
 * <p>
 * For more information on image warp, see
 * <A HREF="http://java.sun.com/products/java-media/jai/forDevelopers/jai1_0_1guide-unc/Geom-image-manip.doc.html">Geometric
 * Image Manipulation</A> in the <cite>Programming in Java Advanced Imaging</cite> guide.
 *
 * @author Martin Desruisseaux (IRD)
 * @author Alessio Fabiani (Geosolutions)
 * @version 3.00
 *
 * @see org.geotoolkit.referencing.operation.builder.LocalizationGrid#getPolynomialTransform(int)
 * @see Warp
 * @see WarpOpImage
 * @see WarpDescriptor
 *
 * @since 1.2
 * @module
 */
public class WarpTransform2D extends AbstractMathTransform implements MathTransform2D, Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -7949539694656719923L;

    /**
     * The maximal polynomial degree allowed.
     *
     * @since 2.4
     */
    public static final int MAX_DEGREE = 7;

    /**
     * The warp object. Transformations will be applied using the
     * {@link Warp#warpPoint(int,int,float[]) warpPoint} method or something equivalent.
     */
    private final Warp warp;

    /**
     * The inverse math transform.
     */
    private final WarpTransform2D inverse;

    /**
     * Constructs a warp transform that approximatively maps the given source coordinates to the
     * given destination coordinates. The transformation is performed using some polynomial warp
     * with the degree supplied in argument. The number of points required for each degree of warp
     * are as follows:
     * <p>
     * <table border="1">
     * <tr align="center" bgcolor="#EEEEFF"><th>Degree of Warp</th><th>Number of Points</th></tr>
     * <tr align="center"><td>1</td><td>3</td></tr>
     * <tr align="center"><td>2</td><td>6</td></tr>
     * <tr align="center"><td>3</td><td>10</td></tr>
     * <tr align="center"><td>4</td><td>15</td></tr>
     * <tr align="center"><td>5</td><td>21</td></tr>
     * <tr align="center"><td>6</td><td>28</td></tr>
     * <tr align="center"><td>7</td><td>36</td></tr>
     * </table>
     *
     * @param srcCoords Source coordinates.
     * @param dstCoords Destination coordinates.
     * @param degree    The desired degree of the warp polynomials.
     */
    public WarpTransform2D(final Point2D[] srcCoords, final Point2D[] dstCoords, final int degree) {
        this(null, srcCoords, 0, null, dstCoords, 0, Math.min(srcCoords.length, dstCoords.length), degree);
    }

    /**
     * Constructs a warp transform that approximatively maps the given source coordinates to the
     * given destination coordinates. The transformation is performed using some polynomial warp
     * with the degree supplied in argument.
     *
     * @param srcBounds Bounding box of source coordinates, or {@code null} if unknow.
     * @param srcCoords Source coordinates.
     * @param srcOffset The inital entry of {@code srcCoords} to be used.
     * @param dstBounds Bounding box of destination coordinates, or {@code null} if unknow.
     * @param dstCoords Destination coordinates.
     * @param dstOffset The inital entry of {@code destCoords} to be used.
     * @param numCoords The number of coordinates from {@code srcCoords} and {@code destCoords} to be used.
     * @param degree    The desired degree of the warp polynomials.
     */
    public WarpTransform2D(final Rectangle2D srcBounds, final Point2D[] srcCoords, final int srcOffset,
                           final Rectangle2D dstBounds, final Point2D[] dstCoords, final int dstOffset,
                           final int numCoords, final int degree)
    {
        this(srcBounds, toFloat(srcCoords, srcOffset, numCoords), 0,
             dstBounds, toFloat(dstCoords, dstOffset, numCoords), 0, numCoords, degree, false);
    }

    /**
     * Converts an array of points into an array of floats.
     * This is used internally for the above constructor only.
     */
    private static float[] toFloat(final Point2D[] points, int offset, final int numCoords) {
        final float[] array = new float[numCoords * 2];
        for (int i=0; i<array.length;) {
            final Point2D point = points[offset++];
            array[i++] = (float) point.getX();
            array[i++] = (float) point.getY();
        }
        return array;
    }

    /**
     * Constructs a warp transform that approximatively maps the given source coordinates to the
     * given destination coordinates. The transformation is performed using some polynomial warp
     * with the degree supplied in argument.
     *
     * @param srcBounds Bounding box of source coordinates, or {@code null} if unknow.
     * @param srcCoords Source coordinates with <var>x</var> and <var>y</var> alternating.
     * @param srcOffset The inital entry of {@code srcCoords} to be used.
     * @param dstBounds Bounding box of destination coordinates, or {@code null} if unknow.
     * @param dstCoords Destination coordinates with <var>x</var> and <var>y</var> alternating.
     * @param dstOffset The inital entry of {@code destCoords} to be used.
     * @param numCoords The number of coordinates from {@code srcCoords} and {@code destCoords} to be used.
     * @param degree    The desired degree of the warp polynomials.
     */
    public WarpTransform2D(final Rectangle2D srcBounds, final float[] srcCoords, final int srcOffset,
                           final Rectangle2D dstBounds, final float[] dstCoords, final int dstOffset,
                           final int numCoords, final int degree)
    {
        this(srcBounds, srcCoords, srcOffset,
             dstBounds, dstCoords, dstOffset,
             numCoords, degree, true);
    }

    /**
     * Work around for a bug in WarpPolynomial.createWarp(...). This constructor should move in
     * the one above when the {@code cloneCoords} argument will no longer be needed (after the
     * JAI bug get fixed).
     */
    private WarpTransform2D(final Rectangle2D srcBounds, float[] srcCoords, int srcOffset,
                            final Rectangle2D dstBounds, float[] dstCoords, int dstOffset,
                            final int numCoords, final int degree, boolean cloneCoords)
    {
        final float preScaleX, preScaleY, postScaleX, postScaleY;
        if (srcBounds != null) {
            preScaleX = (float) srcBounds.getWidth();
            preScaleY = (float) srcBounds.getHeight();
        } else {
            preScaleX = getWidth(srcCoords, srcOffset  , numCoords);
            preScaleY = getWidth(srcCoords, srcOffset+1, numCoords);
        }
        if (dstBounds != null) {
            postScaleX = (float) dstBounds.getWidth();
            postScaleY = (float) dstBounds.getHeight();
        } else {
            postScaleX = getWidth(dstCoords, dstOffset  , numCoords);
            postScaleY = getWidth(dstCoords, dstOffset+1, numCoords);
        }
        /*
         * Workaround for a bug in WarpPolynomial.create(...): the later scale coordinates
         * according the scale values, but the 'preScale' and 'postScale' are interchanged.
         * When JAI bug will be fixed, delete all the following block until the next comment.
         */
        final double scaleX = preScaleX / postScaleX;
        final double scaleY = preScaleY / postScaleY;
        if (scaleX!=1 || scaleY!=1) {
            final int n = numCoords*2;
            if (cloneCoords) {
                srcCoords = Arrays.copyOfRange(srcCoords, srcOffset, srcOffset + n); srcOffset = 0;
                dstCoords = Arrays.copyOfRange(dstCoords, dstOffset, dstOffset + n); dstOffset = 0;
            }
            for (int i=0; i<n;) {
                srcCoords[srcOffset + i  ] /= scaleX;
                dstCoords[dstOffset + i++] *= scaleX;
                srcCoords[srcOffset + i  ] /= scaleY;
                dstCoords[dstOffset + i++] *= scaleY;
            }
        }
        /*
         * Note: Warp semantic (transforms coordinates from destination to source) is the
         *       opposite of MathTransform semantic (transforms coordinates from source to
         *       destination). We have to interchange source and destination arrays for the
         *       direct transform.
         */
        warp = WarpPolynomial.createWarp(dstCoords, dstOffset, srcCoords, srcOffset, numCoords,
                1/preScaleX, 1/preScaleY, postScaleX, postScaleY, degree);
        inverse = new WarpTransform2D(
               WarpPolynomial.createWarp(srcCoords, srcOffset, dstCoords, dstOffset, numCoords,
                1/postScaleX, 1/postScaleY, preScaleX, preScaleY, degree), this);
    }

    /**
     * Returns the maximum minus the minimum ordinate int the specifie array.
     * This is used internally for the above constructor only.
     */
    private static float getWidth(final float[] array, int offset, int num) {
        float min = Float.POSITIVE_INFINITY;
        float max = Float.NEGATIVE_INFINITY;
        while (--num >= 0) {
            float value = array[offset];
            if (value < min) min = value;
            if (value > max) max = value;
            offset += 2;
        }
        return max - min;
    }

    /**
     * Constructs a transform using the specified warp object. Transformations will be applied
     * using the {@link Warp#warpPoint(int,int,float[]) warpPoint} method or something equivalent.
     *
     * @param warp    The image warp to wrap into a math transform.
     * @param inverse An image warp to uses for the {@linkplain #inverse inverse transform},
     *                or {@code null} in none.
     *
     * @see #create
     */
    protected WarpTransform2D(final Warp warp, final Warp inverse) {
        ensureNonNull("warp", warp);
        this.warp    = warp;
        this.inverse = (inverse != null) ? new WarpTransform2D(inverse, this) : null;
    }

    /**
     * Constructs a transform using the specified warp object. This private constructor is used
     * for the construction of {@link #inverse} transform only.
     */
    private WarpTransform2D(final Warp warp, final WarpTransform2D inverse) {
        this.warp    = warp;
        this.inverse = inverse;
    }

    /**
     * Returns a transform using the specified warp object. Transformations will be applied
     * using the {@link Warp#warpPoint(int,int,float[]) warpPoint} method or something equivalent.
     *
     * @param  warp The image warp to wrap into a math transform.
     * @return The transform for the given warp.
     */
    public static MathTransform2D create(final Warp warp) {
        if (warp instanceof WarpAdapter) {
            return ((WarpAdapter) warp).getTransform();
        }
        return new WarpTransform2D(warp, (Warp) null);
    }

    /**
     * Returns a {@linkplain Warp image warp} for the specified transform. The
     * {@link Warp#warpPoint(int,int,float[]) Warp.warpPoint} method transforms coordinates from
     * source to target CRS. Note that JAI's {@linkplain WarpDescriptor warp operation} needs a
     * warp object with the opposite semantic (i.e. the image warp must transforms coordinates from
     * target to source CRS). Consequently, consider invoking {@code getWarp(transform.inverse())}
     * if the warp object is going to be used in an image reprojection.
     *
     * @param  name The image name or {@linkplain org.geotoolkit.coverage.grid.GridCoverage2D coverage}
     *         name, or {@code null} in unknow. Used only for formatting error message if some
     *         {@link TransformException} are thrown by the supplied transform.
     * @param  transform The transform to returns as an image warp.
     * @return The warp for the given transform.
     *
     * @todo We should check for {@link ConcatenatedTransform}. If we detect that a
     * {@code WarpTransform2D} is concatenated with {@code AffineTransform} only, and if the
     * {@code AffineTransform} has scale factors only, then we can ommit the {@code AffineTransform}
     * and merge the scale factors with {@link WarpPolynomial} preScaleX, preScaleY, postScaleX and
     * postScaleY. Additionnaly, the translation term for the post-AffineTransform may also be
     * merged with the first coefficients of WarpPolynomial.xCoeffs and yCoeffs. See GEOT-521.
     *
     * @todo Move this method in some other factory class. Use the optimization applied by
     *       Resampler2D in the case of AffineTransform, and remove that optimization from
     *       Resampler2D.
     */
    public static Warp getWarp(CharSequence name, final MathTransform2D transform) {
        if (transform instanceof WarpTransform2D) {
            return ((WarpTransform2D) transform).getWarp();
        }
        if (name == null) {
            name = Vocabulary.formatInternational(Vocabulary.Keys.UNKNOW);
        }
        return new WarpAdapter(name, transform);
    }

    /**
     * Returns the {@link Warp} wrapped by this transform. Its {@link Warp#warpPoint(int,int,float[])
     * Warp.warpPoint} method transforms coordinates from source to target CRS. Note that JAI's
     * {@linkplain WarpDescriptor warp operation} needs a warp object with the opposite semantic
     * (i.e. the image warp must transforms coordinates from target to source CRS). Consequently,
     * consider invoking <code>{@linkplain #inverse}.getWarp()</code> if the warp object is going
     * to be used in an image reprojection.
     *
     * @return The image warp from source to target CRS.
     */
    public Warp getWarp() {
        return warp;
    }

    /**
     * Returns the parameter descriptors for this math transform.
     */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return (warp instanceof WarpPolynomial) ? PARAMETERS : null;
    }

    /**
     * Returns the parameter values for this math transform.
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        if (warp instanceof WarpPolynomial) {
            final WarpPolynomial poly = (WarpPolynomial) warp;
            final ParameterValue<?>[] p = new ParameterValue<?>[7];
            int c = 0;
            p[c++] = new Parameter<Integer>(DEGREE,   poly.getDegree());
            p[c++] = new Parameter<float[]>(X_COEFFS, poly.getXCoeffs());
            p[c++] = new Parameter<float[]>(Y_COEFFS, poly.getYCoeffs());
            float s;
            if ((s=poly.getPreScaleX ()) != 1) p[c++] = new Parameter<Float>(PRE_SCALE_X,  s);
            if ((s=poly.getPreScaleY ()) != 1) p[c++] = new Parameter<Float>(PRE_SCALE_Y,  s);
            if ((s=poly.getPostScaleX()) != 1) p[c++] = new Parameter<Float>(POST_SCALE_X, s);
            if ((s=poly.getPostScaleY()) != 1) p[c++] = new Parameter<Float>(POST_SCALE_Y, s);
            return new ParameterGroup(getParameterDescriptors(), XArrays.resize(p, c));
        } else {
            return null;
        }
    }

    /**
     * Returns the dimension of input points.
     */
    @Override
    public int getSourceDimensions() {
        return 2;
    }

    /**
     * Returns the dimension of output points.
     */
    @Override
    public int getTargetDimensions() {
        return 2;
    }

    /**
     * Tests if this transform is the identity transform.
     */
    @Override
    public boolean isIdentity() {
        return false;
    }

    /**
     * Transforms source coordinates (usually pixel indices) into destination coordinates
     * (usually "real world" coordinates).
     *
     * @param ptSrc the specified coordinate point to be transformed.
     * @param ptDst the specified coordinate point that stores the result of transforming
     *              {@code ptSrc}, or {@code null}.
     * @return the coordinate point after transforming {@code ptSrc} and storing the result in
     *         {@code ptDst}.
     */
    @Override
    public Point2D transform(Point2D ptSrc, Point2D ptDst) {
        /*
         * We have to copy the coordinate in a temporary point object because we don't know
         * neither the ptSrc or ptDst type. Since mapDestPoint returns a clone of the point
         * given in argument,  giving ptSrc directly would result in a lost of precision if
         * its type is java.awt.Point (for example), even if ptDst had a greater precision.
         *
         * There is also an other reason for creating a temporary object:
         * JAI's Warp is designed for mapping pixel coordinates in J2SE's image. In JAI, pixel
         * coordinates map by definition to the pixel's upper left corner. But for interpolation
         * purpose, JAI needs to map pixel's center. This introduce a shift of 0.5, which is
         * documented (for example) in WarpAffine.mapDestPoint(Point2D).
         */
        ptSrc = new PointDouble(ptSrc.getX() - 0.5, ptSrc.getY() - 0.5);
        final Point2D result = warp.mapDestPoint(ptSrc);
        result.setLocation(result.getX() + 0.5, result.getY() + 0.5);
        if (ptDst == null) {
            // Do not returns 'result' directly, since it has tricked 'clone()' method.
            ptDst = new Point2D.Float();
        }
        ptDst.setLocation(result);
        return ptDst;
    }

    /**
     * Transforms a single source coordinate (usually pixel indices) into destination coordinate
     * (usually "real world" coordinates).
     */
    @Override
    protected void transform(double[] srcPts, int srcOff, double[] dstPts, int dstOff) {
        transform(srcPts, srcOff, dstPts, dstOff, 1);
    }

    /**
     * Transforms source coordinates (usually pixel indices) into destination coordinates
     * (usually "real world" coordinates).
     */
    @Override
    public void transform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        int postIncrement = 0;
        if (srcPts == dstPts) {
            switch (IterationStrategy.suggest(srcOff, 2, dstOff, 2, numPts)) {
                case ASCENDING: {
                    break;
                }
                case DESCENDING: {
                    srcOff += (numPts-1) * 2;
                    dstOff += (numPts-1) * 2;
                    postIncrement = -4;
                    break;
                }
                default: {
                    srcPts = Arrays.copyOfRange(srcPts, srcOff, srcOff + numPts*2);
                    srcOff = 0;
                    break;
                }
            }
        }
        final Point2D.Double ptSrc = new PointDouble();
        while (--numPts >= 0) {
            ptSrc.x = srcPts[srcOff++] - 0.5;  // See the comment in transform(Point2D...)
            ptSrc.y = srcPts[srcOff++] - 0.5;  // for an explanation about the 0.5 shift.
            final Point2D result = warp.mapDestPoint(ptSrc);
            dstPts[dstOff++] = result.getX() + 0.5;
            dstPts[dstOff++] = result.getY() + 0.5;
            dstOff += postIncrement;
        }
    }

    /**
     * Transforms source coordinates (usually pixel indices) into destination coordinates
     * (usually "real world" coordinates).
     */
    @Override
    public void transform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        int postIncrement = 0;
        if (srcPts == dstPts) {
            switch (IterationStrategy.suggest(srcOff, 2, dstOff, 2, numPts)) {
                case ASCENDING: {
                    break;
                }
                case DESCENDING: {
                    srcOff += (numPts-1) * 2;
                    dstOff += (numPts-1) * 2;
                    postIncrement = -4;
                    break;
                }
                default: {
                    srcPts = Arrays.copyOfRange(srcPts, srcOff, srcOff + numPts*2);
                    srcOff = 0;
                    break;
                }
            }
        }
        final Point2D.Float ptSrc = new PointFloat();
        while (--numPts >= 0) {
            ptSrc.x = srcPts[srcOff++] - 0.5f;  // See the comment in transform(Point2D...)
            ptSrc.y = srcPts[srcOff++] - 0.5f;  // for an explanation about the 0.5 shift.
            final Point2D result = warp.mapDestPoint(ptSrc);
            dstPts[dstOff++] = (float) (result.getX() + 0.5);
            dstPts[dstOff++] = (float) (result.getY() + 0.5);
            dstOff += postIncrement;
        }
    }

    /**
     * Transforms source coordinates (usually pixel indices) into destination coordinates
     * (usually "real world" coordinates).
     */
    @Override
    public void transform(double[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        final Point2D.Double ptSrc = new PointDouble();
        while (--numPts >= 0) {
            ptSrc.x = srcPts[srcOff++] - 0.5;  // See the comment in transform(Point2D...)
            ptSrc.y = srcPts[srcOff++] - 0.5;  // for an explanation about the 0.5 shift.
            final Point2D result = warp.mapDestPoint(ptSrc);
            dstPts[dstOff++] = (float) (result.getX() + 0.5);
            dstPts[dstOff++] = (float) (result.getY() + 0.5);
        }
    }

    /**
     * Transforms source coordinates (usually pixel indices) into destination coordinates
     * (usually "real world" coordinates).
     */
    @Override
    public void transform(float[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts) {
        final Point2D.Double ptSrc = new PointDouble();
        while (--numPts >= 0) {
            ptSrc.x = srcPts[srcOff++] - 0.5;  // See the comment in transform(Point2D...)
            ptSrc.y = srcPts[srcOff++] - 0.5;  // for an explanation about the 0.5 shift.
            final Point2D result = warp.mapDestPoint(ptSrc);
            dstPts[dstOff++] = result.getX() + 0.5;
            dstPts[dstOff++] = result.getY() + 0.5;
        }
    }

    /**
     * Returns the inverse transform.
     *
     * @throws NoninvertibleTransformException if no inverse warp were specified at construction time.
     */
    @Override
    public MathTransform2D inverse() throws NoninvertibleTransformException {
        if (inverse != null) {
            return inverse;
        } else {
            return (MathTransform2D) super.inverse();
        }
    }

    /**
     * Returns a hash value for this transform.
     */
    @Override
    public int hashCode() {
        return warp.hashCode() ^ (int) serialVersionUID;
    }

    /**
     * Compares this transform with the specified object for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (super.equals(object)) {
            final WarpTransform2D that = (WarpTransform2D) object;
            return Utilities.equals(this.warp, that.warp);
        }
        return false;
    }

    /**
     * A {@code Point2D.Float} that returns itself when {@link #clone} is invoked.
     * This trick is used for avoiding the creation of thousands of temporary objects
     * when transforming an array of points using {@link Warp#mapDestPoint}.
     */
    @SuppressWarnings("serial")
    private static final class PointFloat extends Point2D.Float {
        @Override
        public PointFloat clone() {
            return this;
        }
    }

    /**
     * A {@code Point2D.Double} that returns itself when {@link #clone} is invoked.
     * This trick is used for avoiding the creation of thousands of temporary objects
     * when transforming an array of points using {@link Warp#mapDestPoint}.
     */
    @SuppressWarnings("serial")
    private static final class PointDouble extends Point2D.Double {
        public PointDouble() {
            super();
        }

        public PointDouble(double x, double y) {
            super(x,y);
        }

        @Override
        public PointDouble clone() {
            return this;
        }
    }
}
