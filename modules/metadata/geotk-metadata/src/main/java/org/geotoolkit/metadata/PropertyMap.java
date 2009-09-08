/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2007-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotoolkit.metadata;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.geotoolkit.util.Utilities;


/**
 * A view of a metadata object as a map. Keys are property names and values
 * are the value returned by the {@code getFoo()} method using reflection.
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.03
 *
 * @since 2.4
 * @module
 *
 * @see MetadataStandard#asMap
 */
final class PropertyMap extends MetadataMap<Object> {
    /**
     * The metadata object to wrap.
     */
    private final Object metadata;

    /**
     * The behavior of this map toward null or empty values.
     */
    final NullValuePolicy content;

    /**
     * Creates a property map for the specified metadata and accessor.
     *
     * @param metadata The metadata object to wrap.
     * @param accessor The accessor to use for the metadata.
     * @param content  The behavior of this map toward null or empty values.
     * @param keyNames Determines the string representation of keys in the map..
     */
    PropertyMap(final Object metadata, final PropertyAccessor accessor,
            final NullValuePolicy content, final KeyNamePolicy keyNames)
    {
        super(accessor, keyNames);
        this.metadata = metadata;
        this.content  = content;
    }

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     */
    @Override
    public boolean isEmpty() {
        return accessor.count(metadata, 1) == 0;
    }

    /**
     * Returns the number of elements in this map.
     */
    @Override
    public int size() {
        return accessor.count(metadata, Integer.MAX_VALUE);
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key.
     */
    @Override
    public boolean containsKey(final Object key) {
        return get(key) != null;
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null}
     * if this map contains no mapping for the key.
     */
    @Override
    public Object get(final Object key) {
        if (key instanceof String) {
            final Object value = accessor.get(accessor.indexOf((String) key), metadata);
            switch (content) {
                case NON_EMPTY: {
                    // For now this is the only enum requerying a special handling.
                    if (PropertyAccessor.isEmpty(value)) {
                        return null;
                    }
                    break;
                }
            }
            return value;
        }
        return null;
    }

    /**
     * Associates the specified value with the specified key in this map.
     *
     * @throws IllegalArgumentException if the specified property can't be set.
     * @throws ClassCastException if the given value is not of the expected type.
     */
    @Override
    public Object put(final String key, final Object value)
            throws IllegalArgumentException, ClassCastException
    {
        return accessor.set(accessor.requiredIndexOf(key), metadata, value, true);
    }

    /**
     * Puts every entries from the given map. This method is overloaded for performance
     * raisons since we are not interested in the return value of the {@link #put} method.
     */
    @Override
    public void putAll(final Map<? extends String, ?> map) {
        for (final Map.Entry<? extends String, ?> e : map.entrySet()) {
            accessor.set(accessor.requiredIndexOf(e.getKey()), metadata, e.getValue(), false);
        }
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     */
    @Override
    public Object remove(final Object key) {
        if (key instanceof String) {
            return put((String) key, null);
        } else {
            return null;
        }
    }

    /**
     * Creates the entry set.
     */
    @Override
    final Set<Map.Entry<String,Object>> entries() {
        return new Entries();
    }

    /**
     * Returns an iterator over the entries contained in this map.
     */
    @Override
    final Iterator<Map.Entry<String,Object>> iterator() {
        return new Iter();
    }




    /**
     * A map entry for a given property.
     *
     * @author Martin Desruisseaux (Geomatys)
     * @version 3.00
     *
     * @since 2.4
     */
    private final class Property implements Map.Entry<String,Object> {
        /**
         * The property index.
         */
        final int index;

        /**
         * Creates an entry for the given property.
         */
        Property(final int index) {
            this.index = index;
        }

        /**
         * Creates an entry for the given property.
         */
        Property(final String property) {
            index = accessor.indexOf(property);
        }

        /**
         * Returns the key corresponding to this entry.
         */
        @Override
        public String getKey() {
            return accessor.name(index, keyNames);
        }

        /**
         * Returns the value corresponding to this entry.
         */
        @Override
        public Object getValue() {
            final Object value = accessor.get(index, metadata);
            switch (content) {
                case NON_EMPTY: {
                    // For now this is the only enum requerying a special handling.
                    if (PropertyAccessor.isEmpty(value)) {
                        return null;
                    }
                    break;
                }
            }
            return value;
        }

        /**
         * Replaces the value corresponding to this entry with the specified value.
         *
         * @throws ClassCastException if the given value is not of the expected type.
         */
        @Override
        public Object setValue(Object value) throws ClassCastException {
            return accessor.set(index, metadata, value, true);
        }

        /**
         * Compares the specified entry with this one for equality.
         */
        public boolean equals(final Map.Entry<?,?> entry) {
            return Utilities.equals(getKey(),   entry.getKey()) &&
                   Utilities.equals(getValue(), entry.getValue());
        }

        /**
         * Compares the specified object with this entry for equality.
         * Criterions are specified by the {@link Map.Entry} contract.
         */
        @Override
        public boolean equals(final Object object) {
            return (object instanceof Map.Entry<?,?>) && equals((Map.Entry<?,?>) object);
        }

        /**
         * Returns the hash code value for this map entry. The
         * formula is specified by the {@link Map.Entry} contract.
         */
        @Override
        public int hashCode() {
            Object x = getKey();
            int code = (x != null) ? x.hashCode() : 0;
            x = getValue();
            if (x != null) {
                code ^= x.hashCode();
            }
            return code;
        }
    }




    /**
     * The iterator over the {@link Property} elements contained in a {@link Entries} set.
     *
     * @author Martin Desruisseaux (Geomatys)
     * @version 3.04
     *
     * @since 2.4
     */
    private final class Iter extends MetadataMap<Object>.Iter {
        /**
         * The current and the next property, or {@code null} if the iteration is over.
         */
        private Property current, next;

        /**
         * Creates en iterator.
         */
        Iter() {
            move(0);
        }

        /**
         * Moves {@link #next} to the first property with a valid value,
         * starting at the specified index.
         */
        private void move(int index) {
            final int count = accessor.count();
            while (index < count) {
                final Object value = accessor.get(index, metadata);
                final boolean skip;
                switch (content) {
                    case ALL: {
                        skip = false; // Never skip entries.
                        break;
                    }
                    case NON_NULL: {
                        skip = (value == null); // Skip only null values (not empty collections).
                        break;
                    }
                    case NON_EMPTY: {
                        skip = PropertyAccessor.isEmpty(value);
                        break;
                    }
                    default: {
                        throw new AssertionError(content);
                    }
                }
                if (!skip) {
                    next = new Property(index);
                    return;
                }
                index++;
            }
            next = null;
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         */
        @Override
        public boolean hasNext() {
            return next != null;
        }

        /**
         * Returns the next element in the iteration.
         */
        @Override
        public Map.Entry<String,Object> next() {
            if (next != null) {
                current = next;
                move(next.index + 1);
                return current;
            } else {
                throw new NoSuchElementException();
            }
        }

        /**
         * Removes from the underlying collection the last element returned by the iterator.
         */
        @Override
        public void remove() {
            if (current != null) {
                current.setValue(null);
                current = null;
            } else {
                throw new IllegalStateException();
            }
        }
    }




    /**
     * View of the entries contained in the map.
     *
     * @author Martin Desruisseaux (Geomatys)
     * @version 3.04
     *
     * @since 2.4
     */
    private final class Entries extends MetadataMap<Object>.Entries {
        /**
         * Creates an entry set.
         */
        Entries() {
        }

        /**
         * Returns {@code true} if this collection contains the specified element.
         */
        @Override
        public boolean contains(final Object object) {
            if (object instanceof Map.Entry<?,?>) {
                final Map.Entry<?,?> entry = (Map.Entry<?,?>) object;
                final Object key = entry.getKey();
                if (key instanceof String) {
                    return new Property((String) key).equals(entry);
                }
            }
            return false;
        }
    }
}
