package edu.sjsu.android.externalstorage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import android.util.Log;
import android.os.Environment;

public class FileOperations {
    private String path;

    public FileOperations(String path){
        this.path = path;
    }

    public boolean write(String fname, String fContent){
        try{
            File file = new File(path, fname);
            if(!file.exists()){
                boolean success = file.createNewFile();
                if(!success){
                    Log.d("Fail", "The named file already exists.");
                    return false;
                }
            }

            FileWriter fw = new FileWriter(file.getAbsolutePath());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fContent);
            bw.close();

            Log.d("Success","Success");
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public String read(String fname){
        BufferedReader br = null;
        String response = null;

        try{
            StringBuilder output = new StringBuilder();
            File file = new File(path, fname);

            br = new BufferedReader(new FileReader(file));
            String line = "";
            while((line = br.readLine()) != null){
                output.append(line).append("\n");
            }

            response  = output.toString();
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        return response;
    }
}
