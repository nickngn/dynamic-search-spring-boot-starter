/*
 * MIT License
 *
 * Copyright (c) [2023] [NickNgn]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.nickngn.dynamicsearch.builder;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Fly-weight holder for {@link DSSpecBuilder} to reuse builder per Entity types
 */
@UtilityClass
public final class DSSpecBuilders {

    private static final ConcurrentHashMap<Class<?>, DSSpecBuilder<?>> BUILDER_MAP = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> DSSpecBuilder<T> getInstance(Class<T> klass) {
        if (BUILDER_MAP.containsKey(klass)) {
            return (DSSpecBuilder<T>) BUILDER_MAP.get(klass);
        }
        DSSpecBuilder<T> specBuilder = new DSSpecBuilder<>();
        BUILDER_MAP.put(klass, specBuilder);
        return specBuilder;
    }

}
