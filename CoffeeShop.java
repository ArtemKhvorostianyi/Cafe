package cafe;

import java.util.concurrent.Semaphore;

public class CoffeeShop {

    static final Semaphore ordersLimit = new Semaphore(2); // Бариста може готувати 2 замовлення одночасно
    private static boolean isOpen = true;
    private static int visitorsCount = 0; // Лічильник клієнтів, що чекають на замовлення

    // Метод для перевірки, чи відкрита кав'ярня
    public static synchronized boolean isOpen() {
        return isOpen || visitorsCount > 0;
    }

    // Метод для закриття кав'ярні (тільки після завершення всіх замовлень)
    public static synchronized void closeCoffeeShop() {
        isOpen = false;
        System.err.println("============= Кав'ярня зачинена, нові клієнти не можуть увійти =============");
    }

    // Збільшення кількості клієнтів
    public static synchronized void incrementVisitors() {
        visitorsCount++;
    }

    // Зменшення кількості клієнтів
    public static synchronized void decrementVisitors() {
        visitorsCount--;
    }

    public static void main(String[] args) throws InterruptedException {

        Runnable coffeeShop = () -> {
            int i = 0;
            while (isOpen()) {
                if (isOpen) {
                    new Thread(new Client(), "Клієнт " + i).start();
                    incrementVisitors(); // Збільшуємо кількість клієнтів, що чекають на замовлення
                    i++;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread coffeeShopThread = new Thread(coffeeShop, "Кав'ярня");
        coffeeShopThread.start();
        Thread.sleep(6000); // Кав'ярня працює 6 секунд
        closeCoffeeShop();

        coffeeShopThread.join(); // Чекаємо, доки всі клієнти будуть обслуговані

        System.err.println("============= Працівник пішов додому =============");
    }
}