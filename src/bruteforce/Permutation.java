package bruteforce;


import base.City;
import data.InstanceReader;
import data.TSPLIBReader;
import main.Application;
import main.Configuration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.*;

public class Permutation {


    long dimension = 1000l;
    double distance = 0;
    ArrayList<CityCombination> ergebnis = new ArrayList<CityCombination>();

    public void generate(TSPLIBReader tsplibReader, boolean isUnitTest) {
        HashSet<ArrayList<Integer>> set = new HashSet<ArrayList<Integer>>();
        //Schleife für 4 Mrd. Wiederholungen
        while (set.size() < dimension) {

            ArrayList<Integer> cities = new ArrayList<Integer>();
            distance = 0;

            //Schleife die, die Liste füllt
            for (int j = 0; j < 280; j++) {

                cities.add(j);
            }


            //Städte werden gemischt
            Collections.shuffle(cities);

            //Distanzen von Stadt zu Stadt werden addiert
            for (int i = 0; i < 278; i++) {

                distance = distance + tsplibReader.getDistance(cities.get(i), cities.get(i + 1));
            }

            //Von der letzten Stadt geht man zur ersten zurück
            distance = distance + tsplibReader.getDistance(cities.get(279), cities.get(0));
            long sizeBefore = set.size();
            set.add(cities);


            if (set.size() > sizeBefore) {
                ergebnis.add(new CityCombination(cities, distance));
            }
        }
        if (!isUnitTest) {
            evaluation();
        }
    }

    private void evaluation() {

        MinimalDistance min = new MinimalDistance();
        Scanner scanner = new Scanner(System.in);
        System.out.println("What do you want to analyze? Please insert parameter.");
        System.out.println("You can choose between all, first, middle and last. You can go on by typing exit");
        String eval = scanner.nextLine();

        //Evaluierung der Eingabe
        if (eval.equals("all")) {
            CityCombination tour = min.sort(ergebnis);
            System.out.println(tour.getDistance());

            for (int q = 0; q < tour.getCities().size(); q++) {
                System.out.println(tour.getCities().get(q));
            }
        } else if (eval.equals("first")) {
            ArrayList<CityCombination> first = new ArrayList<CityCombination>();

            for (int v = 0; v < ergebnis.size() / 4; v++) {
                first.add(ergebnis.get(v));
            }

            CityCombination tourFirst = min.sort(first);
            System.out.println(tourFirst.getDistance());

            for (int q = 0; q < tourFirst.getCities().size(); q++) {
                System.out.println(tourFirst.getCities().get(q));
            }

        } else if (eval.equals("middle")) {
            ArrayList<CityCombination> middle = new ArrayList<CityCombination>();

            for (int v = ergebnis.size() / 4; v < ergebnis.size() / 4 * 3; v++) {
                middle.add(ergebnis.get(v));
            }


            CityCombination tourMiddle = min.sort(middle);
            System.out.println(tourMiddle.getDistance());

            for (int q = 0; q < tourMiddle.getCities().size(); q++) {
                System.out.println(tourMiddle.getCities().get(q));
            }

        } else if (eval.equals("last")) {
            ArrayList<CityCombination> last = new ArrayList<CityCombination>();

            for (int v = ergebnis.size() / 4 * 3; v < ergebnis.size(); v++) {
                last.add(ergebnis.get(v));
            }

            CityCombination tourLast = min.sort(last);
            System.out.println(tourLast.getDistance());

            for (int q = 0; q < tourLast.getCities().size(); q++) {
                System.out.println(tourLast.getCities().get(q));
            }

        } else if (eval.equals("exit")) {
            return;
        } else {
            System.out.println("Wrong parameter");
        }
    }


    Application app = new Application();

    //test
    @Before
    public void init() {

        app.startupHSQLDB();
        InstanceReader instanceReader = new InstanceReader(Configuration.instance.dataFilePath);
        instanceReader.open();
        TSPLIBReader tsplibReader = new TSPLIBReader(instanceReader);
        generate(tsplibReader, true);
    }

    @Test
    public void hasResultSameElementsAsDimension() {

        assertEquals(dimension, ergebnis.size());
        app.shutdownHSQLDB();
    }
}
