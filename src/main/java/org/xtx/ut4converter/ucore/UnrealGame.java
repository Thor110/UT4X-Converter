/*
 * UT Converter © 2023 by Thomas 'WinterIsComing/XtremeXp' P. is licensed under Attribution-NonCommercial-ShareAlike 4.0 International. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/
 */

/*
 * UT Converter © 2023 by Thomas 'WinterIsComing/XtremeXp' P. is licensed under Attribution-NonCommercial-ShareAlike 4.0 International. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/
 */

package org.xtx.ut4converter.ucore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UnrealGame {


    /**
     * Short name
     */
    @JsonProperty("id")
    private String shortName;

    /**
     * Full name
     */
    private String name;


    /**
     * Unreal Engine version
     */
    @JsonProperty("ue_version")
    private int ueVersion;

    @JsonIgnore
    private UnrealEngine unrealEngine;


    @JsonProperty("map_folder")
    private String mapFolder;

    /**
     * Map filename extension
     */
    @JsonProperty("map_ext")
    private String mapExt;

    /**
     * Texture file extension
     */
    @JsonProperty("tex_ext")
    private String texExt;

    /**
     * Music file extension
     */
    @JsonProperty("music_ext")
    private String musicExt;

    /**
     * Relative path of export program
     * used to export level to t3d and also map resources
     * E.g: /System/ucc.exe
     */
    @JsonProperty("export_exec_path")
    private String exportExecPath;

    /**
     * Where this unreal game is installed
     */
    private File path;


    /**
     * If true, this game will need texture db
     */
    private boolean useTexDb;


    /**
     * Sound file extension
     */
    @JsonProperty("sound_ext")
    private String soundExt;



    // do not delete, constructor for jackson json lib
    public UnrealGame() {

    }


    public String getMapFolder() {
        return mapFolder;
    }

    public void setMapFolder(String mapFolder) {
        this.mapFolder = mapFolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getUeVersion() {
        return ueVersion;
    }

    public void setUeVersion(int ueVersion) {
        this.ueVersion = ueVersion;
        this.unrealEngine = UnrealEngine.from(this.ueVersion);
    }

    public String getMapExt() {
        return mapExt;
    }

    public void setMapExt(String mapExt) {
        this.mapExt = mapExt;
    }

    public String getTexExt() {
        return texExt;
    }

    public void setTexExt(String texExt) {
        this.texExt = texExt;
    }

    public String getSoundExt() {
        return soundExt;
    }

    public void setSoundExt(String soundExt) {
        this.soundExt = soundExt;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public boolean isUseTexDb() {
        return useTexDb;
    }

    public void setUseTexDb(boolean useTexDb) {
        this.useTexDb = useTexDb;
    }

    public String getMusicExt() {
        return musicExt;
    }

    public void setMusicExt(String musicExt) {
        this.musicExt = musicExt;
    }

    public String getExportExecPath() {
        return exportExecPath;
    }

    public void setExportExecPath(String exportExecPath) {
        this.exportExecPath = exportExecPath;
    }


    @Override
    public String toString() {
        return "UnrealGame{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", ueVersion=" + ueVersion +
                '}';
    }
}
