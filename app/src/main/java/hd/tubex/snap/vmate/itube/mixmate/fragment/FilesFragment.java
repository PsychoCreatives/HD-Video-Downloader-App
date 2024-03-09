package hd.tubex.snap.vmate.itube.mixmate.fragment;

import static androidx.databinding.DataBindingUtil.inflate;
import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import static hd.tubex.snap.vmate.itube.mixmate.util.Utils.createFileFolder;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hd.tubex.snap.vmate.itube.mixmate.R;
import hd.tubex.snap.vmate.itube.mixmate.databinding.FragmentFilesBinding;
import hd.tubex.snap.vmate.itube.mixmate.util.AdsUtils;
import hd.tubex.snap.vmate.itube.mixmate.util.AppLangSessionManager;
import hd.tubex.snap.vmate.itube.mixmate.util.Utils;

public class FilesFragment extends Fragment implements IUnityAdsInitializationListener {

    private FragmentFilesBinding binding;

    //unity ids
    String unityGameID = "4939893";
    Boolean testMode = false;
    String topAdUnitId = "Banner_Android";

    AppLangSessionManager appLangSessionManager;


    public FilesFragment() {
        // Required empty public constructor
    }

    // Listener for banner events:
    private BannerView.IListener bannerListener = new BannerView.IListener() {
        @Override
        public void onBannerLoaded(BannerView bannerAdView) {
            // Called when the banner is loaded.
            Log.v("UnityAdsExample", "onBannerLoaded: " + bannerAdView.getPlacementId());
        }

        @Override
        public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
            Log.e("UnityAdsExample", "Unity Ads failed to load banner for " + bannerAdView.getPlacementId() + " with error: [" + errorInfo.errorCode + "] " + errorInfo.errorMessage);
            // Note that the BannerErrorInfo object can indicate a no fill (see API documentation).
        }

        @Override
        public void onBannerClick(BannerView bannerAdView) {
            // Called when a banner is clicked.
            Log.v("UnityAdsExample", "onBannerClick: " + bannerAdView.getPlacementId());
        }

        @Override
        public void onBannerLeftApplication(BannerView bannerAdView) {
            // Called when the banner links out of the application.
            Log.v("UnityAdsExample", "onBannerLeftApplication: " + bannerAdView.getPlacementId());
        }
    };

    // This banner view object will be placed at the top of the screen:
    BannerView topBanner;
    // View objects to display banners:
    //RelativeLayout topBannerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = inflate(inflater, R.layout.fragment_files, container, false);

        UnityAds.initialize(getContext(), unityGameID, testMode, this);

        appLangSessionManager = new AppLangSessionManager(getContext());
        setLocale(appLangSessionManager.getLanguage());
        AdsUtils.showGoogleBannerAd(getContext(),binding.adView);

        initViews();

        return binding.getRoot();
    }

    public void initViews() {
        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);
    /*    binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/

        for (int i = 0; i < binding.tabs.getTabCount(); i++) {
            TextView tv=(TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab,null);
            binding.tabs.getTabAt(i).setCustomView(tv);
        }

        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        createFileFolder();
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new AllinOneGalleryFragment(Utils.ROOTDIRECTORYJOSHSHOW), "Josh");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.ROOTDIRECTORYCHINGARISHOW), "Chingari");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.ROOTDIRECTORYMITRONSHOW), "Mitron");
        adapter.addFragment(new SnackVideoDownloadedFragment(), "Snack Video");
        adapter.addFragment(new SharechatDownloadedFragment(), "Sharechat");
        adapter.addFragment(new RoposoDownloadedFragment(), "Roposo");
        adapter.addFragment(new InstaDownloadedFragment(), "Instagram");
        adapter.addFragment(new WhatsAppDowndlededFragment(), "Whatsapp");
        adapter.addFragment(new TikTokDownloadedFragment(), "TikTok");
        adapter.addFragment(new FBDownloadedFragment(), "Facebook");
        adapter.addFragment(new TwitterDownloadedFragment(), "Twitter");
        adapter.addFragment(new LikeeDownloadedFragment(), "Likee");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.ROOTDIRECTORYMXSHOW), "MXTakaTak");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.ROOTDIRECTORYMOJSHOW), "Moj");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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

    //unity ads
    @Override
    public void onInitializationComplete() {

        topBanner = new BannerView((Activity) getContext(), topAdUnitId, new UnityBannerSize(320, 50));
        // Set the listener for banner lifecycle events:
        topBanner.setListener(bannerListener);
        //topBannerView = findViewById(R.id.topBanner);
        binding.bannerAd.setVisibility(View.VISIBLE);
        LoadBannerAd(topBanner, binding.bannerAd);

    }

    public void LoadBannerAd(BannerView bannerView, RelativeLayout bannerLayout) {
        // Request a banner ad:
        bannerView.load();
        // Associate the banner view object with the banner view:
        bannerLayout.addView(bannerView);
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String s) {

    }

}