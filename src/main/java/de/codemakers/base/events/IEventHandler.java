/*
 *    Copyright 2018 - 2021 Paul Hagedorn (Panzer1119)
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

public interface IEventHandler<E> extends EventListener<E> {
    
    default IEventHandler<E> addEventListeners(Class<E> clazz, EventListener<E>... eventListeners) {
        return addEventListeners(clazz, Arrays.asList(eventListeners));
    }
    
    default IEventHandler<E> addEventListeners(Class<E> clazz, Collection<EventListener<E>> eventListeners) {
        if (eventListeners != null && !eventListeners.isEmpty()) {
            eventListeners.forEach((eventListener) -> addEventListener(clazz, eventListener));
        }
        return this;
    }
    
    default IEventHandler<E> addEventListener(EventListener<E> eventListener) {
        return addEventListener(null, eventListener);
    }
    
    IEventHandler<E> addEventListener(Class<E> clazz, EventListener<E> eventListener);
    
    default IEventHandler<E> removeEventListeners(Class<E> clazz, EventListener<E>... eventListeners) {
        return removeEventListeners(clazz, Arrays.asList(eventListeners));
    }
    
    default IEventHandler<E> removeEventListeners(Class<E> clazz, Collection<EventListener<E>> eventListeners) {
        if (eventListeners != null && !eventListeners.isEmpty()) {
            eventListeners.forEach((eventListener) -> removeEventListener(clazz, eventListener));
        }
        return this;
    }
    
    default IEventHandler<E> removeEventListener(EventListener<E> eventListener) {
        return removeEventListener(null, eventListener);
    }
    
    IEventHandler<E> removeEventListener(Class<E> clazz, EventListener<E> eventListener);
    
    IEventHandler<E> clearEventListeners();
    
    List<EventListener<E>> getEventListeners(Class<E> clazz);
    
}
