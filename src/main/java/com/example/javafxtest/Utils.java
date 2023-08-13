package com.example.javafxtest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

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
        String TLEpath = System.getProperty("user.home") + File.separatorChar + "My Documents" + File.separatorChar + "CelestialSpy" + File.separatorChar + "TLEs";
        File TLEdir = new File(TLEpath);
        if (!TLEdir.exists()) {
            TLEdir.mkdirs();
        }
        File latestUpdate = new File(TLEpath + File.separatorChar + "latestUpdate.txt");
        if (!latestUpdate.exists()) {
            latestUpdate.createNewFile();
            FileWriter fileWriter = new FileWriter(latestUpdate);
            fileWriter.write("0");
            fileWriter.close();
        }
        InputStream inputStream = new FileInputStream(latestUpdate);
        String latestUpdateString = readFromInputStream(inputStream);
        Date latestUpdateDate = new Date(Long.parseLong(latestUpdateString.replace("\n", "")));
        if ((latestUpdateDate.getTime() - new Date().getTime()) > 604800000) {
            System.out.println("TLEs are older than 7 days, updating...");
            for (String key : Globals.SATELLITES_URL.keySet()) {
                String data = httpGetCall(Globals.SATELLITES_URL.get(key));
                //Dump data to file
                File file = new File(TLEpath + File.separatorChar + key + ".txt");
                if (!file.exists()) {
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
        return false;
    }
}
