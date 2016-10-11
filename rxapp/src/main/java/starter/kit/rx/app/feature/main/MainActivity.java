package starter.kit.rx.app.feature.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import butterknife.BindView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import java.util.ArrayList;
import java.util.List;
import starter.kit.feature.rx.RxStarterActivity;
import starter.kit.rx.app.R;
import starter.kit.rx.app.RxApp;
import starter.kit.rx.app.feature.auth.LoginActivity;
import starter.kit.rx.app.feature.content.ContentActivity;
import starter.kit.rx.app.feature.feed.IdFeedFragment;
import starter.kit.rx.app.feature.feed.NoPageFeedFragment;
import starter.kit.rx.app.feature.feed.PageFeedFragment;
import starter.kit.rx.app.feature.util.SimpleHudActivity;

public class MainActivity extends RxStarterActivity implements Drawer.OnDrawerItemClickListener {

  //save our header or result
  private AccountHeader headerResult = null;
  private Drawer result = null;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.viewPager) ViewPager mViewPager;
  @BindView(R.id.tabLayout) TabLayout mTabLayout;
  @BindView(R.id.collapsingToolbarLayout) CollapsingToolbarLayout mCollapsingToolbarLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setupToolbar();
    setupDrawer(savedInstanceState);

    setupViewPager();
    setupTabLayout();

    setupCollapsingToolbar();
  }

  private void setupCollapsingToolbar() {
    mCollapsingToolbarLayout.setTitleEnabled(false);
  }

  private void setupTabLayout() {
    mTabLayout.setupWithViewPager(mViewPager);
  }

  private void setupViewPager() {
    SimplePagerAdapter adapter = new SimplePagerAdapter(getSupportFragmentManager());
    adapter.addFrag(IdFeedFragment.create(), "RxIdentifier");
    adapter.addFrag(PageFeedFragment.create(), "RxPager");
    adapter.addFrag(NoPageFeedFragment.create(), "No Page");
    mViewPager.setAdapter(adapter);

    final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, RxApp.appResources().getDisplayMetrics());
    mViewPager.setPageMargin(pageMargin);
  }

  private void setupToolbar() {
    setSupportActionBar(mToolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setTitle(R.string.app_name);
    }
  }

  private void setupDrawer(Bundle savedInstanceState) {
    // Create a few sample profile
    // NOTE you have to define the loader logic too. See the CustomApplication for more details
    final IProfile profile = new ProfileDrawerItem().withName("Smartydroid")
        .withEmail("smartydroid@gmail.com")
        .withIcon("https://avatars2.githubusercontent.com/u/13810934?v=3&s=460");

    // Create the AccountHeader
    headerResult = new AccountHeaderBuilder().withActivity(this)
        .withHeaderBackground(R.drawable.header)
        .addProfiles(profile)
        .withSavedInstance(savedInstanceState)
        .build();

    //Create the drawer
    result = new DrawerBuilder().withActivity(this)
        .withToolbar(mToolbar)
        .withHasStableIds(true)
        .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
        .addDrawerItems(
            new PrimaryDrawerItem().withName("Feeds").withIcon(FontAwesome.Icon.faw_android).withTag(TAG_FEEDS),
            new PrimaryDrawerItem().withName("Login").withIcon(FontAwesome.Icon.faw_adjust).withTag(TAG_LOGIN),
            new PrimaryDrawerItem().withName("SimpleHud").withIcon(FontAwesome.Icon.faw_adjust).withTag(TAG_SIMPLE_HUD),
            new PrimaryDrawerItem().withName("Content").withIcon(FontAwesome.Icon.faw_connectdevelop).withTag(TAG_CONTENT)
        )
        .addStickyDrawerItems(
            new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
            new PrimaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github)
        )
        .withOnDrawerItemClickListener(this)
        .withSavedInstance(savedInstanceState)
        .withShowDrawerOnFirstLaunch(true)
        .build();
  }

  private static final String TAG_FEEDS = "feeds";
  private static final String TAG_LOGIN = "login";
  private static final String TAG_SIMPLE_HUD = "SimpleHud";
  private static final String TAG_CONTENT = "ContentDemo";

  @Override public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
    if (TAG_LOGIN.equals(drawerItem.getTag())) {
      startActivity(new Intent(MainActivity.this, LoginActivity.class));
      return true;
    }
    if (TAG_SIMPLE_HUD.equals(drawerItem.getTag())) {
      startActivity(new Intent(MainActivity.this, SimpleHudActivity.class));
      return true;
    }
    if (TAG_CONTENT.equals(drawerItem.getTag())) {
      startActivity(new Intent(MainActivity.this, ContentActivity.class));
      return true;
    }
    return false;
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    //add the values which need to be saved from the drawer to the bundle
    outState = result.saveInstanceState(outState);
    //add the values which need to be saved from the accountHeader to the bundle
    outState = headerResult.saveInstanceState(outState);
    super.onSaveInstanceState(outState);
  }

  @Override public void onBackPressed() {
    //handle the back press :D close the drawer first and if the drawer is closed close the activity
    if (result != null && result.isDrawerOpen()) {
      result.closeDrawer();
    } else {
      super.onBackPressed();
    }
  }

  static class SimplePagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public SimplePagerAdapter(FragmentManager manager) {
      super(manager);
    }

    @Override
    public Fragment getItem(int position) {
      return mFragments.get(position);
    }

    @Override
    public int getCount() {
      return mFragments.size();
    }

    public void addFrag(Fragment fragment, String title) {
      mFragments.add(fragment);
      mFragmentTitles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return mFragmentTitles.get(position);
    }
  }

}
