/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.paimon.data.columnar;

import org.apache.paimon.fs.Path;
import org.apache.paimon.reader.VectorizedRecordIterator;

import javax.annotation.Nullable;

import static org.apache.paimon.utils.Preconditions.checkArgument;

/** A {@link ColumnarRowIterator} with {@link VectorizedRecordIterator}. */
public class VectorizedRowIterator extends ColumnarRowIterator implements VectorizedRecordIterator {

    public VectorizedRowIterator(Path filePath, ColumnarRow row, @Nullable Runnable recycler) {
        super(filePath, row, recycler);
    }

    @Override
    public VectorizedColumnBatch batch() {
        return row.batch();
    }

    @Override
    protected VectorizedRowIterator copy(ColumnVector[] vectors) {
        checkArgument(returnedPositionIndex == 0, "copy() should not be called after next()");
        VectorizedRowIterator newIterator =
                new VectorizedRowIterator(filePath, row.copy(vectors), recycler);
        newIterator.reset(positionIterator);
        return newIterator;
    }
}
