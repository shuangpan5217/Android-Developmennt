package edu.sjsu.android.animallisting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// ActionBarActivity is deprecated.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] details = new String[5];
        details[0] = "The tiger is endangered throughout its range, " +
                "which stretches from the Russian Far East through" +
                " parts of North Korea, China, India, and Southeast Asia" +
                " to the Indonesian island of Sumatra. The Siberian," +
                " or Amur, tiger (P. tigris altaica) is the largest" +
                ", measuring up to 4 metres (13 feet) " +
                "in total length and weighing up to 300 " +
                "kg (660 pounds).";
        details[1] = "Lions have strong, compact bodies and powerful forelegs" +
                ", teeth and jaws for pulling down and killing prey. Their coats" +
                " are yellow-gold, and adult males have shaggy manes that range" +
                " in color from blond to reddish-brown to black. The length and" +
                " color of a lion's mane is likely determined by age, genetics" +
                " and hormones. Young lions have light spotting on their coats" +
                " that disappears as they grow.";
        details[2] = "The giant panda has an insatiable appetite for bamboo. " +
                "A typical animal eats half the day—a full 12 out of every 24" +
                " hours—and relieves itself dozens of times a day. It takes 28" +
                " pounds of bamboo to satisfy a giant panda's daily dietary needs," +
                " and it hungrily plucks the stalks with elongated wrist bones that" +
                " function rather like thumbs. Pandas will sometimes eat birds " +
                "or rodents as well.";
        details[3] = "The red panda is dwarfed by the black-and-white giant that" +
                " shares its name. These pandas typically grow to the size of a" +
                " house cat, though their big, bushy tails add an additional 18" +
                " inches. The pandas use their ringed tails as wraparound " +
                "blankets in the chilly mountain heights.";
        details[4] = "Earth’s largest living crocodilian—and, some say, the animal" +
                " most likely to eat a human—is the saltwater or estuarine crocodile." +
                " Average-size males reach 17 feet and 1,000 pounds, but specimens" +
                " 23 feet long and weighing 2,200 pounds are not uncommon.";

        setRecyclerView(details);
    }

    // inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_main, menu);
        return true;
    }

    // menu items listeners
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.information){
            Intent dialIntent = new Intent(this, ZooInfo.class);
            startActivity(dialIntent);
            return true;
        }else if(id == R.id.uninstall){
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE);
            uninstallIntent.setData(Uri.parse("package:" + "edu.sjsu.android.animallisting"));
            startActivity(uninstallIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //set recycler view
    private void setRecyclerView(String[] details){
        List<Animal> animalList = new ArrayList<>();

        animalList.add(new Animal("Tiger","tiger.jpg", details[0]));
        animalList.add(new Animal("Giant Panda","panda.jpg", details[2]));
        animalList.add(new Animal("Red Panda","red_panda.jpg", details[3]));
        animalList.add(new Animal("Lion","lion.jpg", details[1]));
        animalList.add(new Animal("Saltwater Crocodile","crocodile.jpg", details[4]));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        AnimalAdapter animalAdapter = new AnimalAdapter(animalList, MainActivity.this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(animalAdapter);
        System.out.println("Does it run");
    }
}