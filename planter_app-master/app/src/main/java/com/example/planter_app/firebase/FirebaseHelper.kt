@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.example.planter_app.firebase
import android.net.Uri
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

object FirebaseHelper {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    fun savePlantData(
        uri: Uri,
        disease: String,
        treatment: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val storageReference = storage.reference
        val imageRef = storageReference.child("images/${uri.lastPathSegment}")
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                val contactsRef = database.reference.child("MyPlants")
                val newContactRef = contactsRef.push()
                val contact = Plant(newContactRef.key!!, downloadUrl.toString(), disease, treatment)
                newContactRef.setValue(contact)
                onSuccess()
            }
        }.addOnFailureListener {
            onFailure(it)
        }
    }

    fun fetchPlantData(onDataFetched: (List<Plant>) -> Unit, onFailure: (DatabaseError) -> Unit) {
        val myRef: DatabaseReference = database.reference.child("MyPlants")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val plants = mutableListOf<Plant>()
                dataSnapshot.children.forEach { child ->
                    val key = child.key ?: ""
                    val imageUrl = child.child("image").getValue(String::class.java) ?: ""
                    val disease = child.child("disease").getValue(String::class.java) ?: ""
                    val treatment = child.child("treatment").getValue(String::class.java) ?: ""
                    plants.add(Plant(key, imageUrl, disease, treatment))
                }
                onDataFetched(plants)
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure(error)
            }
        })
    }

    fun deletePlantData(key: String, onSuccess: () -> Unit, onFailure: (DatabaseError) -> Unit) {
        val plantRef = database.reference.child("MyPlants").child(key)
        plantRef.removeValue().addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailure(it as DatabaseError)
        }
    }
}
