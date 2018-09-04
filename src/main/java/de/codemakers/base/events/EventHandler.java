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
import de.codemakers.base.logger.Logger;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class EventHandler<T extends Event> implements IEventHandler<T> {
    
    private final Map<EventListener<? extends T>, Class<? extends T>> eventListeners = new ConcurrentHashMap<>();
    private ExecutorService executorService = null;
    private boolean containsNull = false;
    
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
    public final <E extends T> EventHandler<T> addEventListener(Class<E> clazz, EventListener<E> eventListener) {
        eventListeners.put(eventListener, clazz);
        if (clazz == null) {
            containsNull = true;
        }
        return this;
    }
    
    @Override
    public final <E extends T> EventHandler<T> removeEventListener(Class<E> clazz, EventListener<E> eventListener) {
        eventListeners.remove(eventListener, clazz);
        containsNull = eventListeners.values().contains(null);
        return this;
    }
    
    @Override
    public final EventHandler<T> clearEventListeners() {
        return this;
    }
    
    @Override
    public final <E extends T> List<EventListener<E>> getEventListeners(Class<E> clazz) {
        return eventListeners.entrySet().stream().filter((entry) -> Objects.equals(entry.getValue(), clazz)).map(Map.Entry::getKey).map((eventListener) -> (EventListener<E>) eventListener).collect(Collectors.toList());
    }
    
    @Override
    public final void onEvent(T event) {
        if (event == null) {
            return;
        }
        eventListeners.entrySet().stream().filter((entry) -> entry.getValue() != null).filter((entry) -> entry.getValue().isAssignableFrom(event.getClass())).map(Map.Entry::getKey).map((eventListener) -> (EventListener<T>) eventListener).forEach((eventListener) -> {
            if (executorService != null) {
                executorService.submit(() -> {
                    try {
                        eventListener.onEvent(event);
                    } catch (Exception ex) {
                        Logger.handleError(ex);
                    }
                });
            } else {
                try {
                    eventListener.onEvent(event);
                } catch (Exception ex) {
                    Logger.handleError(ex);
                }
            }
        });
        if (containsNull) {
            eventListeners.entrySet().stream().filter((entry) -> entry.getValue() == null).map(Map.Entry::getKey).map((eventListener) -> {
                try {
                    return (EventListener<T>) eventListener;
                } catch (Exception ex) {
                    return null;
                }
            }).filter(Objects::nonNull).forEach((eventListener) -> {
                if (executorService != null) {
                    executorService.submit(() -> {
                        try {
                            eventListener.onEvent(event);
                        } catch (Exception ex) {
                            Logger.handleError(ex);
                        }
                    });
                } else {
                    try {
                        eventListener.onEvent(event);
                    } catch (Exception ex) {
                        Logger.handleError(ex);
                    }
                }
            });
        }
    }
    
}
