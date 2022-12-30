package en.education;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Map<String, List<Pair<String, String>>> init = init();
        List<Path> paths = paths();

        System.out.println("Hi here is the available topics: " + format(paths));
        System.out.println("Please choose the one");

        Scanner scan = new Scanner(System.in);

        String input = scan.nextLine();
        maybeTurnOff(input);

        if ( !init.keySet().contains(input)) {
            System.out.println("try again");
            System.exit(0);
        }


        System.out.println("We will go through this topic " + input);

        List<Pair<String, String>> pairs = init.get(input);



        Pair<String, String> pair = randomValue(pairs);

        boolean isEnglish = booleanRandom();

        String question = engOrRus(pair, isEnglish);
        String expectedAnswer = engOrRus(pair, !isEnglish);

        System.out.println(question);

        String answer = scan.nextLine();

        maybeTurnOff(answer);

        if (expectedAnswer.contains(answer)) {

            System.out.println("Excellent!!!");

            System.out.println(question);
            System.out.println();
            System.out.println(answer);
        }

    }

    private static void maybeTurnOff(String string) {
        if (string.contains("exit") || string.contains("quit")) {
            System.out.println("GoodBye");
            System.exit(0);
        }
    }

    private static Pair<String, String> randomValue(List<Pair<String, String>> pairs) {
        Random random = new Random();
        return pairs.get(random.nextInt(pairs.size()));
    }

    private static boolean booleanRandom(){
        Random random = new Random();
        return random.nextBoolean();
    }

    private static String engOrRus(Pair<String, String> pair, boolean isEnglish) {
        return (isEnglish) ? pair.first() : pair.second();
    }


    public static List<String> format(List<Path> paths) {
        return paths.stream()
                .map(Main::formattingFunction)
                .collect(Collectors.toList());
    }

    public static String formattingFunction(Path paths) {
        String s = paths.toString();
        return s.substring(6, s.length() - 4);
    }

    public static Map<String, List<Pair<String, String>>> init() {
        Map<String, List<Pair<String, String>>> mapa = new HashMap<>();
        paths().forEach(e -> mapa.put(Main.formattingFunction(e), Main.read(e)));
        return mapa;
    }

    public static List<Path> paths() {
        try (Stream<Path> stream = Files.list(Path.of("files"))) {
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Files could not be found");
        }
    }

    public static List<Pair<String, String>> read(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            return lines.filter(e -> !e.isEmpty())
                    .map(e -> e.split(" — "))
                    .map(Main::toPair)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Cant read file", e);
        }

    }

    public static Pair<String, String> toPair(String[] strings) {
        if (strings.length != 2) throw new IllegalArgumentException("Unexpected data" + Arrays.toString(strings));
        return new Pair<>(strings[0], strings[1]);
    }
}


record Pair<K, V>(K first, V second) {
}
