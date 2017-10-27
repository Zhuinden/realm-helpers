package com.zhuinden.realmpaginationexample.application;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.zhuinden.realmpaginationexample.application.injection.AppComponent;
import com.zhuinden.realmpaginationexample.application.injection.DaggerAppComponent;
import com.zhuinden.realmpaginationexample.data.dao.TaskDao;
import com.zhuinden.realmpaginationexample.data.entity.Task;

import java.util.Date;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Owner on 2017. 10. 09..
 */

public class CustomApplication
        extends Application {
    private static CustomApplication INSTANCE;

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder() //
                                              .deleteRealmIfMigrationNeeded()
                                              .initialData(realm -> {
                                                  Date date = new Date();
                                                  for(Tasks tasks : Tasks.values()) {
                                                      Task task = new Task();
                                                      task.setId(tasks.ordinal() + 1);
                                                      task.setText(tasks.getText());
                                                      task.setDate(date);
                                                      realm.insert(task);
                                                  }
                                              })
                                              .build());
        INSTANCE = this;
        appComponent = DaggerAppComponent.builder() //
                .application(this) //
                .build();

        final TaskDao taskDao = appComponent.taskDao();
        // this is even uglier
        final Random random = new Random();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Task task = new Task();
            task.setId(random.nextInt(500) + 250);
            task.setText("Poochy");
            task.setDate(new Date());
            taskDao.insert(task);
        }, 2500);
        handler.postDelayed(() -> {
            Task task = new Task();
            task.setId(random.nextInt(500) + 250);
            task.setText("Thomas");
            task.setDate(new Date());
            taskDao.insert(task);
        }, 5000);
        handler.postDelayed(() -> {
            Task task = new Task();
            task.setId(random.nextInt(500) + 250);
            task.setText("Bogus");
            task.setDate(new Date());
            taskDao.insert(task);
        }, 7500);
        handler.postDelayed(() -> {
            Task task = new Task();
            task.setId(random.nextInt(500) + 250);
            task.setText("Anderson");
            task.setDate(new Date());
            taskDao.insert(task);
        }, 10000);
    }

    public static CustomApplication get() {
        return INSTANCE;
    }

    private enum Tasks {
        _1("Figure out how to use Room."),
        _2("Consider applying Dagger-Android to show scoping things."),
        _3("Add a considerably higher number of items so that you can actually paginate."),
        BUBBLES("Bubbles"),
        BUNDLES("Bundles"),
        BUNNY("Bunny"),
        BUTTERCUP("Buttercup"),
        BUTTON("Button"),
        CHIPMUNK("Chipmunk"),
        CINNAMON("Cinnamon"),
        CUDDLES("Cuddles"),
        DAISY("Daisy"),
        DIMPLES("Dimples"),
        HICCUP("Hiccup"),
        HUGGIE("Huggie"),
        JELLYBEAN("Jellybean"),
        JIGGLES("Jiggles"),
        JUJUBE("Jujube"),
        KITKAT("Kitkat"),
        LOLLIPOP("Lollipop"),
        MARSHMALLOW("Marshmallow"),
        MUNCHKIN("Munchkin"),
        NIBBLES("Nibbles"),
        NUGGET("Nugget"),
        PANDA("Panda"),
        PEACHES("Peaches"),
        PICKLES("Pickles"),
        PIXIE("Pixie"),
        POCKET("Pocket"),
        SCHMOOPY("Schmoopy"),
        SKITTLES("Skittles"),
        SNICKERS("Snickers"),
        SNOWBALL("Snowball"),
        SNUGGLES("Snuggles"),
        SQUIGGLE("Squiggle"),
        TAFFY("Taffy"),
        TEACUP("Teacup"),
        TIPSY("Tipsy"),
        TWINKLES("Twinkles"),
        VELVET("Velvet"),
        WAFFLES("Waffles"),
        WIGGLES("Wiggles"),
        WINKY("Winky"),
        BAMBI("Bambi"),
        BARBIE("Barbie"),
        BLOSSOM("Blossom"),
        BLUEBELL("Bluebell"),
        CALYPSO("Calypso"),
        COOKIE("Cookie"),
        CUPCAKE("Cupcake"),
        ELECTRA("Electra"),
        GIGGLES("Giggles"),
        GINGER("Ginger"),
        GOLDILOCKS("Goldilocks"),
        GUMDROP("Gumdrop"),
        HONEYBEE("Honeybee"),
        JASMINE("Jasmine"),
        JULIETTE("Juliette"),
        JUNIPER("Juniper"),
        LADYBUG("Ladybug"),
        LAKSHMI("Lakshmi"),
        MISTY("Misty"),
        NUTMEG("Nutmeg"),
        OLYMPIA("Olympia"),
        PRINCESS("Princess"),
        RUBY("Ruby"),
        TIARA("Tiara"),
        TINKERBELL("Tinkerbell"),
        TRIXIE("Trixie"),
        TWINKLE("Twinkle"),
        VENUS("Venus"),
        AMIGO("Amigo"),
        BANJO("Banjo"),
        COSMO("Cosmo"),
        CROCKET("Crocket"),
        DONATELLO("Donatello"),
        ELECTRO("Electro"),
        ELVIS("Elvis"),
        EURIPIDES("Euripides"),
        FIGARO("Figaro"),
        FONZIE("Fonzie"),
        GALILEO("Galileo"),
        GERONIMO("Geronimo"),
        HENDRIX("Hendrix"),
        HERCULES("Hercules"),
        HOBBES("Hobbes"),
        HOUDINI("Houdini"),
        LANCELOT("Lancelot"),
        MARS("Mars"),
        MOSES("Moses"),
        OZZY("Ozzy"),
        PHARAOH("Pharaoh"),
        PICASSO("Picasso"),
        PRINCE("Prince"),
        REMBRANDT("Rembrandt"),
        ROMEO("Romeo"),
        RUMI("Rumi"),
        SIMBA("Simba"),
        TARZAN("Tarzan"),
        WIZARD("Wizard"),
        ZORRO("Zorro");


        private final String text;

        Tasks(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
