package ru.example.concurrentcollections;

import java.util.List;
import java.util.concurrent.*;

public class EventSystem {
    private final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>(100);

    private final ConcurrentHashMap<String, Event> processedCache = new ConcurrentHashMap<>();

    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();

    private final java.util.concurrent.atomic.AtomicInteger processedCount = new java.util.concurrent.atomic.AtomicInteger(0);

    public void addListener(EventListener listener) {
        listeners.add(listener);
        System.out.println("Добавлен новый слушатель. Всего слушателей: " + listeners.size());
    }

    public void publish(Event event) throws InterruptedException {
        eventQueue.put(event);
        System.out.println("Событие опубликовано: " + event.getId());
    }

    public void startProcessing() {
        Thread processorThread = new Thread(() -> {
            while (true) {
                try {
                    Event event = eventQueue.take();

                    Thread.sleep(100);

                    processedCache.put(event.getId(), event);
                    processedCount.incrementAndGet();

                    System.out.println("Обработано: " + event);

                    for (EventListener listener : listeners) {
                        listener.onEvent(event);
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "EventProcessor");
        processorThread.setDaemon(true);
        processorThread.start();
    }

    public Event getFromCache(String id) {
        return processedCache.get(id);
    }

    public int getProcessedCount() {
        return processedCount.get();
    }
}