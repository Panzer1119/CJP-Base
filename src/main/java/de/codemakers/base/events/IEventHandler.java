/*
 *    Copyright 2018 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.codemakers.base.events;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface IEventHandler<E extends Event> extends EventListener<E> {

    default <T extends E> IEventHandler addEventListeners(Class<T> clazz, EventListener<T>... eventListeners) {
        return addEventListeners(clazz, Arrays.asList(eventListeners));
    }

    default <T extends E> IEventHandler addEventListeners(Class<T> clazz, Collection<EventListener<T>> eventListeners) {
        if (eventListeners != null && !eventListeners.isEmpty()) {
            eventListeners.forEach((eventListener) -> addEventListener(clazz, eventListener));
        }
        return this;
    }

    <T extends E> IEventHandler addEventListener(Class<T> clazz, EventListener<T> eventListener);

    default <T extends E> IEventHandler removeEventListeners(Class<T> clazz, EventListener<T>... eventListeners) {
        return removeEventListeners(clazz, Arrays.asList(eventListeners));
    }

    default <T extends E> IEventHandler removeEventListeners(Class<T> clazz, Collection<EventListener<T>> eventListeners) {
        if (eventListeners != null && !eventListeners.isEmpty()) {
            eventListeners.forEach((eventListener) -> removeEventListener(clazz, eventListener));
        }
        return this;
    }

    <T extends E> IEventHandler removeEventListener(Class<T> clazz, EventListener<T> eventListener);

    IEventHandler clearEventListeners();

    <T extends E> List<EventListener<T>> getEventListeners(Class<T> clazz);

}
