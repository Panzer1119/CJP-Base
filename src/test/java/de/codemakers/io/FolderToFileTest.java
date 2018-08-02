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

package de.codemakers.io;

import de.codemakers.base.logger.Logger;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;

public class FolderToFileTest {
    
    public static final byte BYTE_FILE = 0xF;
    public static final byte BYTE_NOTHING = 0x0;
    public static final byte BYTE_FOLDER = -0xF;
    public static final byte BYTE_FOLDER_END = -0x8;
    
    public static final void main(String[] args) throws Exception {
        final int i = 23645;
        System.out.println("i = " + i);
        final LinkedList<Boolean> i_ = new LinkedList<>();
        integerToBits(i, i_);
        i_.forEach((b) -> {
            if (b) {
                System.out.print("1");
            } else {
                System.out.print("0");
            }
        });
        System.out.println();
        System.out.println("00000000000000000101110001011101");
        //System.exit(0);
        final File folder = new File(args.length == 0 ? "" : args[0]).getAbsoluteFile();
        File output = new File(folder.getParentFile().getAbsolutePath() + File.separator + folder.getName() + ".txt");
        if (args.length >= 2) {
            output = new File(args[1]);
        }
        System.out.println("LISTING: " + folder);
        final LinkedList<Boolean> buffer = new LinkedList<>();
        listFolder(folder, buffer);
        buffer.forEach((b) -> {
            if (b) {
                //System.out.print("1");
            } else {
                //System.out.print("0");
            }
        });
        final boolean save = true;
        if (!save) {
            System.out.println();
            File parent_ = folder.getParentFile();
            File folder_ = folder;
            while (!buffer.isEmpty()) {
                final byte b = bitsToByte(buffer);
                if (b == BYTE_FILE) {
                    bitsToFile(folder_, buffer);
                } else if (b == BYTE_FOLDER) {
                    folder_ = bitsToFolder(folder_, buffer);
                    parent_ = folder_.getParentFile();
                } else {
                    System.err.println("UNKNOWN FORMAT: " + b);
                }
                final byte b_ = bitsToByte(buffer);
                if (b_ == BYTE_FOLDER_END) {
                    folder_ = parent_;
                    parent_ = folder_.getParentFile();
                } else if (b_ != BYTE_NOTHING) {
                    System.err.println("UNKNOWN END: " + b_);
                }
            }
        } else {
            //System.out.println(String.format("buffer.size() = %d, buffer.size() / 8 = %d, (buffer.size() / 8) * 8 = %d, buffer.size() - ((buffer.size() / 8) * 8) = %d", buffer.size(), buffer.size() / 8, (buffer.size() / 8) * 8, buffer.size() - ((buffer.size() / 8) * 8)));
            final long start_writing = System.currentTimeMillis();
            System.out.println("Start Writing " + start_writing + ": " + output);
            byte[] data = bitsToBytes(buffer.size() / 8, buffer);
            final int remaining = buffer.size() - ((buffer.size() / 8) * 8);
            if (remaining > 0) {
                for (int i__ = 0; i__ < (8 - remaining); i__++) {
                    buffer.addFirst(false);
                }
                data = Arrays.copyOf(data, data.length + 1);
                final byte b = bitsToByte(buffer);
                data[data.length - 1] = b;
            }
            if (!buffer.isEmpty()) {
                System.err.println("Buffer is not completely empty");
                System.exit(-1);
            }
            Files.write(output.toPath(), data);
            final long duration = System.currentTimeMillis() - start_writing;
            System.out.println("Time Taken Writing: " + duration + "ms");
        }
    }
    
    public static final void listFolder(File folder, LinkedList<Boolean> buffer) {
        final File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            final File file = files[i];
            if (file.isDirectory()) {
                folderToBits(file, buffer);
                byteToBits(BYTE_NOTHING, buffer);
                listFolder(file, buffer);
            } else if (file.isFile()) {
                fileToBits(file, buffer);
                byteToBits((i < (files.length - 1)) ? BYTE_NOTHING : BYTE_FOLDER_END, buffer);
            }
        }
    }
    
    public static final void folderToBits(File folder, LinkedList<Boolean> buffer) { //name, canExecute, canRead, canWrite, lastModified
        final String name = folder.getName();
        final long lastModified = folder.lastModified();
        final boolean canExecute = folder.canExecute();
        final boolean canRead = folder.canRead();
        final boolean canWrite = folder.canWrite();
        final byte[] name_ = name.getBytes();
        byteToBits(BYTE_FOLDER, buffer);
        integerToBits(name_.length, buffer);
        bytesToBits(name_, buffer);
        buffer.add(canExecute);
        buffer.add(canRead);
        buffer.add(canWrite);
        longToBits(lastModified, buffer);
    }
    
    public static final void fileToBits(File file, LinkedList<Boolean> buffer) { //name, length, canExecute, canRead, canWrite, lastModified
        final String name = file.getName();
        final long length = file.length();
        final long lastModified = file.lastModified();
        final boolean canExecute = file.canExecute();
        final boolean canRead = file.canRead();
        final boolean canWrite = file.canWrite();
        final byte[] name_ = name.getBytes();
        byteToBits(BYTE_FILE, buffer);
        integerToBits(name_.length, buffer);
        bytesToBits(name_, buffer);
        buffer.add(canExecute);
        buffer.add(canRead);
        buffer.add(canWrite);
        longToBits(lastModified, buffer);
        longToBits(length, buffer);
        byte[] data = new byte[(int) length];
        try {
            data = Files.readAllBytes(file.toPath());
        } catch (Exception ex) {
            Logger.handleError(ex);
        }
        bytesToBits(data, buffer);
    }
    
    public static final void integerToBits(int t, LinkedList<Boolean> buffer) {
        for (int i = 31; i >= 0; i--) {
            buffer.add((t >> i & 1) == 1);
        }
    }
    
    public static final void longToBits(long t, LinkedList<Boolean> buffer) {
        for (int i = 63; i >= 0; i--) {
            buffer.add((t >> i & 1) == 1);
        }
    }
    
    public static final void bytesToBits(byte[] bytes, LinkedList<Boolean> buffer) {
        for (byte b : bytes) {
            byteToBits(b, buffer);
        }
    }
    
    public static final void byteToBits(byte b, LinkedList<Boolean> buffer) {
        for (int i = 7; i >= 0; i--) {
            buffer.add((b >> i & 1) == 1);
        }
    }
    
    public static final File bitsToFolder(File folder, LinkedList<Boolean> buffer) {
        final int name_length = bitsToInteger(buffer);
        final byte[] name_ = bitsToBytes(name_length, buffer);
        final boolean canExecute = buffer.removeFirst();
        final boolean canRead = buffer.removeFirst();
        final boolean canWrite = buffer.removeFirst();
        final long lastModified = bitsToLong(buffer);
        final String name = new String(name_);
        System.out.println(String.format("FOLDER ce: %5b, cr: %5b, cw: %5b, lm: %12d, \"%20s\", parent: \"%20s\"", canExecute, canRead, canWrite, lastModified, name, folder));
        return new File(folder.getAbsolutePath() + File.separator + name);
    }
    
    public static final File bitsToFile(File folder, LinkedList<Boolean> buffer) {
        final int name_length = bitsToInteger(buffer);
        final byte[] name_ = bitsToBytes(name_length, buffer);
        final boolean canExecute = buffer.removeFirst();
        final boolean canRead = buffer.removeFirst();
        final boolean canWrite = buffer.removeFirst();
        final long lastModified = bitsToLong(buffer);
        final String name = new String(name_);
        final long length = bitsToLong(buffer);
        final byte[] data = bitsToBytes((int) length, buffer);
        System.out.println(String.format("File   ce: %5b, cr: %5b, cw: %5b, lm: %12d, \"%20s\", parent: \"%20s\", length: %10d, data.length: %10d", canExecute, canRead, canWrite, lastModified, name, folder, length, data.length));
        return new File(folder.getAbsolutePath() + File.separator + name);
    }
    
    public static final int bitsToInteger(LinkedList<Boolean> buffer) {
        int t = 0;
        for (int i = 31; i >= 0; i--) {
            t |= ((buffer.removeFirst() ? 1 : 0) << i);
        }
        return t;
    }
    
    public static final long bitsToLong(LinkedList<Boolean> buffer) {
        long t = 0;
        for (int i = 63; i >= 0; i--) {
            t |= ((buffer.removeFirst() ? 1 : 0) << i);
        }
        return t;
    }
    
    public static final byte[] bitsToBytes(int length, LinkedList<Boolean> buffer) {
        final byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = bitsToByte(buffer);
        }
        return bytes;
    }
    
    public static final byte bitsToByte(LinkedList<Boolean> buffer) {
        byte t = 0;
        for (int i = 7; i >= 0; i--) {
            t |= ((buffer.removeFirst() ? 1 : 0) << i);
        }
        return t;
    }
    
}
