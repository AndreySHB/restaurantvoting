package ru.restaurantvoting.web.dish;

import ru.restaurantvoting.model.Dish;
import ru.restaurantvoting.web.MatcherFactory;

import static ru.restaurantvoting.web.vote.VoteTestData.CURRENT_DATE;
import static ru.restaurantvoting.web.vote.VoteTestData.YESTERDAY_DATE;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "id", "restId");
    public static final Dish DISH_1 = new Dish("JUK", 10, YESTERDAY_DATE, 1);
    public static final Dish DISH_2 = new Dish("BOBI", 20, YESTERDAY_DATE);
    public static final Dish NEW_DISH1 = new Dish("newJUK", 10, YESTERDAY_DATE, 1);

    public static final Dish NEW_DISH_BAD_PRICE = new Dish("newJUK", 0, CURRENT_DATE, 1);
    public static final Dish NEW_DISH = new Dish("Cacao", 4, CURRENT_DATE, 2);
    public static final Dish NEW_DISH_DUPLICATED = new Dish("JUK", 4, YESTERDAY_DATE, 1);



}
