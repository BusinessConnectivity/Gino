package com.bizconnectivity.gino.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.data.DealCategory;
import com.bizconnectivity.gino.data.Deals;
import com.bizconnectivity.gino.fragments.HomeFragment;
import com.bizconnectivity.gino.fragments.ProfileFragment;
import com.bizconnectivity.gino.fragments.PurchasesFragment;
import com.bizconnectivity.gino.fragments.SearchFragment;
import com.bizconnectivity.gino.models.DealCategoryList;
import com.bizconnectivity.gino.models.DealList;
import com.bizconnectivity.gino.models.FirstTimeInstall;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Layout Binding
        ButterKnife.bind(this);

        // Bottom Navigation
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Default Fragment
        switchFragment(new HomeFragment());

        // Initial Realm
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        // Check if first time install
        if (realm.where(FirstTimeInstall.class).findAll().size() == 0) prePolulateDataSet();
    }

    // Bottom Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    switchFragment(new HomeFragment());
                    return true;

                case R.id.navigation_search:
                    switchFragment(new SearchFragment());
                    return true;

                case R.id.navigation_purchase:
                    switchFragment(new PurchasesFragment());
                    return true;

                case R.id.navigation_profile:
                    switchFragment(new ProfileFragment());
                    return true;
            }
            return false;
        }
    };

    // Switch Fragment
    private void switchFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void prePolulateDataSet() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                FirstTimeInstall firstTimeInstall = realm.createObject(FirstTimeInstall.class);
                firstTimeInstall.setIsFirstTime("No");
                realm.copyToRealm(firstTimeInstall);
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                List<DealCategoryList> dealCategoryLists = DealCategory.dealCategoryData();

                for (int i = 0; i<dealCategoryLists.size(); i++) {

                    DealCategoryList category = realm.createObject(DealCategoryList.class, i+1);
                    category.setCategoryTitle(dealCategoryLists.get(i).getCategoryTitle());
                    category.setCategoryImageURL(dealCategoryLists.get(i).getCategoryImageURL());
                    realm.copyToRealm(category);
                }

                List<DealList> dealLists = Deals.dealListData();

                for (int i=0; i<dealLists.size(); i++) {

                    DealList dealList = realm.createObject(DealList.class, i+1);
                    dealList.setDealTitle(dealLists.get(i).getDealTitle());
                    dealList.setDealDescription(dealLists.get(i).getDealDescription());
                    dealList.setDealPrice(dealLists.get(i).getDealPrice());
                    dealList.setDealLocation(dealLists.get(i).getDealLocation());
                    dealList.setDealImageURL(dealLists.get(i).getDealImageURL());
                    dealList.setDealPromotionStart(dealLists.get(i).getDealPromotionStart());
                    dealList.setDealPromotionEnd(dealLists.get(i).getDealPromotionEnd());
                    dealList.setDealRedeemStart(dealLists.get(i).getDealRedeemStart());
                    dealList.setDealRedeemEnd(dealLists.get(i).getDealRedeemEnd());
                    dealList.setDealMerchant(dealLists.get(i).getDealMerchant());
                    dealList.setDealCategoryID(dealLists.get(i).getDealCategoryID());
                    dealList.setIsExpired(dealLists.get(i).getIsExpired());

                    realm.copyToRealm(dealList);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (!realm.isClosed()) realm.close();
    }
}
