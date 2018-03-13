package dao;

import pti.test.model.Product;
import pti.test.model.Type;
import pti.test.model.authorization.Users;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Util class for creating a collections of random entities.
 *
 * @author Syrotyuk R.
 */
public class Utils {

    public static List<Type> createRandomTypes(int size) {
        return Stream.iterate(new Type(), x -> new Type("Type " + UUID.randomUUID(), "Category " + UUID.randomUUID()))
                .skip(1)
                .limit(size)
                .collect(Collectors.toList());
    }

    public static List<Product> createRandomProducts(int size, List<Type> types) {
        return Stream.iterate(new Product(),
                x -> new Product("Name " + UUID.randomUUID(),
                        new Random().nextInt(5000),
                        new Random().nextInt(5000),
                        new Random().nextInt(Integer.MAX_VALUE),
                        UUID.randomUUID().toString(),
                        types.get(new Random().nextInt(types.size())),
                        "Country",
                        "Supplier"))
                .skip(1)
                .limit(size)
                .collect(Collectors.toList());
    }

    public static List<Users> createRandomUsers(int size) {
        return Stream.iterate(new Users(),
                x -> new Users("Name " + UUID.randomUUID(),
                        "Surname " + UUID.randomUUID(),
                        "Mail " + UUID.randomUUID(),
                        "Password " + UUID.randomUUID(),
                        "USER"))
                .skip(1)
                .limit(size)
                .collect(Collectors.toList());
    }

}
