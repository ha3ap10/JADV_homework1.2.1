package ru.netology;

public class Main {
    public static void main(String[] args) {

        final Dealer dealer = new Dealer();

        Runnable buyCar = dealer::buyCar;
        Runnable produceCar = dealer::production;
        Runnable deliveryCar = dealer::deliveryFromWarehouse;

        new Thread(null, produceCar, "Toyota").start();

        for (int i = 1; i <= 10; i++) {
            new Thread(null, buyCar, "Покупатель " + i).start();
        }

        new Thread(null, deliveryCar, "Доставка").start();
    }
}
