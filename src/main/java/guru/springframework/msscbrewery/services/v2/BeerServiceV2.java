package guru.springframework.msscbrewery.services.v2;

import guru.springframework.msscbrewery.web.model.v2.BeerDtoV2;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by jt on 2019-04-23.
 */
public interface BeerServiceV2 {
    Optional<BeerDtoV2> getBeerById(UUID beerId);

    Optional<BeerDtoV2> saveNewBeer(BeerDtoV2 beerDto);

    void updateBeer(UUID beerId, BeerDtoV2 beerDto);

    void deleteById(UUID beerId);
}
