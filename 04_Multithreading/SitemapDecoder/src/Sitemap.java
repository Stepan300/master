import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class Sitemap {

    private static int linkCount;
    private static int totalBruttoLinks;
    private static int saveNumber;
    private static int linksInFileCount;
    private static final String PARTS = "https://lenta.ru/parts/";
    private static final String INFO = "https://lenta.ru/info/";
    private static final String SITE = "https://lenta.ru/";

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        Elements elements = jsoupParse(SITE);
        assert elements != null;
        List<String> links = absLinksInList(elements);
        links = links.subList(1, links.size()-1);

        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        FJSiteDecoder decoder = new FJSiteDecoder(links);
        pool.invoke(decoder);

        DecimalFormat fd = new DecimalFormat("###,###,###");
        long duration = System.currentTimeMillis() - start;
        pool.shutdown();

        formatFile();
        System.out.printf("%30s%8d\n%30s%8d\n%30s%8d\n%30s%8s%15s\n%38d%8s%3d%8s", "Total selected links:",
                totalBruttoLinks, "Number of saving list:", saveNumber,
                "Number links in file:", linksInFileCount, "Time:", fd.format(duration), "milliseconds",
                duration/1000/60, "minutes", duration/1000%60, "seconds");
    }

    protected static Elements jsoupParse(String link) {
        Document doc = null;
        try {
            Thread.sleep(150);
            doc = Jsoup.connect(link).maxBodySize(0).ignoreHttpErrors(true).userAgent("Mozilla/5.0").get();
        } catch (NullPointerException | IOException | InterruptedException ex) {  // .timeout(1000)
            ex.printStackTrace();   // .ignoreContentType(true)
        }
        assert doc != null;
        Elements elements = doc.select("a");
        if (elements.size() == 0) {
            System.out.println("\tElements is empty!");
            return null;
        }
        else {
            return elements;
        }
    }

    private static List<String> absLinksInList(Elements elements) {
        List<String> links = new ArrayList<>();
        for (Element element : elements) {
            String absLink = element.attr("abs:href");
            String mSite = "https://m.lenta.ru/";
            if (absLink.contains(SITE) || absLink.contains(mSite)) {
                links.add(absLink);
                linkCount++;
                System.out.printf("%4d)%10s%-50s\n", linkCount, "AbsLink: ", absLink);
            }
        }
        links = links.stream().filter(link -> !link.contains("#")).distinct().collect(Collectors.toList());
        if (!links.isEmpty()) {
            totalBruttoLinks += linkCount;
            writeToFile(links);
        }
        return links;
    }

    protected static void writeToFile(List<String> links) {
        try {
            String path = "SitemapDecoder/data/Sitemap.txt";
            Files.write(Paths.get(path), links, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Size of saving list: " + links.size());
        linksInFileCount += links.size();
        saveNumber++;
    }

    protected static void totalCount(int count) {totalBruttoLinks += count;}

    private static void formatFile() {
        String path = "SitemapDecoder/data/Sitemap.txt";
        List<String> links = new ArrayList<>();
        String site = "https://lenta.ru/";
        String mSite = "https://m.lenta.ru/";
        try {
            links = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        links = links.stream().distinct().sorted(comInfo.thenComparing(comParts).thenComparing(comNature))
                .map(link -> {
                    if (link.startsWith(mSite)) {
                        link = "\t".concat(link);
                    }
                    if (!link.equals(site)) {
                        link = "\t".concat(link);
                    }
                    if (link.equals("\t\t".concat(mSite))) {
                        link = link.trim();
                        link = "\t".concat(link);
                    }
                    String[] strings = link.split("/");
                    if (strings.length >= 5 && strings.length < 8) {
                        link = "\t".concat(link);
                        if (strings.length == 6) {
                            link = "\t".concat(link);
                        }
                    }
                    return link;
                }).collect(Collectors.toList());
        try {
            Files.write(Paths.get(path), links);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Comparator<String> comNature = Comparator.naturalOrder();
    public static Comparator<String> comInfo = Comparator.comparing(Sitemap::getInfoLength);
    public static Comparator<String> comParts = Comparator.comparing(Sitemap::getPartsLength);

    private static int getPartsLength(String line) {
        int count = 0;
        String[] strings = line.split("/");
        if (line.startsWith(PARTS)) {
            count = strings.length;
        }
        return count;
    }

    private static int getInfoLength(String line) {
        int count = 0;
        if (line.startsWith(INFO)) {
            count = line.length();
        }
        return count;
    }
}


//        ForkJoinPool pool = new ForkJoinPool(ForkJoinPool.getCommonPoolParallelism(),
//                ForkJoinPool.defaultForkJoinWorkerThreadFactory, null,
//                true, ForkJoinPool.getCommonPoolParallelism(), 256+ForkJoinPool.getCommonPoolParallelism(),
//                1, ForkJoinPool::getAsyncMode, 150, TimeUnit.MILLISECONDS);