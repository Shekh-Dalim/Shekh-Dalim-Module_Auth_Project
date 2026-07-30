package com.Dalim_Auth_App.Dalim_Project_Backend.helpers;

import java.util.UUID;

public class UserHelper {

    // TODO This method converts a String ID into a UUID.
    public static UUID parseUUID(String uuid){
        return UUID.fromString(uuid);
    }
}
