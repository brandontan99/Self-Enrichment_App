package com.example.self_enrichment_app.view.general;

import com.example.self_enrichment_app.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class GlobalVariable {

    public static User currentUser = new User();

}
