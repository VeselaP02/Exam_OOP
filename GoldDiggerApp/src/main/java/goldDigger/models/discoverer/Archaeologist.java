package goldDigger.models.discoverer;

public class Archaeologist extends BaseDiscoverer{
    private static final double energy = 60;
    public Archaeologist(String name) {
        super(name, energy);
    }
}
