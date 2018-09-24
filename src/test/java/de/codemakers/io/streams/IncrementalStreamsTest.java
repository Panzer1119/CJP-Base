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

package de.codemakers.io.streams;

import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.interfaces.Snowflake;

import java.io.EOFException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;

public class IncrementalStreamsTest {
    
    public static final void main(String[] args) throws Exception {
        final PipedOutputStream pipedOutputStream = new PipedOutputStream();
        final PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
        final IncrementalObjectOutputStream<TestObject> incrementalObjectOutputStream = new IncrementalObjectOutputStream<>(pipedOutputStream);
        final IncrementalObjectInputStream<TestObject> incrementalObjectInputStream = new IncrementalObjectInputStream<>(pipedInputStream);
        new Thread(() -> {
            try {
                System.out.println("TEST started");
                TestObject testObject = null;
                while (true) {
                    testObject = incrementalObjectInputStream.readIncrementalObject();
                    System.out.println("TEST: " + testObject);
                }
                //System.out.println("TEST stopped");
            } catch (EOFException ignore) {
                System.err.println("TEST EOF");
            } catch (Exception ex) {
                System.out.println("TEST errored");
                Logger.handleError(ex);
            }
        }).start();
        /*
        new Thread(() -> {
            try {
                System.out.println("BUFFEREDREADER started");
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pipedInputStream));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println("BUFFEREDREADER: " + line);
                }
                bufferedReader.close();
                System.out.println("BUFFEREDREADER stopped");
            } catch (Exception ex) {
                System.out.println("BUFFEREDREADER errored");
                Logger.handleError(ex);
            }
        }).start();
        */
        Thread.sleep(500);
        /*
        final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(pipedOutputStream));
        bufferedWriter.write("Test " + Math.random());
        bufferedWriter.newLine();
        bufferedWriter.flush();
        bufferedWriter.close();
        //pipedInputStream.close();
        */
        final TestObject testObject = new TestObject();
        incrementalObjectOutputStream.writeObject(testObject);
        incrementalObjectOutputStream.flush();
        Thread.sleep(1000);
        testObject.newRandomNumber();
        incrementalObjectOutputStream.writeObject(testObject);
        incrementalObjectOutputStream.flush();
        Thread.sleep(5000);
        incrementalObjectOutputStream.close();
        //incrementalObjectInputStream.close();
    }
    
    static class TestObject implements Serializable, Snowflake {
        
        private final long id;
        public double random = Math.random();
        
        public TestObject() {
            this.id = generateId();
        }
        
        public TestObject(long id) {
            this.id = id;
        }
        
        public void newRandomNumber() {
            random = Math.random();
        }
        
        @Override
        public String toString() {
            return getClass().getSimpleName() + "{" + "id=" + id + ", random=" + random + '}';
        }
        
        @Override
        public long getId() {
            return id;
        }
        
    }
    
}
