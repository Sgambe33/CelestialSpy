package com.example.javafxtest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

public class Utils {
    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public String httpGetCall(String url) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("GET");
        int status = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer tleContent = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            tleContent.append(inputLine + System.lineSeparator());
        }
        if (status != 200) {
            throw new IOException("HTTP GET call failed with status code: " + status);
        }
        return tleContent.toString();
    }

    public boolean updateTLEs() throws IOException {
        String TLEpath = System.getProperty("user.home") + File.separatorChar + "Documents" + File.separatorChar + "CelestialSpy" + File.separatorChar + "TLEs";
        File TLEdir = new File(TLEpath);
        if (!TLEdir.exists()) {
            System.out.println("Creating TLE directory");
            TLEdir.mkdirs();
        }
        File latestUpdate = new File(TLEpath + File.separatorChar + "latestUpdate.txt");
        if (!latestUpdate.exists()) {
            System.out.println("Creating latestUpdate file");
            latestUpdate.createNewFile();
            FileWriter fileWriter = new FileWriter(latestUpdate);
            fileWriter.write("0");
            fileWriter.close();
        }
        InputStream inputStream = new FileInputStream(latestUpdate);
        String latestUpdateString = readFromInputStream(inputStream);
        Date latestUpdateDate = new Date(Long.parseLong(latestUpdateString.replace("\n", "")));
        if ((new Date().getTime() - latestUpdateDate.getTime()) > 604800000) {
            System.out.println("TLEs are older than 7 days, updating...");
            for (String key : Globals.SATELLITES_URL.keySet()) {
                String data = httpGetCall(Globals.SATELLITES_URL.get(key));
                //Dump data to file
                File file = new File(TLEpath + File.separatorChar + key + ".txt");
                if (!file.exists()) {
                    System.out.println("Creating TLE file for " + key);
                    file.createNewFile();
                }
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(data);
                fileWriter.close();
            }
            FileWriter fileWriter = new FileWriter(latestUpdate);
            fileWriter.write(String.valueOf(new Date().getTime()));
            fileWriter.close();
            return true;
        }
        System.out.println("TLEs are up to date");
        return false;
    }

    public Satellite[] readTLEs(String category){
        File TLEpath = new File(System.getProperty("user.home") + File.separatorChar + "Documents" + File.separatorChar + "CelestialSpy" + File.separatorChar + "TLEs" + File.separatorChar + category + ".txt");
        if (!TLEpath.exists()) {
            System.out.println("TLE file not found");
            return null;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(TLEpath));
            String line;
            int i = 0;
            //Could be optimized by counting the number of lines in the file and then creating the array or even better an ArrayList
            Satellite[] satellites = new Satellite[1000];
            while ((line = br.readLine()) != null) {
                String name = line;
                String line1 = br.readLine();
                String line2 = br.readLine();
                satellites[i] = new Satellite(name.trim(), line1, line2);
                i++;
            }
            return satellites;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public HashMap<String, Satellite> readTLEsH(String category){
        File TLEpath = new File(System.getProperty("user.home") + File.separatorChar + "Documents" + File.separatorChar + "CelestialSpy" + File.separatorChar + "TLEs" + File.separatorChar + category + ".txt");
        if (!TLEpath.exists()) {
            System.out.println("TLE file not found");
            return null;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(TLEpath));
            String line;
            HashMap<String, Satellite> satellites = new HashMap<String, Satellite>();
            while ((line = br.readLine()) != null) {
                String name = line;
                String line1 = br.readLine();
                String line2 = br.readLine();
                satellites.put(name.trim(), new Satellite(name.trim(), line1, line2));
            }
            return satellites;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
