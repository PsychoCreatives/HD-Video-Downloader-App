package hd.tubex.snap.vmate.itube.mixmate.util;

import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdRequest;

public class AdsUtils {

    public static void showGoogleBannerAd(Context context, com.google.android.gms.ads.AdView googleAdView) {

        googleAdView.setVisibility(View.VISIBLE);
        //Load Banner Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        googleAdView.loadAd(adRequest);
    }



}
