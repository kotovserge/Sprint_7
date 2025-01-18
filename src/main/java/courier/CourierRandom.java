package courier;

import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class CourierRandom {

    Faker faker = new Faker(new Locale("ru"));

    public  CourierData generateCourierData() {
        String login = StringUtils.left(faker.name().username(),10);
        String password = StringUtils.left(faker.internet().password(),10);
        String firstName = StringUtils.left(faker.name().firstName(),10);
        return new CourierData( login, password, firstName);

    }

}
