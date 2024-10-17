package goldDigger.models.discoverer;

public class Geologist extends BaseDiscoverer{
    private static final double energy = 100;

    public Geologist(String name) {
        super(name, energy);
    }
}
