package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class CardsRepository {

    private QuestionsDao mQuestionsDao;
    private LiveData<List<Questions>> mAllQuestions;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public CardsRepository(Application application) {
        CardsRoomDatabase db = CardsRoomDatabase.getDatabase(application);
        mQuestionsDao = db.questionDao();
        mAllQuestions = mQuestionsDao.getAlphabetizedWords();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Questions>> getAllWords() {
        return mAllQuestions;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Questions question) {
        CardsRoomDatabase.databaseWriteExecutor.execute(() -> {
            mQuestionsDao.insert(question);
        });
    }
}
