package com.bizconnectivity.gino.data;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.models.DealCategoryList;

import java.util.ArrayList;
import java.util.List;

public class DealCategory {

    public static List<DealCategoryList> dealCategoryData() {

        List<DealCategoryList> dealCategoryLists = new ArrayList<>();

        DealCategoryList category = new DealCategoryList();
        category.setCategoryTitle("Popular");
//        category.setCategoryImageURL("https://web-assets.myfave.com/images/icons/eat-b6651aa009cf18bd5eb111e66aa1c52b.svg");
        category.setCategoryImageURL(R.drawable.ic_whatshot_white_36dp);
        dealCategoryLists.add(category);

        DealCategoryList category6 = new DealCategoryList();
        category6.setCategoryTitle("New");
//        category.setCategoryImageURL("https://web-assets.myfave.com/images/icons/eat-b6651aa009cf18bd5eb111e66aa1c52b.svg");
        category6.setCategoryImageURL(R.drawable.ic_new_releases_white_36dp);
        dealCategoryLists.add(category6);

        DealCategoryList category0 = new DealCategoryList();
        category0.setCategoryTitle("Nearby");
//        category.setCategoryImageURL("https://web-assets.myfave.com/images/icons/eat-b6651aa009cf18bd5eb111e66aa1c52b.svg");
        category0.setCategoryImageURL(R.drawable.ic_near_me_white_36dp);
        dealCategoryLists.add(category0);

        DealCategoryList category1 = new DealCategoryList();
        category1.setCategoryTitle("Eat");
//        category.setCategoryImageURL("https://web-assets.myfave.com/images/icons/eat-b6651aa009cf18bd5eb111e66aa1c52b.svg");
        category1.setCategoryImageURL(R.drawable.ic_restaurant_white_36dp);
        dealCategoryLists.add(category1);

        DealCategoryList category2 = new DealCategoryList();
        category2.setCategoryTitle("Beauty");
//        category2.setCategoryImageURL("https://web-assets.myfave.com/images/icons/beauty-be9e0038a61ebf963e7a1ea0b6edb248.svg");
        category2.setCategoryImageURL(R.drawable.ic_spa_white_36dp);
        dealCategoryLists.add(category2);

        DealCategoryList category3 = new DealCategoryList();
        category3.setCategoryTitle("Activities");
//        category3.setCategoryImageURL("https://web-assets.myfave.com/images/icons/fit-44617e33cc4df79564319a809fbc5f5d.svg");
        category3.setCategoryImageURL(R.drawable.ic_directions_bike_white_36dp);
        dealCategoryLists.add(category3);

        DealCategoryList category4 = new DealCategoryList();
        category4.setCategoryTitle("Services");
//        category4.setCategoryImageURL("https://web-assets.myfave.com/images/icons/service-51c4eff83d403cbd5ac60fe09f1a3e49.svg");
        category4.setCategoryImageURL(R.drawable.ic_build_white_36dp);
        dealCategoryLists.add(category4);

        DealCategoryList category5 = new DealCategoryList();
        category5.setCategoryTitle("Travel");
//        category5.setCategoryImageURL("https://web-assets.myfave.com/images/icons/travel-4803c44af30c0e284857e758188b4e31.svg");
        category5.setCategoryImageURL(R.drawable.ic_flight_white_36dp);
        dealCategoryLists.add(category5);

        return dealCategoryLists;
    }
}
