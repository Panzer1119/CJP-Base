/*
 *     Copyright 2018 Paul Hagedorn (Panzer1119)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package de.codemakers.base.util.units;

public enum DataUnit {
    BIT(false, 10, 0, "bit", "b") {
        @Override
        public long toBits(long amount) {
            return amount;
        }
    
        @Override
        public long toBytes(long amount) {
            return amount / 8;
        }
    
        @Override
        public long toKilobits(long amount) {
            return amount / 1_000;
        }
    
        @Override
        public long toKiloBytes(long amount) {
            return amount / 1_000 / 8;
        }
    
        @Override
        public long toMegabits(long amount) {
            return amount / 1_000_000;
        }
    
        @Override
        public long toMegaBytes(long amount) {
            return amount / 1_000_000 / 8;
        }
    
        @Override
        public long toGigabits(long amount) {
            return amount / 1_000_000_000;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount / 1_000_000_000 / 8;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount / 1_000_000_000_000L;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount / 1_000_000_000_000L / 8;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount / 1_000_000_000_000_000L;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount / 1_000_000_000_000_000L / 8;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount / 1_000_000_000_000_000_000L;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount / 1_000_000_000_000_000_000L / 8;
        }
        
        @Override
        public long toZettabits(long amount) {
            throw new UnsupportedOperationException("Bits can not be converted to Zettabits");
        }
        
        @Override
        public long toZettaBytes(long amount) {
            throw new UnsupportedOperationException("Bits can not be converted to Zettabytes");
        }
        
        @Override
        public long toYottabits(long amount) {
            throw new UnsupportedOperationException("Bits can not be converted to Yottabits");
        }
        
        @Override
        public long toYottaBytes(long amount) {
            throw new UnsupportedOperationException("Bits can not be converted to Yottabytes");
        }
    },
    BYTE(true, 10, 0, "byte", "B") {
        @Override
        public long toBits(long amount) {
            return amount * 8;
        }
        
        @Override
        public long toBytes(long amount) {
            return amount;
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount / 1_000 * 8;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount / 1_000_000 * 8;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount / 1_000_000;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount / 1_000_000_000 * 8;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount / 1_000_000_000;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount / 1_000_000_000_000L * 8;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount / 1_000_000_000_000L;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount / 1_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount / 1_000_000_000_000_000L;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount / 1_000_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount / 1_000_000_000_000_000_000L;
        }
        
        @Override
        public long toZettabits(long amount) {
            throw new UnsupportedOperationException("Bytes can not be converted to Zettabits");
        }
        
        @Override
        public long toZettaBytes(long amount) {
            throw new UnsupportedOperationException("Bytes can not be converted to Zettabytes");
        }
        
        @Override
        public long toYottabits(long amount) {
            throw new UnsupportedOperationException("Bytes can not be converted to Yottabits");
        }
        
        @Override
        public long toYottaBytes(long amount) {
            throw new UnsupportedOperationException("Bytes can not be converted to Yottabytes");
        }
    },
    //
    KILOBIT(false, 10, 3, "kilobit", "kb") {
        @Override
        public long toBits(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toBytes(long amount) {
            return amount * 1_000 / 8;
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount / 8;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount / 1_000 / 8;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount / 1_000_000;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount / 1_000_000 / 8;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount / 1_000_000_000;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount / 1_000_000_000 / 8;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount / 1_000_000_000_000L;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount / 1_000_000_000_000L / 8;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount / 1_000_000_000_000_000L;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount / 1_000_000_000_000_000L / 8;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount / 1_000_000_000_000_000_000L;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount / 1_000_000_000_000_000_000L / 8;
        }
        
        @Override
        public long toYottabits(long amount) {
            throw new UnsupportedOperationException("Kilobits can not be converted to Yottabits");
        }
        
        @Override
        public long toYottaBytes(long amount) {
            throw new UnsupportedOperationException("Kilobits can not be converted to Yottabytes");
        }
    },
    KILOBYTE(true, 10, 3, "kilobyte", "kB") {
        @Override
        public long toBits(long amount) {
            return amount * 1_000 * 8;
        }
        
        @Override
        public long toBytes(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount * 8;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount / 1_000 * 8;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount / 1_000_000 * 8;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount / 1_000_000;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount / 1_000_000_000 * 8;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount / 1_000_000_000;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount / 1_000_000_000_000L * 8;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount / 1_000_000_000_000L;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount / 1_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount / 1_000_000_000_000_000L;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount / 1_000_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount / 1_000_000_000_000_000_000L;
        }
        
        @Override
        public long toYottabits(long amount) {
            throw new UnsupportedOperationException("KiloBytes can not be converted to Yottabits");
        }
        
        @Override
        public long toYottaBytes(long amount) {
            throw new UnsupportedOperationException("KiloBytes can not be converted to Yottabytes");
        }
    },
    KIBIBIT(false, 2, 10, "kibibit", "Kib"),
    KIBIBYTE(true, 2, 10, "kibibyte", "KiB"),
    //
    MEGABIT(false, 10, 6, "megabit", "mb") {
        @Override
        public long toBits(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toBytes(long amount) {
            return amount * 1_000_000 / 8;
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount * 1_000 / 8;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount / 8;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount / 1_000 / 8;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount / 1_000_000;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount / 1_000_000 / 8;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount / 1_000_000_000;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount / 1_000_000_000 / 8;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount / 1_000_000_000_000L;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount / 1_000_000_000_000L / 8;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount / 1_000_000_000_000_000L;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount / 1_000_000_000_000_000L / 8;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount / 1_000_000_000_000_000_000L;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount / 1_000_000_000_000_000_000L / 8;
        }
    },
    MEGABYTE(true, 10, 6, "megabyte", "MB") {
        @Override
        public long toBits(long amount) {
            return amount * 1_000_000 * 8;
        }
        
        @Override
        public long toBytes(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount * 1_000 * 8;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount * 8;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount / 1_000 * 8;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount / 1_000_000 * 8;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount / 1_000_000;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount / 1_000_000_000 * 8;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount / 1_000_000_000;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount / 1_000_000_000_000L * 8;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount / 1_000_000_000_000L;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount / 1_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount / 1_000_000_000_000_000L;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount / 1_000_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount / 1_000_000_000_000_000_000L;
        }
    },
    MEBIBIT(false, 2, 20, "mebibit", "Mib"),
    MEBIBYTE(true, 2, 20, "mebibyte", "MiB"),
    //
    GIGABIT(false, 10, 9, "gigabit", "Gb") {
        @Override
        public long toBits(long amount) {
            return amount * 1_000_000_000;
        }
        
        @Override
        public long toBytes(long amount) {
            return amount * 1_000_000_000 / 8;
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount * 1_000_000 / 8;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount * 1_000 / 8;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount / 8;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount / 1_000 / 8;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount / 1_000_000;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount / 1_000_000 / 8;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount / 1_000_000_000;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount / 1_000_000_000 / 8;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount / 1_000_000_000_000L;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount / 1_000_000_000_000L / 8;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount / 1_000_000_000_000_000L;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount / 1_000_000_000_000_000L / 8;
        }
    },
    GIGABYTE(true, 10, 9, "gigabyte", "GB") {
        @Override
        public long toBits(long amount) {
            return amount * 1_000_000_000 * 8;
        }
        
        @Override
        public long toBytes(long amount) {
            return amount * 1_000_000_000;
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount * 1_000_000 * 8;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount * 1_000 * 8;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount * 8;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount / 1_000 * 8;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount / 1_000_000 * 8;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount / 1_000_000;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount / 1_000_000_000 * 8;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount / 1_000_000_000;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount / 1_000_000_000_000L * 8;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount / 1_000_000_000_000L;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount / 1_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount / 1_000_000_000_000_000L;
        }
    },
    GIBIBIT(false, 2, 30, "gibibit", "Gib"),
    GIBIBYTE(true, 2, 30, "gibibyte", "GiB"),
    //
    TERABIT(false, 10, 12, "terabit", "Tb") {
        @Override
        public long toBits(long amount) {
            return amount * 1_000_000_000_000L;
        }
        
        @Override
        public long toBytes(long amount) {
            return amount * 1_000_000_000_000L / 8;
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount * 1_000_000_000;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount * 1_000_000_000 / 8;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount * 1_000_000 / 8;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount * 1_000 / 8;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount / 8;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount / 1_000 / 8;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount / 1_000_000;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount / 1_000_000 / 8;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount / 1_000_000_000;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount / 1_000_000_000 / 8;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount / 1_000_000_000_000L;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount / 1_000_000_000_000L / 8;
        }
    },
    TERABYTE(true, 10, 12, "terabyte", "TB") {
        @Override
        public long toBits(long amount) {
            return amount * 1_000_000_000_000L * 8;
        }
        
        @Override
        public long toBytes(long amount) {
            return amount * 1_000_000_000_000L;
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount * 1_000_000_000 * 8;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount * 1_000_000_000;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount * 1_000_000 * 8;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount * 1_000 * 8;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount * 8;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount / 1_000 * 8;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount / 1_000_000 * 8;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount / 1_000_000;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount / 1_000_000_000 * 8;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount / 1_000_000_000;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount / 1_000_000_000_000L * 8;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount / 1_000_000_000_000L;
        }
    },
    TEBIBIT(false, 2, 40, "tebibit", "Tib"),
    TEBIBYTE(true, 2, 40, "tebibyte", "TiB"),
    //
    PETABIT(false, 10, 15, "petabit", "Pb") {
        @Override
        public long toBits(long amount) {
            return amount * 1_000_000_000_000_000L;
        }
        
        @Override
        public long toBytes(long amount) {
            return amount * 1_000_000_000_000_000L / 8;
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount * 1_000_000_000_000L;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount * 1_000_000_000_000L / 8;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount * 1_000_000_000;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount * 1_000_000_000 / 8;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount * 1_000_000 / 8;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount * 1_000 / 8;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount / 8;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount / 1_000 / 8;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount / 1_000_000;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount / 1_000_000 / 8;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount / 1_000_000_000;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount / 1_000_000_000 / 8;
        }
    },
    PETABYTE(true, 10, 15, "petabyte", "PB") {
        @Override
        public long toBits(long amount) {
            return amount * 1_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toBytes(long amount) {
            return amount * 1_000_000_000_000_000L;
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount * 1_000_000_000_000L * 8;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount * 1_000_000_000_000L;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount * 1_000_000_000 * 8;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount * 1_000_000_000;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount * 1_000_000 * 8;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount * 1_000 * 8;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount * 8;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount / 1_000 * 8;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount / 1_000_000 * 8;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount / 1_000_000;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount / 1_000_000_000 * 8;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount / 1_000_000_000;
        }
    },
    PEBIBIT(false, 2, 50, "pebibit", "Pib"),
    PEBIBYTE(true, 2, 50, "pebibyte", "PiB"),
    //
    EXABIT(false, 10, 18, "exabit", "Eb") {
        @Override
        public long toBits(long amount) {
            return amount * 1_000_000_000_000_000_000L;
        }
        
        @Override
        public long toBytes(long amount) {
            return amount * 1_000_000_000_000_000_000L / 8;
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount * 1_000_000_000_000_000L;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount * 1_000_000_000_000_000L / 8;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount * 1_000_000_000_000L;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount * 1_000_000_000_000L / 8;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount * 1_000_000_000;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount * 1_000_000_000 / 8;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount * 1_000_000 / 8;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount * 1_000 / 8;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount / 8;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount / 1_000 / 8;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount / 1_000_000;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount / 1_000_000 / 8;
        }
    },
    EXABYTE(true, 10, 18, "exabyte", "EB") {
        @Override
        public long toBits(long amount) {
            return amount * 1_000_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toBytes(long amount) {
            return amount * 1_000_000_000_000_000_000L;
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount * 1_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount * 1_000_000_000_000_000L;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount * 1_000_000_000_000L * 8;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount * 1_000_000_000_000L;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount * 1_000_000_000 * 8;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount * 1_000_000_000;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount * 1_000_000 * 8;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount * 1_000 * 8;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount * 8;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount / 1_000 * 8;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount / 1_000_000 * 8;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount / 1_000_000;
        }
    },
    EXBIBIT(false, 2, 60, "exbibit", "Eib"),
    EXBIBYTE(true, 2, 60, "exbibyte", "EiB"),
    //
    ZETTABIT(false, 10, 21, "zettabit", "Zb") {
        @Override
        public long toBits(long amount) {
            throw new UnsupportedOperationException("Zettabits can not be converted to bits");
        }
        
        @Override
        public long toBytes(long amount) {
            throw new UnsupportedOperationException("Zettabits can not be converted to Bytes");
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount * 1_000_000_000_000_000_000L;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount * 1_000_000_000_000_000_000L / 8;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount * 1_000_000_000_000_000L;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount * 1_000_000_000_000_000L / 8;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount * 1_000_000_000_000L;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount * 1_000_000_000_000L / 8;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount * 1_000_000_000;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount * 1_000_000_000 / 8;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount * 1_000_000 / 8;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount * 1_000 / 8;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount / 8;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount / 1_000;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount / 1_000 / 8;
        }
    },
    ZETTABYTE(true, 10, 21, "zettabyte", "ZB") {
        @Override
        public long toBits(long amount) {
            throw new UnsupportedOperationException("ZettaBytes can not be converted to bits");
        }
        
        @Override
        public long toBytes(long amount) {
            throw new UnsupportedOperationException("ZettaBytes can not be converted to Bytes");
        }
        
        @Override
        public long toKilobits(long amount) {
            return amount * 1_000_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toKiloBytes(long amount) {
            return amount * 1_000_000_000_000_000_000L;
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount * 1_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount * 1_000_000_000_000_000L;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount * 1_000_000_000_000L * 8;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount * 1_000_000_000_000L;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount * 1_000_000_000 * 8;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount * 1_000_000_000;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount * 1_000_000 * 8;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount * 1_000 * 8;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount * 8;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount / 1_000 * 8;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount / 1_000;
        }
    },
    ZEBIBIT(false, 2, 70, "zebibit", "Zib"),
    ZEBIBYTE(true, 2, 70, "zebibyte", "ziB"),
    //
    YOTTABIT(false, 10, 24, "yottabit", "Yb") {
        @Override
        public long toBits(long amount) {
            throw new UnsupportedOperationException("Yottabits can not be converted to bits");
        }
        
        @Override
        public long toBytes(long amount) {
            throw new UnsupportedOperationException("Yottabits can not be converted to Bytes");
        }
        
        @Override
        public long toKilobits(long amount) {
            throw new UnsupportedOperationException("Yottabits can not be converted to Kilobits");
        }
        
        @Override
        public long toKiloBytes(long amount) {
            throw new UnsupportedOperationException("Yottabits can not be converted to KiloBytes");
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount * 1_000_000_000_000_000_000L;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount * 1_000_000_000_000_000_000L / 8;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount * 1_000_000_000_000_000L;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount * 1_000_000_000_000_000L / 8;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount * 1_000_000_000_000L;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount * 1_000_000_000_000L / 8;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount * 1_000_000_000;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount * 1_000_000_000 / 8;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount * 1_000_000 / 8;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount * 1_000 / 8;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount / 8;
        }
    },
    YOTTABYTE(true, 10, 24, "yottabyte", "YB") {
        @Override
        public long toBits(long amount) {
            throw new UnsupportedOperationException("YottaBytes can not be converted to bits");
        }
        
        @Override
        public long toBytes(long amount) {
            throw new UnsupportedOperationException("YottaBytes can not be converted to Bytes");
        }
        
        @Override
        public long toKilobits(long amount) {
            throw new UnsupportedOperationException("YottaBytes can not be converted to Kilobits");
        }
        
        @Override
        public long toKiloBytes(long amount) {
            throw new UnsupportedOperationException("YottaBytes can not be converted to KiloBytes");
        }
        
        @Override
        public long toMegabits(long amount) {
            return amount * 1_000_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toMegaBytes(long amount) {
            return amount * 1_000_000_000_000_000_000L;
        }
        
        @Override
        public long toGigabits(long amount) {
            return amount * 1_000_000_000_000_000L * 8;
        }
        
        @Override
        public long toGigaBytes(long amount) {
            return amount * 1_000_000_000_000_000L;
        }
        
        @Override
        public long toTerabits(long amount) {
            return amount * 1_000_000_000_000L * 8;
        }
        
        @Override
        public long toTeraBytes(long amount) {
            return amount * 1_000_000_000_000L;
        }
        
        @Override
        public long toPetabits(long amount) {
            return amount * 1_000_000_000 * 8;
        }
        
        @Override
        public long toPetaBytes(long amount) {
            return amount * 1_000_000_000;
        }
        
        @Override
        public long toExabits(long amount) {
            return amount * 1_000_000 * 8;
        }
        
        @Override
        public long toExaBytes(long amount) {
            return amount * 1_000_000;
        }
        
        @Override
        public long toZettabits(long amount) {
            return amount * 1_000 * 8;
        }
        
        @Override
        public long toZettaBytes(long amount) {
            return amount * 1_000;
        }
        
        @Override
        public long toYottabits(long amount) {
            return amount * 8;
        }
        
        @Override
        public long toYottaBytes(long amount) {
            return amount;
        }
    },
    YOBIBIT(false, 2, 80, "yobibit", "Yib"),
    YOBIBYTE(true, 2, 80, "yobibyte", "YiB");
    
    private final boolean isByte;
    private final int base;
    private final long exponent;
    private final String name;
    private final String symbol;
    
    DataUnit(boolean isByte, int base, long exponent, String name, String symbol) {
        this.isByte = isByte;
        this.base = base;
        this.exponent = exponent;
        this.name = name;
        this.symbol = symbol;
    }
    
    public static DataUnit ofSymbol(String symbol) {
        if (symbol == null || symbol.isEmpty()) {
            return null;
        }
        for (DataUnit dataUnit : values()) {
            if (symbol.equals(dataUnit.getSymbol())) {
                return dataUnit;
            }
        }
        return null;
    }
    
    public static DataUnit ofName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        for (DataUnit dataUnit : values()) {
            if (name.equals(dataUnit.getName())) {
                return dataUnit;
            }
        }
        return null;
    }
    
    public final boolean isByte() {
        return isByte;
    }
    
    public final int getBase() {
        return base;
    }
    
    public final long getExponent() {
        return exponent;
    }
    
    public final String getName() {
        return name;
    }
    
    public final String getSymbol() {
        return symbol;
    }
    
    public final int getMinByteArrayLength(long amount) {
        return (int) Math.ceil(toBytes(amount));
    }
    
    public final byte[] createByteArray(long amount) {
        return new byte[getMinByteArrayLength(amount)];
    }
    
    public long toBits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toKilobits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toKiloBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toMegabits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toMegaBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toGigabits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toGigaBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toTerabits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toTeraBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toPetabits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toPetaBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toExabits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toExaBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toZettabits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toZettaBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toYottabits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toYottaBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toKibibits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toKibiBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toMebibits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toMebiBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toGibibits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toGibiBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toTebibits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toTebiBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toPebibits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toPebiBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toExbibits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toExbiBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toZebibits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toZebiBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toYobibits(long amount) {
        throw new AbstractMethodError();
    }
    
    public long toYobiBytes(long amount) {
        throw new AbstractMethodError();
    }
    
    @Override
    public final String toString() {
        return getClass().getSimpleName() + "{" + "isByte=" + isByte + ", base=" + base + ", exponent=" + exponent + ", name='" + name + '\'' + ", symbol='" + symbol + '\'' + '}';
    }
    
}
