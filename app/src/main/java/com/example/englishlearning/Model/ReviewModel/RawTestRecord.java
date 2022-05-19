package com.example.englishlearning.Model.ReviewModel;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.PracticeModel.GeneralPractice;
import com.example.englishlearning.MyApplication;
import com.example.englishlearning.OnGetDataListener;
import com.example.englishlearning.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

public class RawTestRecord extends GeneralPractice {

    private double point;
    private String idListenings;
    private String listeningAnswer;
    private String idFillingBlank;
    private String fillingBlankAnswer;
    private String idReading;
    private String readingAnswer;
    private String singleQuestion;
    private String writing;

    public RawTestRecord(long id, String dateTime, double point, String idListenings,
                         String listeningAnswer, String idFillingBlank, String fillingBlankAnswer,
                         String idReading, String readingAnswer, String singleQuestion, String writing) {
        this.id = id;
        this.dateTime = dateTime;
        this.point = point;
        this.idListenings = idListenings;
        this.listeningAnswer = listeningAnswer;
        this.idFillingBlank = idFillingBlank;
        this.fillingBlankAnswer = fillingBlankAnswer;
        this.idReading = idReading;
        this.readingAnswer = readingAnswer;
        this.singleQuestion = singleQuestion;
        this.writing = writing;
    }

    public TestRecord parseToTestRecord(){
        TestRecord testRecord = new TestRecord();
        testRecord.setId(id);
        testRecord.setDateTime(dateTime);
        testRecord.setPoint(point);

        //Listening
        String tmpIdListening = idListenings.replace("[", "");
        String[] tmpIdListenings = tmpIdListening.split("]");
        String[] tmpListeningAnswer = listeningAnswer.split("/");
        ListeningReview listening1 = new ListeningReview( Integer.parseInt(tmpIdListenings[0]), tmpListeningAnswer[0] );
        ListeningReview listening2 = new ListeningReview( Integer.parseInt(tmpIdListenings[1]), tmpListeningAnswer[1] );
        testRecord.getListeningReviews().add(listening1); testRecord.getListeningReviews().add(listening2);

        //Filling Blank
        FillingBlankReview fillingBlankReview = new FillingBlankReview(Integer.parseInt(idFillingBlank), fillingBlankAnswer);
        testRecord.setFillingBlankReview(fillingBlankReview);

        //Reading
        ReadingReview readingReview = new ReadingReview(Integer.parseInt(idReading), readingAnswer);
        testRecord.setReadingReview(readingReview);

        //Single Question
        String tmpSingleQuestion = singleQuestion.replace("[", "");
        String[] listSingleQuestion = tmpSingleQuestion.split("]");
        for(String item: listSingleQuestion){
            String[] tmp = item.split(",");
            testRecord.getSingleQuestionReviews().add( new SingleQuestionReview(Integer.parseInt( tmp[0] ), Integer.parseInt(tmp[1])) );
        }

        //Writing
        String tmpWriting = writing.replace("[", "");
        String[] listWriting = tmpWriting.split("]");
        for(String item: listWriting){
            String[] tmp = item.split(",");
            int id = Integer.parseInt(tmp[0]);
            String answer = "";
            if(tmp.length == 2)
                answer = tmp[1];
            testRecord.getWritingReviews().add( new WritingReview(id, answer) );
        }

        return testRecord;
    }

    public static RawTestRecord getRawTestRecordById(int id){
        UserDataHelper helper = new UserDataHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("test_record", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while(cursor.moveToNext()){
            return new RawTestRecord(cursor.getInt(0), cursor.getString(1),
                    cursor.getDouble(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7),
                    cursor.getString(8),cursor.getString(9),
                    cursor.getString(10));
        }
        cursor.close();
        return null;
    }

    @Override
    public String getResult() {
        return String.valueOf(point) + "/10";
    }
}
