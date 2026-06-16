package tw.nekomimi.nekogram.helpers;

import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.UserConfig;

public class CloudStorageHelper extends AccountInstance {

    private static final CloudStorageHelper[] Instance = new CloudStorageHelper[UserConfig.MAX_ACCOUNT_COUNT];

    public CloudStorageHelper(int num) {
        super(num);
    }

    public static CloudStorageHelper getInstance(int num) {
        CloudStorageHelper localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (CloudStorageHelper.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    Instance[num] = localInstance = new CloudStorageHelper(num);
                }
            }
        }
        return localInstance;
    }
}
