package hd.tubex.snap.vmate.itube.mixmate.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import hd.tubex.snap.vmate.itube.mixmate.R;
import hd.tubex.snap.vmate.itube.mixmate.api.CommonClassForAPI;
import hd.tubex.snap.vmate.itube.mixmate.databinding.LayoutGlobalUiBinding;
import hd.tubex.snap.vmate.itube.mixmate.model.TiktokModel;
import hd.tubex.snap.vmate.itube.mixmate.util.AppLangSessionManager;
import hd.tubex.snap.vmate.itube.mixmate.util.Utils;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Arrays;
import java.util.Locale;

import io.reactivex.observers.DisposableObserver;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static hd.tubex.snap.vmate.itube.mixmate.util.Utils.RootDirectoryTikTok;
import static hd.tubex.snap.vmate.itube.mixmate.util.Utils.createFileFolder;
import static hd.tubex.snap.vmate.itube.mixmate.util.Utils.startDownload;

public class TikTokActivity extends AppCompatActivity {
    private LayoutGlobalUiBinding binding;
    TikTokActivity activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;
    AppLangSessionManager appLangSessionManager;

    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    private static final String TAG = "GetStarted";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_global_ui);
        activity = this;
        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
        initViews();

        binding.imAppIcon.setImageDrawable(getResources().getDrawable(R.drawable.tiktok_logo));
        binding.tvAppName.setText(getResources().getString(R.string.tiktok_app_name));

        interstitital();
        LoadNativeAd();


    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.loginBtn1.setOnClickListener(v -> {
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                GetTikTokData();
            }
        });

        binding.LLOpenApp.setOnClickListener(v -> {
            Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically.go");
            Intent launchIntent1 = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically");
            if (launchIntent != null) {
                activity.startActivity(launchIntent);
            } else if (launchIntent1 != null) {
                activity.startActivity(launchIntent1);
            } else {
                Utils.setToast(activity, getResources().getString(R.string.app_not_available));
            }

        });
    }

    private void GetTikTokData() {
        try {
            createFileFolder();
            String host = binding.etText.getText().toString();
            if (host.contains("tiktok")) {
                Utils.showProgressDialog(activity);
                callVideoDownload(binding.etText.getText().toString());
                showInterstitialAds();

            } else {
                Utils.setToast(activity, "Enter Valid Url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void callVideoDownload(String Url) {
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    commonClassForAPI.callTiktokVideo(tiktokObserver, Url);
                }
            } else {
                Utils.setToast(activity, "No Internet Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private DisposableObserver<TiktokModel> tiktokObserver = new DisposableObserver<TiktokModel>() {
        @Override
        public void onNext(TiktokModel tiktokModel) {
            Utils.hideProgressDialog(activity);
            try {
                if (tiktokModel.getResponsecode().equals("200")) {
                    startDownload(tiktokModel.getData().getMainvideo(),
                            RootDirectoryTikTok, activity, "tiktok_"+System.currentTimeMillis()+".mp4");
                    binding.etText.setText("");
                    showInterstitialAds();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog(activity);
            e.printStackTrace();
        }
        @Override
        public void onComplete() {
            Utils.hideProgressDialog(activity);
        }
    };

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("tiktok")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("tiktok")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("tiktok")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


    }

        private void interstitital() {

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("98296E4266501EF369EE3DC9379E3D21"))
                        .build());

        adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getApplicationContext(),getString(R.string.admob_interstitial_ad), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

    }

    private void showInterstitialAds() {

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(TikTokActivity.this);

                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            interstitital();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            mInterstitialAd = null;
                            Log.d("TAG", "The ad failed to show.");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null;
                            Log.d("TAG", "The ad was shown.");
                        }
                    });
                } else {
                    //callbackInterstitial();
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");

                }

            }

    public void LoadNativeAd() {

            AdLoader adLoader = new AdLoader.Builder(TikTokActivity.this, getString(R.string.admob_native_ad))
                    .forNativeAd(nativeAd -> {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().build();
                        binding.myTemplate.setStyles(styles);
                        binding.myTemplate.setNativeAd(nativeAd);
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }
}