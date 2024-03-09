package hd.tubex.snap.vmate.itube.mixmate.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.nativead.NativeAd;

import hd.tubex.snap.vmate.itube.mixmate.R;

public class ExitDialog extends Dialog {

    NativeAd ad;
    Activity activity;

    public ExitDialog(Activity activity, NativeAd ad) {
        super(activity);
        this.activity = activity;
        this.ad = ad;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.exit_dialog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView yes = findViewById(R.id.btn_yes);
        Button no = findViewById(R.id.btn_no);
        CardView sub = findViewById(R.id.rate);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appName = activity.getPackageName();
                try {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
                }
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                System.exit(0);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        TemplateView ad = findViewById(R.id.ad_template);
        if (this.ad == null) {
            ad.setVisibility(View.GONE);
        } else {
            ad.setVisibility(View.VISIBLE);
            ad.setNativeAd(this.ad);

        }
    }
}
