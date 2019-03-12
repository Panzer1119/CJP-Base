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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class BufferedPipedOutputStreamTest {
    
    public static final void main(String[] args) throws Exception {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINEST);
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendThread().appendLogLevel().appendSource().appendText(": ").appendObject().finishWithoutException();
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendTimestamp().appendSource().appendText(": ").appendObject().finishWithoutException();
        if (false) {
            test();
            return;
        }
        final BufferedPipedOutputStream bufferedPipedOutputStream = new BufferedPipedOutputStream();
        Logger.log("bufferedPipedOutputStream=" + bufferedPipedOutputStream);
        final PipedInputStream pipedInputStream = new PipedInputStream(bufferedPipedOutputStream);
        Logger.log("pipedInputStream=" + pipedInputStream);
        Standard.async(() -> {
            Logger.log("reading: " + pipedInputStream.read());
            Thread.currentThread().setName("RECEIVER");
            Logger.log("Started Receiving");
            final ObjectInputStream objectInputStream = new ObjectInputStream(pipedInputStream);
            Logger.log("received: \"" + objectInputStream.readObject() + "\"");
            Logger.log("Stopping Receiving");
            objectInputStream.close();
            /*
            Logger.log("Started Receiving");
            final DataInputStream dataInputStream = new DataInputStream(pipedInputStream);
            Logger.log("received: \"" + dataInputStream.readInt() + "\"");
            Logger.log("Stopping Receiving");
            dataInputStream.close();
            */
        });
        Logger.log("Waiting");
        Thread.sleep(500);
        Logger.log("Writing");
        bufferedPipedOutputStream.write(44);
        Thread.sleep(1000);
        Thread.currentThread().setName("TRANSMITTER");
        Logger.log("Started Transmitting");
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedPipedOutputStream);
        /*
        for (int i = 0; i < 1; i++) {
            final TestObject testObject = new TestObject();
            Logger.log("transmitting: \"" + testObject + "\"");
            objectOutputStream.writeObject(testObject);
        }
        */
        final TestObject testObject = new TestObject();
        Logger.log("transmitting: \"" + testObject + "\"");
        objectOutputStream.writeObject(testObject);
        Thread.sleep(1000);
        Logger.log("Stopping Transmitting");
        objectOutputStream.close();
        /*
        Logger.log("Started Transmitting");
        final DataOutputStream dataOutputStream = new DataOutputStream(bufferedPipedOutputStream);
        Logger.log("transmitting: \"42\"");
        dataOutputStream.writeInt(42);
        Thread.sleep(1000);
        Logger.log("Stopping Transmitting");
        dataOutputStream.close();
        */
    }
    
    private static void test() throws Exception {
        final BufferedPipedOutputStream bufferedPipedOutputStream = new BufferedPipedOutputStream();
        final PipedInputStream pipedInputStream = new PipedInputStream(bufferedPipedOutputStream);
        Standard.async(() -> {
            final byte[] buffer = new byte[16];
            int read = -1;
            while ((read = pipedInputStream.read(buffer)) >= 0) {
                Logger.log(String.format("buffer=%s, read=%d", Arrays.toString(buffer), read));
            }
            pipedInputStream.close();
        });
        final Random random = new Random();
        final byte[] temp = new byte[64];
        Logger.log("Start");
        for (int i = 0; i < 8; i++) {
            random.nextBytes(temp);
            Logger.log(String.format("temp=%s", Arrays.toString(temp)));
            bufferedPipedOutputStream.write(temp);
        }
        Logger.log("Stop");
        bufferedPipedOutputStream.close();
    }
    
    public static class TestObject implements Serializable {
        
        public final double random;
        
        public TestObject() {
            this(Math.random());
        }
        
        public TestObject(double random) {
            this.random = random;
        }
        
        @Override
        public String toString() {
            return "TestObject{" + "random=" + random + '}';
        }
        
    }
    
}
