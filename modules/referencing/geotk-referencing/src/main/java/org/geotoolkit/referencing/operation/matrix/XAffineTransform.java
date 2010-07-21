/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2001-2010, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2009-2010, Geomatys
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
package org.geotoolkit.referencing.operation.matrix;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;
import java.awt.geom.NoninvertibleTransformException;

import org.geotoolkit.math.XMath;
import org.geotoolkit.lang.Static;
import org.geotoolkit.resources.Errors;

import static java.lang.Math.*;


/**
 * Utility methods for affine transforms. This class provides two kind of services:
 * <p>
 * <ul>
 *   <li>A set of public static methods working on any {@link AffineTransform}.</li>
 *   <li>An abstract base class that override all mutable {@link AffineTransform} methods
 *       in order to check for permission before changing the transform's state.
 *       If {@link #checkPermission} is defined to always throw an exception,
 *       then {@code XAffineTransform} is immutable.</li>
 * </ul>
 *
 * @author Martin Desruisseaux (IRD)
 * @version 3.00
 *
 * @since 1.2
 * @module
 */
@Static
public class XAffineTransform extends AffineTransform {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 5215291166450556451L;

    /**
     * Constructs an identity affine transform. This is for subclasses usage only,
     * for example subclasses that override the {@link #checkPermission} method.
     *
     * {@note This constructor is protected rather than public in order to avoid accidental
     *        instantation of identity transforms that are not of some subclass, which is
     *        likely to be useless since <code>XAffineTransform</code> is immutable.}
     *
     * @since 3.00
     */
    protected XAffineTransform() {
        super();
    }

    /**
     * Constructs a new {@code XAffineTransform} that is a
     * copy of the specified {@code AffineTransform} object.
     *
     * @param tr The affine transform to copy.
     */
    public XAffineTransform(final AffineTransform tr) {
        super(tr);
    }

    /**
     * Constructs a new {@code XAffineTransform} from 6 values representing the 6 specifiable
     * entries of the 3&times;3 transformation matrix. Those values are given unchanged to the
     * {@linkplain AffineTransform#AffineTransform(double,double,double,double,double,double)
     * super class constructor}.
     *
     * @param m00 the X coordinate scaling.
     * @param m10 the Y coordinate shearing.
     * @param m01 the X coordinate shearing.
     * @param m11 the Y coordinate scaling.
     * @param m02 the X coordinate translation.
     * @param m12 the Y coordinate translation.
     *
     * @since 2.5
     */
    public XAffineTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
        super(m00, m10, m01, m11, m02, m12);
    }

    /**
     * Checks if the caller is allowed to change this {@code XAffineTransform} state.
     * If this method is defined to thrown an exception in all case, then this
     * {@code XAffineTransform} is immutable.
     * <p>
     * The default implementation throws the exception in all case, thus making this
     * instance immutable.
     *
     * @throws UnsupportedOperationException if this affine transform is immutable.
     */
    protected void checkPermission() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                Errors.format(Errors.Keys.UNMODIFIABLE_AFFINE_TRANSFORM));
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before translating this transform.
     *
     * @level hidden
     */
    @Override
    public void translate(double tx, double ty) {
        checkPermission();
        super.translate(tx, ty);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before rotating this transform.
     *
     * @level hidden
     */
    @Override
    public void rotate(double theta) {
        checkPermission();
        super.rotate(theta);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before rotating this transform.
     *
     * @level hidden
     */
    @Override
    public void rotate(double theta, double anchorx, double anchory) {
        checkPermission();
        super.rotate(theta, anchorx, anchory);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before rotating this transform.
     *
     * @level hidden
     */
    @Override
    public void rotate(double vecx, double vecy) {
        checkPermission();
        super.rotate(vecx, vecy);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before rotating this transform.
     *
     * @level hidden
     */
    @Override
    public void rotate(double vecx, double vecy, double anchorx, double anchory) {
        checkPermission();
        super.rotate(vecx, vecy, anchorx, anchory);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before rotating this transform.
     *
     * @level hidden
     */
    @Override
    public void quadrantRotate(int numquadrants) {
        checkPermission();
        super.quadrantRotate(numquadrants);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before rotating this transform.
     *
     * @level hidden
     */
    @Override
    public void quadrantRotate(int numquadrants, double anchorx, double anchory) {
        checkPermission();
        super.quadrantRotate(numquadrants, anchorx, anchory);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before scaling this transform.
     *
     * @level hidden
     */
    @Override
    public void scale(double sx, double sy) {
        checkPermission();
        super.scale(sx, sy);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before shearing this transform.
     *
     * @level hidden
     */
    @Override
    public void shear(double shx, double shy) {
        checkPermission();
        super.shear(shx, shy);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before setting this transform.
     *
     * @level hidden
     */
    @Override
    public void setToIdentity() {
        checkPermission();
        super.setToIdentity();
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before setting this transform.
     *
     * @level hidden
     */
    @Override
    public void setToTranslation(double tx, double ty) {
        checkPermission();
        super.setToTranslation(tx, ty);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before setting this transform.
     *
     * @level hidden
     */
    @Override
    public void setToRotation(double theta) {
        checkPermission();
        super.setToRotation(theta);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before setting this transform.
     *
     * @level hidden
     */
    @Override
    public void setToRotation(double theta, double anchorx, double anchory) {
        checkPermission();
        super.setToRotation(theta, anchorx, anchory);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before setting this transform.
     *
     * @level hidden
     */
    @Override
    public void setToRotation(double vecx, double vecy) {
        checkPermission();
        super.setToRotation(vecx, vecy);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before setting this transform.
     *
     * @level hidden
     */
    @Override
    public void setToRotation(double vecx, double vecy, double anchorx, double anchory) {
        checkPermission();
        super.setToRotation(vecx, vecy, anchorx, anchory);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before setting this transform.
     *
     * @level hidden
     */
    @Override
    public void setToQuadrantRotation(int numquadrants) {
        checkPermission();
        super.setToQuadrantRotation(numquadrants);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before setting this transform.
     *
     * @level hidden
     */
    @Override
    public void setToQuadrantRotation(int numquadrants, double anchorx, double anchory) {
        checkPermission();
        super.setToQuadrantRotation(numquadrants, anchorx, anchory);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before setting this transform.
     *
     * @level hidden
     */
    @Override
    public void setToScale(double sx, double sy) {
        checkPermission();
        super.setToScale(sx, sy);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before setting this transform.
     *
     * @level hidden
     */
    @Override
    public void setToShear(double shx, double shy) {
        checkPermission();
        super.setToShear(shx, shy);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before setting this transform.
     *
     * @level hidden
     */
    @Override
    public void setTransform(AffineTransform Tx) {
        checkPermission();
        super.setTransform(Tx);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before setting this transform.
     *
     * @level hidden
     */
    @Override
    public void setTransform(double m00, double m10,
                             double m01, double m11,
                             double m02, double m12) {
        checkPermission();
        super.setTransform(m00, m10, m01, m11, m02, m12);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before concatenating this transform.
     *
     * @level hidden
     */
    @Override
    public void concatenate(AffineTransform Tx) {
        checkPermission();
        super.concatenate(Tx);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before concatenating this transform.
     *
     * @level hidden
     */
    @Override
    public void preConcatenate(AffineTransform Tx) {
        checkPermission();
        super.preConcatenate(Tx);
    }

    /**
     * Checks for {@linkplain #checkPermission permission} before inverting this transform.
     *
     * @level hidden
     */
    @Override
    public void invert() throws NoninvertibleTransformException {
        checkPermission();
        super.invert();
    }

    /**
     * Checks whether or not this {@code XAffineTransform} is the identity by
     * using the provided {@code tolerance}.
     *
     * @param tolerance The tolerance to use for this check.
     * @return {@code true} if the transform is identity, {@code false} otherwise.
     *
     * @since 2.3.1
     */
    public boolean isIdentity(double tolerance) {
        return isIdentity(this, tolerance);
    }

    /**
     * Returns {@code true} if the specified affine transform is an identity transform up to the
     * specified tolerance. This method is equivalent to computing the difference between this
     * matrix and an identity matrix (as created by {@link AffineTransform#AffineTransform()
     * new AffineTransform()}) and returning {@code true} if and only if all differences are
     * smaller than or equal to {@code tolerance}.
     * <p>
     * This method is used for working around rounding error in affine transforms resulting
     * from a computation, as in the example below:
     *
     * {@preformat text
     *     ┌                                                     ┐
     *     │ 1.0000000000000000001  0.0                      0.0 │
     *     │ 0.0                    0.999999999999999999999  0.0 │
     *     │ 0.0                    0.0                      1.0 │
     *     └                                                     ┘
     * }
     *
     * @param tr The affine transform to be checked for identity.
     * @param tolerance The tolerance value to use when checking for identity.
     * @return {@code true} if this tranformation is close enough to the
     *         identity, {@code false} otherwise.
     *
     * @since 2.3.1
     */
    public static boolean isIdentity(final AffineTransform tr, double tolerance) {
        if (tr.isIdentity()) {
            return true;
        }
        tolerance = abs(tolerance);
        return abs(tr.getScaleX() - 1) <= tolerance &&
               abs(tr.getScaleY() - 1) <= tolerance &&
               abs(tr.getShearX())     <= tolerance &&
               abs(tr.getShearY())     <= tolerance &&
               abs(tr.getTranslateX()) <= tolerance &&
               abs(tr.getTranslateY()) <= tolerance;
    }

    /**
     * Transforms the given shape. This method is similar to
     * {@link #createTransformedShape createTransformedShape} except that:
     * <p>
     * <ul>
     *   <li>It tries to preserve the shape kind when possible. For example if the given shape
     *       is an instance of {@link RectangularShape} and the given transform do not involve
     *       rotation, then the returned shape may be some instance of the same class.</li>
     *   <li>It tries to recycle the given object if {@code overwrite} is {@code true}.</li>
     * </ul>
     *
     * @param transform Affine transform to use.
     * @param shape     The shape to transform.
     * @param overwrite If {@code true}, this method is allowed to overwrite {@code shape} with the
     *                  transform result. If {@code false}, then {@code shape} is never modified.
     *
     * @return The direct transform of the given shape. May or may not be the same instance than
     *         the given shape.
     *
     * @see #createTransformedShape
     *
     * @since 2.5
     */
    public static Shape transform(final AffineTransform transform, Shape shape, boolean overwrite) {
        final int type = transform.getType();
        if (type == TYPE_IDENTITY) {
            return shape;
        }
        // If there is only scale, flip, quadrant rotation or translation,
        // then we can optimize the transformation of rectangular shapes.
        if ((type & (TYPE_GENERAL_ROTATION | TYPE_GENERAL_TRANSFORM)) == 0) {
            // For a Rectangle input, the output should be a rectangle as well.
            if (shape instanceof Rectangle2D) {
                final Rectangle2D rect = (Rectangle2D) shape;
                return transform(transform, rect, overwrite ? rect : null);
            }
            // For other rectangular shapes, we restrict to cases without
            // rotation or flip because we don't know if the shape is symetric.
            if ((type & (TYPE_FLIP & TYPE_MASK_ROTATION)) == 0) {
                if (shape instanceof RectangularShape) {
                    RectangularShape rect = (RectangularShape) shape;
                    if (!overwrite) {
                        rect = (RectangularShape) rect.clone();
                    }
                    final Rectangle2D frame = rect.getFrame();
                    rect.setFrame(transform(transform, frame, frame));
                    return rect;
                }
            }
        }
        if (shape instanceof Path2D) {
            final Path2D path = (Path2D) shape;
            if (overwrite) {
                path.transform(transform);
            } else {
                shape = path.createTransformedShape(transform);
            }
        } else if (shape instanceof Area) {
            final Area area = (Area) shape;
            if (overwrite) {
                area.transform(transform);
            } else {
                shape = area.createTransformedArea(transform);
            }
        } else {
            shape = new Path2D.Double(shape, transform);
        }
        return shape;
    }

    /**
     * Returns a rectangle which entirely contains the direct
     * transform of {@code bounds}. This operation is equivalent to:
     *
     * {@preformat java
     *     createTransformedShape(bounds).getBounds2D()
     * }
     *
     * Note that if the source rectangle is an {@linkplain javax.media.jai.PlanarImage#getBounds()
     * image bounds} and the affine transform is a <cite>grid to CRS</cite> transform (i.e. if it
     * converts pixel coordinates to "real world" coordinates), then that transform must map the
     * <strong>upper-left corner</strong> of pixels (as in Java2D usage), not the center of pixels
     * (OGC usage), if the intend is to get the image envelope.
     *
     * @param transform Affine transform to use.
     * @param bounds    Rectangle to transform. This rectangle will not be modified except
     *                  if {@code dest} is the same reference.
     * @param dest      Rectangle in which to place the result.
     *                  If null, a new rectangle will be created.
     *
     * @return The direct transform of the {@code bounds} rectangle.
     *
     * @see org.geotoolkit.referencing.CRS#transform(
     *      org.opengis.referencing.operation.MathTransform2D, Rectangle2D, Rectangle2D)
     */
    public static Rectangle2D transform(final AffineTransform transform,
                                        final Rectangle2D     bounds,
                                        final Rectangle2D     dest)
    {
        double xmin = Double.POSITIVE_INFINITY;
        double ymin = Double.POSITIVE_INFINITY;
        double xmax = Double.NEGATIVE_INFINITY;
        double ymax = Double.NEGATIVE_INFINITY;
        final Point2D.Double point = new Point2D.Double();
        for (int i=0; i<4; i++) {
            point.x = (i & 1) == 0 ? bounds.getMinX() : bounds.getMaxX();
            point.y = (i & 2) == 0 ? bounds.getMinY() : bounds.getMaxY();
            transform.transform(point, point);
            if (point.x < xmin) xmin = point.x;
            if (point.x > xmax) xmax = point.x;
            if (point.y < ymin) ymin = point.y;
            if (point.y > ymax) ymax = point.y;
        }
        if (dest != null) {
            dest.setRect(xmin, ymin, xmax-xmin, ymax-ymin);
            return dest;
        }
        return new Rectangle2D.Double(xmin, ymin, xmax-xmin, ymax-ymin);
    }

    /**
     * Returns a rectangle which entirely contains the inverse
     * transform of {@code bounds}. This operation is equivalent to:
     *
     * {@preformat java
     *     createInverse().createTransformedShape(bounds).getBounds2D()
     * }
     *
     * @param transform Affine transform to use.
     * @param bounds    Rectangle to transform. This rectangle will not be modified.
     * @param dest      Rectangle in which to place the result.  If null, a new
     *                  rectangle will be created.
     *
     * @return The inverse transform of the {@code bounds} rectangle.
     * @throws NoninvertibleTransformException if the affine transform can't be inverted.
     */
    public static Rectangle2D inverseTransform(final AffineTransform transform,
                                               final Rectangle2D     bounds,
                                               final Rectangle2D     dest)
            throws NoninvertibleTransformException
    {
        double xmin = Double.POSITIVE_INFINITY;
        double ymin = Double.POSITIVE_INFINITY;
        double xmax = Double.NEGATIVE_INFINITY;
        double ymax = Double.NEGATIVE_INFINITY;
        final Point2D.Double point = new Point2D.Double();
        for (int i=0; i<4; i++) {
            point.x = (i&1)==0 ? bounds.getMinX() : bounds.getMaxX();
            point.y = (i&2)==0 ? bounds.getMinY() : bounds.getMaxY();
            transform.inverseTransform(point, point);
            if (point.x < xmin) xmin = point.x;
            if (point.x > xmax) xmax = point.x;
            if (point.y < ymin) ymin = point.y;
            if (point.y > ymax) ymax = point.y;
        }
        if (dest != null) {
            dest.setRect(xmin, ymin, xmax-xmin, ymax-ymin);
            return dest;
        }
        return new Rectangle2D.Double(xmin, ymin, xmax-xmin, ymax-ymin);
    }

    /**
     * Calculates the inverse affine transform of a point without without
     * applying the translation components.
     *
     * @param transform Affine transform to use.
     * @param source    Point to transform. This rectangle will not be modified.
     * @param dest      Point in which to place the result.  If {@code null}, a
     *                  new point will be created.
     *
     * @return The inverse transform of the {@code source} point.
     * @throws NoninvertibleTransformException if the affine transform can't be inverted.
     */
    public static Point2D inverseDeltaTransform(final AffineTransform transform,
                                                final Point2D         source,
                                                final Point2D         dest)
            throws NoninvertibleTransformException
    {
        final double m00 = transform.getScaleX();
        final double m11 = transform.getScaleY();
        final double m01 = transform.getShearX();
        final double m10 = transform.getShearY();
        final double det = m00*m11 - m01*m10;
        if (!(abs(det) > Double.MIN_VALUE)) {
            return transform.createInverse().deltaTransform(source, dest);
        }
        final double x0 = source.getX();
        final double y0 = source.getY();
        final double x = (x0*m11 - y0*m01) / det;
        final double y = (y0*m00 - x0*m10) / det;
        if (dest != null) {
            dest.setLocation(x, y);
            return dest;
        }
        return new Point2D.Double(x, y);
    }

    /**
     * Returns an estimation about whatever the specified transform swaps <var>x</var>
     * and <var>y</var> axis. This method assumes that the specified affine transform
     * is built from arbitrary translations, scales or rotations, but no shear. It
     * returns {@code +1} if the (<var>x</var>, <var>y</var>) axis order seems to be
     * preserved, {@code -1} if the transform seems to swap axis to the (<var>y</var>,
     * <var>x</var>) axis order, or {@code 0} if this method can not make a decision.
     *
     * @param tr The affine transform to inspect.
     * @return {@code true} if the given transform seems to swap axis order.
     */
    public static int getSwapXY(final AffineTransform tr) {
        final int flip = getFlip(tr);
        if (flip != 0) {
            final double scaleX = getScaleX0(tr);
            final double scaleY = getScaleY0(tr) * flip;
            final double y = abs(tr.getShearY()/scaleY - tr.getShearX()/scaleX);
            final double x = abs(tr.getScaleY()/scaleY + tr.getScaleX()/scaleX);
            if (x > y) return +1;
            if (x < y) return -1;
            // At this point, we may have (x == y) or some NaN value.
        }
        return 0;
    }

    /**
     * Returns an estimation of the rotation angle in radians. This method assumes that the
     * specified affine transform is built from arbitrary translations, scales or rotations,
     * but no shear. If a flip has been applied, then this method assumes that the flipped
     * axis is the <var>y</var> one in <cite>source CRS</cite> space. For a <cite>grid to
     * world CRS</cite> transform, this is the row number in grid coordinates.
     *
     * @param  tr The affine transform to inspect.
     * @return An estimation of the rotation angle in radians, or {@link Double#NaN NaN}
     *         if the angle can not be estimated.
     */
    public static double getRotation(final AffineTransform tr) {
        final int flip = getFlip(tr);
        if (flip != 0) {
            final double scaleX = getScaleX0(tr);
            final double scaleY = getScaleY0(tr) * flip;
            return atan2(tr.getShearY()/scaleY - tr.getShearX()/scaleX,
                         tr.getScaleY()/scaleY + tr.getScaleX()/scaleX);
        }
        return Double.NaN;
    }

    /**
     * Returns {@code -1} if one axis has been flipped, {@code +1} if no axis has been flipped,
     * or 0 if unknown. A flipped axis in an axis with direction reversed (typically the
     * <var>y</var> axis). This method assumes that the specified affine transform is built
     * from arbitrary translations, scales or rotations, but no shear. Note that it is not
     * possible to determine which of the <var>x</var> or <var>y</var> axis has been flipped.
     * <p>
     * This method can be used in order to set the sign of a scale according the flipping state.
     * The example below choose to apply the sign on the <var>y</var> scale, but this is an
     * arbitrary (while common) choice:
     *
     * {@preformat java
     *     double scaleX0 = getScaleX0(transform);
     *     double scaleY0 = getScaleY0(transform);
     *     int    flip    = getFlip(transform);
     *     if (flip != 0) {
     *         scaleY0 *= flip;
     *         // ... continue the process here.
     *     }
     * }
     *
     * This method is similar to the following code, except that this method
     * distinguish between "unflipped" and "unknow" states.
     *
     * {@preformat java
     *     boolean flipped = (tr.getType() & TYPE_FLIP) != 0;
     * }
     *
     * @param tr The affine transform to inspect.
     * @return -1 if an axis has been flipped, +1 if no flipping, or 0 if unknown.
     */
    public static int getFlip(final AffineTransform tr) {
        final int scaleX = XMath.sgn(tr.getScaleX());
        final int scaleY = XMath.sgn(tr.getScaleY());
        final int shearX = XMath.sgn(tr.getShearX());
        final int shearY = XMath.sgn(tr.getShearY());
        if (scaleX ==  scaleY && shearX == -shearY) return +1;
        if (scaleX == -scaleY && shearX ==  shearY) return -1;
        return 0;
    }

    /**
     * Returns the magnitude of scale factor <var>x</var> by cancelling the
     * effect of eventual flip and rotation. This factor is calculated by:
     * <p>
     * <center><img src="doc-files/scaleX0.png"></center>
     *
     * @param tr The affine transform to inspect.
     * @return The magnitude of scale factor <var>x</var>.
     */
    public static double getScaleX0(final AffineTransform tr) {
        final double scale = tr.getScaleX();
        final double shear = tr.getShearX();
        if (shear == 0) return abs(scale);  // Optimization for a very common case.
        if (scale == 0) return abs(shear);  // Not as common as above, but still common enough.
        return hypot(scale, shear);
    }

    /**
     * Returns the magnitude of scale factor <var>y</var> by cancelling the
     * effect of eventual flip and rotation. This factor is calculated by:
     * <p>
     * <center><img src="doc-files/scaleY0.png"></center>
     *
     * @param tr The affine transform to inspect.
     * @return The magnitude of scale factor <var>y</var>.
     */
    public static double getScaleY0(final AffineTransform tr) {
        final double scale = tr.getScaleY();
        final double shear = tr.getShearY();
        if (shear == 0) return abs(scale);  // Optimization for a very common case.
        if (scale == 0) return abs(shear);  // Not as common as above, but still common enough.
        return hypot(scale, shear);
    }

    /**
     * Returns a global scale factor for the specified affine transform. This scale factor combines
     * {@link #getScaleX0 getScaleX0(tr)} and {@link #getScaleY0 getScaleY0(tr)}. The way to compute
     * such a "global" scale is somewhat arbitrary and may change in a future version.
     *
     * @param tr The affine transform to inspect.
     * @return The magnitude of scale factory.
     */
    public static double getScale(final AffineTransform tr) {
        return 0.5 * (getScaleX0(tr) + getScaleY0(tr));
    }

    /**
     * Returns an affine transform representing a zoom carried out around a
     * anchor point (<var>x</var>, <var>y</var>). The transforms will leave
     * the specified anchor coordinate unchanged.
     *
     * @param sx Scale along <var>x</var> axis.
     * @param sy Scale along <var>y</var> axis.
     * @param anchorx <var>x</var> coordinates of the anchor point.
     * @param anchory <var>y</var> coordinates of the anchor point.
     * @return Affine transform of a zoom which leaves the
     *         anchor (<var>x</var>,<var>y</var>) coordinate unchanged.
     */
    public static AffineTransform getScaleInstance(final double sx, final double sy,
                                                   final double anchorx, final double anchory)
    {
        return new AffineTransform(sx, 0, 0, sy, (1-sx)*anchorx, (1-sy)*anchory);
    }

    /**
     * If scale and shear coefficients are close to integers, replaces their current values by
     * their rounded values. The scale and shear coefficients are handled in a "all or nothing"
     * way; either all of them are rounded, or either none of them. The translation terms are
     * handled separatly, provided that the scale and shear coefficients has been rounded.
     * <p>
     * This rounding up is useful for example in order to speedup image displays.
     *
     * @param tr The matrix to round. Rounding will be applied in place.
     * @param tolerance The maximal departure from integers in order to allow rounding.
     *        It is typically a small number like {@code 1E-6}.
     *
     * @see org.geotoolkit.image.io.metadata.MetadataHelper#adjustForRoundingError(double)
     *
     * @since 2.3.1
     */
    public static void round(final AffineTransform tr, final double tolerance) {
        double r;
        final double m00, m01, m10, m11;
        if (abs((m00 = rint(r=tr.getScaleX())) - r) <= tolerance &&
            abs((m01 = rint(r=tr.getShearX())) - r) <= tolerance &&
            abs((m11 = rint(r=tr.getScaleY())) - r) <= tolerance &&
            abs((m10 = rint(r=tr.getShearY())) - r) <= tolerance)
        {
            /*
             * At this point the scale and shear coefficients can been rounded to integers.
             * Continue only if this rounding doesn't lead to a non-invertible transform.
             */
            if ((m00!=0 || m01!=0) && (m10!=0 || m11!=0)) {
                double m02, m12;
                if (abs((r = rint(m02=tr.getTranslateX())) - m02) <= tolerance) m02=r;
                if (abs((r = rint(m12=tr.getTranslateY())) - m12) <= tolerance) m12=r;
                tr.setTransform(m00, m10, m01, m11, m02, m12);
            }
        }
    }
}
