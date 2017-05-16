package maintenance.infrastructure;

import java.util.UUID;

/**
 * Created by erdem on 16.05.17.
 */
public class IDGenerator {
    public static String generate(){return UUID.randomUUID().toString().substring(0,5);}
}
