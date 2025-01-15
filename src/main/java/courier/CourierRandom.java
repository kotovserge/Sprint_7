package courier;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierRandom {

    public static CourierData generateCourierData() {
        return new CourierData(
                RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(8),
                RandomStringUtils.randomAlphabetic(10));
    }
}
