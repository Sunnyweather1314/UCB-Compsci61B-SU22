import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testcompare() {
        User[] users = {
                new User(2, "Jasmine", ""),
                new User(4, "Zephyr", ""),
                new User(5, "Ethan", ""),
                new User(1, "Jedi", ""),
                new User(1, "Laksith", "")
        };
        Arrays.sort(users);
        for (User user : users) {
            System.out.println(user);
        }
    }
}
