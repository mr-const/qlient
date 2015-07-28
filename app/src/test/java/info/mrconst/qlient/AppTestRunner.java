package info.mrconst.qlient;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.res.FsFile;

import java.lang.reflect.Method;

public class AppTestRunner  extends RobolectricGradleTestRunner {
    private static final int MAX_SDK_LEVEL = 21;

    public AppTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        _prepareManifestPath();
    }

    private void _prepareManifestPath() {
        String buildVariant = (BuildConfig.FLAVOR.equals("")
                ? "" : BuildConfig.FLAVOR+ "/") + BuildConfig.BUILD_TYPE;
        String intermediatesPath = BuildConfig.class.getResource("")
                .toString().replace("file:", "");
        intermediatesPath = intermediatesPath
                .substring(0, intermediatesPath.indexOf("/classes"));

        System.setProperty("android.package",
                BuildConfig.APPLICATION_ID);
        System.setProperty("android.manifest",
                intermediatesPath + "/manifests/full/"
                        + buildVariant + "/AndroidManifest.xml");
        System.setProperty("android.resources",
                intermediatesPath + "/res/" + buildVariant);
        System.setProperty("android.assets",
                intermediatesPath + "/assets/" + buildVariant);
    }

    @Override
    public Config getConfig(Method method) {
        Config config = super.getConfig(method);
        /*
        * Fixing up the Config:
        * SDK can not be higher than 21
        * constants must point to a real BuildConfig class
        */
        config = new Config.Implementation(ensureSdkLevel(
                config.sdk()),
                config.manifest(),
                config.qualifiers(),
                config.packageName(),
                config.resourceDir(),
                config.assetDir(),
                config.shadows(),
                config.application(),
                config.libraries(),
                ensureBuildConfig(config.constants()));

        return config;
    }

    private Class<?> ensureBuildConfig(Class<?> constants) {
        if (constants == Void.class) return BuildConfig.class;
        return constants;
    }

    private int[] ensureSdkLevel(int[] sdkLevel) {
        if (sdkLevel.length == 0)
            sdkLevel = new int[1];
        if (sdkLevel[0] > MAX_SDK_LEVEL) sdkLevel[0] = MAX_SDK_LEVEL;
        if (sdkLevel[0] <= 0) sdkLevel[0] = MAX_SDK_LEVEL;
        return sdkLevel;
    }

    protected AndroidManifest getAppManifest(Config config) {
        AndroidManifest appManifest = super.getAppManifest(config);
        FsFile androidManifestFile = appManifest.getAndroidManifestFile();

        if (androidManifestFile.exists()) {
            return appManifest;
        } else {
            String moduleRoot = getModuleRootPath(config);
            androidManifestFile = FileFsFile.from(moduleRoot, appManifest.getAndroidManifestFile().getPath().replace("bundles", "manifests/full"));
            FsFile resDirectory = FileFsFile.from(moduleRoot, appManifest.getResDirectory().getPath().replace("/res", "").replace("bundles", "res"));
            FsFile assetsDirectory = FileFsFile.from(moduleRoot, appManifest.getAssetsDirectory().getPath().replace("/assets", "").replace("bundles", "assets"));
            return new AndroidManifest(androidManifestFile, resDirectory, assetsDirectory);
        }
    }

    private String getModuleRootPath(Config config) {
        String moduleRoot = config.constants().getResource("").toString().replace("file:", "");
        return moduleRoot.substring(0, moduleRoot.indexOf("/build"));
    }
}
