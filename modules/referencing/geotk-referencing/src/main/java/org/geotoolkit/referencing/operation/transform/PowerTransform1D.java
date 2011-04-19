/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2011, Geomatys
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

import java.io.Serializable;
import net.jcip.annotations.Immutable;

import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform1D;

import org.geotoolkit.util.Utilities;


/**
 * Raises the given value at some fixed power. Current implementation is defined mostly for the
 * needs of the {@link ExponentialTransform1D#concatenateLog(LogarithmicTransform1D, boolean)}.
 * Future version may expand on that.
 * <p>
 * Before to make this class public, we need to revisit the class name, define parameters and
 * improve the {@link #concatenate(MathTransform, boolean)} method.
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.17
 *
 * @since 3.17
 * @module
 */
@Immutable
final class PowerTransform1D extends AbstractMathTransform1D implements Serializable {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = 4618931749313510016L;

    /**
     * The power.
     */
    public final double power;

    /**
     * The inverse of this transform. Created only when first needed.
     * Serialized in order to avoid rounding error if this transform
     * is actually the one which was created from the inverse.
     */
    private PowerTransform1D inverse;

    /**
     * Constructs a new exponential transform. This constructor is provided for subclasses only.
     * Instances should be created using the {@linkplain #create factory method}, which
     * may returns optimized implementations for some particular argument values.
     *
     * @param power The power at which to raise the values.
     */
    protected PowerTransform1D(final double power) {
        this.power = power;
    }

    /**
     * Constructs a new power transform.
     *
     * @param power The power at which to raise the values.
     * @return The math transform.
     */
    public static MathTransform1D create(final double power) {
        if (power == 1) return IdentityTransform1D.IDENTITY;
        if (power == 0) return new ConstantTransform1D(1);
        return new PowerTransform1D(power);
    }

    /**
     * Creates the inverse transform of this object.
     */
    @Override
    public MathTransform1D inverse() {
        if (inverse == null) {
            inverse = new PowerTransform1D(1 / power);
            inverse.inverse = this;
        }
        return inverse;
    }

    /**
     * Gets the derivative of this function at a value.
     */
    @Override
    public double derivative(final double value) {
        return power * Math.pow(value, power - 1);
    }

    /**
     * Transforms the specified value.
     */
    @Override
    public double transform(final double value) {
        return Math.pow(value, power);
    }

    /**
     * Transforms a single coordinate in a list of ordinal values.
     */
    @Override
    protected void transform(final double[] srcPts, final int srcOff, final double[] dstPts, final int dstOff) {
        dstPts[dstOff] = Math.pow(srcPts[srcOff], power);
    }

    /**
     * Transforms many coordinates in a list of ordinal values.
     */
    @Override
    public void transform(final double[] srcPts, int srcOff, final double[] dstPts, int dstOff, int numPts) {
        if (srcPts!=dstPts || srcOff>=dstOff) {
            while (--numPts >= 0) {
                dstPts[dstOff++] = Math.pow(srcPts[srcOff++], power);
            }
        } else {
            srcOff += numPts;
            dstOff += numPts;
            while (--numPts >= 0) {
                dstPts[--dstOff] = Math.pow(srcPts[--srcOff], power);
            }
        }
    }

    /**
     * Transforms many coordinates in a list of ordinal values.
     */
    @Override
    public void transform(final float[] srcPts, int srcOff, final float[] dstPts, int dstOff, int numPts) {
        if (srcPts!=dstPts || srcOff>=dstOff) {
            while (--numPts >= 0) {
                dstPts[dstOff++] = (float) Math.pow(srcPts[srcOff++], power);
            }
        } else {
            srcOff += numPts;
            dstOff += numPts;
            while (--numPts >= 0) {
                dstPts[--dstOff] = (float) Math.pow(srcPts[--srcOff], power);
            }
        }
    }

    /**
     * Transforms many coordinates in a list of ordinal values.
     */
    @Override
    public void transform(final double[] srcPts, int srcOff, final float[] dstPts, int dstOff, int numPts) {
        while (--numPts >= 0) {
            dstPts[dstOff++] = (float) Math.pow(srcPts[srcOff++], power);
        }
    }

    /**
     * Transforms many coordinates in a list of ordinal values.
     */
    @Override
    public void transform(final float[] srcPts, int srcOff, final double[] dstPts, int dstOff, int numPts) {
        while (--numPts >= 0) {
            dstPts[dstOff++] = Math.pow(srcPts[srcOff++], power);
        }
    }

    /**
     * Concatenates in an optimized way a {@link MathTransform} {@code other} to this
     * {@code MathTransform}.
     *
     * @param  other The math transform to apply.
     * @param  applyOtherFirst {@code true} if the transformation order is {@code other}
     *         followed by {@code this}, or {@code false} if the transformation order is
     *         {@code this} followed by {@code other}.
     * @return The combined math transform, or {@code null} if no optimized combined
     *         transform is available.
     */
    @Override
    final MathTransform concatenate(final MathTransform other, final boolean applyOtherFirst) {
        if (other instanceof PowerTransform1D) {
            return create(power + ((PowerTransform1D) other).power);
        }
        // TODO: more optimization could go here for logarithmic and exponential cases.
        return super.concatenate(other, applyOtherFirst);
    }

    /**
     * Returns a hash value for this transform. This value need not remain
     * consistent between different implementations of the same class.
     */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(power);
        return ((int) (code >>> 32)) ^ ((int) code) ^ (int) serialVersionUID;
    }

    /**
     * Compares the specified object with this math transform for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            return Utilities.equals(this.power, ((PowerTransform1D) object).power);
        }
        return false;
    }
}
