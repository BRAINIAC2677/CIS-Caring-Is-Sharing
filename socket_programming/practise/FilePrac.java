package practise;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;

enum PizzaStatus {
    ORDERED(5),
    READY(2),
    DELIVERED(0);

    private int timeToDelivery;

    PizzaStatus(int timeToDelivery) {
        this.timeToDelivery = timeToDelivery;
    }

    // Method that gets the timeToDelivery variable.
}

public class FilePrac {
    public static void main(String[] args) {
        try {
            // Path path = Paths.get("socket_programming/storage/a.publ/docs");
            // System.out.println(path.subpath(2, path.getNameCount()).toString());
            System.out.println(PizzaStatus.ORDERED);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}