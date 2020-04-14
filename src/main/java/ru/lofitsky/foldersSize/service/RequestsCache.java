package ru.lofitsky.foldersSize.service;

import ru.lofitsky.foldersSize.MyFile.MyFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

class RequestsCache {
    private static RequestsCache instance;

    private Map<String, MyFile> cache = new HashMap<>();

    static RequestsCache getInstance() {
        return instance == null ? instance = new RequestsCache() : instance;
    }

    MyFile add(String path) {
        // no parent cached. Store new file.

        MyFile myFile = new MyFile(path);
        myFile.calculateSize();
        cache.put(path, myFile);

        // Look for child branches and remove them if present.
        List<String> children = getChildBranches(path);
        if(children != null) {
            children.forEach(cache::remove);
        }

        return myFile;
    }

    MyFile get(String path, boolean calculated) {
        MyFile myFile = cache.get(path);

        if(myFile == null) {
            // path has not been cached yet. Look for its cached parent
            String parentBranch = getParentBranch(path);
            if(parentBranch != null) {
                // cache contains parent branch. Retrieve myFile from cache
                myFile = cache.get(parentBranch);

                String quotedPathSeparator = Pattern.quote(FilesService.PATH_SEPARATOR);
                String tail = path.replace(parentBranch, "");

                if(tail.startsWith(FilesService.PATH_SEPARATOR)) {
                    tail = tail.replaceFirst(quotedPathSeparator, "");
                }

                String[] splitted = tail.split(quotedPathSeparator);

                for(String child : splitted) {
                    myFile = myFile.getChild(child);
                }

            } else {
                // cache does not contain parent branch. Store it if calculated == true, or return null otherwise
                myFile = calculated ? add(path) : null;
            }
        }

        return myFile;
    }

    /**
     * Looks for parent branch stored in cache. Returns it if found, or null otherwise.
     *
     * @param childPath child branch String path, for which parent is being looking for.
     * @return parent branch path, or null
     */
    private String getParentBranch(String childPath) {
        for(String cachedPath : cache.keySet()) {
            if(childPath.contains(cachedPath))
                return cachedPath;
        }

        return null;
    }

    /**
     * Looks for child branches stored in cache. Returns them if found, or null otherwise.
     *
     * @param parentPath child branch String path, for which parent is being looking for.
     * @return list of child branches, or null if none.
     */
    private List<String> getChildBranches(String parentPath) {
        List<String> children = new ArrayList<>();
        for(String cachedPath : cache.keySet()) {
            if(cachedPath.contains(parentPath) && !cachedPath.equals(parentPath))
                children.add(cachedPath);
        }

        return children.size() != 0 ? children : null;
    }
}