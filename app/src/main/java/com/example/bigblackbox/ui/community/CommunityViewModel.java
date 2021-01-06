package com.example.bigblackbox.ui.community;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommunityViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CommunityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("社区界面");
    }

    public LiveData<String> getText() {
        return mText;
    }
}