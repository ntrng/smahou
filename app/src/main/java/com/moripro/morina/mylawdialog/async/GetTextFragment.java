package com.moripro.morina.mylawdialog.async;

import android.util.Log;
import androidx.fragment.app.Fragment;
import com.moripro.morina.mylawdialog.App;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class GetTextFragment extends Fragment implements Callable<String> {

    private String key;

    public GetTextFragment(String key){
        this.key = key;
    }

    @Override
    public String call(){
        return getFileStream();
    }

    private String getFileStream(){
        Log.i("TAG_ATTACHED", key);
        if(key.equals("def")){
            return "タブに法令をセットしてください";
        }else {
            try {
                FileInputStream fis = App.getMcontext().openFileInput(key);
                InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                final StringBuilder builder = new StringBuilder();

                final BufferedReader reader = new BufferedReader((isr));
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line).append('\n');
                    line = reader.readLine();
                }

                return builder.toString();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
