package info.mrconst.qlient.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import info.mrconst.qlient.AppTestRunner;
import info.mrconst.qlient.BuildConfig;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AppTestRunner.class)
@Config(constants = BuildConfig.class)
public class StreetFeedTest {

    @Test
    public void testItemConformsToFilterCondition() throws Exception {
        StreetFeed feed = new StreetFeed(RuntimeEnvironment.application);
        assertThat(feed).isNotNull();
        feed.startFilter("новозабарская");
        assertThat(feed.size()).isEqualTo(1);
    }
}