package fr.xephi.authmebungee.utils;

import java.io.File;
import java.io.IOException;

public final class FileUtils {

  private FileUtils() {}

  public static void create(File file) {

    try {

      file.getParentFile().mkdirs();

      boolean result = file.createNewFile();

      if (!result) {
        throw new IllegalStateException("Could not create file '" + file + "'");
      }

    } catch (IOException e) {
      throw new IllegalStateException("Error while creating file '" + file + "'", e);
    }
  }
}
