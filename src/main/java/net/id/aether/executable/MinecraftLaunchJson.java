package net.id.aether.executable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MinecraftLaunchJson {
    public String id;
    public String inheritsFrom;
    public final String releaseTime = Utils.ISO_8601.format(new Date());
    public String time = releaseTime;
    public String type = "release";
    public final String mainClass;
    public transient String mainClassServer;
    public final Arguments arguments = new Arguments();
    public final List<Library> libraries = new ArrayList<>();

    //Used for reading the fabric-launch.json and populating the minecraft format
    public MinecraftLaunchJson(JsonObject jsonObject) {

        if (!jsonObject.get("mainClass").isJsonObject()) {
            mainClass = jsonObject.get("mainClass").getAsString();
        } else {
            mainClass = jsonObject.get("mainClass").getAsJsonObject().get("client").getAsString();
            //Done like this as this object is written to a vanilla profile json
            mainClassServer = jsonObject.get("mainClass").getAsJsonObject().get("server").getAsString();
        }

        if (jsonObject.has("launchwrapper")) {
            String clientTweaker = jsonObject.get("launchwrapper").getAsJsonObject().get("tweakers").getAsJsonObject().get("client").getAsJsonArray().get(0).getAsString();

            arguments.game.add("--tweakClass");
            arguments.game.add(clientTweaker);
        }

        String[] validSides = new String[]{"common", "server"};
        JsonObject librariesObject = jsonObject.getAsJsonObject("libraries");
        for (String side : validSides) {
            JsonArray librariesArray = librariesObject.getAsJsonArray(side);
            librariesArray.forEach(jsonElement -> libraries.add(new Library(jsonElement)));
        }
    }

    public static class Library {

        public final String name;
        public String url;

        public Library(String name, String url) {
            this.name = name;
            this.url = url;
        }

        private Library(JsonElement jsonElement) {
            JsonObject jsonObject = (JsonObject) jsonElement;
            name = jsonObject.get("name").getAsString();
            if (jsonObject.has("url")) {
                url = jsonObject.get("url").getAsString();
            }
        }

        public String getURL() {
            String path;
            String[] parts = this.name.split(":", 3);
            path = parts[0].replace(".", "/") + "/" + parts[1] + "/" + parts[2] + "/" + parts[1] + "-" + parts[2] + ".jar";
            return url + path;
        }

        public String getPath() {
            String[] parts = this.name.split(":", 3);
            String path = parts[0].replace(".", File.separator) + File.separator + parts[1] + File.separator + parts[2] + File.separator + parts[1] + "-" + parts[2] + ".jar";
            return path.replaceAll(" ", "_");
        }

        public File getFile(File baseDir) {
            return new File(baseDir, getPath());
        }

        public String getFileName() {
            String path = getPath();
            return path.substring(path.lastIndexOf("\\") + 1);
        }
    }

    public static class Arguments {

        public final List<String> game = new ArrayList<>();
    }
}
