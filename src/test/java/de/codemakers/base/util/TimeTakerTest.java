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

package de.codemakers.base.util;

public class TimeTakerTest {
    
    public static final void main(String[] args) throws Exception {
        final TimeTaker timeTaker = new TimeTaker();
        timeTaker.start();
        Thread.sleep(1000);
        //System.out.println("Time taken: " + timeTaker.getDuration() + " ms");
        timeTaker.pause();
        Thread.sleep(2000);
        timeTaker.unpause();
        Thread.sleep(3000);
        timeTaker.stop();
        System.out.println("Time taken: " + timeTaker.getDuration() + " ms");
    }
}
