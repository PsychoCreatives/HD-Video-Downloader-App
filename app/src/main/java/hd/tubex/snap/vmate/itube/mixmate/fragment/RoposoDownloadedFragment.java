package hd.tubex.snap.vmate.itube.mixmate.fragment;

/*
 * Copyright (c) 2020
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import hd.tubex.snap.vmate.itube.mixmate.R;
import hd.tubex.snap.vmate.itube.mixmate.activity.FullViewActivity;
import hd.tubex.snap.vmate.itube.mixmate.activity.ActHome;
import hd.tubex.snap.vmate.itube.mixmate.adapter.FileListAdapter;
import hd.tubex.snap.vmate.itube.mixmate.databinding.FragmentHistoryBinding;
import hd.tubex.snap.vmate.itube.mixmate.interfaces.FileListClickInterface;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

import static androidx.databinding.DataBindingUtil.inflate;
import static hd.tubex.snap.vmate.itube.mixmate.util.Utils.RootDirectoryRoposoShow;

public class RoposoDownloadedFragment extends Fragment implements FileListClickInterface {
    private FragmentHistoryBinding binding;
    private FileListAdapter fileListAdapter;
    private ArrayList<File> fileArrayList;
    private ActHome activity;
    public static RoposoDownloadedFragment newInstance(String param1) {
        RoposoDownloadedFragment fragment = new RoposoDownloadedFragment();
        Bundle args = new Bundle();
        args.putString("m", param1);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NotNull Context _context) {
        super.onAttach(_context);
        activity = (ActHome) _context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("m");
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        activity = (ActHome) getActivity();
        getAllFiles();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, R.layout.fragment_history, container, false);
        initViews();
        return binding.getRoot();
    }
    private void initViews(){
        binding.swiperefresh.setOnRefreshListener(() -> {
            getAllFiles();
            binding.swiperefresh.setRefreshing(false);
        });
    }

    private void getAllFiles(){
        fileArrayList = new ArrayList<>();
        File[] files = RootDirectoryRoposoShow.listFiles();
        if (files!=null) {
            for (File file : files) {
                fileArrayList.add(file);
            }
            fileListAdapter = new FileListAdapter(activity, fileArrayList, RoposoDownloadedFragment.this);
            binding.rvFileList.setAdapter(fileListAdapter);
        }
    }
    @Override
    public void getPosition(int position, File file) {
        Intent inNext = new Intent(activity, FullViewActivity.class);
        inNext.putExtra("ImageDataFile", fileArrayList);
        inNext.putExtra("Position", position);
        activity.startActivity(inNext);
    }
}