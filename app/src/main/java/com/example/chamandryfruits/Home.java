package com.example.chamandryfruits;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.util.Objects;

import static com.example.chamandryfruits.RegisterActivity.setSignUpFragment;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int HOME_FRAGMENT = 0;
    private static final int ORDERS_FRAGMENT = 1;
    private static final int REWARDS_FRAGMENT = 2;
    private static final int CART_FRAGMENT = 3;
    private static final int WISHLIST_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;
    public static boolean showCart = false;
    public static boolean restHome = false;

    private ImageView mainLogo;
    private AppBarConfiguration mAppBarConfiguration;
    public static FrameLayout frameLayout;
    private ImageView noInternetConnection;
    private NavigationView navigationView;
    public static DrawerLayout drawer;
    private int currentFragment = -1;
    public static Activity mainActivity;

    private Window window;
    public static Toolbar toolbar;

    private Dialog signInDialog;

    private FirebaseUser currentUser;
    private TextView badgeCount;
    private int scrollFlags;
    private AppBarLayout.LayoutParams params;
    private int previousPosition = 0;
    private int currentPosition = 0;


    MenuItem item;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        mainLogo = findViewById(R.id.main_logo);
        setSupportActionBar(toolbar);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations
        signInDialog = new Dialog(Home.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        Objects.requireNonNull(signInDialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_my_mall, R.id.nav_my_orders, R.id.nav_my_rewards)
                .setDrawerLayout(drawer)
                .build();

        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.getMenu().getItem(0).setChecked(true);
        ////baki method ma jaye ga

        frameLayout = findViewById(R.id.main_layout);
        boolean cart = getIntent().getBooleanExtra("CART", false);
        if (showCart) {
            drawer.closeDrawers();
            mainActivity = this;
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            GoToFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
        } else {
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            SetFragment(new HomeFragment(), HOME_FRAGMENT);
        }


        final Button signInBtn = signInDialog.findViewById(R.id.dialog_sign_in_button);
        Button signUpBtn = signInDialog.findViewById(R.id.dialog_sign_up_button);

        final Intent registerIntent = new Intent(Home.this, RegisterActivity.class);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseButton = true;
                SignUpFragment.disableCloseButton = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseButton = true;
                SignInFragment.disableCloseButton = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });
        onNavigationItemSelected(item);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        onNavigationItemSelected(item);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        } else {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }
        if (restHome) {
            restHome = false;
            mainLogo.setVisibility(View.VISIBLE);
            SetFragment(new HomeFragment(), HOME_FRAGMENT);
            navigationView.getMenu().getItem(0).setChecked(true);
        }

        invalidateOptionsMenu();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment == HOME_FRAGMENT) {
                currentFragment = -1;
                super.onBackPressed();
            } else {
                if (showCart) {
                    mainActivity = null;
                    showCart = false;
                    finish();
                } else {
                    mainLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    SetFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_search_icon) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        } else if (id == R.id.main_notification_icon) {
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                GoToFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
            }
            return true;

        } else if (id == android.R.id.home) {
            if (showCart) {
                mainActivity = null;
                showCart = false;
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void GoToFragment(String title, Fragment fragment, int fragmentNo) {

        onNavigationItemSelected(item);
        navigationView.getMenu().getItem(previousPosition).setChecked(false);
        navigationView.getMenu().getItem(currentPosition).setChecked(true);
        previousPosition = currentPosition;

        drawer.closeDrawers();
        drawer.closeDrawer(GravityCompat.START);
        mainLogo.setVisibility(View.GONE);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        SetFragment(fragment, fragmentNo);

        if (fragmentNo == CART_FRAGMENT || showCart) {
            params.setScrollFlags(0);
        } else {
            params.setScrollFlags(scrollFlags);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == HOME_FRAGMENT) {
            drawer.closeDrawers();
            drawer.closeDrawer(GravityCompat.START);
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.home, menu);
            MenuItem cartItem = menu.findItem(R.id.main_cart_icon);
            cartItem.setActionView(R.layout.badge_layout);
            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.mipmap.cart_white);

            if (currentUser != null) {

                if (DBQueries.cartList.size() == 0) {
                    DBQueries.LoadCartList(Home.this, new Dialog(Home.this), false, badgeCount, new TextView(Home.this));
                } else {
                    badgeCount.setVisibility(View.VISIBLE);
                    if (DBQueries.cartList.size() < 99) {
                        badgeCount.setText(String.valueOf(DBQueries.cartList.size()));
                    } else {
                        badgeCount.setText("99");
                    }
                }

            }
            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        GoToFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                    }
                }
            });

        }
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void SetFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment) {
            if (fragmentNo == REWARDS_FRAGMENT) {
                window.setStatusBarColor(Color.parseColor("#5B04B1"));
                toolbar.setBackgroundColor(Color.parseColor("#5B04B1"));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        drawer.closeDrawer(GravityCompat.START);
                        item = menuItem;

                        if (currentUser != null) {
                            int id = item.getItemId();
                            if (id == R.id.nav_my_mall) {
                                mainLogo.setVisibility(View.VISIBLE);
                                invalidateOptionsMenu();
                                currentPosition = HOME_FRAGMENT;
                                navigationView.getMenu().getItem(previousPosition).setChecked(false);
                                navigationView.getMenu().getItem(0).setChecked(true);
                                previousPosition = 0;
                                SetFragment(new HomeFragment(), HOME_FRAGMENT);
                            } else if (id == R.id.nav_my_account) {
                                currentPosition = ACCOUNT_FRAGMENT;
                                drawer.closeDrawer(GravityCompat.START);
                                GoToFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);
                            } else if (id == R.id.nav_my_cart) {
                                currentPosition = CART_FRAGMENT;
                                drawer.closeDrawer(GravityCompat.START);
                                GoToFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                            } else if (id == R.id.nav_my_orders) {
                                currentPosition = ORDERS_FRAGMENT;
                                drawer.closeDrawer(GravityCompat.START);
                                GoToFragment("My Orders", new MyOrdersFragment(), ORDERS_FRAGMENT);
                            } else if (id == R.id.nav_my_rewards) {
                                currentPosition = REWARDS_FRAGMENT;
                                drawer.closeDrawer(GravityCompat.START);
                                GoToFragment("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);
                            } else if (id == R.id.nav_my_wishlist) {
                                currentPosition = WISHLIST_FRAGMENT;
                                drawer.closeDrawer(GravityCompat.START);
                                GoToFragment("My Wishlist", new MyWishListFragment(), WISHLIST_FRAGMENT);
                            } else if (id == R.id.sign_out) {
                                FirebaseAuth.getInstance().signOut();
                                DBQueries.clearData();
                                Intent registerIntent = new Intent(Home.this, RegisterActivity.class);
                                startActivity(registerIntent);
                                finish();
                            }
                            menuItem.setChecked(true);
                            //drawerLayout.closeDrawers();
                            return true;
                        } else {
                            drawer.closeDrawer(GravityCompat.START);
                            signInDialog.show();
                            return false;
                        }
                    }
                });

        return true;
    }

    public class CartBroadCast extends BroadcastReceiver {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), "SHOW_CART")) {
                mainActivity = Home.this;
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                GoToFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
            }
        }
    }

}


