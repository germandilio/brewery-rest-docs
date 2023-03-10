package guru.springframework.msscbrewery.services.v2;

import guru.springframework.msscbrewery.web.model.v2.BeerDtoV2;
import java.util.Optional;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by jt on 2019-04-23.
 */
@Service
public class BeerServiceV2Impl implements BeerServiceV2 {
    @Override
    public Optional<BeerDtoV2> getBeerById(UUID beerId) {
        return Optional.empty();
    }

    @Override
    public Optional<BeerDtoV2> saveNewBeer(BeerDtoV2 beerDto) {
        return Optional.empty();
    }

    @Override
    public void updateBeer(UUID beerId, BeerDtoV2 beerDto) {

    }

    @Override
    public void deleteById(UUID beerId) {

    }
}
