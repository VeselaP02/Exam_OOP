package goldDigger.core;

import goldDigger.models.discoverer.Anthropologist;
import goldDigger.models.discoverer.Archaeologist;
import goldDigger.models.discoverer.Discoverer;
import goldDigger.models.discoverer.Geologist;
import goldDigger.models.operation.Operation;
import goldDigger.models.operation.OperationImpl;
import goldDigger.models.spot.Spot;
import goldDigger.models.spot.SpotImpl;
import goldDigger.repositories.DiscovererRepository;
import goldDigger.repositories.SpotRepository;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static goldDigger.common.ConstantMessages.*;
import static goldDigger.common.ExceptionMessages.*;

public class ControllerImpl implements Controller{
    private static final double NEEDED_ENERGY_FOR_MISSION = 45;
    private DiscovererRepository discovererRepository;
    private SpotRepository spotRepository;
    private int spotCount;
    @Override
    public String addDiscoverer(String kind, String discovererName) {
        Discoverer discoverer;
       switch (kind) {
           case "Anthropologist":
               discoverer = new Anthropologist(discovererName);
               break;
           case "Archaeologist":
               discoverer = new Archaeologist(discovererName);
               break;
           case "Geologist":
               discoverer = new Geologist(discovererName);
               break;
           default:
               throw new IllegalArgumentException(DISCOVERER_INVALID_KIND);
       }
       discovererRepository.add(discoverer);
       return String.format(DISCOVERER_ADDED,kind,discovererName);

    }

    @Override
    public String addSpot(String spotName, String... exhibits) {
        Spot spot = new SpotImpl(spotName);
        for (String exhibit : exhibits) {
            spot.getExhibits().add(exhibit);
        }
        spotRepository.add(spot);
        return String.format(SPOT_ADDED,spotName);
    }

    @Override
    public String excludeDiscoverer(String discovererName) {
        Discoverer discoverer = discovererRepository.byName(discovererName);
            if (discoverer == null){
                throw  new IllegalArgumentException(String.format(DISCOVERER_DOES_NOT_EXIST,discovererName));
            }
        discovererRepository.remove(discoverer);
        return String.format(DISCOVERER_EXCLUDE,discovererName);

    }

    @Override
    public String inspectSpot(String spotName) {
        List<Discoverer> suitableDiscoverer = discovererRepository.getCollection().
                stream().filter(d -> d.getEnergy() > NEEDED_ENERGY_FOR_MISSION).collect(Collectors.toList());
        if (suitableDiscoverer.isEmpty()){
            throw new IllegalArgumentException(SPOT_DISCOVERERS_DOES_NOT_EXISTS);
        }
        Spot spot = spotRepository.byName(spotName);
        Operation operation = new OperationImpl();
        operation.startOperation(spot,suitableDiscoverer);
        long tiredDiscoverers = suitableDiscoverer.stream().filter(d -> d.getEnergy() == 0).count();
        spotCount++;
        return String.format(INSPECT_SPOT,spotName,tiredDiscoverers);
    }

    @Override
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(FINAL_SPOT_INSPECT,spotCount)).append(System.lineSeparator());
        sb.append(FINAL_DISCOVERER_INFO).append(System.lineSeparator());
        Collection<Discoverer> discoverers = discovererRepository.getCollection();
        for (Discoverer discoverer : discoverers) {
            sb.append(String.format(FINAL_DISCOVERER_NAME,discoverer.getName())).append(System.lineSeparator());
            sb.append(String.format(FINAL_DISCOVERER_ENERGY,discoverer.getEnergy())).append(System.lineSeparator());
            Collection<String> exhibits = discoverer.getMuseum().getExhibits();
            if (exhibits.isEmpty()){
                sb.append(String.format(FINAL_DISCOVERER_MUSEUM_EXHIBITS,"None")).append(System.lineSeparator());
            } else {
                String allMuseumText  = String.join(FINAL_DISCOVERER_MUSEUM_EXHIBITS_DELIMITER,exhibits);
                sb.append(String.format(FINAL_DISCOVERER_MUSEUM_EXHIBITS,allMuseumText));
            }
        }
        return sb.toString();
    }
}
