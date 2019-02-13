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

package de.codemakers.base.entities;

import de.codemakers.base.logger.Logger;

import java.util.Arrays;

public class RingBufferTest {
    
    public static final void main(String[] args) throws Exception {
        final RingBuffer ringBuffer = new RingBuffer(Integer.class, 5);
        Logger.log("ringBuffer=" + ringBuffer);
        Logger.log("ringBuffer.toArray=" + Arrays.toString(ringBuffer.toArray()));
        Logger.log("ringBuffer.isEmpty=" + ringBuffer.isEmpty());
        Logger.log("==============================================");
        for (int i = 0; i < 11; i++) {
            ringBuffer.add(i);
            Logger.log("ringBuffer=" + ringBuffer);
            Logger.log("ringBuffer.toArray=" + Arrays.toString(ringBuffer.toArray()));
            Logger.log("ringBuffer.isEmpty=" + ringBuffer.isEmpty());
            Logger.log("==============================================");
        }
        for (int i = 0; i < 5; i++) {
            Logger.log("ringBuffer.remove=" + ringBuffer.remove());
            Logger.log("ringBuffer=" + ringBuffer);
            Logger.log("ringBuffer.toArray=" + Arrays.toString(ringBuffer.toArray()));
            Logger.log("ringBuffer.isEmpty=" + ringBuffer.isEmpty());
            Logger.log("==============================================");
        }
    }
    
}
