package hd.tubex.snap.vmate.itube.mixmate.fragment;

import static android.content.Context.CLIPBOARD_SERVICE;
import static androidx.databinding.DataBindingUtil.inflate;
import static hd.tubex.snap.vmate.itube.mixmate.util.Utils.createFileFolder;
import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import hd.tubex.snap.vmate.itube.mixmate.R;
import hd.tubex.snap.vmate.itube.mixmate.activity.ActHome;
import hd.tubex.snap.vmate.itube.mixmate.activity.ChingariActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.FacebookActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.GalleryActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.InstagramActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.JoshActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.LikeeActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.MXTakaTakActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.MitronActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.MojActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.RoposoActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.ShareChatActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.SnackVideoActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.TikTokActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.TwitterActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.WhatsappActivity;
import hd.tubex.snap.vmate.itube.mixmate.databinding.FragmentHomeBinding;
import hd.tubex.snap.vmate.itube.mixmate.util.AdsUtils;
import hd.tubex.snap.vmate.itube.mixmate.util.AppLangSessionManager;
import hd.tubex.snap.vmate.itube.mixmate.util.ClipboardListener;

public class HomeFragment extends Fragment implements View.OnClickListener, IUnityAdsInitializationListener {

    private FragmentHomeBinding binding;

    private String unityGameID = "4939893";
    private Boolean testMode = false;
    private String adUnitId = "Interstitial_Android";

    HomeFragment activity;
    private ClipboardManager clipBoard;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String CopyKey = "";
    String CopyValue = "";

    AppLangSessionManager appLangSessionManager;


    public HomeFragment() {
        // Required empty public constructor
    }

    private IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            UnityAds.show((Activity)getContext(), adUnitId, new UnityAdsShowOptions(), showListener);
        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
        }
    };

    private IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: " + placementId);
        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: " + placementId);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = inflate(inflater, R.layout.fragment_home, container, false);
        activity = this;

        UnityAds.initialize(getContext(), unityGameID, testMode, this);

        appLangSessionManager = new AppLangSessionManager(getContext());
        AdsUtils.showGoogleBannerAd(getContext(),binding.adView);

        initViews();

        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        activity = this;
        clipBoard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
    }

    public void initViews() {
        clipBoard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        if (getActivity().getIntent().getExtras() != null) {
            for (String key : getActivity().getIntent().getExtras().keySet()) {
                CopyKey = key;
                String value = getActivity().getIntent().getExtras().getString(CopyKey);
                if (CopyKey.equals("android.intent.extra.TEXT")) {
                    CopyValue = getActivity().getIntent().getExtras().getString(CopyKey);
                    CopyValue = extractLinks(CopyValue);
                    callText(value);
                } else {
                    CopyValue = "";
                    callText(value);
                }
            }
        }
        if (clipBoard != null) {
            clipBoard.addPrimaryClipChangedListener(new ClipboardListener() {
                @Override
                public void onPrimaryClipChanged() {
                    try {
                        showNotification(Objects.requireNonNull(clipBoard.getPrimaryClip().getItemAt(0).getText()).toString());
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(0);
        }

        binding.rvLikee.setOnClickListener(this);
        binding.rvInsta.setOnClickListener(this);
        binding.rvWhatsApp.setOnClickListener(this);
        binding.rvTikTok.setOnClickListener(this);
        binding.rvFB.setOnClickListener(this);
        binding.rvTwitter.setOnClickListener(this);
        binding.rvSnack.setOnClickListener(this);
        binding.rvShareChat.setOnClickListener(this);
        binding.rvRoposo.setOnClickListener(this);
        binding.rvJosh.setOnClickListener(this);
        binding.rvChingari.setOnClickListener(this);
        binding.rvMitron.setOnClickListener(this);
        binding.rvMoj.setOnClickListener(this);
        binding.rvMX.setOnClickListener(this);

        createFileFolder();

    }

    private void callText(String CopiedText) {
        try {
            if (CopiedText.contains("likee")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(100);
                } else {
                    callLikeeActivity();
                }
            } else if (CopiedText.contains("instagram.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
            } else if (CopiedText.contains("facebook.com") || CopiedText.contains("fb")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(104);
                } else {
                    callFacebookActivity();
                }
            } else if (CopiedText.contains("tiktok.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
            } else if (CopiedText.contains("twitter.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
            } else if (CopiedText.contains("sharechat")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(107);
                } else {
                    callShareChatActivity();
                }
            } else if (CopiedText.contains("roposo")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(108);
                } else {
                    callRoposoActivity();
                }
            } else if (CopiedText.contains("snackvideo") || CopiedText.contains("sck.io")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(109);
                } else {
                    callSnackVideoActivity();
                }
            } else if (CopiedText.contains("josh")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(110);
                } else {
                    callJoshActivity();
                }
            } else if (CopiedText.contains("chingari")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(111);
                } else {
                    callChingariActivity();
                }
            } else if (CopiedText.contains("mitron")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(112);
                } else {
                    callMitronActivity();
                }
            } else if (CopiedText.contains("mxtakatak")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(113);
                } else {
                    callMXActivity();
                }
            }else if (CopiedText.contains("moj")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(114);
                } else {
                    callMojActivity();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.rvLikee) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(100);
            } else {
                callLikeeActivity();
            }
        } else if (id == R.id.rvInsta) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(101);
            } else {
                callInstaActivity();
            }
        } else if (id == R.id.rvWhatsApp) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(102);
            } else {
                callWhatsappActivity();
            }
        } else if (id == R.id.rvTikTok) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(103);
            } else {
                callTikTokActivity();
            }
        } else if (id == R.id.rvFB) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(104);
            } else {
                callFacebookActivity();
            }
        } else if (id == R.id.rvTwitter) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(106);
            } else {
                callTwitterActivity();
            }
        } else if (id == R.id.rvShareChat) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(107);
            } else {
                callShareChatActivity();
            }
        } else if (id == R.id.rvRoposo) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(108);
            } else {
                callRoposoActivity();
            }
        } else if (id == R.id.rvSnack) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(109);
            } else {
                callSnackVideoActivity();
            }
        } else if (id == R.id.rvJosh) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(110);
            } else {
                callJoshActivity();
            }
        } else if (id == R.id.rvChingari) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(111);
            } else {
                callChingariActivity();
            }
        } else if (id == R.id.rvMitron) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(112);
            } else {
                callMitronActivity();
            }
        } else if (id == R.id.rvMX) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(113);
            } else {
                callMXActivity();
            }
        } else if (id == R.id.rvMoj) {
            if (Build.VERSION.SDK_INT >= 23) {
                checkPermissions(114);
            } else {
                callMojActivity();
            }
                /*            case R.id.rvGames:
                callGamesActivity();
                break;*/
        }
    }


    public void callJoshActivity() {
                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), JoshActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callChingariActivity() {
                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), ChingariActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callMitronActivity() {
                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), MitronActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callMXActivity(){
                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), MXTakaTakActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callMojActivity() {
                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), MojActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callLikeeActivity() {

                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), LikeeActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callInstaActivity() {
                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), InstagramActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callWhatsappActivity() {
                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), WhatsappActivity.class);
        startActivity(i);
    }

    public void callTikTokActivity() {
                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), TikTokActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callFacebookActivity() {
                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), FacebookActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);

    }

    public void callTwitterActivity() {
                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), TwitterActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callGalleryActivity() {
                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), GalleryActivity.class);
        startActivity(i);
    }

    public void callRoposoActivity() {
                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), RoposoActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callShareChatActivity() {
                DisplayInterstitialAd();
        Intent i = new Intent(getContext(), ShareChatActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callSnackVideoActivity() {
        Intent i = new Intent(getContext(), SnackVideoActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }



    public void showNotification(String Text) {
        if (Text.contains("instagram.com") || Text.contains("facebook.com") || Text.contains("fb") || Text.contains("tiktok.com")
                || Text.contains("twitter.com") || Text.contains("likee")
                || Text.contains("sharechat") || Text.contains("roposo") || Text.contains("snackvideo") || Text.contains("sck.io")
                || Text.contains("chingari") || Text.contains("myjosh") || Text.contains("mitron")) {
            Intent intent = new Intent(getContext(), ActHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", Text);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(getContext(), getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(getResources().getColor(R.color.black))
                    .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(),
                            R.mipmap.ic_launcher))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle("Copied text")
                    .setContentText(Text)
                    .setChannelId(getResources().getString(R.string.app_name))
                    .setFullScreenIntent(pendingIntent, true);
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

    private boolean checkPermissions(int type) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) (getContext()),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), type);
            return false;
        } else {
            if (type == 100) {
                callLikeeActivity();
            } else if (type == 101) {
                callInstaActivity();
            } else if (type == 102) {
                callWhatsappActivity();
            } else if (type == 103) {
                callTikTokActivity();
            } else if (type == 104) {
                callFacebookActivity();
            } else if (type == 105) {
                callGalleryActivity();
            } else if (type == 106) {
                callTwitterActivity();
            } else if (type == 107) {
                callShareChatActivity();
            } else if (type == 108) {
                callRoposoActivity();
            } else if (type == 109) {
                callSnackVideoActivity();
            } else if (type == 110) {
                callJoshActivity();
            } else if (type == 111) {
                callChingariActivity();
            } else if (type == 112) {
                callMitronActivity();
            }else if (type == 113) {
                callMXActivity();
            }else if (type == 114) {
                callMojActivity();
            }

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callLikeeActivity();
            }
        } else if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callInstaActivity();
            }
        } else if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callWhatsappActivity();
            }
        } else if (requestCode == 103) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTikTokActivity();
            }
        } else if (requestCode == 104) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callFacebookActivity();
            }
        } else if (requestCode == 105) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callGalleryActivity();
            }
        } else if (requestCode == 106) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTwitterActivity();
            }
        } else if (requestCode == 107) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callShareChatActivity();
            }
        } else if (requestCode == 108) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callRoposoActivity();
            }
        } else if (requestCode == 109) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callSnackVideoActivity();
            }
        } else if (requestCode == 110) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callJoshActivity();
            }
        } else if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callChingariActivity();
            }
        } else if (requestCode == 112) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callMitronActivity();
            }
        }else if (requestCode == 113) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callMXActivity();
            }
        }else if (requestCode == 114) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callMojActivity();
            }
        }

    }

    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        Intent refresh = new Intent(getContext(), ActHome.class);
        startActivity(refresh);
    }

    public static String extractLinks(String text) {
        Matcher m = Patterns.WEB_URL.matcher(text);
        String url = "";
        while (m.find()) {
            url = m.group();
            Log.d("New URL", "URL extracted: " + url);

            break;
        }
        return url;
    }

    @Override
    public void onInitializationComplete() {
 //       Toast.makeText(getContext(),"Ad loaded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String s) {

    }

    // Implement a function to load an interstitial ad. The ad will start to show after the ad has been loaded.
    public void DisplayInterstitialAd () {
        UnityAds.load(adUnitId, loadListener);
    }
}