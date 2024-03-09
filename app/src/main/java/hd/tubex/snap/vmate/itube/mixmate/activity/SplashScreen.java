package hd.tubex.snap.vmate.itube.mixmate.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import hd.tubex.snap.vmate.itube.mixmate.R;
import hd.tubex.snap.vmate.itube.mixmate.util.AppLangSessionManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import java.util.Locale;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class SplashScreen extends AppCompatActivity {

    SplashScreen activity;
    Context context;
    AppUpdateManager appUpdateManager;
    AppLangSessionManager appLangSessionManager;

    LottieAnimationView appLoader;
    ImageView logo;
    TextView name, subname, companyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = activity = this;
        appUpdateManager = AppUpdateManagerFactory.create(context);
        appLangSessionManager = new AppLangSessionManager(activity);
        UpdateApp();

        appLoader = findViewById(R.id.appLoader);
        logo= findViewById(R.id.logo);
        name= findViewById(R.id.name);
        subname= findViewById(R.id.subname);
        companyName= findViewById(R.id.companyName);


        animate();
        setLocale(appLangSessionManager.getLanguage());
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        appLangSessionManager = new AppLangSessionManager(activity);

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, IMMEDIATE, activity, 101);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void HomeScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, ActHome.class);
                startActivity(i);
            }
        }, 5500);

    }

    public void UpdateApp() {
        try {
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, IMMEDIATE, activity, 101);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else {
                    HomeScreen();
                }
            }).addOnFailureListener(e -> {
                e.printStackTrace();
                HomeScreen();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode != RESULT_OK) {
                HomeScreen();
            } else {
                HomeScreen();
            }
        }
    }


    public void setLocale(String lang) {
        if (lang.equals("")){
            lang="en";
        }
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    private void animate() {

        logo.setAlpha(0f);
        logo.animate()
                .translationY(1f)
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(1000)
                .translationY(1f)
                .alpha(1f)
                .setDuration(1200)
                .setStartDelay(1500);

        name.setAlpha(0f);
        name.animate()
                .translationY(1f)
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(1000)
                .translationY(1f)
                .alpha(1f)
                .setDuration(1200)
                .setStartDelay(1500);

        subname.setAlpha(0f);
        subname.animate()
                .translationY(1f)
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(1000)
                .translationY(1f)
                .alpha(1f)
                .setDuration(1200)
                .setStartDelay(1500);

        companyName.setAlpha(0f);
        companyName.animate()
                .translationY(1f)
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(1000)
                .translationY(1f)
                .alpha(1f)
                .setDuration(1200)
                .setStartDelay(1500);

        appLoader.setAlpha(0f);
        appLoader.animate()
                .translationY(1f)
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(1000)
                .translationY(1f)
                .alpha(1f)
                .setDuration(1200)
                .setStartDelay(1500);
    }

}
