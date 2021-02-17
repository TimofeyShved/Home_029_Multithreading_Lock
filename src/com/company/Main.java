package com.company;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) throws InterruptedException {

	    Resource resource = new Resource(); // создаем ресурс
	    resource.i = 5;
        resource.j = 3;

	    MyThread myThread_1 = new MyThread(); // создаем потоки и называем их
	    myThread_1.setName("one");
	    MyThread myThread_2 = new MyThread();
        myThread_2.setName("two");

	    myThread_1.resource = resource; // запихиваем в них ссылку на ресурс
	    myThread_2.resource = resource;

	    myThread_1.start(); // запускаем
	    myThread_2.start();

	    myThread_1.join(); // до смерти
	    myThread_2.join();

        System.out.println("i - "+resource.i); // вывод
        System.out.println("j - "+resource.j);
    }

    static class MyThread extends Thread{ // сам поток
        Resource resource;
        @Override
        public void run(){ // 2 операции с ресурсами
            resource.ChangI();
            resource.ChangJ();
        }
    }
}

class Resource{
    int i;
    int j;

    Lock lock = new ReentrantLock(); // создаем блокировку, пока не выполнит один поток

    void ChangI(){
        lock.lock(); // закрываем
        int i = this.i;
        if(Thread.currentThread().getName().equals("one")){
            Thread.yield(); // даем возможность запустить другой поток
        }
        i++;
        this.i = i;
    }

    void ChangJ(){
        int j = this.j;
        if(Thread.currentThread().getName().equals("one")){
            Thread.yield();
        }
        j++;
        this.j = j;
        lock.unlock(); // открываем
    }
}
