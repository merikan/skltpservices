/**
 * Copyright (c) 2012, Sjukvardsradgivningen. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package se.skl.skltpservices.components.analyzer.domain;

import java.util.Iterator;
import java.util.List;

import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;

import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.query.SliceQuery;

/**
 * Iterates over all matching columns, with a batch size.
 * 
 * @author Peter
 *
 * @param <N> the name type
 * @param <V> the value type.
 */
public class ColumnIterator<N, V> implements Iterator<HColumn<N, V>> {
    private N start;
    private int count;
    private Iterator<HColumn<N, V>> columnsIterator;
    private SliceQuery<?, N, V> query;
    private boolean isLastIteration;

    public ColumnIterator(SliceQuery<?, N, V> query) {
    	this(query, null);
    }
    
    public ColumnIterator(SliceQuery<?, N, V> query, N start) {
        this.start = start;
        count = 100;
        columnsIterator = null;
        this.query = query;
        isLastIteration = false;
    }

    public Iterator<HColumn<N, V>> iterator() {
        return this;
    }

    public boolean hasNext() {
        if (columnsIterator == null || !columnsIterator.hasNext()) {
            if (isLastIteration)
                return false;

            if (!fetchMore())
                return false;
        }
        return true;
    }

    public HColumn<N, V> next() {
        return columnsIterator.next();
    }

    private boolean fetchMore() {
        try {
            query.setRange(start, null, false, count);
            ColumnSlice<N, V> slice = query.execute().get();
            List<HColumn<N, V>> columns = slice.getColumns();
            int origSize = columns.size();

            if (origSize == 0) {
                return false;
            }

            if (origSize >= count)
                start = columns.remove(columns.size()-1).getName();

            columnsIterator = columns.iterator();

            if (origSize < count)
                isLastIteration = true;

            return true;
        } catch (HectorException e) {
        	e.printStackTrace();
            return false;
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
