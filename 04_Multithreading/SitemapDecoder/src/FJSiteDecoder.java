import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

public class FJSiteDecoder extends RecursiveAction {

    private int linkCount;
    private final List<String> links;
    // ===================================================================

    public FJSiteDecoder(List<String> links) {
        this.links = links;
     }

    @Override
    protected void compute() {
        if (links.size() > 10) {
            ForkJoinTask.invokeAll(createSubtasks());
        }
        else {
            processing();
        }
    }

    private List<FJSiteDecoder> createSubtasks() {
        int sixth = links.size() / 6;
        List<String> tasks1 = new ArrayList<>(links.subList(0, sixth));
        List<String> tasks2 = new ArrayList<>(links.subList(sixth, 2*sixth));
        List<String> tasks3 = new ArrayList<>(links.subList(2*sixth, 3*sixth));
        List<String> tasks4 = new ArrayList<>(links.subList(3*sixth, 4*sixth));
        List<String> tasks5 = new ArrayList<>(links.subList(4*sixth, 5*sixth));
        List<String> tasks6 = new ArrayList<>(links.subList(5*sixth, links.size()));
        List<FJSiteDecoder> subtasks = new ArrayList<>();
        subtasks.add(new FJSiteDecoder(tasks1));
        subtasks.add(new FJSiteDecoder(tasks2));
        subtasks.add(new FJSiteDecoder(tasks3));
        subtasks.add(new FJSiteDecoder(tasks4));
        subtasks.add(new FJSiteDecoder(tasks5));
        subtasks.add(new FJSiteDecoder(tasks6));
        return subtasks;
    }

    private void processing() {
        for (String link : links) {
            Elements elements = Sitemap.jsoupParse(link);
            assert elements != null;
            ForkJoinTask.invokeAll(new FJSiteDecoder(absLinksInList(elements, link)));
        }
    }

    private List<String> absLinksInList(Elements elements, String site) {
        List<String> links = new ArrayList<>();
        for (Element element : elements) {
            String absLink = element.attr("abs:href");
            if (absLink.contains(site) && !absLink.equals(site)) {
                links.add(absLink);
                linkCount++;
                System.out.printf("%4d)%10s%-50s\n", linkCount, "AbsLink: ", absLink);
            }
        }
        links = links.stream().filter(link -> !link.contains("#")).distinct().collect(Collectors.toList());
        if (!links.isEmpty()) {
            Sitemap.totalCount(linkCount);
            Sitemap.writeToFile(links);
        }
        return links;
    }
}

