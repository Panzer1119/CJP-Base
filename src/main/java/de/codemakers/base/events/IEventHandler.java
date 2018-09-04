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

public interface IEventHandler<T extends Event> extends EventListener<T> {
    
    default IEventHandler<T> addEventListeners(Class<T> clazz, EventListener<T>... eventListeners) {
        return addEventListeners(clazz, Arrays.asList(eventListeners));
    }
    
    default IEventHandler<T> addEventListeners(Class<T> clazz, Collection<EventListener<T>> eventListeners) {
        if (eventListeners != null && !eventListeners.isEmpty()) {
            eventListeners.forEach((eventListener) -> addEventListener(clazz, eventListener));
        }
        return this;
    }
    
    IEventHandler<T> addEventListener(Class<T> clazz, EventListener<T> eventListener);
    
    default IEventHandler<T> removeEventListeners(Class<T> clazz, EventListener<T>... eventListeners) {
        return removeEventListeners(clazz, Arrays.asList(eventListeners));
    }
    
    default IEventHandler<T> removeEventListeners(Class<T> clazz, Collection<EventListener<T>> eventListeners) {
        if (eventListeners != null && !eventListeners.isEmpty()) {
            eventListeners.forEach((eventListener) -> removeEventListener(clazz, eventListener));
        }
        return this;
    }
    
    IEventHandler<T> removeEventListener(Class<T> clazz, EventListener<T> eventListener);
    
    IEventHandler<T> clearEventListeners();
    
    List<EventListener<T>> getEventListeners(Class<T> clazz);
    
}
