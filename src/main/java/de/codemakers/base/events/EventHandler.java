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

import de.codemakers.base.CJP;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class EventHandler<T extends Event> implements IEventHandler<T> {
    
    private final Map<EventListener<T>, Class<T>> eventListeners = new ConcurrentHashMap<>();
    private ExecutorService executorService = null;
    
    public EventHandler() {
        this(CJP.getInstance().getSingleExecutorService());
    }
    
    public EventHandler(ExecutorService executorService) {
        this.executorService = executorService;
    }
    
    public final ExecutorService getEexecutorService() {
        return executorService;
    }
    
    public final EventHandler<T> setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }
    
    @Override
    public final EventHandler<T> addEventListener(Class<T> clazz, EventListener<T> eventListener) {
        eventListeners.put(eventListener, clazz);
        return this;
    }
    
    @Override
    public final EventHandler<T> removeEventListener(Class<T> clazz, EventListener<T> eventListener) {
        eventListeners.remove(eventListener, clazz);
        return this;
    }
    
    @Override
    public final EventHandler<T> clearEventListeners() {
        return this;
    }
    
    @Override
    public final List<EventListener<T>> getEventListeners(Class<T> clazz) {
        return eventListeners.entrySet().stream().filter((entry) -> Objects.equals(entry.getValue(), clazz)).map(Map.Entry::getKey).map((eventListener) -> (EventListener<T>) eventListener).collect(Collectors.toList());
    }
    
    @Override
    public final void onEvent(T event) {
        if (event == null) {
            return;
        }
        eventListeners.entrySet().stream().filter((entry) -> entry.getValue().isAssignableFrom(event.getClass())).map(Map.Entry::getKey).map((eventListener) -> (EventListener<T>) eventListener).forEach((eventListener) -> {
            if (executorService != null) {
                executorService.submit(() -> {
                    try {
                        eventListener.onEvent(event);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            } else {
                try {
                    eventListener.onEvent(event);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
}
