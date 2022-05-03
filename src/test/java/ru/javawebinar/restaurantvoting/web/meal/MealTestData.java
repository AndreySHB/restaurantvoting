package ru.javawebinar.restaurantvoting.web.meal;

import ru.javawebinar.restaurantvoting.model.Meal;
import ru.javawebinar.restaurantvoting.web.MatcherFactory;

import java.util.List;

public class MealTestData {
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Meal.class, "id", "restId");

    public static final Meal meal1Rest1 = new Meal("JUK", 10);
    public static final Meal meal2Rest1 = new Meal("BOBI", 20);
    public static final List<Meal> rest1Meals = List.of(meal1Rest1, meal2Rest1);

    public static final Meal meal1Rest2 = new Meal("KENGURU", 90);
    public static final Meal meal2Rest2 = new Meal("MEDUZA", 20);
    public static final List<Meal> rest2Meals = List.of(meal1Rest2, meal2Rest2);

    public static final Meal meal1Rest3 = new Meal("LEPECHKA", 10);
    public static final Meal meal2Rest3 = new Meal("POHLEBKA", 30);
    public static final List<Meal> rest3Meals = List.of(meal1Rest3, meal2Rest3);

    public static final Meal meal1Rest4 = new Meal("HACHAPURI", 50);
    public static final Meal meal2Rest4 = new Meal("LEPECHKA", 20);
    public static final List<Meal> rest4Meals = List.of(meal1Rest4, meal2Rest4);

    public static final Meal meal1Rest5 = new Meal("KASHA", 40);
    public static final Meal meal2Rest5 = new Meal("KOMPOT", 20);
    public static final List<Meal> rest5Meals = List.of(meal1Rest5, meal2Rest5);

    public static final Meal meal1Rest6 = new Meal("SHASHLIK", 80);
    public static final Meal meal2Rest6 = new Meal("BANAN", 10);
    public static final List<Meal> rest6Meals = List.of(meal1Rest6, meal2Rest6);

    public static final Meal meal1Rest7 = new Meal("PURE", 20);
    public static final Meal meal2Rest7 = new Meal("SIRNIKI", 30);
    public static final List<Meal> rest7Meals = List.of(meal1Rest7, meal2Rest7);

    public static final Meal meal1Rest8 = new Meal("LAVASH", 10);
    public static final Meal meal2Rest8 = new Meal("BARAN", 70);
    public static final List<Meal> rest8Meals = List.of(meal1Rest8, meal2Rest8);

    public static final Meal meal1Rest9 = new Meal("KREVETKA", 20);
    public static final Meal meal2Rest9 = new Meal("MASAMORA", 30);
    public static final List<Meal> rest9Meals = List.of(meal1Rest9, meal2Rest9);

    public static final Meal meal1Rest1Updated = new Meal("updatedJUK", 100);
    public static final Meal meal1Rest1UpdatedBadPrice = new Meal("newJUK", 0);
    public static final Meal mealNewRest1 = new Meal("Cacao", 4);
    public static final Meal mealNewRest1DuplicatedNames = new Meal("JUK", 4);
    public static final List<Meal> rest1MealsNew = List.of(meal1Rest1, meal2Rest1, mealNewRest1);


}
