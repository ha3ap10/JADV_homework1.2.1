package ru.netology;

import java.util.ArrayList;
import java.util.List;

public class Dealer {
    public List<Car> availableCars = new ArrayList<>();
    public List<Car> producedCars = new ArrayList<>();

    final Object warehouse = new Object();

    private static final int PRODUCTION_TIME = 2000;
    private static final int WAREHOUSE_LIMIT = 4;
    private static final int WAITING_TIME = 100;
    private static final int PURCHASE_TIME = 4000;
    private static final int AMOUNT_OF_DEALS = 10;
    private int carsSold;


    public void buyCar() {
        try {
            synchronized (warehouse) {
                String customerName = Thread.currentThread().getName();
                System.out.printf("%s пришел покупать авто.\n", customerName);
                if (availableCars.size() == 0) {
                    System.out.printf("%s расстроен, cейчас машин в наличии нет.\n", customerName);
                }

                while (availableCars.size() == 0) {
                    warehouse.wait();
                }

                Thread.sleep(PURCHASE_TIME);
                carsSold++;
                availableCars.remove(0);
                System.out.printf("%s уехал домой на новеньком авто.\n", customerName);
                System.out.printf("Вcего продано машин: %d.\n", carsSold);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deliveryFromWarehouse() {
        while (carsSold < AMOUNT_OF_DEALS) {
            while (availableCars.size() < WAREHOUSE_LIMIT) {
                if (producedCars.size() > 0) {
                    availableCars.add(producedCars.remove(0));
                    System.out.printf("Авто доставлено на склад. На складе %d авто\n", availableCars.size());
                } else {
                    try {
                        Thread.sleep(WAITING_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            synchronized (warehouse) {
                warehouse.notify();
            }
        }
    }

    public void production() {
        String manufacturerName = Thread.currentThread().getName();

        while (carsSold < AMOUNT_OF_DEALS) {
            producedCars.add(new Car());
            System.out.printf("Производитель %s выпустил авто. В наличии всего %d\n",
                    manufacturerName, producedCars.size());
            try {
                Thread.sleep(PRODUCTION_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
