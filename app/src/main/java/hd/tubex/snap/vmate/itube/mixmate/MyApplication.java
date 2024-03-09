package hd.tubex.snap.vmate.itube.mixmate;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import hd.tubex.snap.vmate.itube.mixmate.util.AppLangSessionManager;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public class MyApplication extends Application {
    AppLangSessionManager appLangSessionManager;
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        appLangSessionManager = new AppLangSessionManager(getApplicationContext());
        setLocale(appLangSessionManager.getLanguage());

/*        MobileAds.initialize(this, initializationStatus -> {
            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
            for (String adapterClass : statusMap.keySet()) {
                AdapterStatus status = statusMap.get(adapterClass);
                Log.d("MyApp", String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status.getDescription(), status.getLatency()));
            }

            // Start loading ads here...
        });*/
        MobileAds.initialize(this, initializationStatus -> {
        });

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("98296E4266501EF369EE3DC9379E3D21"))
                        .build());
    }

    public void setLocale(String lang) {
        if (lang.equals("")){
            lang="en";
        }
        Log.d("Support",lang+"");
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


    }




}
