import core.Line;
import core.Station;
import junit.framework.TestCase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RouteCalculatorTest extends TestCase {
    private String dataTestFile = "src/main/resources/mapTest.json";
    private StationIndex stationIndex;
    private String fromLine = "Зелёная";
    private String toLine = "Коктейльная";
    private String fromRev = "Корабельная";
    private String toRev = "Глубокая";
    private String from1Con = "Голубая";
    private String to1Con = "Живописная";
    private String from1Con2 = "Высокая";
    private String to1Con2 = "Воздушная";
    private String from1Or2Con2 = "Голубая";
    private String to1Or2Con2 = "Прекрасная";
    private String from1Or2Con1 = "Глубокая";
    private String to1Or2Con1 = "Небесная";
    private String from2Con = "Зелёная";
    private String to2Con = "Чудесная";
    private String from2Con2 = "Последняя";
    private String to2Con2 = "Воздушная";

    List<Station> route;
    List<Station> routeLine;
    List<Station> routeLineRev;
    List<Station> routeOneConnection;
    List<Station> routeOneConnection2;
    List<Station> route1Or2Connections2;
    List<Station> route1Or2Connections1;
    List<Station> routeTwoConnections;
    List<Station> routeTwoConnections2;

    @Override
    protected void setUp() throws Exception {

        route = new ArrayList<>();
        routeLine = new ArrayList<>();
        routeLineRev = new ArrayList<>();
        routeOneConnection = new ArrayList<>();
        routeOneConnection2 = new ArrayList<>();
        route1Or2Connections2 = new ArrayList<>();
        route1Or2Connections1 = new ArrayList<>();
        routeTwoConnections = new ArrayList<>();
        routeTwoConnections2 = new ArrayList<>();

        Line line1 = new Line(1, "Коктейльно-Морская");
        Line line2 = new Line(2, "Небесно-Воздушная");
        Line line3 = new Line(3, "Прохладно-Пивная");
        Line line4 = new Line(4, "Широко-Последняя");
   // On the Line
        routeLine.add(new Station("Зелёная", line1));
        routeLine.add(new Station("Голубая", line1));
        routeLine.add(new Station("Глубокая", line1));
        routeLine.add(new Station("Высокая", line1));
        routeLine.add(new Station("Корабельная", line1));
        routeLine.add(new Station("Коктейльная", line1));
   // Reverse on the Line
        routeLineRev.add(new Station("Корабельная", line1));
        routeLineRev.add(new Station("Высокая", line1));
        routeLineRev.add(new Station("Глубокая", line1));
   // One connection #1
        routeOneConnection.add(new Station("Голубая", line1));
        routeOneConnection.add(new Station("Глубокая", line1));
        routeOneConnection.add(new Station("Высокая", line1));
        routeOneConnection.add(new Station("Корабельная", line1));
        routeOneConnection.add(new Station("Пастельная", line2));
        routeOneConnection.add(new Station("Живописная", line2));
   // One connection #2
        routeOneConnection2.add(new Station("Высокая", line1));
        routeOneConnection2.add(new Station("Корабельная", line1));
        routeOneConnection2.add(new Station("Пастельная", line2));
        routeOneConnection2.add(new Station("Воздушная", line2));
   // One or Two connections - Two connections
        route1Or2Connections2.add(new Station("Голубая", line1));
        route1Or2Connections2.add(new Station("Виноградная", line3));
        route1Or2Connections2.add(new Station("Центральная", line3));
        route1Or2Connections2.add(new Station("Грибная", line3));
        route1Or2Connections2.add(new Station("Прекрасная", line2));
   // One or Two connections - One connection
        route1Or2Connections1.add(new Station("Глубокая", line1));
        route1Or2Connections1.add(new Station("Высокая", line1));
        route1Or2Connections1.add(new Station("Корабельная", line1));
        route1Or2Connections1.add(new Station("Пастельная", line2));
        route1Or2Connections1.add(new Station("Живописная", line2));
        route1Or2Connections1.add(new Station("Прекрасная", line2));
        route1Or2Connections1.add(new Station("Чудесная", line2));
        route1Or2Connections1.add(new Station("Небесная", line2));
   // Two connections #1
        routeTwoConnections.add(new Station("Зелёная", line1));
        routeTwoConnections.add(new Station("Голубая", line1));
        routeTwoConnections.add(new Station("Виноградная", line3));
        routeTwoConnections.add(new Station("Центральная", line3));
        routeTwoConnections.add(new Station("Грибная", line3));
        routeTwoConnections.add(new Station("Прекрасная", line2));
        routeTwoConnections.add(new Station("Чудесная", line2));
   // Two connections #2
        routeTwoConnections2.add(new Station("Последняя", line4));
        routeTwoConnections2.add(new Station("Песчаная", line4));
        routeTwoConnections2.add(new Station("Широкая", line4));
        routeTwoConnections2.add(new Station("Глубокая", line1));
        routeTwoConnections2.add(new Station("Высокая", line1));
        routeTwoConnections2.add(new Station("Корабельная", line1));
        routeTwoConnections2.add(new Station("Пастельная", line2));
        routeTwoConnections2.add(new Station("Воздушная", line2));
    }

    public void testRouteCalculate() {
        RouteCalculator calculator = getRouteCalculator();
        assertNotNull("calculator isn't null ", calculator);
    }

    public void testGetShortestRoute() {
   // On the Line
        RouteCalculator calculator = getRouteCalculator();
        Station from = stationIndex.getStation(fromLine);
        Station to = stationIndex.getStation(toLine);
        route = calculator.getShortestRoute(from, to);
        System.out.println("\t\tOn the Line");
        printRoute(route);
        printRoute(routeLine);
        assertEquals(routeLine, route);
   // Reverse on the Line
        from = stationIndex.getStation(fromRev);
        to = stationIndex.getStation(toRev);
        route = calculator.getShortestRoute(from, to);
        System.out.println("\t\tReverse on the Line");
        printRoute(route);
        printRoute(routeLineRev);
        assertEquals(routeLineRev, route);
   // One connection #1
        from = stationIndex.getStation(from1Con);
        to = stationIndex.getStation(to1Con);
        route = calculator.getShortestRoute(from, to);
        System.out.println("\t\tOne connection #1");
        printRoute(route);
        printRoute(routeOneConnection);
        assertEquals(routeOneConnection, route);
   // One connection #2
        from = stationIndex.getStation(from1Con2);
        to = stationIndex.getStation(to1Con2);
        route = calculator.getShortestRoute(from, to);
        System.out.println("\t\tOne connection #2");
        printRoute(route);
        printRoute(routeOneConnection2);
        assertEquals(routeOneConnection2, route);
   // One or Two connections - Two connections
        from = stationIndex.getStation(from1Or2Con2);
        to = stationIndex.getStation(to1Or2Con2);
        route = calculator.getShortestRoute(from, to);
        System.out.println("\t\tOne or Two connections - Two connections");
        printRoute(route);
        printRoute(route1Or2Connections2);
        assertEquals(route1Or2Connections2, route);
   // One or Two connections - One connection
        from = stationIndex.getStation(from1Or2Con1);
        to = stationIndex.getStation(to1Or2Con1);
        route = calculator.getShortestRoute(from, to);
        System.out.println("\t\tOne or Two connections - One connection");
        printRoute(route);
        printRoute(route1Or2Connections1);
        assertEquals(route1Or2Connections1, route);
   // Two connections #1
        from = stationIndex.getStation(from2Con);
        to = stationIndex.getStation(to2Con);
        route = calculator.getShortestRoute(from, to);
        System.out.println("\t\tTwo connections #1");
        printRoute(route);
        printRoute(routeTwoConnections);
        assertEquals(routeTwoConnections, route);
   // Two connections #2
        from = stationIndex.getStation(from2Con2);
        to = stationIndex.getStation(to2Con2);
        route = calculator.getShortestRoute(from, to);
        System.out.println("\t\tTwo connections #2");
        printRoute(route);
        printRoute(routeTwoConnections2);
        assertEquals(routeTwoConnections2, route);
    }

    public void testCalculateDuration() {
   // On the Line
        RouteCalculator calculator = getRouteCalculator();
        Station from = stationIndex.getStation(fromLine);
        Station to = stationIndex.getStation(toLine);
        route = calculator.getShortestRoute(from, to);
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 12.5;
        assertEquals(expected, actual);
   // Reverse on the Line
        from = stationIndex.getStation(fromRev);
        to = stationIndex.getStation(toRev);
        route = calculator.getShortestRoute(from, to);
        actual = RouteCalculator.calculateDuration(route);
        expected = 5.0;
        assertEquals(expected, actual);
   // One connection #1
        from = stationIndex.getStation(from1Con);
        to = stationIndex.getStation(to1Con);
        route = calculator.getShortestRoute(from, to);
        actual = RouteCalculator.calculateDuration(route);
        expected = 13.5;
        assertEquals(expected, actual);
   // One connection #2
        from = stationIndex.getStation(from1Con2);
        to = stationIndex.getStation(to1Con2);
        route = calculator.getShortestRoute(from, to);
        actual = RouteCalculator.calculateDuration(route);
        expected = 8.5;
        assertEquals(expected, actual);
   // One or Two connections - #2
        from = stationIndex.getStation(from1Or2Con2);
        to = stationIndex.getStation(to1Or2Con2);
        route = calculator.getShortestRoute(from, to);
        actual = RouteCalculator.calculateDuration(route);
        expected = 12.0;
        assertEquals(expected, actual);
   // One or Two connections - #1
        from = stationIndex.getStation(from1Or2Con1);
        to = stationIndex.getStation(to1Or2Con1);
        route = calculator.getShortestRoute(from, to);
        actual = RouteCalculator.calculateDuration(route);
        expected = 18.5;
        assertEquals(expected, actual);
   // Two connections #1
        from = stationIndex.getStation(from2Con);
        to = stationIndex.getStation(to2Con);
        route = calculator.getShortestRoute(from, to);
        actual = RouteCalculator.calculateDuration(route);
        expected = 17.0;
        assertEquals(expected, actual);
   // Two connections #2
        from = stationIndex.getStation(from2Con2);
        to = stationIndex.getStation(to2Con2);
        route = calculator.getShortestRoute(from, to);
        actual = RouteCalculator.calculateDuration(route);
        expected = 19.5;
        assertEquals(expected, actual);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    //=========================================================================

    private RouteCalculator getRouteCalculator() {
        createStationIndex();
        return new RouteCalculator(stationIndex);
    }

    private void createStationIndex() {
        stationIndex = new StationIndex();
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonData = (JSONObject) parser.parse(getJsonFile());

            JSONArray linesArray = (JSONArray) jsonData.get("lines");
            parseLines(linesArray);

            JSONObject stationsObject = (JSONObject) jsonData.get("stations");
            parseStations(stationsObject);

            JSONArray connectionsArray = (JSONArray) jsonData.get("connections");
            parseConnections(connectionsArray);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void parseConnections(JSONArray connectionsArray) {
        connectionsArray.forEach(connectionObject ->
        {
            JSONArray connection = (JSONArray) connectionObject;
            List<Station> connectionStations = new ArrayList<>();
            connection.forEach(item ->
            {
                JSONObject itemObject = (JSONObject) item;
                int lineNumber = ((Long) itemObject.get("line")).intValue();
                String stationName = (String) itemObject.get("station");

                Station station = stationIndex.getStation(stationName, lineNumber);
                if (station == null) {
                    throw new IllegalArgumentException("core.Station " +
                            stationName + " on line " + lineNumber + " not found");
                }
                connectionStations.add(station);
            });
            stationIndex.addConnection(connectionStations);
        });
    }

    private void parseStations(JSONObject stationsObject) {
        stationsObject.keySet().forEach(lineNumberObject ->
        {
            int lineNumber = Integer.parseInt((String) lineNumberObject);
            Line line = stationIndex.getLine(lineNumber);
            JSONArray stationsArray = (JSONArray) stationsObject.get(lineNumberObject);
            stationsArray.forEach(stationObject ->
            {
                Station station = new Station((String) stationObject, line);
                stationIndex.addStation(station);
                line.addStation(station);
            });
        });
    }

    private void parseLines(JSONArray linesArray) {
        linesArray.forEach(lineObject -> {
            JSONObject lineJsonObject = (JSONObject) lineObject;
            Line line = new Line(
                    ((Long) lineJsonObject.get("number")).intValue(),
                    (String) lineJsonObject.get("name")
            );
            stationIndex.addLine(line);
        });
    }

    private String getJsonFile() {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(dataTestFile));
            lines.forEach(line -> builder.append(line));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }

    private void printRoute(List<Station> route)
    {
        Station previousStation = null;
        for(Station station : route)
        {
            if(previousStation != null)
            {
                Line prevLine = previousStation.getLine();
                Line nextLine = station.getLine();
                if(!prevLine.equals(nextLine))
                {
                    System.out.println("\tПереход на станцию " +
                            station.getName() + " (" + nextLine.getName() + " линия)");
                }
            }
            System.out.println("\t" + station.getName());
            previousStation = station;
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
    }
}