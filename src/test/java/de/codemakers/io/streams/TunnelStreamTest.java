/*
 *     Copyright 2018 - 2019 Paul Hagedorn (Panzer1119)
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

package de.codemakers.io.streams;

import de.codemakers.base.Standard;
import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;
import de.codemakers.io.streams.exceptions.StreamClosedException;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class TunnelStreamTest {
    
    public static final byte ID_1 = 1;
    public static final byte ID_2 = ID_1 + 1;
    
    public static final String FORMAT_RECEIVED = "[ RECEIVER  ][%d]: %s";
    public static final String FORMAT_TRANSMITTED = "[TRANSMITTED][%d]: %s";
    public static final String FORMAT_STOPPED = "[%s] %d STOPPED ";
    
    public static final int PORT = 1454;
    
    public static void main(String[] args) throws Exception {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINE);
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendTimestamp().appendThread().appendText(": ").appendObject().finishWithoutException();
        Logger.log("test");
        Logger.log("test");
        Logger.log(Arrays.toString("test".getBytes()));
        Logger.log("t€st");
        Logger.log(Arrays.toString("t€st".getBytes()));
        //test();
        //final PipedInputStream pipedInputStream = new PipedInputStream();
        //final PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);
        final AtomicReference<InputStream> inputStreamAtomicReference = new AtomicReference<>();
        final AtomicReference<OutputStream> outputStreamAtomicReference = new AtomicReference<>();
        final ServerSocket serverSocket = new ServerSocket(PORT);
        Standard.addShutdownHook(serverSocket::close);
        Standard.async(() -> {
            final Socket socket_out = new Socket(InetAddress.getLocalHost(), PORT);
            outputStreamAtomicReference.set(socket_out.getOutputStream());
            Standard.addShutdownHook(socket_out::close);
        });
        final Socket socket_in = serverSocket.accept();
        inputStreamAtomicReference.set(socket_in.getInputStream());
        Standard.addShutdownHook(socket_in::close);
        Standard.async(() -> {
            Standard.silentSleepWhile(10000, () -> inputStreamAtomicReference.get() == null);
            Standard.silentSleepWhile(10000, () -> outputStreamAtomicReference.get() == null);
            Logger.log("Waiting finished");
            runReceiver(new TunnelInputStream(inputStreamAtomicReference.get()));
            runTransmitter(new TunnelOutputStream(outputStreamAtomicReference.get()));
        });
    }
    
    private static void test() throws Exception {
        final String lineSeparator = java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));
        Logger.logDebug("lineSeparator=" + lineSeparator);
        Logger.logDebug("lineSeparator=" + Arrays.toString(lineSeparator.getBytes()));
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream));
        bufferedWriter.write(getRandomText((byte) 0));
        Logger.logDebug("byteArrayOutputStream=" + Arrays.toString(byteArrayOutputStream.toByteArray()));
        bufferedWriter.flush();
        Logger.logDebug("byteArrayOutputStream=" + Arrays.toString(byteArrayOutputStream.toByteArray()));
        bufferedWriter.newLine();
        Logger.logDebug("byteArrayOutputStream=" + Arrays.toString(byteArrayOutputStream.toByteArray()));
        bufferedWriter.flush();
        Logger.logDebug("byteArrayOutputStream=" + Arrays.toString(byteArrayOutputStream.toByteArray()));
    }
    
    private static void runReceiver(TunnelInputStream tunnelInputStream) throws Exception {
        Logger.log("Starting receiver");
        Logger.logDebug("START tunnelInputStream=" + tunnelInputStream);
        Standard.async(() -> {
            Thread.sleep(1000);
            Logger.logDebug("AFTER START tunnelInputStream=" + tunnelInputStream);
        });
        Standard.async(() -> {
            try {
                final InputStream inputStream = tunnelInputStream.getOrCreateInputStream(ID_1);
                while (Objects.equals(0, 1)) {
                    inputStream.read();
                    //Logger.log("1 READ: " + inputStream.read());
                }
                /*
                final byte[] buffer = new byte[35];
                int times = 0;
                while (true) {
                    final byte[] data = new byte[inputStream.read(buffer)];
                    System.arraycopy(buffer, 0, data, 0, data.length);
                    Logger.log(String.format("[%d][times:%d] 1 buffer=%s", ID_1, times, Arrays.toString(buffer)));
                    Logger.log(String.format("[%d][times:%d] 1 data  =%s", ID_1, times, Arrays.toString(data)));
                    Logger.log(String.format("[%d][times:%d] 2 data  =%s", ID_1, times++, new String(data)));
                }
                */
                final Scanner scanner = new Scanner(inputStream);
                while (scanner.hasNextLine()) {
                    Logger.log(String.format(FORMAT_RECEIVED, ID_1, scanner.nextLine()));
                }
                Logger.logDebug(String.format(FORMAT_STOPPED, " RECEIVER  ", ID_1));
                Logger.logDebug("1 tunnelInputStream=" + tunnelInputStream);
            } catch (Exception ex) {
                if ("Pipe broken".equals(ex.getMessage()) || ex instanceof StreamClosedException) {
                    return;
                }
                Logger.handleError(ex);
            }
        });
        Standard.async(() -> {
            try {
                final InputStream inputStream = tunnelInputStream.getOrCreateInputStream(ID_2);
                final Scanner scanner = new Scanner(inputStream);
                while (scanner.hasNextLine()) {
                    Logger.log(String.format(FORMAT_RECEIVED, ID_2, scanner.nextLine()));
                }
                Logger.logDebug(String.format(FORMAT_STOPPED, " RECEIVER  ", ID_2));
                Logger.logDebug("2 tunnelInputStream=" + tunnelInputStream);
            } catch (Exception ex) {
                Logger.handleError(ex);
            }
        });
    }
    
    private static void runTransmitter(TunnelOutputStream tunnelOutputStream) throws Exception {
        Logger.log("Starting transmitter");
        Logger.logDebug("START tunnelOutputStream=" + tunnelOutputStream);
        Standard.async(() -> {
            Thread.sleep(1000);
            Logger.logDebug("AFTER START tunnelOutputStream=" + tunnelOutputStream);
        });
        Standard.async(() -> {
            final OutputStream outputStream = tunnelOutputStream.getOrCreateOutputStream(ID_1);
            final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            for (int i = 0; i < 1; i++) {
                final String text = getRandomText(ID_1);
                bufferedWriter.write(text);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                Logger.log(String.format(FORMAT_TRANSMITTED, ID_1, text));
                Thread.sleep(500);
            }
            Thread.sleep(6000);
            outputStream.write(getAllValues());
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Logger.log(String.format(FORMAT_TRANSMITTED, ID_1, Arrays.toString(getAllValues())));
            Thread.sleep(1000);
            try {
                bufferedWriter.close();
            } catch (Exception ex) {
                Logger.logError(ex);
            }
            Logger.logDebug(String.format(FORMAT_STOPPED, "TRANSMITTER", ID_1));
            Logger.logDebug("1 tunnelOutputStream=" + tunnelOutputStream);
        });
        Standard.async(() -> {
            final OutputStream outputStream = tunnelOutputStream.getOrCreateOutputStream(ID_2);
            final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            for (int i = 0; i < 2; i++) {
                final String text = getRandomText(ID_2);
                bufferedWriter.write(text);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                Logger.log(String.format(FORMAT_TRANSMITTED, ID_2, text));
                Thread.sleep(500);
            }
            Thread.sleep(2000);
            try {
                bufferedWriter.close();
            } catch (Exception ex) {
                Logger.logError(ex);
            }
            Logger.logDebug(String.format(FORMAT_STOPPED, "TRANSMITTER", ID_2));
            Logger.logDebug("2 tunnelOutputStream=" + tunnelOutputStream);
        });
    }
    
    private static String getRandomText(byte id) {
        return "lol" + id + "ran" + id + "dom" + id + ": " + Math.random();
    }
    
    public static byte[] getAllValues() {
        final byte[] bytes = new byte[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
        int counter = 0;
        for (byte b = Byte.MIN_VALUE; b <= Byte.MAX_VALUE; b++) {
            bytes[counter++] = b;
            if (b == Byte.MAX_VALUE) {
                break;
            }
        }
        return bytes;
    }
    
}
