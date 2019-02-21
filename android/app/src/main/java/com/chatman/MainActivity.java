package com.chatman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.chatman.adapter.ChatListAdapter;
import com.chatman.helper.PreferencesHelper;
import com.chatman.model.ChatList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recycler;
    private ChatListAdapter adapter;
    private List<ChatList> chatLists;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        // Recycler View
        recycler = findViewById(R.id.chat_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);
        getChatLists();
        adapter = new ChatListAdapter(chatLists);
        recycler.setAdapter(adapter);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent navigationBarIntent;
        if (id == R.id.nav_side_logout) {
            // Handle the camera action
            navigationBarIntent = new Intent(MainActivity.this, AuthActivity.class);
            PreferencesHelper.setUserFirebaseKey(this, "");
            PreferencesHelper.setUserName(this,"");
            PreferencesHelper.setHasLogin(this, false);

            finish();
            startActivity(navigationBarIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Todo: Ganti data dummmy jadi data asli ngambil dari database
    private List<ChatList> getChatLists() {
        chatLists = new ArrayList<>();
        chatLists.add(new ChatList(
                R.drawable.priagung,
                "Priagung Satyagama",
                "Woi sini ke sekre 2 temani diriku yang kesepian"
        ));
        chatLists.add(new ChatList(
                R.drawable.rama,
                "Yusuf Rahmat Pratama",
                "Eh, gmn gmn?"
        ));
        chatLists.add(new ChatList(
                R.drawable.priagung,
                "Priagung Satyagama",
                "Woi sini ke sekre 2 temani diriku yang kesepian"
        ));
        chatLists.add(new ChatList(
                R.drawable.rama,
                "Yusuf Rahmat Pratama",
                "Eh, gmn gmn?"
        ));
        chatLists.add(new ChatList(
                R.drawable.priagung,
                "Priagung Satyagama",
                "Woi sini ke sekre 2 temani diriku yang kesepian"
        ));
        chatLists.add(new ChatList(
                R.drawable.rama,
                "Yusuf Rahmat Pratama",
                "Eh, gmn gmn?"
        ));
        chatLists.add(new ChatList(
                R.drawable.priagung,
                "Priagung Satyagama",
                "Woi sini ke sekre 2 temani diriku yang kesepian"
        ));
        chatLists.add(new ChatList(
                R.drawable.rama,
                "Yusuf Rahmat Pratama",
                "Eh, gmn gmn?"
        ));
        chatLists.add(new ChatList(
                R.drawable.priagung,
                "Priagung Satyagama",
                "Woi sini ke sekre 2 temani diriku yang kesepian"
        ));
        chatLists.add(new ChatList(
                R.drawable.rama,
                "Yusuf Rahmat Pratama",
                "Eh, gmn gmn?"
        ));
        chatLists.add(new ChatList(
                R.drawable.priagung,
                "Priagung Satyagama",
                "Woi sini ke sekre 2 temani diriku yang kesepian"
        ));
        chatLists.add(new ChatList(
                R.drawable.rama,
                "Yusuf Rahmat Pratama",
                "Eh, gmn gmn?"
        ));
        return chatLists;
    }
}
