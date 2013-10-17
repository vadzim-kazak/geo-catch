package com.jrew.geocatch.repository.util;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 9/11/13
 * Time: 11:26 AM
 *
 * Contains util methods to work with folders.
 */
public class FolderUtils {

    /**
     *
     * @param folderPath
     * @throws IOException
     */
    public static void checkOrCreateFoldersStructure(String folderPath) throws IOException {
        File folder = new File(folderPath);
        if(!folder.exists()) {
            boolean success = folder.mkdir();
            if (!success) {
                throw new IOException("Can't create " + folderPath + " folder.");
            }
        }
    }
}
