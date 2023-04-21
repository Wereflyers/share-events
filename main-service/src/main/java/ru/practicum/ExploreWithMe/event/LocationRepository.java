package ru.practicum.ExploreWithMe.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ExploreWithMe.event.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByLonAndLat(Float lon, Float lat);
}
