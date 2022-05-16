package ru.restaurantvoting.web.restaurant;

import ru.restaurantvoting.model.Restaurant;
import ru.restaurantvoting.web.MatcherFactory;

import java.util.List;


public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "dishes", "votes");

    public static final Restaurant REST1 = new Restaurant(1, "BELORYSKIY");
    public static final Restaurant REST2 = new Restaurant(2, "ASIATKIY");
    public static final Restaurant REST3 = new Restaurant(3, "ARGENTINSKIY");

    public static final Restaurant UPDATED_REST1 = new Restaurant(1, "newBELORYSKIY");

    public static final Restaurant NEW_REST = new Restaurant("newRestaurant");

    public static final Restaurant NEW_REST_INVALIDNAME = new Restaurant("");

    public static final List<Restaurant> ALL_RESTS = List.of(REST1, REST2, REST3);

}
