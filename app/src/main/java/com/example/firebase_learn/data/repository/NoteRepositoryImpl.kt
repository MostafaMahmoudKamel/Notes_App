package com.example.firebase_learn.data.repository

import com.example.firebase_learn.data.model.Note
import com.example.firebase_learn.domain.repository.NoteRepository
import com.example.firebase_learn.utils.Collections
import com.example.firebase_learn.utils.UiResource
import com.example.firebase_learn.utils.wrapWithFlow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : NoteRepository {


    override suspend fun addNote(note: Note): Flow<UiResource<Boolean>> = wrapWithFlow {
        val uid = firebaseAuth.currentUser?.uid ?: "error Uid"

        val noteWithId = note.copy(userId = uid)//create a new note and add uid to newNote

        firestore.collection(Collections.notes).document(noteWithId.noteId).set(noteWithId)
            .await() //add list of NOte to user
        true
    }

    override suspend fun updateNote(newNote: Note): Flow<UiResource<Boolean>> = wrapWithFlow {
        firestore.collection(Collections.notes).document(newNote.noteId).set(newNote).await()
        true
    }


    override suspend fun deleteNote(noteId: String): Flow<UiResource<Boolean>> = wrapWithFlow {
        firestore.collection(Collections.notes).document(noteId).delete() //delete
        true

    }

    //    //get info of note selected when i need to update data on it
    override suspend fun getNoteById(noteId: String): Flow<UiResource<Note>> = wrapWithFlow {
        val snapshot = firestore.collection(Collections.notes).document(noteId).get().await()
        val myNOte = snapshot.toObject(Note::class.java) ?: throw Exception("noteId not found")
        myNOte// Automatically returned
    }


    //not called
    override suspend fun clearNotes(): Flow<UiResource<Boolean>> = wrapWithFlow {
        val uid = firebaseAuth.currentUser?.uid ?: "error Uid"

        val snapshot =
            firestore.collection(Collections.notes).whereEqualTo("userId", uid).get().await()
        val batch = firestore.batch()
        snapshot.documents.forEach { document -> batch.delete(document.reference) }
        batch.commit().await()

        true //result Success

    }

    override suspend fun getAllNote(): Flow<UiResource<List<Note>>> = wrapWithFlow {
        val uid = firebaseAuth.currentUser?.uid ?: "error Uid"

        // Get all documents in the "NOTES" collection
        val snapshot = firestore.collection("NOTES")
            .whereEqualTo("userId", uid) //condition  -->userId is in Note dataClass
            .get().await()

        // Map each document to a Note object
        val notes = snapshot.documents.mapNotNull { it.toObject(Note::class.java) }//document by do.
        notes
    }


}