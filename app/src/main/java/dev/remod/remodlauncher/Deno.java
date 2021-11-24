package dev.remod.remodlauncher;

import android.content.Context;
import android.webkit.JavascriptInterface;

import java.io.File;

public final class Deno {
    static class fs {
        private final Context ctx;
        fs (Context c) {
            ctx = c;
        }

        @JavascriptInterface
        public static Boolean ensureFile(File path) {
            return path.exists() && !path.isDirectory() && path.isFile();
        }
        @JavascriptInterface
        public static Boolean ensureDir(File path) {
            return path.exists() && path.isDirectory() && !path.isFile();
        }
    }
    static class os {
        private final Context ctx;
        os (Context c) {
            ctx = c;
        }


    }
}
