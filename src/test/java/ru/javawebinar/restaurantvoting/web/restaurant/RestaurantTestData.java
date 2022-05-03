package ru.javawebinar.restaurantvoting.web.restaurant;

import ru.javawebinar.restaurantvoting.model.Restaurant;
import ru.javawebinar.restaurantvoting.web.MatcherFactory;

import java.util.List;

import static ru.javawebinar.restaurantvoting.web.vote.VoteTestData.*;


public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "id", "menu");

    public static final Restaurant rest1 = new Restaurant("AFRIKANSKIY", YESTERDAY_DATE);
    public static final Restaurant rest2 = new Restaurant("AVSTRALIYSKIY", YESTERDAY_DATE);
    public static final Restaurant rest3 = new Restaurant("SLOVACKIY", YESTERDAY_DATE);
    public static final List<Restaurant> yesterdayRests = List.of(rest1, rest2, rest3);

    public static final Restaurant rest4 = new Restaurant("GRUZINSKIY", CURRENT_DATE);
    public static final Restaurant rest5 = new Restaurant("RUSSKIY", CURRENT_DATE);
    public static final Restaurant rest6 = new Restaurant("ARMANSKIY", CURRENT_DATE);
    public static final List<Restaurant> todayRests = List.of(rest4, rest5, rest6);

    public static final Restaurant rest7 = new Restaurant("BELORYSKIY", TOMORROW_DATE);
    public static final Restaurant rest8 = new Restaurant("ASIATKIY", TOMORROW_DATE);
    public static final Restaurant rest9 = new Restaurant("ARGENTINSKIY", TOMORROW_DATE);

    public static final Restaurant restNew = new Restaurant("newRest", CURRENT_DATE);

    public static final Restaurant restNewInvalid = new Restaurant("", CURRENT_DATE);

    public static final List<Restaurant> tomorrowRests = List.of(rest7, rest8, rest9);
}
