package ru.restaurantvoting.web.dish;

import ru.restaurantvoting.model.Dish;
import ru.restaurantvoting.web.MatcherFactory;

import java.util.List;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "id", "restId");

    public static final Dish DISH_1_REST_1 = new Dish("JUK", 10);
    public static final Dish DISH_2_REST_1 = new Dish("BOBI", 20);
    public static final List<Dish> REST_1_DISHES = List.of(DISH_1_REST_1, DISH_2_REST_1);

    public static final Dish DISH_1_REST_2 = new Dish("KENGURU", 90);
    public static final Dish DISH_2_REST_2 = new Dish("MEDUZA", 20);
    public static final List<Dish> REST_2_DISHES = List.of(DISH_1_REST_2, DISH_2_REST_2);

    public static final Dish DISH_1_REST_3 = new Dish("LEPECHKA", 10);
    public static final Dish DISH_2_REST_3 = new Dish("POHLEBKA", 30);
    public static final List<Dish> REST_3_DISHES = List.of(DISH_1_REST_3, DISH_2_REST_3);

    public static final Dish DISH_1_REST_4 = new Dish("HACHAPURI", 50);
    public static final Dish DISH_2_REST_4 = new Dish("LEPECHKA", 20);
    public static final List<Dish> REST_4_DISHES = List.of(DISH_1_REST_4, DISH_2_REST_4);

    public static final Dish DISH_1_REST_5 = new Dish("KASHA", 40);
    public static final Dish DISH_2_REST_5 = new Dish("KOMPOT", 20);
    public static final List<Dish> REST_5_DISHES = List.of(DISH_1_REST_5, DISH_2_REST_5);

    public static final Dish DISH_1_REST_6 = new Dish("SHASHLIK", 80);
    public static final Dish DISH_2_REST_6 = new Dish("BANAN", 10);
    public static final List<Dish> REST_6_DISHES = List.of(DISH_1_REST_6, DISH_2_REST_6);

    public static final Dish DISH_1_REST_7 = new Dish("PURE", 20);
    public static final Dish DISH_2_REST_7 = new Dish("SIRNIKI", 30);
    public static final List<Dish> REST_7_DISHES = List.of(DISH_1_REST_7, DISH_2_REST_7);

    public static final Dish DISH_1_REST_8 = new Dish("LAVASH", 10);
    public static final Dish DISH_2_REST_8 = new Dish("BARAN", 70);
    public static final List<Dish> REST_8_DISHES = List.of(DISH_1_REST_8, DISH_2_REST_8);

    public static final Dish DISH_1_REST_9 = new Dish("KREVETKA", 20);
    public static final Dish DISH_2_REST_9 = new Dish("MASAMORA", 30);
    public static final List<Dish> REST_9_DISHES = List.of(DISH_1_REST_9, DISH_2_REST_9);

    public static final Dish DISH_1_REST_1_UPDATED = new Dish("updatedJUK", 100);
    public static final Dish DISH_1_REST_1_UPDATED_BAD_PRICE = new Dish("newJUK", 0);
    public static final Dish DISH_NEW_REST_1 = new Dish("Cacao", 4);
    public static final Dish DISH_NEW_REST_1_DUPLICATED_NAMES = new Dish("JUK", 4);
    public static final List<Dish> REST_1_MEALS_NEW = List.of(DISH_1_REST_1, DISH_2_REST_1, DISH_NEW_REST_1);


}
