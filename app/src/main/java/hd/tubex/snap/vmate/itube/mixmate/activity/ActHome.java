package hd.tubex.snap.vmate.itube.mixmate.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.ViewGroup;
import android.view.Window;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

import hd.tubex.snap.vmate.itube.mixmate.R;
import hd.tubex.snap.vmate.itube.mixmate.databinding.ActivityActHomeBinding;
import hd.tubex.snap.vmate.itube.mixmate.fragment.FilesFragment;
import hd.tubex.snap.vmate.itube.mixmate.fragment.GamesFragment;
import hd.tubex.snap.vmate.itube.mixmate.fragment.HomeFragment;
import hd.tubex.snap.vmate.itube.mixmate.util.ClipboardListener;

public class ActHome extends AppCompatActivity {


    ActivityActHomeBinding binding;
    NativeAd adobj;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    String CopyKey = "";
    String CopyValue = "";
    private ClipboardManager clipBoard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_act_home);

        nativeAd();

        binding.bottomnavigationbar.setBackground(null);
        binding.bottomnavigationbar.getMenu();
        getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer,new HomeFragment()).commit();
        binding.bottomnavigationbar.setOnItemSelectedListener(item -> {
            Fragment temp = null;
            switch (item.getItemId())
            {
                case R.id.mHome : temp = new HomeFragment();
                    break;
                case R.id.mSearch : temp = new FilesFragment();
                    break;
                case R.id.mPerson : temp = new GamesFragment();

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer,temp).commit();
            return true;
        });
    }

    private void nativeAd() {

        AdLoader.Builder builder = new AdLoader.Builder(ActHome.this, getResources().getString(R.string.admob_native_ad));
        builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        });
        builder.forNativeAd(NativeAd -> adobj = NativeAd);
        AdLoader adLoader = builder.build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onBackPressed() {

        ExitDialog exitDialog = new ExitDialog(ActHome.this, this.adobj);
        exitDialog.show();
        Window window = exitDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void initViews() {
        clipBoard = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
        if (this.getIntent().getExtras() != null) {
            for (String key : this.getIntent().getExtras().keySet()) {
                CopyKey = key;
                String value = this.getIntent().getExtras().getString(CopyKey);
                if (CopyKey.equals("android.intent.extra.TEXT")) {
                    CopyValue = this.getIntent().getExtras().getString(CopyKey);
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

    }

    @Override
    public void onResume() {
        super.onResume();
        clipBoard = (ClipboardManager) ActHome.this.getSystemService(CLIPBOARD_SERVICE);
    }

    public void callJoshActivity() {
        Intent i = new Intent(ActHome.this, JoshActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callChingariActivity() {
        Intent i = new Intent(ActHome.this, ChingariActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callMitronActivity() {
        Intent i = new Intent(ActHome.this, MitronActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callMXActivity(){
        Intent i = new Intent(ActHome.this, MXTakaTakActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callMojActivity() {
        Intent i = new Intent(ActHome.this, MojActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callLikeeActivity() {
        Intent i = new Intent(ActHome.this, LikeeActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callInstaActivity() {
        Intent i = new Intent(ActHome.this, InstagramActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callWhatsappActivity() {
        Intent i = new Intent(ActHome.this, WhatsappActivity.class);
        startActivity(i);
    }

    public void callTikTokActivity() {
        Intent i = new Intent(ActHome.this, TikTokActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callFacebookActivity() {
        Intent i = new Intent(ActHome.this, FacebookActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);

    }

    public void callTwitterActivity() {
        Intent i = new Intent(ActHome.this, TwitterActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callGalleryActivity() {
        Intent i = new Intent(ActHome.this, GalleryActivity.class);
        startActivity(i);
    }

    public void callRoposoActivity() {
        Intent i = new Intent(ActHome.this, RoposoActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callShareChatActivity() {
        Intent i = new Intent(ActHome.this, ShareChatActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
    }

    public void callSnackVideoActivity() {
        Intent i = new Intent(ActHome.this, SnackVideoActivity.class);
        i.putExtra("CopyIntent", CopyValue);
        startActivity(i);
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


    private boolean checkPermissions(int type) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) (this),
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

    public void showNotification(String Text) {
        if (Text.contains("instagram.com") || Text.contains("facebook.com") || Text.contains("fb") || Text.contains("tiktok.com")
                || Text.contains("twitter.com") || Text.contains("likee")
                || Text.contains("sharechat") || Text.contains("roposo") || Text.contains("snackvideo") || Text.contains("sck.io")
                || Text.contains("chingari") || Text.contains("myjosh") || Text.contains("mitron")) {
            Intent intent = new Intent(this, ActHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", Text);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(getResources().getColor(R.color.black))
                    .setLargeIcon(BitmapFactory.decodeResource(ActHome.this.getResources(),
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

}