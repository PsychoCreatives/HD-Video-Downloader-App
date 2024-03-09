package hd.tubex.snap.vmate.itube.mixmate.fragment;

import static androidx.databinding.DataBindingUtil.inflate;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import hd.tubex.snap.vmate.itube.mixmate.R;
import hd.tubex.snap.vmate.itube.mixmate.activity.GamesPlayActivity;
import hd.tubex.snap.vmate.itube.mixmate.databinding.FragmentGamesBinding;

public class GamesFragment extends Fragment {

    private FragmentGamesBinding binding;

    public GamesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = inflate(inflater, R.layout.fragment_games, container, false);


        nativeAd();

        initViews();

        return binding.getRoot();

    }



    private void initViews() {

        binding.RL2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), GamesPlayActivity.class);
                i.putExtra("url","2048");
                startActivity(i);
            }
        });

    }

    private void nativeAd() {
        AdLoader adLoader = new AdLoader.Builder(getContext(), getString(R.string.admob_native_ad))
                .forNativeAd(nativeAd -> {
                    binding.adTemplate.setVisibility(View.VISIBLE);
                    NativeTemplateStyle styles = new NativeTemplateStyle.Builder().build();
                    binding.adTemplate.setStyles(styles);
                    binding.adTemplate.setNativeAd(nativeAd);
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }



}