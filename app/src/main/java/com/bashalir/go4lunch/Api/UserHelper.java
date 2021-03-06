package com.bashalir.go4lunch.Api;

import com.bashalir.go4lunch.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture, String idRestaurant) {
        User userToCreate = new User(uid, username, urlPicture, idRestaurant);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Query getAllUserbyRestaurant(String idRestaurant){
        return UserHelper.getUsersCollection()
                .whereEqualTo("idRestaurant",idRestaurant)
                .orderBy("username");
    }

    public static Task<DocumentSnapshot> getRestaurant(String idRestaurant){
        return UserHelper.getUsersCollection().document(idRestaurant).get();
    }



    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }
    public static Task<Void> updateRestaurant(String idRestaurant, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("idRestaurant", idRestaurant);
    }
    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}
