package ru.lofitsky.foldersSize.service;

public enum OSType {
    UNKNOWN,
    WINDOWS,
    NIX,
    NUX,
    AIX,
    MAC;
    
    public static OSType getOS() {
        String os = System.getProperty("os.name").toLowerCase();

        if(os.indexOf("win") >= 0) return WINDOWS;

        if(os.indexOf("nix") >= 0) return NIX;
        if(os.indexOf("nux") >= 0) return NUX;
        if(os.indexOf("aix") >= 0) return AIX;

        if(os.indexOf("mac") >= 0) return MAC;

        return UNKNOWN;
    }
}