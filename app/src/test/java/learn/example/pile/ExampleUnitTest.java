package learn.example.pile;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import learn.example.pile.factory.OpenEyeVideoFactory;
import learn.example.pile.object.OpenEyes;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {



    @Test
    public void addition_isCorrect() {

    }


    public static class bean{


        private FruitBean fruit;

        public FruitBean getFruit() {
            return fruit;
        }

        public void setFruit(FruitBean fruit) {
            this.fruit = fruit;
        }

        public static class FruitBean {
            private int count;
            private int time;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }
        }
    }


    public String scriptTag(String js)
    {
        return "<script src='"+js+"'>"+"</script>";
    }



}