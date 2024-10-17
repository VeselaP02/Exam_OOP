package goldDigger.models.operation;

import goldDigger.models.discoverer.Discoverer;
import goldDigger.models.spot.Spot;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OperationImpl implements Operation{
    @Override
    public void startOperation(Spot spot, Collection<Discoverer> discoverers) {
        Collection<String> exhibitsInSpot = spot.getExhibits();

        for (Discoverer discoverer : discoverers) {
            while (discoverer.canDig() && exhibitsInSpot.iterator().hasNext()){
                discoverer.dig();
                String currentExhibits = exhibitsInSpot.iterator().next();
                discoverer.getMuseum().getExhibits().add(currentExhibits);
                exhibitsInSpot.remove(currentExhibits);
            }
        }

    }
}
