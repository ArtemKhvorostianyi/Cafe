package cafe;

public class Client implements Runnable {

    @Override
    public void run() {
        try {
            System.err.printf("%s зайшов до кав'ярні і робить замовлення\n", Thread.currentThread().getName());
            Thread.sleep(50); // Час на вибір замовлення
            CoffeeShop.ordersLimit.acquire(); // Бариста може взяти 2 замовлення одночасно

            System.out.printf("%s зробив замовлення і бариста почав його готувати\n", Thread.currentThread().getName());
            Thread.sleep(2000); // Час на приготування кави

            System.out.printf("Бариста приготував каву для %s\n", Thread.currentThread().getName());
            Thread.sleep(50); // Клієнт забирає замовлення
            CoffeeShop.ordersLimit.release(); // Звільняється місце для наступного замовлення

            System.err.printf("%s отримав своє замовлення і пішов\n", Thread.currentThread().getName());
            CoffeeShop.decrementVisitors(); // Клієнт йде, зменшуємо кількість активних клієнтів

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}