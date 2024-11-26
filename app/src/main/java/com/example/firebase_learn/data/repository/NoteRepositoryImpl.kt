package com.example.firebase_learn.data.repository

import com.example.firebase_learn.data.model.Note
import com.example.firebase_learn.data.model.UiResource
import com.example.firebase_learn.domain.repository.NoteRepository
import com.example.firebase_learn.utils.Collections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : NoteRepository {

//    val uid = firebaseAuth.currentUser?.uid //big error declare Uid for each function

    override suspend fun addNote(note: Note): Flow<UiResource<Boolean>> = flow {
        val uid = firebaseAuth.currentUser?.uid ?: "error Uid"

        val noteWithId = note.copy(userId = uid)//create a new note and add uid to newNote

        firestore.collection("NOTES").document(noteWithId.noteId).set(noteWithId)
            .await() //add list of NOte to user
//        firestore.collection(Collections.users).document(uid).collection(Collections.notes).document(UUID.randomUUID().toString())
//            .set(noteWithId).await()

        emit(UiResource.Loading)
        delay(1000)
        try {
            emit(UiResource.Success(true))
        } catch (e: Exception) {
            emit(UiResource.Failure(e))
        }


    }

    override suspend fun updateNote(newNote: Note): Flow<UiResource<Boolean>> = flow {

        emit(UiResource.Loading)
        delay(1000)
        firestore.collection(Collections.notes).document(newNote.noteId).set(newNote).await()//remo
        try {
            emit(UiResource.Success(true))
        } catch (e: Exception) {
            emit(UiResource.Failure(e))
        }
    }

    override suspend fun deleteNote(noteId: String): Flow<UiResource<Boolean>> = flow {
        firestore.collection(Collections.notes).document(noteId).delete() //delete
        emit(UiResource.Loading)                                //loading
        delay(1000)

        try {
            emit(UiResource.Success(true))                 //success
        } catch (e: Exception) {                                //error
            emit(UiResource.Failure(e))
        }
    }
    //get info of note selected when i need to update data on it
    override suspend fun getNoteById(noteId: String): Flow<UiResource<Note>> = flow {
        val snapshot = firestore.collection(Collections.notes).document(noteId).get().await()
        val myNOte = snapshot.toObject(Note::class.java) ?: throw Exception("noteId not found")
//        emit(UiResource.Loading)
        try {
            emit(UiResource.Success(myNOte))
        } catch (e: Exception) {
            emit(UiResource.Failure(e))
        }
    }


    //not called
    override suspend fun clearNotes(): Flow<UiResource<Boolean>> = flow {
        val uid = firebaseAuth.currentUser?.uid ?: "error Uid"

        val snapshot =
            firestore.collection(Collections.notes).whereEqualTo("userId", uid).get().await()
        val batch = firestore.batch()
        snapshot.documents.forEach { document ->
            batch.delete(document.reference)
        }
        emit(UiResource.Loading)
        delay(1000)
        try {
            emit(UiResource.Success(true))
        } catch (e: Exception) {
            emit(UiResource.Failure(e))
        }
    }


    override suspend fun getAllNote(): Flow<UiResource<List<Note>>> {
        val uid = firebaseAuth.currentUser?.uid ?: "error Uid"

        // Get all documents in the "NOTES" collection
        val snapshot = firestore.collection("NOTES")
            .whereEqualTo("userId", uid) //condition  -->userId is in Note dataClass
            .get().await()

        // Map each document to a Note object
        val notes = snapshot.documents.mapNotNull { it.toObject(Note::class.java) }//document by do.

        return flow {
            emit(UiResource.Loading)
            delay(1000)
            try {
                emit(UiResource.Success(notes))
            } catch (e: Exception) {
                emit(UiResource.Failure(Exception("can't getAllNote ")))
            }
        }
    }


}