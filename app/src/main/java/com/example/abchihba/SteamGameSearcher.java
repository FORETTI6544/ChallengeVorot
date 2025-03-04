package com.example.abchihba;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.core.util.Pair;

import com.google.gson.*;
import java.io.*;
import java.util.*;
import okhttp3.*;

public class SteamGameSearcher {
    private static SteamGameSearcher instance;

    private final String apiKey = "F94805476D3C1ADDF2BFDDF9F5F77AD4";
    private final List<Pair<Integer, String>> appList = new ArrayList<>();
    private int lastAppId = 0;
    private final Context context;
    private final OkHttpClient client;

    private SteamGameSearcher(Context context) {
        this.context = context;
        this.client = new OkHttpClient();
        loadDataFromCsv();
        loadLastAppId();
    }

    public static synchronized SteamGameSearcher getInstance(Context context) {
        if (instance == null) {
            instance = new SteamGameSearcher(context);
        }
        return instance;
    }

    public boolean updateData() {
        String url = String.format(
                "https://api.steampowered.com/IStoreService/GetAppList/v1/?key=%s&include_games=true&max_result=50000&last_appid=%d",
                apiKey, lastAppId);
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                JsonArray apps = json.getAsJsonObject("response").getAsJsonArray("apps");

                if (apps != null && apps.size() > 0) {
                    for (JsonElement app : apps) {
                        JsonObject appObject = app.getAsJsonObject();
                        int appId = appObject.get("appid").getAsInt();
                        String name = appObject.get("name").getAsString();
                        appList.add(new Pair<>(appId, name));
                    }
                    lastAppId = appList.get(appList.size() - 1).first;
                    saveDataToCsv();
                    saveLastAppId();
                    return true;
                }
            } else {
                System.err.println("Failed to retrieve data: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveDataToCsv() {
        File internalStorageDir = context.getFilesDir();
        File steamGamesFile = new File(internalStorageDir, "steam_games.csv");

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(steamGamesFile, false)))) {
            writer.write("appid,name\n");
            for (Pair<Integer, String> app : appList) {
                writer.write(app.first + "," + app.second + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDataFromCsv() {
        File internalStorageDir = context.getFilesDir();
        File steamGamesFile = new File(internalStorageDir, "steam_games.csv");

        if (steamGamesFile.exists() && appList.isEmpty()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(steamGamesFile)))) {
                String line;
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }
                    String[] data = line.split(",");
                    if (data.length == 2) {
                        int appId = Integer.parseInt(data[0]);
                        String name = data[1];
                        appList.add(new Pair<>(appId, name));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Pair<Integer, String>> getAppList() {
        return appList;
    }

    private void saveLastAppId() {
        SharedPreferences prefs = context.getSharedPreferences("SteamGameSearcher", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("lastAppId", lastAppId);
        editor.apply();
    }

    private void loadLastAppId() {
        SharedPreferences prefs = context.getSharedPreferences("SteamGameSearcher", Context.MODE_PRIVATE);
        lastAppId = prefs.getInt("lastAppId", 0);
    }

    @Override
    public String toString() {
        return "SteamGameSearcher with " + appList.size() + " games loaded.";
    }
}
