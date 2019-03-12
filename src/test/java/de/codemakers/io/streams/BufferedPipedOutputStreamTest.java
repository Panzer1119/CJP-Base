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

public class BufferedPipedOutputStreamTest {
    
    public static final void main(String[] args) throws Exception {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINEST);
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendThread().appendLogLevel().appendSource().appendText(": ").appendObject().finishWithoutException();
        final BufferedPipedOutputStream bufferedPipedOutputStream = new BufferedPipedOutputStream();
        Logger.log("bufferedPipedOutputStream=" + bufferedPipedOutputStream);
        final PipedInputStream pipedInputStream = new PipedInputStream(bufferedPipedOutputStream);
        Logger.log("pipedInputStream=" + pipedInputStream);
        Standard.async(() -> {
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
    
    public static class TestObject implements Serializable {
        
        private final double random = Math.random();
        
        @Override
        public String toString() {
            return "TestObject{" + "random=" + random + '}';
        }
        
    }
    
}
