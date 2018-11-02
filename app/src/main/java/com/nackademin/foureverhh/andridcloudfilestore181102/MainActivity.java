package com.nackademin.foureverhh.andridcloudfilestore181102;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);
        textView3 = findViewById(R.id.text3);

        //WRITE AND READ DATA
        //addNewContact();
        readSingleContact();
        readAllContacts();
        readSingleContactCustomObject();

         //UPDATE DATA 
         updateData();






    }

    private void updateData() {
        //Get Document
        DocumentReference contact = db.collection("AddressBook").document("1");
        contact.update("name","EddyLee").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this,"Update",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error",e.getMessage());
            }
        });
    }

    private void readSingleContactCustomObject() {
        DocumentReference contact = db.collection("AddressBook").document("1");
        contact.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AddressBook addressBook = documentSnapshot.toObject(AddressBook.class);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Name is:"+addressBook.getName());
                stringBuilder.append("\n");
                stringBuilder.append("Phone is:"+addressBook.getPhone());
                stringBuilder.append("\n");
                stringBuilder.append("Email is:"+addressBook.getEmail());
                stringBuilder.append("\n");
                textView3.setText(stringBuilder.toString());
            }
        });





    }

    private void readAllContacts() {
        db.collection("AddressBook").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    StringBuilder data = new StringBuilder();
                    for(QueryDocumentSnapshot document : task.getResult())   {
                         data.append("Info in ID."+document.getId()).append(" is ").append(document.getData());
                         data.append("\n");//.append(document.getId())
                    }
                    textView2.setText(data.toString());
                }
            }
        }) ;
    }

    private void addNewContact(){
        //Add new Contact to Address Book
        Map<String,Object> newContact = new HashMap<>();
        newContact.put("name","EddyDN");
        newContact.put("email","eddydn@gmail.com");
        newContact.put("phone","888-888-8888");

        db.collection("AddressBook")//.add(newContact)
                .document("1")
                .set(newContact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,
                                "Added new Contact",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error",e.getMessage());
                    }
                });
        newContact.clear();

        newContact.put("name","Linda");
        newContact.put("job","teacher");
        newContact.put("mobile","123-456-7890");
        db.collection("AddressBook").add(newContact).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.e("DocumentID","ID is "+documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Error",e.getMessage());
            }
        });
    }

    private void readSingleContact(){
        DocumentReference contact = db.collection("AddressBook").document("1");
        contact.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                     DocumentSnapshot documentSnapshot = task.getResult();
                     StringBuilder stringBuilder = new StringBuilder();
                     stringBuilder.append("Name: ").append(documentSnapshot.getString("name"));
                     stringBuilder.append("\n");
                     stringBuilder.append("Email: ").append(documentSnapshot.getString("email"));
                     stringBuilder.append("\n");
                     stringBuilder.append("Phone: ").append(documentSnapshot.getString("phone"));
                     stringBuilder.append("\n");
                     textView1.setText(stringBuilder.toString());
                }
            }
        }) ;

    }
}
