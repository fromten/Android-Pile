package learn.example.pile;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import learn.example.pile.factory.OpenEyeVideoFactory;
import learn.example.pile.object.OpenEyes;
import learn.example.pile.util.TextUtil;
import learn.example.pile.util.TimeUtil;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {



    @Test
    public void addition_isCorrect() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.roll(Calendar.DAY_OF_YEAR,1);
        System.out.println(cal.getTime().getTime());
    }





    public String scriptTag(String js)
    {
        return "<script src='"+js+"'>"+"</script>";
    }



}