package ru.example.concurrentcollections;

public class MainApp {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Многопоточная система событий");
        EventSystem system = new EventSystem();
        
        system.addListener(event -> System.out.println("  [Слушатель 1] Получено: " + event.getId()));
        system.addListener(event -> System.out.println("  [Слушатель 2] Анализ типа: " + event.getType()));
        
        system.startProcessing();

        Thread[] producers = new Thread[3];
        for (int i = 0; i < 3; i++) {
            int producerId = i + 1;
            producers[i] = new Thread(() -> {
                for (int j = 1; j <= 5; j++) {
                    try {
                        String eventId = "E-" + producerId + "-" + j;
                        system.publish(new Event(eventId, "TYPE_" + (j % 3), "Данные от потока " + producerId));
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }, "Producer-" + producerId);
            producers[i].start();
        }

        for (Thread p : producers) {
            p.join();
        }

        Thread.sleep(2000);

        System.out.println("\nИтоги");
        System.out.println("Всего обработано событий: " + system.getProcessedCount());

        Event cached = system.getFromCache("E-1-1");
        System.out.println("Событие из кэша: " + (cached != null ? cached : "не найдено"));

        System.exit(0);
    }
}