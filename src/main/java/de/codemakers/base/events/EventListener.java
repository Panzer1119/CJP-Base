/*
 *     Copyright 2018 - 2020 Paul Hagedorn (Panzer1119)
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

package de.codemakers.base.events;

import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.tough.ToughConsumer;

@FunctionalInterface
public interface EventListener<T extends Event> {
    
    /**
     * Triggered if an Event has occurred
     *
     * @param event Event
     *
     * @return return <tt>true</tt> if the event should get consumed
     *
     * @throws Exception Error
     */
    boolean onEvent(T event) throws Exception;
    
    default boolean onEvent(T event, ToughConsumer<Throwable> failure) {
        try {
            return onEvent(event);
        } catch (Exception ex) {
            if (failure != null) {
                failure.acceptWithoutException(ex);
            } else {
                Logger.handleError(ex);
            }
            return false;
        }
    }
    
    default boolean onEventWithoutException(T event) {
        return onEvent(event, null);
    }
    
}
