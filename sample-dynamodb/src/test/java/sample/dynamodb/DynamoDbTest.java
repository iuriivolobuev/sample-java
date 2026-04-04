package sample.dynamodb;

import org.junit.Test;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import static org.junit.Assert.assertEquals;

public class DynamoDbTest {
    private static final DynamoDbTable<Dog> DOG_TABLE = DynamoDb.dogTable();

    @Test
    public void putsItem() {
        Dog saved = put(Dog.random());
        Dog loaded = get(saved.getId());
        assertDogsEqual(saved, loaded);
    }

    private static Dog get(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return DOG_TABLE.getItem(key);
    }

    private static Dog put(Dog dog) {
        DOG_TABLE.putItem(dog);
        return dog;
    }

    private static void assertDogsEqual(Dog expected, Dog actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }
}
