package com.bizconnectivity.gino.data;

import com.bizconnectivity.gino.models.DealCategoryList;

import java.util.ArrayList;
import java.util.List;

public class DealCategory {

    public static List<DealCategoryList> dealCategoryData() {

        List<DealCategoryList> dealCategoryLists = new ArrayList<>();

        DealCategoryList category = new DealCategoryList();
        category.setCategoryTitle("Eat");
        category.setCategoryImageURL("https://web-assets.myfave.com/images/icons/eat-b6651aa009cf18bd5eb111e66aa1c52b.svg");
        dealCategoryLists.add(category);

        DealCategoryList category2 = new DealCategoryList();
        category2.setCategoryTitle("Beauty");
        category2.setCategoryImageURL("https://web-assets.myfave.com/images/icons/beauty-be9e0038a61ebf963e7a1ea0b6edb248.svg");
        dealCategoryLists.add(category2);

        DealCategoryList category3 = new DealCategoryList();
        category3.setCategoryTitle("Activities");
        category3.setCategoryImageURL("https://web-assets.myfave.com/images/icons/fit-44617e33cc4df79564319a809fbc5f5d.svg");
        dealCategoryLists.add(category3);

        DealCategoryList category4 = new DealCategoryList();
        category4.setCategoryTitle("Services");
        category4.setCategoryImageURL("https://web-assets.myfave.com/images/icons/service-51c4eff83d403cbd5ac60fe09f1a3e49.svg");
        dealCategoryLists.add(category4);

        DealCategoryList category5 = new DealCategoryList();
        category5.setCategoryTitle("Travel");
        category5.setCategoryImageURL("https://web-assets.myfave.com/images/icons/travel-4803c44af30c0e284857e758188b4e31.svg");
        dealCategoryLists.add(category5);

        return dealCategoryLists;
    }
}
