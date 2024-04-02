/* $Id: Bsp_01.java,v 1.1 2011/11/01 13:42:44 guenther Exp $
 ************************************************************************/
/*  Copyright (C):     Guenther Koehler, 2011                           */

/************************************************************************

 Versuch: Thread-Synchronisation ueber Warteschleife.

 Ablauf:
 - Hauptthread startet Warte-Thread.
 - Dieser wartet in einer while-Schleife darauf, das "flag" "false" wird.
 - Haupt-Thread arbeitet (schlaeft 2 s) und signalisiert dann ueber
 "flag" den wartenden Thread.

 Funktioniert das so?
 ************************************************************************/
/*class Bsp_01 extends Thread {

    private boolean flag = true;

    // Thread-Code
    public void run() {
        System.out.println("> Start Thread");

        // busy waiting
        while (flag)  *//* wait *//* ;

        System.out.println("> Ende Thread");

    }

*//************************************************************************//*
//				Hauptprogramm

    *//************************************************************************//*


    public static void main(String[] args) {

        // Thread erzeugen und starten
        Bsp_01 my = new Bsp_01();
        my.start();

        // Wartet
        try {
            sleep(2000);
        } catch (InterruptedException e) {
        }

        // anderen Thread signalisieren
        System.out.println("Signalisiere Thread ...");
        my.flag = false;

        // fertig!
        System.out.println("Warte auf Beendigung des Threads");
        try {
            my.join();
        } catch (InterruptedException e) {
        }

    }
}*/


public class Aufgabe_3 extends Thread {

    private boolean  flag = true;


    // Thread-Code
    public  void run() {
        System.out.println("> Start Thread");

        try {
            sleep(3000);
        } catch (InterruptedException e) {
        }

        // busy waiting
        while (this.flag)  /* wait */ ;

        System.out.println("> Ende Thread");

    }

    public static void main(String[] args) throws InterruptedException {

        // Thread erzeugen und starten
        Aufgabe_3 my = new Aufgabe_3();
        my.start();

        // Wartet
        try {
            sleep(2000);
        } catch (InterruptedException e) {
        }

        // anderen Thread signalisieren
        System.out.println("Signalisiere Thread ...");
        my.flag = false;


        // fertig!
        System.out.println("Warte auf Beendigung des Threads");
        try {
            my.join();
        } catch (InterruptedException e) {}

    }
}
