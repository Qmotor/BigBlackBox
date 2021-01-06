package com.example.bigblackbox.ui.mine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MineViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MineViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("我的界面");
    }

    public LiveData<String> getText() {
        return mText;
    }
}